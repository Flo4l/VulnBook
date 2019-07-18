package de.albers.vulnbook.post;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Post {

    private long postId;
    private LocalDateTime time;
    private String text;
    private long likes;
    private long userId;

    public Post(long postId, LocalDateTime time, String text, long likes, long userId) {
        this.postId = postId;
        this.time = time;
        this.text = text;
        this.likes = likes;
        this.userId = userId;
    }

    public Post(LocalDateTime time, String text, long likes, long userId) {
        this.time = time;
        this.text = text;
        this.likes = likes;
        this.userId = userId;
    }

    public long getPostId() {
        return postId;
    }

    public void setPostId(long postId) {
        this.postId = postId;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getLikes() {
        return likes;
    }

    public void setLikes(long likes) {
        this.likes = likes;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getTimeText() {
        return time.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
    }

    @Override
    public String toString() {
        return "Post{" +
                "postId=" + postId +
                ", time=" + time +
                ", text='" + text + '\'' +
                ", likes=" + likes +
                ", userId=" + userId +
                '}';
    }
}
