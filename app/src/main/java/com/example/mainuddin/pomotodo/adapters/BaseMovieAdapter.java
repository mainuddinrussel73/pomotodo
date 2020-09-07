package com.example.mainuddin.pomotodo.adapters;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.CallSuper;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mainuddin.pomotodo.R;
import com.example.mainuddin.pomotodo.model.Movie;
import com.zhukic.sectionedrecyclerview.SectionedRecyclerViewAdapter;

import java.util.List;

public abstract class BaseMovieAdapter extends SectionedRecyclerViewAdapter<BaseMovieAdapter.SubheaderHolder, BaseMovieAdapter.MovieViewHolder> {

    public interface OnItemClickListener {
        void onItemClicked(Movie movie);

        void onSubheaderClicked(int position);
    }

    List<Movie> movieList;

    OnItemClickListener onItemClickListener;

    static class SubheaderHolder extends RecyclerView.ViewHolder {

        private static Typeface meduiumTypeface = null;

        TextView mSubheaderText;
        ImageView mArrow;

        SubheaderHolder(View itemView) {
            super(itemView);
            this.mSubheaderText = itemView.findViewById(R.id.subheaderText);
            this.mArrow = itemView.findViewById(R.id.arrow);

            if (meduiumTypeface == null) meduiumTypeface = Typeface.DEFAULT;
        }

    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {

        TextView textMovieTitle;

        MovieViewHolder(View itemView) {
            super(itemView);
            this.textMovieTitle = itemView.findViewById(R.id.movieTitle);
        }
    }

    BaseMovieAdapter(List<Movie> itemList) {
        super();
        this.movieList = itemList;
    }

    @Override
    public MovieViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        return new MovieViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false));
    }

    @Override
    public SubheaderHolder onCreateSubheaderViewHolder(ViewGroup parent, int viewType) {
        return new SubheaderHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_header, parent, false));
    }

    @Override
    @CallSuper
    public void onBindSubheaderViewHolder(SubheaderHolder subheaderHolder, int nextItemPosition) {

        boolean isSectionExpanded = isSectionExpanded(getSectionIndex(subheaderHolder.getAdapterPosition()));

        if (isSectionExpanded) {
            subheaderHolder.mArrow.setImageDrawable(ContextCompat.getDrawable(subheaderHolder.itemView.getContext(), R.drawable.ic_arrow_drop_up_black_24dp));
        } else {
            subheaderHolder.mArrow.setImageDrawable(ContextCompat.getDrawable(subheaderHolder.itemView.getContext(), R.drawable.ic_baseline_keyboard_arrow_down_24));
        }

        subheaderHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onSubheaderClicked(subheaderHolder.getAdapterPosition());
            }
        });

    }

    @Override
    public int getItemSize() {
        return movieList.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

}
