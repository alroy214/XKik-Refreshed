package alroy214.xkik.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import alroy214.xkik.R;
import alroy214.xkik.utilities.Util;

public class HomeFragment extends Fragment {

    public JSONObject loadJSONFromAsset(@Nullable Context context) {
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


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_home, container, false);

        final TextView statusTxt = root.findViewById(R.id.statusText);
        final JSONObject tweaksObject = loadJSONFromAsset(getContext());
        new Thread(){
            @Override
            public void run() {
                final String s = Util.urlToString("https://raw.githubusercontent.com/alroy214/XKik_Refreshed/master/status.txt");
                if(getContext() == null) {
                    return;
                }
                try {
                    ((TextView)root.findViewById(R.id.versiontv)).setText(
                            getContext().getString(R.string.version).
                                    concat(tweaksObject.getString("app_version")));
                    ((TextView)root.findViewById(R.id.compabilitytv)).setText(
                            getContext().getString(R.string.compability).
                                    concat(tweaksObject.getString("kik_version")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (s == null) {
                    statusTxt.setText("Could not fetch status.");
                } else
                    statusTxt.setText(s);
                }
        }.start();
        return root;
    }
}
