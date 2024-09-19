package com.example.recall.model;

import java.util.HashMap;
import java.util.Map;

public class User {
    private String name;
    private String email;

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
   public Map<String, Object> map() {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("name", this.name);
        userMap.put("email", this.email);
        return userMap;
    }
}
