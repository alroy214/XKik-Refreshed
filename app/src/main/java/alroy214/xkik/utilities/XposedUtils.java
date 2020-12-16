package alroy214.xkik.utilities;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static alroy214.xkik.utilities.Statics.XPOSED_DATABASE_FILE_NAME;

public class XposedUtils {
    /**
     * Generates a toast using KIK's context
     *
     * @param text Text to display on toast
     */
    public static void kikToast(final Context context, final String text) {
        if (context == null) {
            return;
        }

        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context.getApplicationContext(), text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void prependPrefix(File input, String prefix) throws IOException {
        LineIterator li = FileUtils.lineIterator(input);
        File tempFile = File.createTempFile("prependPrefix", ".tmp");
        BufferedWriter w = new BufferedWriter(new FileWriter(tempFile));
        try {
            w.write(prefix);
            while (li.hasNext()) {
                w.write(li.next());
                w.write("\n");
            }
        } finally {
            IOUtils.closeQuietly(w);
            LineIterator.closeQuietly(li);
        }
        FileUtils.deleteQuietly(input);
        FileUtils.moveFile(tempFile, input);
    }

    private static String buildCoreID(String coreId, String dataBase) {
        return coreId + "." + dataBase;
    }

    public static String userNameFromJID (Context context, String coreId, String jid) {
        Cursor rawQuery = context.openOrCreateDatabase(
                buildCoreID(coreId, XPOSED_DATABASE_FILE_NAME), 0, null).
                rawQuery("SELECT display_name FROM KIKcontactsTable WHERE jid = '"
                        + jid + "';", null);
        rawQuery.moveToNext();
        String string = (rawQuery.getCount() == 0) ? "" : rawQuery.getString(0);
        rawQuery.close();
        return string;
    }

    private static void beGone(View v, int state) {
        if(v instanceof ViewGroup) {
            ViewGroup b = (ViewGroup)v;
            for (int i = 0; i < b.getChildCount(); i++) {
                beGone(b.getChildAt(i), state);
            }
        } if(v != null) {
            v.setVisibility(state);
        }
    }

    public static void setMeetViewState(View meetView, int state) {
        if(meetView != null) {
            beGone(meetView, state);
        }
    }

}
