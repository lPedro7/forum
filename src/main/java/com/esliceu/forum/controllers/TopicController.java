package com.esliceu.forum.controllers;


import com.esliceu.forum.models.Account;
import com.esliceu.forum.models.Category;
import com.esliceu.forum.models.Topic;
import com.esliceu.forum.services.AccountServiceImpl;
import com.esliceu.forum.services.CategoryServiceImpl;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @PreAuthorize("")
    @GetMapping("/categories/{category}")
    public String getTopics(@PathVariable String category) throws JsonProcessingException {

        Category cat = categoryService.getByName(category);


        System.out.println("Current category = " + category);
        System.out.println("Get Topics");
        return new ObjectMapper().writeValueAsString(cat);
    }

    @GetMapping("/categories/{category}/topics")
    public List<Topic> getAllTopics(@PathVariable String category) {

        System.out.println("Categoria actual = " + category);
        System.out.println("GetAllTopics");


        Category cat = categoryService.getByName(category);

        return topicService.getAllByCategoryId(cat.getId());
    }

    @PostMapping("/topics")
    public String newTopic(@RequestBody String data, @RequestHeader("Authorization") String auth) throws JsonProcessingException {

        System.out.println(data);

        String email = jwtTokenUtil.getUsername(auth.replace("Bearer ", ""));
        Account account = accountService.getUserByEmail(email);

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> topicMap = objectMapper.readValue(data, Map.class);

        Topic topic = new Topic();
        topic.setTitle(topicMap.get("title"));
        topic.setContent(topicMap.get("content"));

        Category category = categoryService.getByName(topicMap.get("category"));

        topic.setCategory(category);
        topic.setCreatedAt(Date.from(Instant.now()));

        System.out.println("account = " + account.getName());
        topic.setUser(account);

        topicService.newTopic(topic);

        System.out.println("GetUser " + topic.getUser());

        return new ObjectMapper().writeValueAsString(topic);
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

        return topic;

    }

}


