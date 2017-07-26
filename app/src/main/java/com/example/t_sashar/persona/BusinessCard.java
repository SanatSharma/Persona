package com.example.t_sashar.persona;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by JSoyinka on 7/25/17.
 */

public class BusinessCard {
    public String user_name;
    public String user_email;
    public String user_number;
    public String user_organization;
    public String Id;
    public HashMap<String, ArrayList<String>> tagMap;

    public BusinessCard(String name, String email, String number, String org) {
        this.user_name = name;
        this.user_email = email;
        this.user_number = number;
        this.user_organization = org;

        HashMap<String, ArrayList<String>> map = new HashMap<>();
        ArrayList<String> test_tags = new ArrayList<>();
        test_tags.add("Tag1");
        test_tags.add("Tag2");
        map.put("Id1", test_tags);
        tagMap = map;
    }
}
