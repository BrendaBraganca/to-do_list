package com.brendabraganca.todolist.controler;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RestController
@RequestMapping("/rota")

public class controller {
    @GetMapping("/")
    public String Mensagem() {
        return "funcionando";
    }
}
