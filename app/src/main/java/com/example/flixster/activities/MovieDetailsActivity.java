package com.example.flixster.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.flixster.R;
import com.example.flixster.models.Movie;

import org.parceler.Parcels;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieDetailsActivity extends AppCompatActivity {

    Movie movie;
    TextView tvTitle;
    TextView tvOverview;
    RatingBar rbVoteAverage;
    ImageView ivVideo;
    ImageView playBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        tvTitle = findViewById(R.id.tvTitle);
        tvOverview = findViewById(R.id.tvOverview);
        rbVoteAverage = findViewById(R.id.rbVoteAverage);
        ivVideo = findViewById(R.id.ivVideo);
        playBtn = findViewById(R.id.playBtn);



        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d("MovieDetailsActivity", String.format("Showing details for '%s'", movie.getTitle()));

        Log.d("MovieDetailsActivity", String.format("getVideoKey: " + movie.getVideoKey()));
        if (movie.getVideoKey() != null) {
            Glide.with(this)
                    .load(R.drawable.play_button)
                    .into(playBtn);
        }

        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());

        Log.d("MovieDetailsActivity", "BACKDROP Path: " + movie.getBackdropPath());

        Glide.with(this)
                .load(movie.getBackdropPath())
                .transform(new RoundedCornersTransformation(30, 10))
                .placeholder(R.drawable.flicks_backdrop_placeholder)
                .into(ivVideo);

        float voteAverage = movie.getVoteAverage().floatValue();
        rbVoteAverage.setRating(voteAverage = voteAverage > 0 ? voteAverage / 2.0f : voteAverage);

        getSupportActionBar().setTitle(movie.getTitle() + " Movie Details");

        ivVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("MovieDetailsActivity", "onClickVideo");
                Context context = getApplicationContext();
                Intent intent = new Intent(context, MovieTrailerActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Log.d("MovieDetailsActivity", "Video Key: " + movie.getVideoKey());
                if (movie.getVideoKey() != null) {
                    intent.putExtra("videoId", Parcels.wrap(movie.getVideoKey()));
                    context.startActivity(intent);

                }


            }
        });
    }
}