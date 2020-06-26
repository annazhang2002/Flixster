package com.example.flixster.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.R;
import com.example.flixster.adapters.MovieAdapter;
import com.example.flixster.databinding.ActivityMainBinding;
import com.example.flixster.models.Movie;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Headers;

public class MainActivity extends AppCompatActivity {

    public static final String NOW_PLAYING_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=";
    public static final String UPCOMING_URL = "https://api.themoviedb.org/3/movie/upcoming?api_key=";
    public static final String POPULAR_URL = "https://api.themoviedb.org/3/movie/popular?api_key=";


    // tag constant makes it easy to log data
    public static final String TAG = "MainActivity";

    List<Movie> movies;
    final String[] url = {NOW_PLAYING_URL};

    Button btnToggle1;
    Button btnToggle2;
    Button btnToggle3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());

        View view = binding.getRoot();
        setContentView(view);

        getSupportActionBar().hide();



        final RecyclerView rvMovies = binding.rvMovies;
        movies = new ArrayList<>();
        btnToggle1 = binding.btnToggle;
        btnToggle2 = binding.btnToggle2;
        btnToggle3 = binding.btnToggle3;


        // Create the adapter
        final MovieAdapter movieAdapter = new MovieAdapter(this, movies);

        adapterUpdate(movieAdapter, rvMovies, url);


        setBtnListeners(movieAdapter);

    }

    private void setBtnListeners(final MovieAdapter movieAdapter) {
        btnToggle1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClickBtnToggle - switch to now playing");
                url[0] = NOW_PLAYING_URL;
                apiCall(movieAdapter, url);
            }
        });

        btnToggle1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClickBtnToggle - switch to upcoming");
                url[0] = UPCOMING_URL;
                apiCall(movieAdapter, url);
            }
        });

        btnToggle1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClickBtnToggle - switch to popular");
                url[0] = POPULAR_URL;
                apiCall(movieAdapter, url);
            }
        });
    }

    @NotNull
    private void adapterUpdate(MovieAdapter movieAdapter, RecyclerView rvMovies, String[] url) {
        // Set the adapter on the recycler view
        rvMovies.setAdapter(movieAdapter);

        // Set a Layout Manager on the RV
        rvMovies.setLayoutManager(new LinearLayoutManager(this));

        apiCall(movieAdapter, url);
    }


    public void apiCall(final MovieAdapter movieAdapter, String[] url) {

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url[0] + getString(R.string.movies_api_key), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray results = jsonObject.getJSONArray("results");
                    Log.i(TAG, "Results: " + results.toString());

                    movies.clear();

                    movies.addAll(Movie.fromJsonArray(results));
                    Collections.sort(movies);

                    Log.d(TAG, "Notify Adapter that data changed");

                    movieAdapter.notifyDataSetChanged();
                    Log.i(TAG, "Movies: " + movies.size());
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