package com.allenanker.quora.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@Controller
public class IndexController {
    @RequestMapping(path = {"/", "/index"}, method = {RequestMethod.GET})
    @ResponseBody
    public String index() {
        return "I come back!";
    }

    @RequestMapping(path = {"/profile/{userName}/{userId}"})
    @ResponseBody
    public String profile(@PathVariable("userName") String username,
                          @PathVariable("userId") int userId,
                          @RequestParam(value = "type", defaultValue = "0") int type,
                          @RequestParam(value = "key", defaultValue = "1") int key) {
        return "User Info: " + username + " / " + userId + " & " + type + " " + key;
    }

    @RequestMapping(path = {"/template/index"}, method = {RequestMethod.GET})
    public String template(Model model) {
        HashMap<String, String> map = new HashMap<>();
        map.put("name", "Allen Anker");
        model.addAttribute("name", "Allen Anker");
        model.addAttribute("map", map);
        return "index";
    }
}
