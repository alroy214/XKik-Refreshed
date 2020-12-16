package alroy214.xkik.databases;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {WhoRead.class}, version = 1)
public abstract class WhoReadDatabase extends RoomDatabase {
    public abstract WhoReadDao WhoReadDao();
}