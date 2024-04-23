package es.udc.ps.p34dorio.module.main.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import java.util.ArrayList;
import java.util.List;

import es.udc.ps.p34dorio.R;
import es.udc.ps.p34dorio.domain.Artist;

public class ArtistAdapter extends Adapter<ArtistAdapter.ArtistHolder> {

    private List<Artist> mItems;

    public ArtistAdapter() {

        mItems = new ArrayList<>();
    }

    public void setItems(List<Artist> items) {

        mItems = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ArtistHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                         int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_artist, parent, false);
        return new ArtistHolder(v);
    }

    @Override
    public int getItemCount() {

        return mItems.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistHolder holder,
                                 int position) {

        holder.bind(mItems.get(position));
    }

    static class ArtistHolder extends RecyclerView.ViewHolder {

        private TextView mTvId;
        private TextView mTvName;

        private ArtistHolder(View v) {

            super(v);
            mTvId = v.findViewById(R.id.row_artist_id);
            mTvName = v.findViewById(R.id.row_artist_name);
        }

        private void bind(Artist artist) {

            mTvId.setText(String.valueOf(artist.getId()));
            mTvName.setText(artist.getName());
        }
    }

}
