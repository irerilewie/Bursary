package com.returno.bursarychecker;

import java.io.Serializable;
import java.util.List;

public class Upload implements Serializable {
    List<String> downloadUrls;
    String userId;
    String uploadId;
    String gender;
    String status;
    String appDate;

    public Upload(List<String> downloadUrls, String userId, String uploadId, String gender,String status,String appDate) {
        this.downloadUrls = downloadUrls;
        this.userId = userId;
        this.uploadId=uploadId;
        this.gender=gender;
        this.status=status;
        this.appDate=appDate;
    }

    public Upload() {
    }

    public String getAppDate() {
        return appDate;
    }

    public void setAppDate(String appDate) {
        this.appDate = appDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUploadId() {
        return uploadId;
    }

    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }

    public List<String> getDownloadUrls() {
        return downloadUrls;
    }

    public void setDownloadUrls(List<String> downloadUrls) {
        this.downloadUrls = downloadUrls;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
