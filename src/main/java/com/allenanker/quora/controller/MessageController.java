package com.allenanker.quora.controller;

import com.allenanker.quora.model.HostHolder;
import com.allenanker.quora.model.Message;
import com.allenanker.quora.model.User;
import com.allenanker.quora.service.MessageService;
import com.allenanker.quora.service.UserService;
import com.allenanker.quora.util.QuoraUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Controller
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @Autowired
    HostHolder hostHolder;

    @RequestMapping(path = {"/msg/addMessage"}, method = {RequestMethod.POST})
    @ResponseBody
    public String addMessage(@RequestParam("content") String content,
                             @RequestParam("toName") String toUsername) {
        try {
            if (hostHolder.getUser() == null) {
                return QuoraUtils.getJSONString(1, "User not login in");
            }
            User user = userService.getUserByName(toUsername);
            if (user == null) {
                return QuoraUtils.getJSONString(2, "User not found");
            }
            Message message = new Message();
            message.setFromId(hostHolder.getUser().getId());
            message.setToId(user.getId());
            message.setContent(content);
            message.setHasRead(0);
            message.setCreatedDate(new Date());
            messageService.addMessage(message);

            return QuoraUtils.getJSONString(0, "Succeed");
        } catch (Exception e) {
            logger.error("Add message failed: " + e.getMessage());
            return QuoraUtils.getJSONString(1, "Add message failed");
        }
    }
}
