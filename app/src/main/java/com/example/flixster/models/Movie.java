package com.example.flixster.models;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.R;
import com.example.flixster.activities.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

@Parcel
public class Movie implements Comparable<Movie>{

    private static final String TAG = "Movie";

    String  MOVIE_VIDEO_URL = "https://api.themoviedb.org/3/movie/";

    String posterPath;
    String backdropPath;
    String title;
    String overview;
    Double vote_average;
    Integer vote_count;
    Integer id;
    String videoKey;
    String releaseDate;

    String allGenresString;
    String[] recs = new String[3];



    // no-arg, empty constructor required for Parceler
    public Movie() {}

    public Movie(JSONObject jsonObject) throws JSONException {
        posterPath = jsonObject.getString("poster_path");
        backdropPath = jsonObject.getString("backdrop_path");
        title = jsonObject.getString("title");
        overview = jsonObject.getString("overview");
        vote_average = jsonObject.getDouble("vote_average");
        vote_count = jsonObject.getInt("vote_count");
        id = jsonObject.getInt("id");
        releaseDate = jsonObject.getString("release_date");

        MOVIE_VIDEO_URL += id + "/videos?api_key=" + "a07e22bc18f5cb106bfe4cc1f83ad8ed" + "&language=en-US";
        Log.d("Movie", "Movie Video Url" + MOVIE_VIDEO_URL);

        getGenres();
        retrieveRecs();

    }

    public void retrieveKey() {
        AsyncHttpClient client = new AsyncHttpClient();

        client.get(MOVIE_VIDEO_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray results = jsonObject.getJSONArray("results");
                    Log.i(TAG, "Results: " + results.toString());
//                    Log.i(TAG, "JSONObject : " + results.getJSONObject(0).toString());
//                    Log.i(TAG, "JSONObject : " + results.getJSONObject(0).getString("site"));

                    if (results.getJSONObject(0).getString("site").equals("YouTube")) {
                        videoKey = results.getJSONObject(0).getString("key");
                    }

                    Log.i(TAG, "retrieve Video Key: " + videoKey);
                } catch (JSONException e) {
                    Log.e(TAG, "Hit json exception ", e);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d(TAG, "onFailure");
            }
        });
    }

    public void getGenres() {
        AsyncHttpClient client = new AsyncHttpClient();

        String MOVIE_DETAILS_URL = "https://api.themoviedb.org/3/movie/" + id + "?api_key=" + "a07e22bc18f5cb106bfe4cc1f83ad8ed" + "&language=en-US";
        Log.d("Movie", "MOVIE DETAILS URL: " + MOVIE_DETAILS_URL);


        client.get(MOVIE_DETAILS_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray genres = jsonObject.getJSONArray("genres");
                    Log.i(TAG, "Results: " + genres.toString());

                    String genresString = "";
                    for (int i =0; i<genres.length() - 1; i++) {
                        genresString += genres.getJSONObject(i).getString("name") + ", ";
                    }
                    genresString += genres.getJSONObject(genres.length() - 1).getString("name");

                    allGenresString = genresString;
                    Log.i(TAG, "return genres: " + allGenresString);
                } catch (JSONException e) {
                    Log.e(TAG, "Hit json exception ", e);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d(TAG, "onFailure");
            }
        });
    }

    public void retrieveRecs() {
        AsyncHttpClient client = new AsyncHttpClient();

        String MOVIE_RECS_URL = "https://api.themoviedb.org/3/movie/" + id + "/recommendations?api_key=" + "a07e22bc18f5cb106bfe4cc1f83ad8ed" + "&language=en-US&page=1";
        Log.d("Movie", "MOVIE RECS URL: " + MOVIE_RECS_URL);


        client.get(MOVIE_RECS_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray results = jsonObject.getJSONArray("results");
                    Log.i(TAG, "Recs Results: " + results.toString());

                    for (int i =0; i<3 && i< results.length(); i++) {
//                        Log.i(TAG, "Recs " + i + ": " + results.getJSONObject(i).getString("poster_path"));
                        recs[i] = "https://image.tmdb.org/t/p/w342" + results.getJSONObject(i).getString("poster_path");
                        Log.d("Movie", "rec[i]: " + recs[i]);
                    }

                    Log.i(TAG, "return recs: " + recs.toString());
                } catch (JSONException e) {
                    Log.e(TAG, "Hit json exception ", e);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d(TAG, "onFailure");
            }
        });
    }

    public String[] getRecs() {
        return recs;
    }

    public String getAllGenresString() {
        Log.d("Movie", "getAllGenresString: " + allGenresString);

        return allGenresString;
    }

    public static List<Movie> fromJsonArray(JSONArray movieJsonArray) throws JSONException {
        // create a list of movie we get
        List<Movie> movies = new ArrayList<>();

        // for each element in the given json array, add the object to the movies array
        for (int i = 0; i < movieJsonArray.length(); i++) {
            movies.add(new Movie(movieJsonArray.getJSONObject(i)));
        }
        return movies;
    }

    public Double getVoteAverage() {
        return vote_average;
    }

    public Integer getVoteCount() {
        return vote_count;
    }

    public String getBackdropPath() {
        Log.d("Movie", "getBackdropPath: " + backdropPath);
        if (backdropPath != null && !backdropPath.isEmpty() && !backdropPath.equals("null")) {
            Log.d("Movie", "HEree");
            return String.format("https://image.tmdb.org/t/p/w342/%s", backdropPath);

        } else {
            Log.d("Movie", "there");
            return String.format("https://image.tmdb.org/t/p/w342/%s", posterPath);

        }


    }

    public String getPosterPath() {
        return String.format("https://image.tmdb.org/t/p/w342/%s", posterPath);
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getVideoKey() {
        retrieveKey();
        return videoKey;
    }

    public String getReleaseDate() {
        return "Release Date: " + releaseDate;

    }

    @Override
    public int compareTo(Movie movie) {
        return movie.vote_average.compareTo(vote_average);
    }
}
