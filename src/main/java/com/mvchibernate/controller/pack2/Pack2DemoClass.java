package com.mvchibernate.controller.pack2;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class Pack2DemoClass {

    @RequestMapping("/pack2")
    public String display(){
        return "inside controller package 2";
    }

}
