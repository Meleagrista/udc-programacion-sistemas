package es.udc.ps.p34dorio.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ArtistDao {
    @Insert
    void insertAll(List<ArtistEntity> artists);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(ArtistEntity artist);

    @Query("SELECT * FROM artists WHERE id = :id")
    ArtistEntity getArtistById(String id);

    @Query("SELECT * FROM artists")
    List<ArtistEntity> getAll();

    @Query("SELECT * FROM artists WHERE name LIKE :name")
    List<ArtistEntity> searchArtistsByName(String name);
}