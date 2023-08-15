package be.ehb.garbapp;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

public class Report {
    public int postId;
    public String title, description, userUid, repostUserName;
    public boolean approved = false;
    public Date createdPost = Calendar.getInstance().getTime();
    public String pictureUrl;
    public String address;
    public double lati;
    public double longti;
    public double garbPoints;


    public Report() {
    }

    public Report(int postId, String title, String description, String userUid, String repostUserName, boolean approved, Date createdPost, String pictureUrl, double lati, double longti, String address, double garbPoints) {
        this.postId = postId;
        this.title = title;
        this.description = description;
        this.userUid = userUid;
        this.repostUserName = repostUserName;
        this.approved = false;
        this.createdPost = Calendar.getInstance().getTime();
        this.pictureUrl = pictureUrl;
        this.lati = lati;
        this.longti = longti;
        this.address = address;
        this.garbPoints = garbPoints;

    }


    public double getGarbPoints() {
        return garbPoints;
    }

    public void setGarbPoints(double garbPoints) {
        this.garbPoints = garbPoints;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getRepostUserName() {
        return repostUserName;
    }

    public void setRepostUserName(String repostUserName) {
        this.repostUserName = repostUserName;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public Date getCreatedPost() {
        return createdPost;
    }

    public void setCreatedPost(Date createdPost) {
        this.createdPost = createdPost;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLati() {
        return lati;
    }

    public void setLati(double lati) {
        this.lati = lati;
    }

    public double getLongti() {
        return longti;
    }

    public void setLongti(double longti) {
        this.longti = longti;
    }
}
