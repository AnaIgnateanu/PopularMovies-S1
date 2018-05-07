package com.example.android.popularmoviess1;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by anamanolache on 4/26/18.
 */

public class PosterAdapter extends RecyclerView.Adapter<PosterAdapter.PosterAdapterViewHolder> {
        private static final String TAG = PosterAdapter.class.getSimpleName();
        private List<Movie> mMovieItems;
        private final PosterAdapterOnClickHandler mClickHandler;
        private Context mContext;
        OnBottomReachedListener onBottomReachedListener;


    public interface PosterAdapterOnClickHandler {
        void onClick(Movie movie);
    }

    public PosterAdapter(PosterAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    public void setOnBottomReachedListener(OnBottomReachedListener onBottomReachedListener){

        this.onBottomReachedListener = onBottomReachedListener;
    }

    @Override
    public PosterAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        int layoutIdForListItem = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new PosterAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PosterAdapterViewHolder holder, int position) {
        Movie movie = mMovieItems.get(position);
        holder.mTitleTextView.setText(movie.getTitle());
        Drawable placeholder = mContext.getResources().getDrawable(R.drawable.icon_movie);
        Picasso.with(mContext)
                .load(movie.getPosterPathSmall().toString())
                .placeholder(placeholder)
                .into(holder.mPosterImageView);
        if (position == mMovieItems.size() - 1)
            onBottomReachedListener.onBottomReached(position);
    }


    @Override
        public int getItemCount() {
        if (mMovieItems == null || mMovieItems.isEmpty()) return 0;
        return mMovieItems.size();
    }

    public void setMovieData(List<Movie> movieData) {
        mMovieItems = movieData;
        notifyDataSetChanged();
    }

        public class PosterAdapterViewHolder extends RecyclerView.ViewHolder {
            private final TextView mTitleTextView;
            private final ImageView mPosterImageView;

            PosterAdapterViewHolder(View itemView) {
                super(itemView);
                itemView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int adapterPosition = getAdapterPosition();
                        Movie movie = mMovieItems.get(adapterPosition);
                        mClickHandler.onClick(movie);
                    }
                });
                mTitleTextView = itemView
                        .findViewById(R.id.title_textView);
                mPosterImageView = itemView
                        .findViewById(R.id.poster_imageView);
            }

        }

}
