package es.udc.ps.p34dorio.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "artists")
public class ArtistEntity {
    @PrimaryKey
    @NonNull
    private String id;
    private String name;

    public ArtistEntity(@NonNull String id, String name) {
        this.id = id;
        this.name = name;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}