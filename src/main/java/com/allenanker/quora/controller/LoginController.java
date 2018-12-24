package com.allenanker.quora.controller;

import com.allenanker.quora.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    UserService userService;

    @RequestMapping(path = {"/logout"}, method = {RequestMethod.GET})
    public String logout(@CookieValue("ticket") String ticket) {
        userService.logout(ticket);
        return "redirect:/";
    }

    @RequestMapping(path = {"/register/"}, method = {RequestMethod.POST})
    public String register(Model model,
                           @RequestParam("username") String username,
                           @RequestParam("password") String password,
                           HttpServletResponse response) {

        try {
            Map<String, String> msgMap = userService.register(username, password);
            if (msgMap.containsKey("msg")) {
                model.addAttribute("msg", msgMap.get("msg"));
                return "login";
            }
            if (msgMap.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket", msgMap.get("ticket"));
                cookie.setPath("/");
                response.addCookie(cookie);
            }
            return "redirect:/";
        } catch (Exception e) {
            logger.error("Register Error: " + e.getMessage());
            return "login";
        }
    }

    @RequestMapping(path = {"/login/"}, method = {RequestMethod.POST})
    public String login(Model model,
                        @RequestParam("username") String username,
                        @RequestParam("password") String password,
                        @RequestParam(value = "rememberme", defaultValue = "false") boolean rememberMe,
                        HttpServletResponse response) {

        try {
            Map<String, String> msgMap = userService.login(username, password);
            if (msgMap.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket", msgMap.get("ticket"));
                cookie.setPath("/");
                if (rememberMe) {
                    cookie.setMaxAge(3600 * 24 * 365);
                }
                response.addCookie(cookie);
                return "redirect:/";
            }
            if (msgMap.containsKey("msg")) {
                model.addAttribute("msg", msgMap.get("msg"));
            }
            return "login";
        } catch (Exception e) {
            logger.error("Login Error: " + e.getMessage());
            return "login";
        }
    }

    @RequestMapping(path = {"/loginPage"}, method = {RequestMethod.GET})
    public String loginPage() {
        return "login";
    }
}
