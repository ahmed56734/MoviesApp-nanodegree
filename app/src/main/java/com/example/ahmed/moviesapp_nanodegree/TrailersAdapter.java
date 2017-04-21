package com.example.ahmed.moviesapp_nanodegree;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ahmed on 4/22/17.
 */

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailersViewHolder> {

    private List<Trailer> mTrailers;
    private TrailerClickListener mTrailerClickListener;


    interface TrailerClickListener{
        void onTrailerClick(String url);
    }



    TrailersAdapter(TrailerClickListener trailerClickListener){
        mTrailers = new ArrayList<>();
        mTrailerClickListener = trailerClickListener;
    }

    @Override
    public TrailersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_list_item, parent, false);
        return new TrailersViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(TrailersViewHolder holder, int position) {

        holder.trailerNameTextView.setText(mTrailers.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mTrailers.size();
    }

    class TrailersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView trailerNameTextView;
        ImageView trailerIcon;

        TrailersViewHolder(View itemView) {
            super(itemView);
            trailerNameTextView = (TextView) itemView.findViewById(R.id.trailer_name);
            trailerIcon = (ImageView) itemView.findViewById(R.id.ic_play_trailer);
            trailerIcon.setOnClickListener(this);


        }

        @Override
        public void onClick(View view) {
            String url = mTrailers.get(getAdapterPosition()).getUrl();
            mTrailerClickListener.onTrailerClick(url);
        }
    }

    public void updateData(List<Trailer> trailers){
        mTrailers.clear();
        mTrailers.addAll(trailers);
        notifyDataSetChanged();
    }
}
