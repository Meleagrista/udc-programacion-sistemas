package es.udc.ps.p34dorio.domain;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchArtists {

    @SerializedName("count")
    private int count;

    @SerializedName("artists")
    private List<Artist> artists;

    public SearchArtists(int count,
                         List<Artist> artists) {

        this.count = count;
        this.artists = artists;
    }

    public int getCount() {

        return count;
    }

    public List<Artist> getArtists() {

        return artists;
    }
}
