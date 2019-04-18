package com.example.hugopeuz.epicture;

import android.media.session.MediaSession;
import android.util.Log;

import java.io.Serializable;
import java.util.Map;

class User implements Serializable {
    String ClientName;
    String AccessToken;
    String RefreshToken;
    String TokenType;
    String AccountType;
    String ClientId;

    public User(Map<String, String> data){
        AccessToken = data.get("access_token");
        RefreshToken = data.get("refresh_token");
        TokenType = data.get("token_type");
        ClientName = data.get("account_username");
        AccountType = data.get("account_id");
        ClientId = data.get("account_id");
    }
}
