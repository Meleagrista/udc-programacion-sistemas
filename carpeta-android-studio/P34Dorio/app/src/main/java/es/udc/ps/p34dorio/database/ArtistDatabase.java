package es.udc.ps.p34dorio.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {ArtistEntity.class}, version = 1, exportSchema = false)
public abstract class ArtistDatabase extends RoomDatabase {
    public abstract ArtistDao artistDao();
}