package com.esliceu.forum.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "topic")
public class Topic {

    @Id
    @GeneratedValue
    int id;

    @Column(name = "title")
    String title;

    @Column(name = "content")
    String content;

    @Column(name = "views")
    int views;

    @Column(name = "createdAt")
    Date createdAt;

    @ManyToOne
    Account user;

    @Column(name = "category_id",insertable = false,updatable = false)
    int categoryId;

    @ManyToOne
    Category category;

    @JsonIgnore
    @OneToMany(mappedBy = "topic")
    Set<Reply> replies;

    public Set<Reply> getReplies() {
        return replies;
    }

    public void setReplies(Set<Reply> replies) {
        this.replies = replies;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Account getUser() {
        return user;
    }

    public void setUser(Account account) {
        this.user = account;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
