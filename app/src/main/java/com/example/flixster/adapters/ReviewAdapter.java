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
import com.example.flixster.R;
import com.example.flixster.models.Review;

import org.parceler.Parcels;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    // need context on where this adapter is constructed from
    Context context;

    List<Review> reviews;

    public ReviewAdapter(Context context, List<Review> reviews) {
        this.context = context;
        this.reviews = reviews;
    }


    // Usually involved inflating a layout from XML and returning the holder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("ReviewAdapter", "onCreateViewHolder");
        View reviewView = LayoutInflater.from(context).inflate(R.layout.item_review, parent, false);
        return new ViewHolder(reviewView);
    }


    // Involves populating data into the item through holder
    // take data from a position and put it into the given viewholder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("ReviewAdapter", "onBindViewHolder " + position);
        // Get the review at the passed in position
        Review review = reviews.get(position);
        // Bind the review data into the viewholder
        holder.bind(review);
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvAuthor;
        TextView tvContent;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            tvContent = itemView.findViewById(R.id.tvContent);

        }

        public void bind(Review review) {
            tvAuthor.setText(review.getAuthor());
            tvContent.setText(review.getContent());
        }

    }
}
