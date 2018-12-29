package com.allenanker.quora.controller;

import com.allenanker.quora.model.HostHolder;
import com.allenanker.quora.model.Question;
import com.allenanker.quora.service.QuestionService;
import com.allenanker.quora.service.SensitiveWordsService;
import com.allenanker.quora.service.UserService;
import com.allenanker.quora.util.QuoraUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import java.util.Date;

@Controller
public class QuestionController {
    private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);

    @Autowired
    QuestionService questionService;

    @Autowired
    UserService userService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    SensitiveWordsService sensitiveWordsService;

    @RequestMapping(path = {"/question/add"}, method = {RequestMethod.POST})
    @ResponseBody
    public String addQuestion(@RequestParam("title") String title,
                              @RequestParam("content") String content) {
        try {
            Question question = new Question();
            question.setTitle(HtmlUtils.htmlEscape(sensitiveWordsService.filterBySensitiveWords(title)));
            question.setContent(HtmlUtils.htmlEscape(sensitiveWordsService.filterBySensitiveWords(content)));
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

    @RequestMapping(path = {"/question/{qid}"}, method = {RequestMethod.GET})
    public String questionDetail(Model model,
                                 @PathVariable("qid") int qid) {
        Question question = questionService.findQuestionById(qid);
        model.addAttribute("question", question);
        model.addAttribute("user", userService.getUser(question.getUserId()));

        return "detail";
    }
}
