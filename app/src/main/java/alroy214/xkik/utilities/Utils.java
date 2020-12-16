package alroy214.xkik.utilities;

import android.content.Context;

import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import de.robv.android.xposed.XposedHelpers;

public class Utils {
    /**
     * Gat a field from an object
     *
     * @param obj The object
     * @param fld The field
     * @return The field, or null if it doesn't exist
     */
    public static Object getObjField(Object obj, String fld) {
        try {
            return XposedHelpers.getObjectField(obj, fld);
        } catch (NoSuchFieldError e) {
            return null;
        }

    }


    /**
     * Reads a url to a string, ignoring newlines
     *
     * @param url the url
     * @return the url contents
     */
    public static String urlToString(String url) {
        try {
            String out = "";
            URL urlo = new URL(url);
            InputStream is = urlo.openStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String line;
            while ((line = br.readLine()) != null)
                out += line;

            br.close();
            is.close();
            return out;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static JSONObject loadJSONFromAsset(@Nullable Context context) {
        if(context == null) {
            return null;
        }
        String json = null;
        try {
            InputStream is = context.getAssets().open("tweaks.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            int read = is.read(buffer);

            is.close();

            json = new String(buffer, StandardCharsets.UTF_8);


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        try {
            return new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
