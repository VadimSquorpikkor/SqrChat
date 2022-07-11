package com.squorpikkor.sqrchat;

public class ChatMessage {

    private String text;
    private String name;
    private String ImgUrl;

    public ChatMessage() {
    }

    public ChatMessage(String text, String name, String imgUrl) {
        this.text = text;
        this.name = name;
        ImgUrl = imgUrl;
    }

    public String getText() {
        return text;
    }

    public String getName() {
        return name;
    }

    public String getImgUrl() {
        return ImgUrl;
    }


    public void setText(String text) {
        this.text = text;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImgUrl(String imgUrl) {
        ImgUrl = imgUrl;
    }
}
