package com.allenanker.quora.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;

@Controller
public class IndexController {
    /**
    @RequestMapping(path = {"/", "/index"}, method = {RequestMethod.GET})
    @ResponseBody
    public String index(HttpSession httpSession) {
        return "I come back! " + "redirected: " + httpSession.getAttribute("redirected");
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

    @RequestMapping(path = {"/redirect/{code}"})
    public RedirectView redirectText(@PathVariable("code") int code,
                                     HttpSession session,
                                     HttpServletRequest request,
                                     HttpServletResponse response) {
        session.setAttribute("redirected", true);
        RedirectView rv = new RedirectView("/", true);
        if (code == 301) {
            rv.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        }
        return rv;
    }

    @RequestMapping(path = {"/exceptionTest"})
    @ResponseBody
    public String exceptionTest(@RequestParam("username") String username) {
        if (!username.equals("admin")) {
            throw new IllegalArgumentException("Username invalid");
        }

        return "Username: " + username;
    }

    @ExceptionHandler()
    @ResponseBody
    public String error(Exception e) {
        return "Error: " + e.getMessage();
    }
    **/
}
