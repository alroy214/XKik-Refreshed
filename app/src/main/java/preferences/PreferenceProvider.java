package preferences;

import com.crossbowffs.remotepreferences.RemotePreferenceProvider;

import static alroy214.xkik.utilities.Statics.APP_DEFAULT_PREFERENCE;
import static alroy214.xkik.utilities.Statics.APP_PREFERENCE_PACKAGE;
import static alroy214.xkik.utilities.Statics.KIK_PACKAGE;

public class PreferenceProvider extends RemotePreferenceProvider {
    @Override
    protected boolean checkAccess(String prefFileName, String prefKey, boolean write) {
        // Only allow access from certain apps
        return (KIK_PACKAGE.equals(getCallingPackage()));
    }

    public PreferenceProvider() {
        super(APP_PREFERENCE_PACKAGE, new String[]{APP_DEFAULT_PREFERENCE});
    }
}
