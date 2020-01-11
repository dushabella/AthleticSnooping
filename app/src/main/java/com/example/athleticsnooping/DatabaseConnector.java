package com.example.athleticsnooping;

import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import io.appbase.client.AppbaseClient;

public class DatabaseConnector {
    private String dbUrl;
    private String dbUsername;
    private String dbPassword;

    public DatabaseConnector() {
        this.dbUrl = "https://soft-eng-group-4581601701.eu-central-1.bonsaisearch.net:443";
        this.dbUsername = "n7vbcy3d66";
        this.dbPassword = "ybyjlbkp87";
    }


    public void addUser(String email, String password) {
        String index = "users";
        AppbaseClient client = new AppbaseClient(dbUrl, index, dbUsername, dbPassword);

        Map<String, String> body = new HashMap<>();
        body.put("password", DigestUtils.sha256Hex(password));
        try {
            String result = client.prepareIndex("_doc", email, body) // params: type - whatever you put, it will be "_doc" due to old versions of ES; document id; document body
                    .execute()
                    .body()
                    .string();
            System.out.println(result);
        } catch(IOException ie) {
            ie.printStackTrace();
        }
        System.out.println("Added new user.");
    }


    public boolean isRegistered(String email) {
        String index = "users";
        AppbaseClient client = new AppbaseClient(dbUrl, index, dbUsername, dbPassword);

        int result = client.prepareGet("_doc", email).execute().code();
        return result == 200;
    }


    public boolean authenticateUser(String email, String password) {
        String index = "users";
        AppbaseClient client = new AppbaseClient(dbUrl, index, dbUsername, dbPassword);
        String savedPassword = "";

        try{
            String result = client.prepareGet("_doc", email).execute().body().string();
            JSONObject json = new JSONObject(result);
            savedPassword = json.getJSONObject("_source").getString("password");
        } catch(IOException ie) {
            ie.printStackTrace();
        } catch(JSONException je) {
            je.printStackTrace();
        }

        return savedPassword.equals(DigestUtils.sha256Hex(password));
    }
}