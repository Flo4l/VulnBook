package de.albers.vulnbook.post;

import java.time.LocalDateTime;

public class Post {

    private long postId;
    private LocalDateTime time;
    private String text;
    private long likes;
    private long userid;

    public Post(long postId, LocalDateTime time, String text, long likes, long userid) {
        this.postId = postId;
        this.time = time;
        this.text = text;
        this.likes = likes;
        this.userid = userid;
    }

    public Post(LocalDateTime time, String text, long likes, long userid) {
        this.time = time;
        this.text = text;
        this.likes = likes;
        this.userid = userid;
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

    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }

    @Override
    public String toString() {
        return "Post{" +
                "postId=" + postId +
                ", time=" + time +
                ", text='" + text + '\'' +
                ", likes=" + likes +
                ", userid=" + userid +
                '}';
    }
}
