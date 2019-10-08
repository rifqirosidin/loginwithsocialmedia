package com.example.tugaseai.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tugaseai.DetailPost;
import com.example.tugaseai.R;
import com.example.tugaseai.model.Post;

import java.util.ArrayList;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolderPost> {
    Context context;
    List<Post> lisPost;

    public PostAdapter(Context context, List<Post> lisPost) {
        this.context = context;
        this.lisPost = lisPost;
    }


    public List<Post> getLisPost() {
        return lisPost;
    }

    @NonNull
    @Override
    public ViewHolderPost onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);

        return new  ViewHolderPost(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderPost viewHolderPost, int i) {
        viewHolderPost.tvTitle.setText(getLisPost().get(i).getTitle());
        viewHolderPost.tvBody.setText(getLisPost().get(i).getBody());

    }

    @Override
    public int getItemCount() {
        return getLisPost().size();
    }

    public class ViewHolderPost extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvTitle, tvBody;
        public ViewHolderPost(@NonNull final View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.title_post);
            tvBody = itemView.findViewById(R.id.body);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(itemView.getContext(), DetailPost.class);
            intent.putExtra("TITLE", getLisPost().get(getAdapterPosition()).getTitle());
            intent.putExtra("BODY", getLisPost().get(getAdapterPosition()).getBody());
            intent.putExtra("ID", getLisPost().get(getAdapterPosition()).getId());
            itemView.getContext().startActivity(intent);
        }
    }
}
