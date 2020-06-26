package com.example.flixster.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.R;
import com.example.flixster.adapters.MovieAdapter;
import com.example.flixster.adapters.ReviewAdapter;
import com.example.flixster.databinding.ActivityReviewBinding;
import com.example.flixster.models.Movie;
import com.example.flixster.models.Review;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Headers;

public class ReviewActivity extends AppCompatActivity {

    public static final String TAG = "ReviewActivity";

    RecyclerView rvReviews;
    List<Review> reviews;
    Integer id;
    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityReviewBinding binding = ActivityReviewBinding.inflate(getLayoutInflater());

        View view = binding.getRoot();
        setContentView(view);

        id =  Parcels.unwrap(getIntent().getParcelableExtra("id"));
        title =  Parcels.unwrap(getIntent().getParcelableExtra("title"));

        rvReviews = binding.rvReviews;
        reviews = new ArrayList<>();

        getSupportActionBar().setTitle(title + " Reviews");

        // Create the adapter
        final ReviewAdapter reviewAdapter = new ReviewAdapter(this, reviews);

        adapterUpdate(reviewAdapter, rvReviews);
    }

    @NotNull
    private void adapterUpdate(ReviewAdapter reviewAdapter, RecyclerView rvReviews) {
        // Set the adapter on the recycler view
        rvReviews.setAdapter(reviewAdapter);

        // Set a Layout Manager on the RV
        rvReviews.setLayoutManager(new LinearLayoutManager(this));

        apiCall(reviewAdapter);
    }

    public void apiCall(final ReviewAdapter reviewAdapter) {

        AsyncHttpClient client = new AsyncHttpClient();

        String REVIEW_URL = "https://api.themoviedb.org/3/movie/" + id + "/reviews?api_key=";
        Log.d(TAG, "REVIEW_URL: " + REVIEW_URL + "a07e22bc18f5cb106bfe4cc1f83ad8"+ "ed&language=en-US&page=1");

        client.get(REVIEW_URL + "a07e22bc18f5cb106bfe4cc1f83ad8"+ "ed&language=en-US&page=1", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray results = jsonObject.getJSONArray("results");
                    Log.i(TAG, "Results: " + results.toString());

//                    reviews.clear();

                    reviews.addAll(Review.fromJsonArray(results));
//                    Collections.sort(reviews);

                    Log.d(TAG, "Notify Adapter that data changed");

                    reviewAdapter.notifyDataSetChanged();
                    Log.i(TAG, "Reviews: " + reviews.size());
                } catch (JSONException e) {
                    Log.e(TAG, "Hit json exception", e);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d(TAG, "onFailure");
            }
        });
    }
}