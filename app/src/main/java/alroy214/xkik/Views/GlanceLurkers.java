package alroy214.xkik.Views;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import alroy214.xkik.settings.Settings;
import de.robv.android.xposed.XposedBridge;
import top.defaults.drawabletoolbox.DrawableBuilder;

import static alroy214.xkik.enums.Colors.COLOR_CODE_WHITE;
import static alroy214.xkik.utilities.Statics.WHO_READ_DIR_NAME;
import static alroy214.xkik.utilities.Statics.WHO_READ_FILE_EXTENSION;
import static alroy214.xkik.utilities.Statics.WHO_READ_FILE_NAME;

/**
 * Created by Eitan on 2/13/2018.
 */

public class GlanceLurkers extends DialogFragment {

    private boolean sureDelete = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Material_Dialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if(getDialog() != null && getDialog().getWindow() != null) {
            Drawable drawable = new DrawableBuilder().rectangle()
                    .solidColor(0xf0303070).cornerRadius(30).build();
            Dialog dialog = getDialog();
            Window window = dialog.getWindow();
            window.setBackgroundDrawable(drawable);
            dialog.setTitle("Lurker Glance");
        }
        Settings settings = new Settings(getActivity());
        final TextView lurkersTextView = new TextView(getActivity());
        lurkersTextView.setTextIsSelectable(true);
        lurkersTextView.setGravity(Gravity.CENTER_VERTICAL);
        lurkersTextView.setVerticalScrollBarEnabled(true);
        lurkersTextView.setHorizontalScrollBarEnabled(true);
        final File file = new File(getActivity().getExternalFilesDir(WHO_READ_DIR_NAME) +
                File.separator + WHO_READ_FILE_NAME + WHO_READ_FILE_EXTENSION);
        try {
            lurkersTextView.setText(FileUtils.readFileToString(file, Charset.defaultCharset()));
        } catch (IOException e) {
            e.printStackTrace();
            lurkersTextView.setText("Failure: "+e.toString());
        }
        Button doneButton = new Button(getActivity());
        doneButton.getBackground().setColorFilter(settings.getColor(COLOR_CODE_WHITE), PorterDuff.Mode.MULTIPLY);
        doneButton.setTransformationMethod(null);
        doneButton.setText("Done");
            doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getDialog() != null) {
                    getDialog().cancel();
                }
            }
        });
        Button deleteButton = new Button(getActivity());
        deleteButton.getBackground().setColorFilter(settings.getColor(COLOR_CODE_WHITE), PorterDuff.Mode.MULTIPLY);
        deleteButton.setText("Delete");
        deleteButton.setTransformationMethod(null); // Fix Caps locked
        deleteButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(!sureDelete) {
                    ((Button)view).setText("Sure?");
                } else {
                    ((Button)view).setText("Delete");
                }
                sureDelete = !sureDelete;
                return true;
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                if (sureDelete) {
                    ((Button) view).setText("Delete");
                    int i = 1;
                    while (new File(getActivity().getExternalFilesDir(WHO_READ_DIR_NAME) + File.separator +
                            WHO_READ_FILE_NAME + i + WHO_READ_FILE_EXTENSION).exists()) {
                        i++;
                    }
                    try {
                        FileUtils.copyFile(file, new File(getActivity().getExternalFilesDir(WHO_READ_DIR_NAME) +
                                        File.separator + WHO_READ_FILE_NAME + i + WHO_READ_FILE_EXTENSION));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    XposedBridge.log("File deleted: " + file.delete());
                    lurkersTextView.setText("Cleared B)");
                    sureDelete = false;
                } else {
                    try {
                        lurkersTextView.setText(FileUtils.readFileToString(file, Charset.defaultCharset()));
                        Toast.makeText(getActivity(), "Long press to delete and click again", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), "Failed to show lurker file", Toast.LENGTH_SHORT).show();
                        lurkersTextView.setText("Failure: " + e.toString());
                    }
                }
            }
        });
        RelativeLayout relativeLayout = new RelativeLayout(getActivity());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        LinearLayout linearLayout = new LinearLayout(getActivity());
        LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        linearParams.setMargins(10, 10, 10, 10);
        linearLayout.addView(deleteButton, linearParams);
        linearLayout.addView(doneButton, linearParams);
        relativeLayout.addView(linearLayout, params);
        linearLayout.setId(View.generateViewId());
        params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ABOVE, linearLayout.getId());
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        relativeLayout.addView(lurkersTextView, params);
        return relativeLayout;
    }
    
}
