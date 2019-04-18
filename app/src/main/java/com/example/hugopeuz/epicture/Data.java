package com.example.hugopeuz.epicture;

import java.util.HashMap;
import java.util.Map;

public class Data {
    private String avatar;
    private String avatarName;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAvatarName() {
        return avatarName;
    }

    public void setAvatarName(String avatarName) {
        this.avatarName = avatarName;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}
