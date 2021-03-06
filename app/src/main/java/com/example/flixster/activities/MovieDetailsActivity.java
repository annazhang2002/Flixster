package com.example.flixster.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.flixster.R;
import com.example.flixster.databinding.ActivityMovieDetailsBinding;
import com.example.flixster.models.Movie;

import org.parceler.Parcels;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieDetailsActivity extends AppCompatActivity {

    Movie movie;
    TextView tvTitle;
    TextView tvOverview;
    RatingBar rbVoteAverage;
    ImageView ivVideo;
    ImageView playBtn;
    TextView tvRelease;
    TextView tvStarInfo;
    TextView tvGenres;
    ImageView[] recs = new ImageView[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // simple_activity.xml -> SimpleActivityBinding
        ActivityMovieDetailsBinding binding = ActivityMovieDetailsBinding.inflate(getLayoutInflater());

        // layout of activity is stored in a special property called root
        View view = binding.getRoot();
        setContentView(view);

        tvTitle = binding.tvTitle;
        tvOverview = binding.tvOverview;
        rbVoteAverage = binding.rbVoteAverage;

        rbVoteAverage.setIsIndicator(true);

        ivVideo = binding.ivVideo;
        playBtn = binding.playBtn;
        tvRelease = binding.tvRelease;
        tvStarInfo = binding.tvStarInfo;
        tvGenres = binding.tvGenres;
        recs[0] = binding.ivRec1;
        recs[1] = binding.ivRec2;
        recs[2] = binding.ivRec3;



        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d("MovieDetailsActivity", String.format("Showing details for '%s'", movie.getTitle()));

        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());
        tvRelease.setText(movie.getReleaseDate());
        tvGenres.setText("Genres: " + movie.getAllGenresString());

        Log.d("MovieDetailsActivity", "BACKDROP Path: " + movie.getBackdropPath());

        String imgUrl;
        int placeholderUrl;

        int orientation = getResources().getConfiguration().orientation;
        // if phone is in landscape
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            imgUrl = movie.getBackdropPath();
            placeholderUrl = R.drawable.flicks_backdrop_placeholder;
        } else {
            imgUrl = movie.getPosterPath();
            placeholderUrl =  R.drawable.flicks_movie_placeholder;
        }


        Glide.with(this)
                .load(imgUrl)
                .transform(new RoundedCornersTransformation(30, 10))
                .placeholder(placeholderUrl)
                .into(ivVideo);

        Log.d("MovieDetailsActivity", String.format("getVideoKey: " + movie.getVideoKey() + " for " + movie.getTitle()));
        if (movie.getVideoKey() != null && !movie.getVideoKey().isEmpty() && !movie.getVideoKey().equals("null")) {
            Glide.with(this)
                    .load(R.drawable.play_button)
                    .into(playBtn);
        } else {
            Glide.with(this)
                    .load(R.drawable.none)
                    .into(playBtn);
        }

        float voteAverage = movie.getVoteAverage().floatValue();
        if (voteAverage == 0) {
            tvStarInfo.setText(movie.getVoteCount() + " Reviews");
        } else {
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                tvStarInfo.setText(String.valueOf(voteAverage / 2.0f) + "\n" + movie.getVoteCount() + " Reviews");
            } else {
                tvStarInfo.setText(String.valueOf(voteAverage / 2.0f) + "\t\t\t\t\t           " + movie.getVoteCount() + " Reviews");

            }
        }
        rbVoteAverage.setRating(voteAverage = voteAverage > 0 ? voteAverage / 2.0f : voteAverage);

        getSupportActionBar().setTitle(movie.getTitle() + " Movie Details");

        String[] recPaths = movie.getRecs();
        for (int i = 0; i < recs.length; i++) {
            Log.d("MovieDetailsActivity", "recPaths[" + i + "] : " + recPaths[i]);
            if (recPaths[i] != null) {
                Glide.with(this)
                        .load(recPaths[i])
                        .transform(new RoundedCornersTransformation(30, 0))
                        .placeholder(R.drawable.flicks_movie_placeholder)
                        .into(recs[i]);
            }

        }

        ivVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("MovieDetailsActivity", "onClickVideo: " + movie.getTitle());
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

        tvStarInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("MovieDetailsActivity", "onClickRatingBar: " + movie.getTitle());

                if (movie.getNumReviews() != 0) {
                    Context context = getApplicationContext();
                    Intent intent = new Intent(context, ReviewActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                Log.d("MovieDetailsActivity", "Video Key: " + movie.getVideoKey());
                    intent.putExtra("id", Parcels.wrap(movie.getId()));
                    intent.putExtra("title", Parcels.wrap(movie.getTitle()));
                    context.startActivity(intent);

                }


            }
        });
    }
}