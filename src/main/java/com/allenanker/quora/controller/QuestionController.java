package com.allenanker.quora.controller;

import com.allenanker.quora.model.HostHolder;
import com.allenanker.quora.model.Question;
import com.allenanker.quora.service.QuestionService;
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
public class QuestionController {
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    QuestionService questionService;

    @Autowired
    HostHolder hostHolder;

    @RequestMapping(path = {"/question/add"}, method = {RequestMethod.POST})
    @ResponseBody
    public String addQuestion(@RequestParam("title") String title,
                              @RequestParam("content") String content) {
        try {
            Question question = new Question();
            question.setTitle(title);
            question.setContent(content);
            question.setCreatedDate(new Date());
            question.setCommentCount(0);
            if (hostHolder.getUser() != null) {
                question.setUserId(hostHolder.getUser().getId());
            } else {
                question.setUserId(QuoraUtils.ANONYMOUS_USER_ID);
            }
            if (questionService.addQuestion(question) >= 0) {
                return QuoraUtils.getJSONString(0 , "Success");
            }
        } catch (Exception e) {
            logger.error("Add question failed: " + e.getMessage());
        }

        return QuoraUtils.getJSONString(1, "Failure");
    }
}
