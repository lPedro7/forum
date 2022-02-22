package com.esliceu.forum.controllers;

import com.esliceu.forum.models.Account;
import com.esliceu.forum.models.Reply;
import com.esliceu.forum.models.Topic;
import com.esliceu.forum.services.AccountServiceImpl;
import com.esliceu.forum.services.ReplyServiceImpl;
import com.esliceu.forum.services.TopicService;
import com.esliceu.forum.services.TopicServiceImpl;
import com.esliceu.forum.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestController
public class ReplyController {

    @Autowired
    ReplyServiceImpl replyService;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    AccountServiceImpl accountService;

    @Autowired
    TopicServiceImpl topicService;

    @PreAuthorize("hasAnyRole('User','Moderator','Admin')")
    @PostMapping("/topics/{id}/replies")
    public Map<String, Object> newReply(@RequestHeader("Authorization") String auth, @PathVariable String id, @RequestBody Map data){

        String email = jwtTokenUtil.getUsername(auth.replace("Bearer ",""));

        Account account = accountService.getUserByEmail(email);

        String content = (String) data.get("content");

        Reply reply = new Reply();

        Topic topic = topicService.getById(Integer.parseInt(id));

        reply.setTopic(topic);
        reply.setContent(content);
        reply.setUser(account);
        reply.setCreatedAt(Date.from(Instant.now()));

        replyService.newReply(reply);

        Map<String,Object> replyMap = new HashMap<>();
        replyMap.put("id",reply.getId());
        replyMap.put("_id",reply.getId());
        replyMap.put("user",reply.getUser());
        replyMap.put("createdAt",reply.getCreatedAt());
        replyMap.put("content",reply.getContent());

        return replyMap;
    }

    @PreAuthorize("hasAnyRole('Moderator','Admin')")
    @PutMapping("/topics/{idtopic}/replies/{idreply}")
    public String updateReply(@PathVariable String idreply,@RequestBody Map newReply){

        Reply reply = replyService.getById(Integer.parseInt(idreply));
        reply.setContent((String) newReply.get("content"));
        return "ok";
    }

    @PreAuthorize("hasAnyRole('Moderator','Admin')")
    @DeleteMapping("/topics/{idtopic}/replies/{idreply}")
    public String deleteReply(@PathVariable String idreply){

        Reply reply = replyService.getById(Integer.parseInt(idreply));
        replyService.deleteReply(reply);
        return "ok";
    }

    @ExceptionHandler
    public ResponseEntity<Map<String,String>> exceptionHandler(Exception ex){
        Map<String,String> map = new HashMap();
        System.out.println(ex);
        map.put("message",ex.getMessage());
        return ResponseEntity.badRequest().body(map);
    }

}
