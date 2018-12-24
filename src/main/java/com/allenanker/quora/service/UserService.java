package com.allenanker.quora.service;

import com.allenanker.quora.dao.UserDAO;
import com.allenanker.quora.model.User;
import com.allenanker.quora.util.QuoraUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;


@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserDAO userDAO;

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
        }

        return msgMap;
    }

    public User getUser(int id) {
        return userDAO.selectById(id);
    }
}
