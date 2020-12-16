package alroy214.xkik.Views;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import com.pavelsikun.vintagechroma.ChromaDialog;
import com.pavelsikun.vintagechroma.OnColorSelectedListener;
import com.pavelsikun.vintagechroma.colormode.ColorMode;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

import alroy214.xkik.enums.Colors;
import alroy214.xkik.settings.Settings;
import de.robv.android.xposed.XposedBridge;
import top.defaults.drawabletoolbox.DrawableBuilder;

import static alroy214.xkik.enums.Colors.COLOR_CODE_BACKGROUND;
import static alroy214.xkik.enums.Colors.COLOR_CODE_INCOMING;
import static alroy214.xkik.enums.Colors.COLOR_CODE_WHITE;
import static alroy214.xkik.utilities.Statics.WHO_READ_DIR_NAME;
import static alroy214.xkik.utilities.Statics.WHO_READ_FILE_EXTENSION;
import static alroy214.xkik.utilities.Statics.WHO_READ_FILE_NAME;

/**
 * Class for managing quick toggleable settings
 */
public class QuickConfiguration extends DialogFragment {
    public QuickConfiguration()
    {
        //Empty Constructor
    }

    private Settings settings;

    private boolean sureDelete = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Material_Dialog);
    }


    private View genColorTweak(String label, final Colors colorCode, final int default_color) {
        LinearLayout v = new LinearLayout(getActivity());
        v.setBackgroundColor(settings.getColor(COLOR_CODE_INCOMING));
        final TextView b = new Switch(getActivity());
        b.setTransformationMethod(null);
        b.setText(label);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ChromaDialog.Builder()
                        .initialColor(settings.getColor(colorCode))
                        .colorMode(ColorMode.RGB)
                        .onColorSelected(new OnColorSelectedListener() {
                            @Override
                            public void onColorSelected(@ColorInt int color) {
                                b.setBackgroundColor(color);
                                settings.setColor(colorCode, color);
                            }
                        })
                        .create();
                        //.show(getActivity().getFragmentManager(), "ChromaDialog");
            }
        });
        b.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                b.setBackgroundColor(default_color);
                settings.setColor(colorCode, default_color);
                return true;
            }
        });
        v.addView(b);
        return v;
    }

    private View genSwitchTweak(String label, final Colors colorCode, final int default_color) {
        LinearLayout v = new LinearLayout(getActivity());
        v.setBackgroundColor(settings.getColor(COLOR_CODE_INCOMING));
        final Switch b = new Switch(getActivity());
        b.setTransformationMethod(null);
        b.setText(label);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ChromaDialog.Builder()
                        .initialColor(settings.getColor(colorCode))
                        .colorMode(ColorMode.RGB)
                        .onColorSelected(new OnColorSelectedListener() {
                            @Override
                            public void onColorSelected(@ColorInt int color) {
                                b.setBackgroundColor(color);
                                settings.setColor(colorCode, color);
                            }
                        })
                        .create();
                //.show(getActivity().getFragmentManager(), "ChromaDialog");
            }
        });
        b.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                b.setBackgroundColor(default_color);
                settings.setColor(colorCode, default_color);
                return true;
            }
        });
        v.addView(b);
        return v;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if(getActivity()== null) {
            return new LinearLayout(getActivity(),null, android.R.style.Theme_Material);
        }
        settings = new Settings(getActivity());

        if(getDialog() != null && getDialog().getWindow() != null) {
            Drawable drawable = new DrawableBuilder().rectangle()
                    .solidColor(0xf0303070).cornerRadius(30)
                    .build();
            getDialog().getWindow().setBackgroundDrawable(drawable);
            getDialog().setTitle("Quick Settings");
        }

        NestedScrollView scrollView = new NestedScrollView(getActivity());
        scrollView.setLayoutParams(new NestedScrollView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        scrollView.setClipToOutline(true);
        LinearLayout v = new LinearLayout(getActivity(),null, android.R.style.Theme_Material);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        v.setClipToOutline(true);
        v.setLayoutParams(params);
        v.setGravity(Gravity.CENTER);
        v.setVerticalScrollBarEnabled(true);
        v.setHorizontalScrollBarEnabled(true);
        v.setOrientation(LinearLayout.VERTICAL);
        scrollView.addView(v);
        scrollView.setHorizontalScrollBarEnabled(true);
        scrollView.setVerticalScrollBarEnabled(true);
        scrollView.setNestedScrollingEnabled(true);
        TextView view = new TextView(getActivity());
        v.addView(view);
        View whiteColor = genColorTweak("Incoming Color", COLOR_CODE_INCOMING, Color.parseColor("#ffeeeeee"));
        v.addView(whiteColor);
        Button setBackground = new Button(getActivity());
        setBackground.getBackground().setColorFilter(settings.getColor(COLOR_CODE_WHITE), PorterDuff.Mode.MULTIPLY);
        setBackground.setTransformationMethod(null);
        setBackground.setText("Background Image/s");
     /*   setBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                DialogConfig dialogConfig = new DialogConfig.Builder()
                        .supportFiles(new SupportFile(".jpg", android.R.drawable.ic_menu_gallery),
                                new SupportFile(".jpeg", android.R.drawable.ic_menu_gallery),
                                new SupportFile(".png", android.R.drawable.ic_menu_gallery),
                                new SupportFile(".bmp", android.R.drawable.ic_menu_gallery))
                        .enableMultipleSelect(true)
                        .build();
                new FilePickerDialogFragment.Builder()
                        .configs(dialogConfig)
                        .onFilesSelected(new UnsupportedFilePickerDialogFragment.OnFilesSelectedListener() {
                            @Override
                            public void onFileSelected(final List<File> list) {
                                settings.setFileList(list, false);
                            }
                        })
                        .build()
                        .show(((Activity)v.getContext()).getFragmentManager(), null);
            }
        });*/
        v.addView(setBackground);
        v.setPadding(10, 10, 10, 10);
        Switch graphics = new Switch(getActivity());
        graphics.setText("Graphics");
        graphics.setPadding(10, 10, 10, 10);
        Switch fakeCam = new Switch(getActivity());
        fakeCam.setText("Fake Camera");
        fakeCam.setPadding(10, 10, 10, 10);
        Switch gifCam = new Switch(getActivity());
        gifCam.setText("Fake GIF");
        gifCam.setPadding(10, 10, 10, 10);
        Switch disableRead = new Switch(getActivity());
        disableRead.setText("Disable Read Receipts");
        disableRead.setPadding(10, 10, 10, 10);
        Switch disableType = new Switch(getActivity());
        disableType.setText("Disable Typing Receipts");
        disableType.setPadding(10, 10, 10, 10);
        Switch whoLurk = new Switch(getActivity());
        final Switch toastLurkers = new Switch(getActivity());
        toastLurkers.setText("Toast Lurkers");
        toastLurkers.setPadding(10, 10, 10, 10);
        final Switch wave = new Switch(getActivity());
        wave.setText("Do The Wave");
        wave.setPadding(10, 10, 10, 10);
        final Switch dark = new Switch(getActivity());
        dark.setText("Dark Mode");
        dark.setPadding(10, 10, 10, 10);
        final Switch meet = new Switch(getActivity());
        meet.setText("Hide Meet People");
        meet.setPadding(10, 10, 10, 10);

        final Switch gradient = new Switch(getActivity());
        gradient.setText("Outgoing Gradient");
        gradient.setPadding(10, 10, 10, 10);
        Button readLurkers = new Button(getActivity());
        readLurkers.getBackground().setColorFilter(settings.getColor(COLOR_CODE_WHITE), PorterDuff.Mode.MULTIPLY);
        v.addView(readLurkers);
        readLurkers.setText("Show Lurkers :3");
        readLurkers.setTransformationMethod(null);
        final File file = new File(getActivity().getExternalFilesDir(WHO_READ_DIR_NAME) + File.separator +
                WHO_READ_FILE_NAME + WHO_READ_FILE_EXTENSION);
        readLurkers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sureDelete) {
                    ((Button) view).setText("Show Lurkers :3");
                    int i = 1;
                    while (new File(getActivity().getExternalFilesDir(WHO_READ_DIR_NAME)  +
                            File.separator + WHO_READ_FILE_NAME + i + WHO_READ_FILE_EXTENSION).exists()) {
                        i++;
                    }
                    try {
                        FileUtils.copyFile(file,
                                new File(getActivity().getExternalFilesDir(WHO_READ_DIR_NAME)  +
                                File.separator + WHO_READ_FILE_NAME + i + WHO_READ_FILE_EXTENSION));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    XposedBridge.log("XKik - delete file: " + file.delete());
                    sureDelete = false;
                } else {
                    GlanceLurkers qc = new GlanceLurkers();
                    qc.show(getActivity().getFragmentManager(), "glance_lurker");
                }
            }
        });
        readLurkers.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(!sureDelete)
                {
                    ((Button)view).setText("Sure Delete? :0");
                }
                else
                {
                    ((Button)view).setText("Show Lurkers :3");
                }
                sureDelete = !sureDelete;
                return true;
            }
        });
        wave.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new ChromaDialog.Builder()
                        .initialColor(settings.getColor(Colors.COLOR_CODE_BACKGROUND))
                        .colorMode(ColorMode.RGB)
                        .onColorSelected(new OnColorSelectedListener() {
                            @Override
                            public void onColorSelected(@ColorInt int color) {
                                wave.setBackgroundColor(color);
                                settings.setColor(Colors.COLOR_CODE_BACKGROUND, color);
                            }
                        })
                        .create()
                        .show((((AppCompatActivity)getActivity()).getSupportFragmentManager()), "ChromaDialog");
                return true;
            }
        });
        /*
        final Switch innerWave = new Switch(getActivity());
        innerWave.setText("Inner Wave");
        innerWave.setPadding(10, 10, 10, 10);
        innerWave.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final String colorcode[] = new String[]{INNERW+};
                final int default_color = Color.RED;
                new UnsupportedChromaDialog.Builder()
                        .initialColor(getColor(colorcode[0], default_color))
                        .colorMode(ColorMode.RGB)
                        .onColorSelected(new OnColorSelectedListener() {

                            @Override
                            public void onColorSelected(@ColorInt int color) {
                                innerWave.setBackgroundColor(color);
                                if(settings!=null) {
                                    for (String c : colorcode) {
                                        settings.setColor(c, color, true);
                                    }
                                }

                            }
                        })
                        .create()
                        .show(getFragmentManager(), "UnsupportedChromaDialog");
                return true;
            }
        });
        final Switch customOutgoing = new Switch(getActivity());
        customOutgoing.setText("Custom Outgoing");
        customOutgoing.setPadding(10, 10, 10, 10);
        customOutgoing.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final String colorCode[] = new String[]{outgoingColor};
                final int default_color = Color.RED;
                new UnsupportedChromaDialog.Builder()
                        .initialColor(getColor(colorCode[0], default_color))
                        .colorMode(ColorMode.RGB)
                        .onColorSelected(new OnColorSelectedListener() {
                            @Override
                            public void onColorSelected(@ColorInt int color) {
                                customOutgoing.setBackgroundColor(color);
                                if(settings!=null) {
                                    for (String c : colorCode) {
                                        settings.setColor(c, color, true);
                                    }
                                }

                            }
                        })
                        .create()
                        .show(getFragmentManager(), "UnsupportedChromaDialog");
                return true;
            }
        });
        final Switch customIncoming = new Switch(getActivity());
        customIncoming.setText("Custom Incoming");
        customIncoming.setPadding(10, 10, 10, 10);
        customIncoming.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final String colorCode[] = new String[]{incomingColor};
                final int default_color = Color.RED;
                new UnsupportedChromaDialog.Builder()
                        .initialColor(getColor(colorCode[0], default_color))
                        .colorMode(ColorMode.RGB)
                        .onColorSelected(new OnColorSelectedListener() {
                            @Override
                            public void onColorSelected(@ColorInt int color) {
                                customIncoming.setBackgroundColor(color);
                                if(settings!=null) {
                                    for (String c : colorCode) {
                                        settings.setColor(c, color, true);
                                    }
                                }

                            }
                        })
                        .create()
                        .show(getFragmentManager(), "UnsupportedChromaDialog");
                return true;
            }
        });
         */
        final Button doneButton = new Button(getActivity());
        doneButton.setTransformationMethod(null);
        doneButton.setText("Done");
        whoLurk.setText("Who's Lurking");
        whoLurk.setPadding(10, 10, 10, 10);
        try {
            doneButton.setBackgroundColor(settings.getColor(COLOR_CODE_WHITE));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        doneButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, final MotionEvent event) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(settings != null) {
                            switch (event.getAction()) {
                                case MotionEvent.ACTION_DOWN: {
                                    doneButton.setBackgroundColor(settings.getColor(Colors.COLOR_CODE_BACKGROUND));
                                    break;
                                }
                                case MotionEvent.ACTION_UP:
                                case MotionEvent.ACTION_CANCEL: {
                                    doneButton.setBackgroundColor(settings.getColor(Colors.COLOR_CODE_WHITE));
                                    break;
                                }
                            }
                        }
                    }
                });
                return false;
            }
        });


        doneButton.setTextColor(Color.WHITE);
        v.addView(graphics);
        v.addView(fakeCam);
        v.addView(gifCam);
        v.addView(disableRead);
        v.addView(disableType);
        v.addView(whoLurk);
        v.addView(toastLurkers);
        v.addView(wave);
 //       v.addView(innerWave);
   //     v.addView(customIncoming);
     //   v.addView(customOutgoing);
        v.addView(gradient);
        v.addView(dark);
        v.addView(meet);
        v.addView(doneButton);
        if (settings != null) {
            try {
                meet.setChecked(settings.getNoMeet());
                graphics.setChecked(settings.getGraphics());
                fakeCam.setChecked(settings.getFakeCam());
                gifCam.setChecked(settings.getFakeGif());
       //         disableRead.setChecked(settings.getNoReadreceipt());
                disableType.setChecked(settings.getNoTyping());
                whoLurk.setChecked(settings.getWhosLurking());
                wave.setChecked(settings.getTheWave());
        /*        innerWave.setChecked(settings.getInnerWave());
                innerWave.setEnabled(settings.getTheWave());
                customIncoming.setChecked(settings.getIncoming());
                customOutgoing.setChecked(settings.getOutgoing());
                gradient.setChecked(settings.getGradient()); */
                dark.setChecked(settings.getDarkBg());
                toastLurkers.setChecked(settings.getLurkingToast());
                int outerColor = Color.WHITE;
                try {
                    outerColor = settings.getColor(COLOR_CODE_BACKGROUND);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                wave.setBackgroundColor(outerColor);
              /*  int innerColor = Color.RED;
                try {
                    innerColor = settings.getColors().get(innerWaveColor);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                innerWave.setBackgroundColor(innerColor);
                int outgoing = Color.BLUE;
                try {
                    outgoing = settings.getColors().get(outgoingColor);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                customOutgoing.setBackgroundColor(outgoing);
                int incoming = Color.BLUE;
                try {
                    incoming = settings.getColors().get(incomingColor);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                customIncoming.setBackgroundColor(incoming);*/
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                graphics.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                       /* settings.setGraphics(isChecked, false);
                        if(x!=null)
                        {
                            x.updateSettingsButtonColor();
                        }*/
                    }
                });
                fakeCam.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        settings.setFakeCam(isChecked);
                    }
                });

                gifCam.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        settings.setFakeGif(isChecked);
                    }
                });

                disableRead.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        settings.setNoReadReceipt(isChecked);
                    }
                });

                disableType.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        settings.setNoTyping(isChecked);
                    }
                });

                whoLurk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        settings.setWhosLurking(isChecked);
                        toastLurkers.setEnabled(isChecked);
                    }
                });
                toastLurkers.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        settings.setLurkingToast(isChecked);
                    }
                });
       /*         wave.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        settings.setTheWave(isChecked);
                        innerWave.setEnabled(isChecked);
                    }
                });
                innerWave.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        settings.setInnerWave(isChecked);
                    }
                });
                customOutgoing.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        settings.setOutgoing(isChecked);
                    }
                });
                customIncoming.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        settings.setIncoming(isChecked);
                    }
                });
                gradient.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        settings.setGradient(isChecked);
                    }
                });*/
                dark.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        settings.setDarkBg(isChecked);
                    }
                });
                meet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        settings.setNoMeet(isChecked);
                        if(isChecked) {
                          //  x.setMeetViewState(View.GONE); //TODO: X GONNA GIVE IT TO U
                        } else {
                           // x.setMeetViewState(View.VISIBLE);
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            doneButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getDialog().cancel();
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return scrollView;
    }


}
