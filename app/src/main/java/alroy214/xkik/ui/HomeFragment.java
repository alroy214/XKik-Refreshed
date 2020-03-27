package alroy214.xkik.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import java.util.Objects;

import alroy214.xkik.R;
import alroy214.xkik.utilities.Util;

public class HomeFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView statustxt = root.findViewById(R.id.statusText);
        new Thread(){
            @Override
            public void run() {
                final String s = Util.urlToString("https://raw.githubusercontent.com/xkik-dev/xkik-status/master/status.txt");
                try {
                    Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (s == null) {
                                statustxt.setText("Could not fetch status.");
                            } else
                                statustxt.setText(s);
                        }
                    });
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }.start();
        return root;
    }
}
