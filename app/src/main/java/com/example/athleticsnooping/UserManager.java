package com.example.athleticsnooping;

import android.content.Context;
import android.content.SharedPreferences;

public class UserManager {
    private String username = ""; // The username in the application is user's email address
    private SharedPreferences pref;

    public UserManager(Context context) {
        this.pref = context.getSharedPreferences("user_management", 0); // 0 - for private mode
        String tmp_username = pref.getString("username", ""); // (key, default value if not exists)
        if(!"".equals(tmp_username))
            this.username = tmp_username;
    }

    public String getUsername() {
        return username;
    }

    public boolean isLoggedIn() {
        return !username.isEmpty();
    }

    public void setUsername(String username) {
        this.username = username;
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("username", username);
        editor.commit();
        //System.out.println("zmienilem");
    }

    public void logOutUser() {
        this.username = "";
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("username", username);
        editor.commit();
    }

}
