package com.example.flixster.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Review {

    String author;
    String content;


    public Review(JSONObject jsonObject) throws JSONException {
        author = jsonObject.getString("author");
        content = jsonObject.getString("content");
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public static List<Review> fromJsonArray(JSONArray reviewJsonArray) throws JSONException {
        // create a list of review we get
        List<Review> reviews = new ArrayList<>();

        // for each element in the given json array, add the object to the reviews array
        for (int i = 0; i < reviewJsonArray.length(); i++) {
            reviews.add(new Review(reviewJsonArray.getJSONObject(i)));
        }
        return reviews;
    }

}
