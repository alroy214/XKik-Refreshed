package alroy214.xkik.databases;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import static alroy214.xkik.utilities.XposedStatics.WEEK_IN_MILLISECONDS;

@Entity
public class WhoRead {
    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "messageTime")
    public long messageTime;

    @ColumnInfo(name = "uuid")
    public String uuid;

    @ColumnInfo(name = "whoRead")
    String whoRead;

    public WhoRead(String whoRead, String uuid) {
        messageTime = System.currentTimeMillis() + WEEK_IN_MILLISECONDS;
        this.whoRead = whoRead;
        this.uuid = uuid;
    }

    @NonNull
    @Override
    public String toString() {
        return whoRead;
    }
}
