package com.example.flixster.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.flixster.activities.MovieDetailsActivity;
import com.example.flixster.R;
import com.example.flixster.models.Movie;

import org.parceler.Parcels;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    // need context on where this adapter is constructed from
    Context context;

    List<Movie> movies;

    public MovieAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }


    // Usually involved inflating a layout from XML and returning the holder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("MovieAdapter", "onCreateViewHolder");
        View movieView = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(movieView);
    }


    // Involves populating data into the item through holder
    // take data from a position and put it into the given viewholder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("MovieAdapter", "onBindViewHolder " + position);
        // Get the movie at the passed in position
        Movie movie = movies.get(position);
        // Bind the movie data into the viewholder
        holder.bind(movie);
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tvTitle;
        TextView tvOverview;
        ImageView ivPoster;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvOverview = itemView.findViewById(R.id.tvOverview);
            ivPoster = itemView.findViewById(R.id.ivPoster);

            itemView.setOnClickListener(this);

        }

        public void bind(Movie movie) {
            tvTitle.setText(movie.getTitle());
            Integer voteCount = movie.getVoteCount();

            String overview = movie.getOverview();
            Integer periodI = overview.indexOf('.') + 1;
            String shortOverview = overview.substring(0, periodI);
            if (periodI < overview.length()) {
                shortOverview += "..";
            }
            if (voteCount == 0) {
                tvOverview.setText(shortOverview + "\n\nRating: " + "No reviews yet");
            } else {
                tvOverview.setText(shortOverview + "\n\nRating: " + (movie.getVoteAverage()/2.0f) + "/5 stars");
            }

            String imgUrl;
            int placeholderUrl;

            // if phone is in landscape
            if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                imgUrl = movie.getBackdropPath();
                placeholderUrl = R.drawable.flicks_backdrop_placeholder;
            } else {
                imgUrl = movie.getPosterPath();
                placeholderUrl =  R.drawable.flicks_movie_placeholder;
            }

            Glide.with(context)
                    .load(imgUrl)
                    .transform(new RoundedCornersTransformation(30, 10))
                    .placeholder(placeholderUrl)
                    .into(ivPoster);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();

            // making sure the position is valid
            if (position != RecyclerView.NO_POSITION) {
                Movie movie = movies.get(position);

                // creating a new intent to go to the new activity
                Intent intent = new Intent(context, MovieDetailsActivity.class);

                // pass information to the intent with the parceler
                intent.putExtra(Movie.class.getSimpleName(), Parcels.wrap(movie));

                context.startActivity(intent);
            }
        }
    }
}
