package com.allenanker.quora.service;

import com.allenanker.quora.dao.LoginTicketDAO;
import com.allenanker.quora.dao.UserDAO;
import com.allenanker.quora.model.LoginTicket;
import com.allenanker.quora.model.User;
import com.allenanker.quora.util.QuoraUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private LoginTicketDAO loginTicketDAO;

    public void logout(String ticket) {
        loginTicketDAO.updateStatus(ticket, 1);
    }

    public Map<String, String> login(String username, String password) {
        Map<String, String> msgMap = new HashMap<>();
        if (StringUtils.isBlank(username)) {
            msgMap.put("msg", "Username cannot be empty");
            return msgMap;
        }
        if (StringUtils.isBlank(password)) {
            msgMap.put("msg", "Password cannot be empty");
            return msgMap;
        }

        User user = userDAO.selectByName(username);
        if (user == null) {
            msgMap.put("msg", "Username not found");
            return msgMap;
        }

        if (!user.getPassword().equals(QuoraUtils.MD5(password + user.getSalt()))) {
            msgMap.put("msg", "Username or Password is invalid");
            return msgMap;
        }

        msgMap.put("ticket", addLoginTicket(user.getId()));
        return msgMap;
    }

    public Map<String, String> register(String username, String password) {
        Map<String, String> msgMap = new HashMap<>();
        if (StringUtils.isBlank(username)) {
            msgMap.put("msg", "Username cannot be empty");
            return msgMap;
        }
        if (StringUtils.isBlank(password)) {
            msgMap.put("msg", "Password cannot be empty");
            return msgMap;
        }

        User user = userDAO.selectByName(username);
        if (user != null) {
            msgMap.put("msg", "Username has been taken, please choose another one");
            return msgMap;
        } else {
            user = new User();
            user.setName(username);
            user.setSalt(UUID.randomUUID().toString().substring(0, 5));
            user.setPassword(QuoraUtils.MD5(password + user.getSalt()));
            String headUrl = String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000));
            user.setHeadUrl(headUrl);
            userDAO.addUser(user);
            msgMap.put("ticket", addLoginTicket(user.getId()));
        }

        return msgMap;
    }

    public User getUserById(int id) {
        return userDAO.selectById(id);
    }

    public User getUserByName(String name) {
        return userDAO.selectByName(name);
    }

    private String addLoginTicket(int userId) {
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(userId);
        Date now = new Date();
        now.setTime(3600 * 24 * 1000 * 7 + now.getTime());
        loginTicket.setExpired(now);
        loginTicket.setStatus(0);
        loginTicket.setTicket(UUID.randomUUID().toString().replace("-", ""));
        loginTicketDAO.addTicket(loginTicket);

        return loginTicket.getTicket();
    }
}
