package alroy214.xkik.databases;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
@Dao
public interface WhoReadDao {
        @Query("SELECT * FROM WhoRead")
        List<WhoRead> getAll();

        @Query("SELECT * FROM WhoRead WHERE uuid LIKE :uuid")
        List<WhoRead> findByUuid(String uuid);

        @Query("SELECT * FROM WhoRead WHERE messageTime <= :currentTime")
        List<WhoRead> findByTime(long currentTime);

        @Insert
        void insert(WhoRead whoRead);

        @Update
        void update(WhoRead whoRead);

        @Delete
        void delete(WhoRead user);
}
