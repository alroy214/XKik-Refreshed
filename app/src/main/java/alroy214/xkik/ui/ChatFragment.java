package alroy214.xkik.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import net.cachapa.expandablelayout.ExpandableLayout;

import alroy214.xkik.R;
import alroy214.xkik.settings.Settings;

public class ChatFragment extends Fragment {

    private ExpandableLayout lurkEL;
    private Settings settings;

    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chat, container, false);
        // Inflate the layout for this fragment
        settings = new Settings(getContext());


        Switch meetRecpt = rootView.findViewById(R.id.meet_switch);
        Switch readRecpt = rootView.findViewById(R.id.read_recpt_switch);
        Switch typingRecpt = rootView.findViewById(R.id.typing_recpt_switch);
        Switch fakeCam = rootView.findViewById(R.id.fake_cam_switch);
        Switch lurkDetector = rootView.findViewById(R.id.lurk_detector);
        Switch lurkToast = rootView.findViewById(R.id.lurk_toast);
        Switch longCam = rootView.findViewById(R.id.long_cam);
        Switch disableFwd = rootView.findViewById(R.id.disable_fwd);
        Switch disableSave = rootView.findViewById(R.id.disable_save);
        Switch disableFilter = rootView.findViewById(R.id.unfilter_gif);
        Switch autoloop = rootView.findViewById(R.id.auto_loop_video);
        Switch automute = rootView.findViewById(R.id.auto_mute_video);
        Switch autoplay = rootView.findViewById(R.id.auto_play_video);
        lurkEL = rootView.findViewById(R.id.toastEL);
        Switch bypassSave = rootView.findViewById(R.id.bypass_save);
        meetRecpt.setChecked(settings.getNoMeet());
        readRecpt.setChecked(settings.getNoReadReceipt());
        typingRecpt.setChecked(settings.getNoTyping());
        fakeCam.setChecked(settings.getFakeCam());
        longCam.setChecked(settings.getLongCam());

        disableFwd.setChecked(settings.getDisableFwd());
        disableFwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settings.setDisableFwd(isChecked);
            }
        });
        bypassSave.setChecked(settings.getBypassSave());
        bypassSave.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settings.setBypassSave(isChecked);
            }
        });
        autoloop.setChecked(settings.getAutoLoop());
        autoloop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settings.setAutoLoop(isChecked);
            }
        });
        automute.setChecked(settings.getAutoMute());
        automute.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settings.setAutoMute(isChecked);
            }
        });
        autoplay.setChecked(settings.getAutoPlay());
        autoplay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settings.setAutoPlay(isChecked);
            }
        });
        disableSave.setChecked(settings.getDisableSave());
        disableSave.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settings.setDisableSave(isChecked);
            }
        });

        disableFilter.setChecked(settings.getUnfilteredGIFs());
        disableFilter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settings.setUnfilteredGIFs(isChecked);
            }
        });


        readRecpt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settings.setNoReadReceipt(isChecked);
            }
        });



        lurkToast.setChecked(settings.getLurkingToast());
        lurkToast.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settings.setLurkingToast(isChecked);
            }
        });

        typingRecpt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settings.setNoTyping(isChecked);
            }
        });

        fakeCam.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settings.setFakeCam(isChecked);
            }
        });

        meetRecpt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settings.setNoMeet(isChecked);
            }
        });

        lurkDetector.setChecked(settings.getWhosLurking());
        lurkEL.setExpanded(settings.getWhosLurking());
        lurkDetector.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settings.setWhosLurking(isChecked);
                if (isChecked){
                    lurkEL.expand();
                }else{
                    lurkEL.collapse();
                }
            }
        });

        longCam.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settings.setLongCam(isChecked);
            }
        });

        return rootView;
    }
}