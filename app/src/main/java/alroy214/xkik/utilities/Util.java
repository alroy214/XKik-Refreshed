package alroy214.xkik.utilities;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class Util {
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
}
