package com.example.tugaseai.ajeng;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tugaseai.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolderMovie>{

    Context context;
    ArrayList<dataAPI> listMovie;




    public MovieAdapter(Context context, ArrayList<dataAPI> listMovie) {
        this.context = context;
        this.listMovie = listMovie;
    }

    public ArrayList<dataAPI> getListMovie() {
        return listMovie;
    }

    @NonNull
    @Override
    public ViewHolderMovie onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.itemmovie, viewGroup, false);
        return new ViewHolderMovie(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderMovie holder, int i) {

        holder.title.setText(getListMovie().get(i).getTitle());
        holder.release.setText(getListMovie().get(i).getRelease_date());
        Picasso.get()
                .load("https://image.tmdb.org/t/p/w185_and_h278_bestv2/"+getListMovie().get(i).getPoster_path())
                .into(holder.img);

    }

    @Override
    public int getItemCount() {
        return getListMovie().size();
    }

    public class ViewHolderMovie extends RecyclerView.ViewHolder {
        TextView title, release;
        ImageView img;
        public ViewHolderMovie(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.titleMovie);
            img = itemView.findViewById(R.id.imgMovie);
            release = itemView.findViewById(R.id.releaseMovie);
        }
    }
}
