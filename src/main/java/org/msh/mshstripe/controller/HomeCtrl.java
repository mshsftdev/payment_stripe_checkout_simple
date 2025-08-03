package org.msh.mshstripe.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeCtrl {

    @Value("${stripe.publickey}")
    private String pk;

    @GetMapping
    public String index(){
        return "index";
    }

    @GetMapping("/2")
    public String index2( Model model){
        model.addAttribute("pk", pk);
        return "index2";
    }

    @GetMapping("/success")
    public String success(){
        return "success";
    }
}
