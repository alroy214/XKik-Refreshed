package alroy214.xkik;

import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.documentfile.provider.DocumentFile;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONObject;

import java.io.File;

import alroy214.xkik.settings.Settings;

import static alroy214.xkik.utilities.Utils.loadJSONFromAsset;

public class MainActivity extends AppCompatActivity {


    private JSONObject tweaksObject;
    private Settings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

      //  Settings.verifyStoragePermissions(this); // make sure we can access settings

        tweaksObject = loadJSONFromAsset(this);
        settings = new Settings(this);

        File a = new File(Environment.getExternalStorageDirectory()+ java.io.File.separator + "backup_2");
        DocumentFile s = DocumentFile.fromFile(a);
        SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("test_key","working");
        editor.apply();


        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_chat, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    public String getKikVersionName(){
        PackageManager pm = getPackageManager();
        PackageInfo pInfo = null;
        try {
            pInfo = pm.getPackageInfo("kik.android", 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
        return pInfo.versionName;
    }

    public Settings getSettings(){
        return settings;
    }

    public JSONObject getTweakObject(){
        return tweaksObject;
    }

}
