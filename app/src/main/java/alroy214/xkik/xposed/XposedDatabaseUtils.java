package alroy214.xkik.xposed;

import alroy214.xkik.databases.WhoRead;
import alroy214.xkik.databases.WhoReadDatabase;
import de.robv.android.xposed.XposedBridge;

public class XposedDatabaseUtils {


    /**
     * Add a user who read a message
     *
     * @param who  Who read it
     * @param uuid UUID of message
     */
    public static void addWhoRead(WhoReadDatabase whoReadDatabase, String who, String uuid) {
        if(whoReadDatabase != null) {
            whoReadDatabase.WhoReadDao().insert(new WhoRead(who, uuid));
            XposedBridge.log("XKIK - who add: " + uuid);
        }
        else {
            XposedBridge.log("XKIK - who Problem");
        }
    }

    /**
     * Purges the config file from old read notifications, to keep file size small
     */
    public void purgeWhoRead() {
        /*
        ArrayList<String> del = new ArrayList<>();
        for (String key : whoRead.keySet()) {
            if (whoRead.get(key).getLong() < System.currentTimeMillis()) {
                del.add(key);
            }
        }
        for (String key : del){
            whoRead.remove(key);
        }
    */}
}
