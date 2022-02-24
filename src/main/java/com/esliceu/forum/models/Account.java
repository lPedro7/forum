package com.esliceu.forum.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.nio.charset.StandardCharsets;
import java.util.*;


@Table(name = "account")
@Entity
public class Account {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(name = "email")
    String email;
    @Column(name = "name")
    String name;
    @Column(name = "password")
    String password;

    @Column(name = "photo")
    byte[] avatar;
    @Column(name = "role")
    String role;

    @JsonIgnore
    @OneToMany
    Set<Topic> topics;

    @JsonIgnore
    @OneToMany
    Set<Reply> replies;

    public enum Role {
        User,
        Moderator,
        Admin
    }

    public Set<Topic> getTopics() {
        return topics;
    }

    public void setTopics(Set<Topic> topics) {
        this.topics = topics;
    }

    public Set<Reply> getReplies() {
        return replies;
    }

    public void setReplies(Set<Reply> replies) {
        this.replies = replies;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatar() {

        return new String(this.avatar, StandardCharsets.UTF_8);
    }


    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    public Map<String,Object> toJsonMap() {

        Map<String, Object> json = new HashMap<>();
        json.put("avatar", getAvatar());
        json.put("email", getEmail());
        json.put("id", getId());
        json.put("name", getName());

        //permissions
        Map<String, Object> permissions = new HashMap<>();
        List<String> root = new ArrayList<>();
        root.add("own_topics:write");
        root.add("own_topics:delete");
        root.add("own_replies:write");
        root.add("own_replies:delete");
        root.add("categories:write");
        root.add("categories:delete");
        permissions.put("root", root);

        json.put("permissions", permissions);

        return json;

    }

}
