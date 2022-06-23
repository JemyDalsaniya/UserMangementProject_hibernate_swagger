package com.mvchibernate.controller.pack;

import com.mvchibernate.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
public class DemoClass {

    @RequestMapping("/adminpack")
    public String display(){
        return "inside controller package";
    }

    @GetMapping(value = "/show")
    public ModelAndView show() {

        ModelAndView mav = new ModelAndView();

//        ModelAndView now = LocalDateTime.now();
//        ModelAndView formatter = DateTimeFormatter.ISO_DATE_TIME;
//        ModelAndView dateTimeFormatted = formatter.format(now);

        mav.addObject("now", mav);
        mav.setViewName("show");

        return mav;
    }
}
