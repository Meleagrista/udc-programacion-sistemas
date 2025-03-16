package es.udc.ps.p34dorio.data;

import es.udc.ps.p34dorio.BuildConfig;
import es.udc.ps.p34dorio.domain.SearchArtists;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface MusicBrainzAPI {

    // http://musicbrainz.org/ws/2/artist/?query=artist:rancid&fmt=json
    @Headers("User-Agent: PSiCleanArch/" + BuildConfig.VERSION_CODE + " ( cvazquez@udc.es )" )
    @GET("artist/")
    Call<SearchArtists> searchArtistByName(@Query("query") String query,
                                           @Query("fmt") String format);
}
