package alroy214.xkik.ui.notifications;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.documentfile.provider.DocumentFile;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

public class NotificationsViewModel extends ViewModel {

    private MutableLiveData<String> mText;


    public NotificationsViewModel() throws IOException {
        mText = new MutableLiveData<>();
        mText.setValue("This is the notification fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}