package com.example.moop_finalproject;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    List<Movies> moviesList;
    Context context;

    public MovieAdapter(List<Movies> moviesList, Context context) {
        this.moviesList = moviesList;
        this.context = context;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_card, parent, false);
        MovieViewHolder holder = new MovieViewHolder(view); 

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapter.MovieViewHolder holder, int position) {
        String movieID = moviesList.get(position).getImdbID();
        holder.tv_movieTitle.setText(moviesList.get(position).getTitle());
        holder.tv_year.setText(moviesList.get(position).getYear());
        holder.tv_type.setText(moviesList.get(position).getType());
        holder.tv_dotYear.setText("·");
        Glide.with(this.context).load(moviesList.get(position).getPosterUrl()).into(holder.img_poster);
        if(MainActivity.watchListDatabase.watchDao().isInWatchList(movieID) == 1){
            holder.img_fav.setImageResource(R.drawable.ic_favorite);
        } else {
            holder.img_fav.setImageResource(R.drawable.ic_favorite_border);
        }

//        Set click listener
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MovieDetail.class);
                intent.putExtra("imdbID", moviesList.get(position).getImdbID());
                context.startActivity(intent);
            }
        });


        holder.img_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WatchList watchList = new WatchList();
                watchList.setImbdID(movieID);

                if(MainActivity.watchListDatabase.watchDao().isInWatchList(movieID) != 1){
                    holder.img_fav.setImageResource(R.drawable.ic_favorite);
                    MainActivity.watchListDatabase.watchDao().addData(watchList);
                    Toast.makeText(context, "Movie has been added to watchlist successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    holder.img_fav.setImageResource(R.drawable.ic_favorite_border);
                    MainActivity.watchListDatabase.watchDao().deleteByImdbID(movieID);
                    Toast.makeText(context, "Movie has been removed to watchlist successfully!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView img_poster;
        TextView tv_movieTitle;
        TextView tv_year;
        TextView tv_type;
        TextView tv_dotYear;
        ImageButton img_fav;
        ConstraintLayout parentLayout;


        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            img_poster = itemView.findViewById(R.id.img_poster);
            tv_movieTitle = itemView.findViewById(R.id.tv_movieTitle);
            tv_year = itemView.findViewById(R.id.tv_year);
            tv_type = itemView.findViewById(R.id.tv_type);
            tv_dotYear = itemView.findViewById(R.id.tv_dotYear);
            parentLayout = itemView.findViewById(R.id.movie_card);
            img_fav = itemView.findViewById(R.id.img_fav);
        }
    }
}
