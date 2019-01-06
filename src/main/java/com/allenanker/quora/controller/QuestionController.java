package com.allenanker.quora.controller;

import com.allenanker.quora.model.*;
import com.allenanker.quora.service.CommentService;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    @Autowired
    CommentService commentService;

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
        List<Comment> commentList = commentService.getCommentsByEntity(qid, EntityType.ENTITY_QUESTION);
        List<ViewObject> comments = new ArrayList<>();
        for (Comment comment : commentList) {
            ViewObject vo = new ViewObject();
            vo.set("comment", comment);
            vo.set("user", userService.getUser(comment.getUserId()));
            comments.add(vo);
        }
        model.addAttribute("comments", comments);

        return "detail";
    }
}
