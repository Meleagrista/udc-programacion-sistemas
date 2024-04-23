package es.udc.ps.p34dorio.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ArtistDao {
    @Insert
    void insertAll(List<ArtistEntity> artists);

    @Query("SELECT * FROM artists")
    List<ArtistEntity> getAll();
}