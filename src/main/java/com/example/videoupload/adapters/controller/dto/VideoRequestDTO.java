package com.example.videoupload.adapters.controller.dto;

import com.example.videoupload.domain.enums.VideoStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

public class VideoRequestDTO {

    @JsonProperty("id")
    @Schema(example = "123e4567-e89b-12d3-a456-426614174000")
    private String id;

    @JsonProperty("url")
    @Schema(example = "https://www.youtube.com/watch?v=123456")
    private String url;

    @JsonProperty("userName")
    @Schema(example = "user123")
    private String userName;

    @JsonProperty("userMail")
    @Schema(example = "user@email.com")
    private String userMail;

    @JsonProperty("status")
    @Schema(example = "EM_PROCESSAMENTO")
    private VideoStatus status;

    public VideoRequestDTO(String id, String url, String userName, String userMail, VideoStatus status) {
        this.id = id;
        this.url = url;
        this.userName = userName;
        this.userMail = userMail;
        this.status = status;
    }

    public VideoRequestDTO(){
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserMail() {
        return userMail;
    }

    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }

    public VideoStatus getStatus() {
        return status;
    }

    public void setStatus(VideoStatus status) {
        this.status = status;
    }
}
