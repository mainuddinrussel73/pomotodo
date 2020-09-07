package com.example.mainuddin.pomotodo.adapters;

import com.example.mainuddin.pomotodo.model.Movie;

import java.util.List;

public class MovieAdapterByGenre extends BaseMovieAdapter {


    public MovieAdapterByGenre(List<Movie> itemList) {
        super(itemList);
    }

    @Override
    public boolean onPlaceSubheaderBetweenItems(int position) {
        final String movieGenre = movieList.get(position).getGroup();
        final String nextMovieGenre = movieList.get(position + 1).getGroup();

        return !movieGenre.equals(nextMovieGenre);
    }

    @Override
    public void onBindItemViewHolder(final MovieViewHolder holder, final int position) {
        final Movie movie = movieList.get(position);

        holder.textMovieTitle.setText(movie.getTitle());

        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClicked(movie));
    }

    @Override
    public void onBindSubheaderViewHolder(SubheaderHolder subheaderHolder, int nextItemPosition) {
        super.onBindSubheaderViewHolder(subheaderHolder, nextItemPosition);
        final Movie nextMovie = movieList.get(nextItemPosition);
        final String genre = nextMovie.getGroup();
        final String subheaderText = genre;
        subheaderHolder.mSubheaderText.setText(subheaderText);
    }
}
