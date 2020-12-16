package alroy214.xkik.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

import alroy214.xkik.MainActivity;
import alroy214.xkik.R;
import alroy214.xkik.utilities.Utils;

import static alroy214.xkik.utilities.HookKeys.appVersion;
import static alroy214.xkik.utilities.HookKeys.kikVersion;

public class HomeFragment extends Fragment {



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView statusTxt = root.findViewById(R.id.statusText);
        boolean loadedProperly = false;
        if (getActivity() != null) {
            loadedProperly = ((MainActivity)getActivity()).getSettings().checkLoadSettings(
                    ((MainActivity)getActivity()).getTweakObject(),
                    ((MainActivity)getActivity()).getKikVersionName());
        }
        final boolean finalLoadedProperly = loadedProperly;

        new Thread(){
            @Override
            public void run() {
                final String s = Utils.urlToString("https://raw.githubusercontent.com/alroy214/XKik_Refreshed/master/status.txt");
                if (getActivity() == null) {
                    return;
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject tweaksObject = ((MainActivity)getActivity()).getTweakObject();
                            if(tweaksObject != null) {
                                ((TextView) root.findViewById(R.id.versiontv)).setText(
                                                getActivity().getString(R.string.version).
                                                concat(tweaksObject.getString(appVersion)));
                                ((TextView) root.findViewById(R.id.compabilitytv)).setText(
                                        getActivity().getString(R.string.compability).
                                                concat(tweaksObject.getString(kikVersion)));
                                ((TextView) root.findViewById(R.id.currenttv)).setText(
                                        getActivity().getString(R.string.current_version).
                                                concat(((MainActivity) getActivity()).getKikVersionName()));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (s == null) {
                            statusTxt.setText(R.string.status_fetch_failed);
                        } else {
                            statusTxt.setText(s +
                                    "\n check working: " +
                                            ((MainActivity)getActivity()).getPreferences(Context.MODE_PRIVATE).getString("test_key","not working")
                                    +"\nLoaded Properly: "+ finalLoadedProperly);
                        }
                    }
                });
            }
        }.start();
        return root;
    }
}
