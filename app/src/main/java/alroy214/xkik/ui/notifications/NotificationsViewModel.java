package alroy214.xkik.ui.notifications;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.IOException;

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