package es.udc.ps.p34dorio.module.main;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import es.udc.ps.p34dorio.R;
import es.udc.ps.p34dorio.data.MusicBrainzAPI;
import es.udc.ps.p34dorio.database.ArtistDatabase;
import es.udc.ps.p34dorio.database.ArtistEntity;
import es.udc.ps.p34dorio.domain.Artist;
import es.udc.ps.p34dorio.domain.SearchArtists;
import es.udc.ps.p34dorio.module.BaseActivity;
import es.udc.ps.p34dorio.module.main.adapter.ArtistAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends BaseActivity {

    private ArtistDatabase artistDatabase;

    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.main_list)
    RecyclerView mRecycler;

    private ArtistAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        artistDatabase = Room.databaseBuilder(getApplicationContext(), ArtistDatabase.class, "artist-database").build();

        setUpView();
    }

    private void setUpView() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecycler.setLayoutManager(linearLayoutManager);

        mAdapter = new ArtistAdapter();
        mRecycler.setAdapter(mAdapter);

        getArtists();
    }

    public boolean isInternetAvailable() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm == null)
            return false;

        NetworkCapabilities capabilities = cm.getNetworkCapabilities(cm.getActiveNetwork());
        return capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR));
    }

    private void getArtists() {
        if (isInternetAvailable()) {
            Retrofit retrofit = new Retrofit.Builder().baseUrl("https://musicbrainz.org/ws/2/")
                    .addConverterFactory(GsonConverterFactory.create()).build();

            MusicBrainzAPI api = retrofit.create(MusicBrainzAPI.class);

            String query = "artist:rancid";
            String format = "json";
            Call<SearchArtists> call = api.searchArtistByName(query, format);

            call.enqueue(new Callback<SearchArtists>() {
                @Override
                public void onResponse(@NonNull Call<SearchArtists> call, @NonNull Response<SearchArtists> response) {
                    if (response.isSuccessful()) {
                        Log.i(TAG, "Response OK: " + response.code());
                        assert response.body() != null;
                        mAdapter.setItems(response.body().getArtists());
                        // Save artists to the local database
                        saveArtistsToDatabase(response.body().getArtists());
                    } else {
                        Log.e(TAG, "Response fails: " + response.code());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<SearchArtists> call, @NonNull Throwable t) {
                    Log.e(TAG, "Response fails: " + t.getMessage());
                    Toast.makeText(MainActivity.this, R.string.error_general, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // Si no hay conexión a internet, recuperar datos de la base de datos local
            loadArtistsFromDatabase();
        }
    }

    private void saveArtistsToDatabase(List<Artist> artists) {
        new Thread(() -> {
            List<ArtistEntity> artistEntities = new ArrayList<>();
            for (Artist artist : artists) {
                ArtistEntity entity = new ArtistEntity(artist.getId(), artist.getName());
                artistEntities.add(entity);
            }
            artistDatabase.artistDao().insertAll(artistEntities);
        }).start();
    }

    private void loadArtistsFromDatabase() {
        new Thread(() -> {
            List<ArtistEntity> artistEntities = artistDatabase.artistDao().getAll();
            List<Artist> artists = new ArrayList<>();
            for (ArtistEntity entity : artistEntities) {
                Artist artist = new Artist(entity.getId(), entity.getName());
                artists.add(artist);
            }
            runOnUiThread(() -> mAdapter.setItems(artists));
        }).start();
    }
}
