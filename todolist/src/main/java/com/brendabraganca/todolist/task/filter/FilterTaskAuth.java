package com.brendabraganca.todolist.task.filter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.brendabraganca.todolist.user.iUserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {
    @Autowired
    private iUserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

                var servletPath = request.getServletPath();
                if(servletPath.startsWith("/tasks/")) {
                    //Pegar autenticacao (usuario e senha)
                    var authorization = request.getHeader("Authorization");
                    var authEnconded = authorization.substring("Basic".length()).trim();
                    byte[] authDecode = Base64.getDecoder().decode(authEnconded);
                    String authString = new String(authDecode);
                    String[] credentials =  authString.split(":");
                    String username = credentials[0];
                    String password = credentials[1];

                    //Validar usuario
                    var user = userRepository.findByUsername(username);
                    if(user == null) {
                        response.sendError(401);
                    } else {
                        //Validar senha
                        var passwordVerify = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
                        if (passwordVerify.verified) {
                            request.setAttribute("idUser", user.getId());
                            filterChain.doFilter(request, response);
                        } else {
                            response.sendError(401);
                        }


                    }


                } else{
                    filterChain.doFilter(request, response);
                }
    }
}
