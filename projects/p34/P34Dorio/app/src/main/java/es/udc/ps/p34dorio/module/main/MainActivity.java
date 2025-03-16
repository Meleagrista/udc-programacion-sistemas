package es.udc.ps.p34dorio.module.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
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

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.main_list)
    RecyclerView mRecycler;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.search_bar)
    EditText mSearchBar;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.search_button)
    Button mSearchButton;

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

        mSearchButton.setOnClickListener(v -> {
            String query = mSearchBar.getText().toString().trim();
            if (!query.isEmpty()) {
                searchArtists(query);
            } else {
                Toast.makeText(MainActivity.this, "Please enter a search query", Toast.LENGTH_SHORT).show();
            }
        });

        //getArtists();
    }

    public boolean isInternetAvailable() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm == null)
            return false;

        NetworkCapabilities capabilities = cm.getNetworkCapabilities(cm.getActiveNetwork());
        return capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR));
    }

    /*private void getArtists() {
        String query = "artist:rancid";
        if (isInternetAvailable()) {
            Retrofit retrofit = new Retrofit.Builder().baseUrl("https://musicbrainz.org/ws/2/")
                    .addConverterFactory(GsonConverterFactory.create()).build();

            MusicBrainzAPI api = retrofit.create(MusicBrainzAPI.class);

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
            loadArtistsFromDatabase(query);
        }
    }*/

    private void searchArtists(String query) {
        if (isInternetAvailable()) {
            Retrofit retrofit = new Retrofit.Builder().baseUrl("https://musicbrainz.org/ws/2/")
                    .addConverterFactory(GsonConverterFactory.create()).build();

            MusicBrainzAPI api = retrofit.create(MusicBrainzAPI.class);

            String format = "json";
            Call<SearchArtists> call = api.searchArtistByName("artist:" + query, format);

            call.enqueue(new Callback<SearchArtists>() {
                @Override
                public void onResponse(@NonNull Call<SearchArtists> call, @NonNull Response<SearchArtists> response) {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        List<Artist> artists = response.body().getArtists();
                        Log.i(TAG, "Response OK: " + response.code());
                        if (artists != null && !artists.isEmpty()) {
                            mAdapter.setItems(artists);
                            // Save artists to the local database
                            saveArtistsToDatabase(artists);
                        } else {
                            Toast.makeText(MainActivity.this, "No artists found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.e(TAG, "Response fails: " + response.code());
                        Toast.makeText(MainActivity.this, "Failed to search artists", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<SearchArtists> call, @NonNull Throwable t) {
                    Log.e(TAG, "Response fails: " + t.getMessage());
                    Toast.makeText(MainActivity.this, "Failed to search artists", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // If no internet, load artists from local database
            loadArtistsFromDatabase(query);
        }
    }

    private void saveArtistsToDatabase(List<Artist> artists) {
        new Thread(() -> {
            for (Artist artist : artists) {
                // Check if the artist with the same ID already exists in the database
                ArtistEntity existingArtist = artistDatabase.artistDao().getArtistById(artist.getId());
                if (existingArtist == null) {
                    // If the artist doesn't exist, insert it into the database
                    ArtistEntity entity = new ArtistEntity(artist.getId(), artist.getName());
                    artistDatabase.artistDao().insert(entity);
                }
            }
        }).start();
    }

    private void loadArtistsFromDatabase(String query) {
        new Thread(() -> {
            List<ArtistEntity> artistEntities = artistDatabase.artistDao().searchArtistsByName("%" + query + "%");
            List<Artist> artists = new ArrayList<>();
            for (ArtistEntity entity : artistEntities) {
                Artist artist = new Artist(entity.getId(), entity.getName());
                artists.add(artist);
            }
            runOnUiThread(() -> {
                if (artists.isEmpty()) {
                    Toast.makeText(MainActivity.this, "No matching artists found in the database", Toast.LENGTH_SHORT).show();
                } else {
                    mAdapter.setItems(artists);
                }
            });
        }).start();
    }
}
