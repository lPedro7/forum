package com.esliceu.forum.controllers;


import com.esliceu.forum.models.Account;
import com.esliceu.forum.models.Category;
import com.esliceu.forum.models.Reply;
import com.esliceu.forum.models.Topic;
import com.esliceu.forum.services.AccountServiceImpl;
import com.esliceu.forum.services.CategoryServiceImpl;
import com.esliceu.forum.services.ReplyServiceImpl;
import com.esliceu.forum.services.TopicServiceImpl;
import com.esliceu.forum.utils.JwtTokenUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.Instant;
import java.util.*;

@RestController
public class TopicController {

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    AccountServiceImpl accountService;

    @Autowired
    CategoryServiceImpl categoryService;

    @Autowired
    TopicServiceImpl topicService;

    @Autowired
    ReplyServiceImpl replyService;


    @GetMapping("/categories/{category}")
    public String getTopics(@PathVariable String category) throws JsonProcessingException {

        Category cat = categoryService.getByName(category);

        return new ObjectMapper().writeValueAsString(cat);
    }

    @GetMapping("/categories/{category}/topics")
    public List<Topic> getAllTopics(@PathVariable String category) {

        Category cat = categoryService.getByName(category);

        return topicService.getAllByCategoryId(cat.getId());
    }

    @PreAuthorize("hasAnyRole('User','Moderator','Admin')")
    @PostMapping("/topics")
    public Map<String,Object> newTopic(@RequestBody Map<String,Object> topicMap, @RequestHeader("Authorization") String auth) throws JsonProcessingException {


        String email = jwtTokenUtil.getUsername(auth.replace("Bearer ", ""));
        Account account = accountService.getUserByEmail(email);

        Topic topic = new Topic();
        topic.setTitle((String) topicMap.get("title"));
        topic.setContent((String) topicMap.get("content"));
        Category category = categoryService.getByName((String) topicMap.get("category"));

        topic.setCategory(category);
        topic.setCreatedAt(Date.from(Instant.now()));
        topic.setUser(account);

        topicService.newTopic(topic);


        Map<String,Object> topicResponse = new ObjectMapper().convertValue(topic,Map.class);

        topicResponse.put("_id", String.valueOf(topic.getId()));

        return topicResponse;
    }

    @GetMapping("/topics/{idtopic}")
    public Map<String,Object> getTopic(@PathVariable String idtopic) throws NumberFormatException{

        Topic t = topicService.getById(Integer.parseInt(idtopic));
        Map<String,Object> topic = new HashMap<>();
        topic.put("id",t.getId());
        topic.put("content",t.getContent());
        topic.put("createdAt",t.getCreatedAt());
        topic.put("category",t.getCategory());
        topic.put("user",t.getUser());
        topic.put("title",t.getTitle());
        topic.put("_id",t.getId());

        List<Reply> replies = List.copyOf(t.getReplies());
        List<Map<String,Object>> repliesMod = new ArrayList<>();
        for (Reply r : replies) {
            Map<String, Object> map = new HashMap<>();
            map.put("content", r.getContent());
            map.put("createdAt", r.getCreatedAt());
            map.put("topic", r.getTopic().getId());
            map.put("user", r.getUser());
            map.put("_id", r.getId());
            map.put("id", r.getId());
            repliesMod.add(map);
        }
        topic.put("replies",repliesMod);
        return topic;

    }

    @PutMapping("/topics/{id}")
    @PreAuthorize("hasAnyRole('User','Moderator','Admin')")
    public String updateTopic(@PathVariable String id,@RequestBody Map<String,Object> topicValues){

        Topic topic = topicService.getById(Integer.parseInt(id));
        topic.setTitle((String) topicValues.get("title"));
        topic.setContent((String) topicValues.get("content"));
        Category category = categoryService.getByName((String) topicValues.get("category"));
        topic.setCategory(category);
        topicService.newTopic(topic);

        return "Ok";
    }

}


