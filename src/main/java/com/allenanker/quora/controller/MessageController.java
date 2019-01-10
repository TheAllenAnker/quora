package com.allenanker.quora.controller;

import com.allenanker.quora.model.HostHolder;
import com.allenanker.quora.model.Message;
import com.allenanker.quora.model.User;
import com.allenanker.quora.model.ViewObject;
import com.allenanker.quora.service.MessageService;
import com.allenanker.quora.service.UserService;
import com.allenanker.quora.util.QuoraUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    @RequestMapping(path = {"/msg/list"}, method = {RequestMethod.GET})
    public String getConversationList(Model model) {
        if (hostHolder.getUser() == null) {
            return "redirect:/loginPage";
        }
        int userId = hostHolder.getUser().getId();
        List<Message> messageList = messageService.getConversationList(userId, 0, 10);
        List<ViewObject> conversations = new ArrayList<>();
        for (Message message : messageList) {
            // the message id here is actually the total message count in each conversation
            int targetId = message.getFromId() == userId ? message.getToId() : message.getFromId();
            ViewObject vo = new ViewObject();
            vo.set("conversation", message);
            vo.set("user", userService.getUserById(targetId));
            vo.set("unread", message.getId());
            conversations.add(vo);
        }
        model.addAttribute("conversations", conversations);

        return "letter";
    }

    @RequestMapping(path = {"/msg/detail"}, method = {RequestMethod.GET})
    public String getConversationDetails(Model model,
                                         @RequestParam("conversationId") String conversationId) {
        List<Message> messageList = messageService.getConversationMessages(conversationId, 0, 10);
        List<ViewObject> messages = new ArrayList<>();
        for (Message message : messageList) {
            ViewObject vo = new ViewObject();
            vo.set("message", message);
            User fromUser = userService.getUserById(message.getFromId());
            if (fromUser == null) {
                continue;
            }
            vo.set("headUrl", fromUser.getHeadUrl());
            vo.set("userId", fromUser.getId());
            messages.add(vo);
        }
        model.addAttribute("messages", messages);

        return "letterDetail";
    }
}
