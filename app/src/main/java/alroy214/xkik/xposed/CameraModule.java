package alroy214.xkik.xposed;

import android.media.MediaRecorder;

import java.util.concurrent.TimeUnit;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static alroy214.xkik.utilities.HookKeys.kikCamObj;
import static alroy214.xkik.utilities.HookKeys.kikCameraTimer;
import static alroy214.xkik.utilities.HookKeys.kikCircleBar;
import static alroy214.xkik.utilities.HookKeys.kikRecordTextMgr;

public class CameraModule {
    private void longCamMethods(final XC_LoadPackage.LoadPackageParam loadPackageParam) {

        final XposedObject[] camObj = new XposedObject[1];
        /*
        This sets camObj so it doesn't interfere with any other apps that use MediaRecorder
         */
        XposedHelpers.findAndHookMethod(kikCamObj, loadPackageParam.classLoader, "a", String.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                camObj[0] = new XposedObject(param.thisObject);
                super.beforeHookedMethod(param);
            }
        });



        /*
        Sets camera max duration to longvidTime
         */
        XposedHelpers.findAndHookMethod(MediaRecorder.class, "setMaxDuration", int.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                if (param.thisObject == null || camObj[0] == null) {
                    return;
                }
                if (param.thisObject.equals
                        (camObj[0].get("i"))) {
                    param.args[0] = (int) TimeUnit.MINUTES.toMillis(2);
                }
                super.beforeHookedMethod(param);
            }
        });

        /*
        Sets camera max file size to 20mb
         */
        XposedHelpers.findAndHookMethod(MediaRecorder.class, "setMaxFileSize", long.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                if (param.thisObject == null || camObj[0] == null) {
                    return;
                }
                if (param.thisObject.equals
                        (camObj[0].get("i"))) {
                    param.args[0] = 20971520L; // 20mb is the max KIK file size playable
                }
                super.beforeHookedMethod(param);
            }
        });

        /*
        Modifies the circle progress bar while recording to be correct with the modified
        time
         */
        XposedHelpers.findAndHookMethod(kikCircleBar, loadPackageParam.classLoader, "a", int.class, new XC_MethodReplacement() {
            @Override
            protected Object replaceHookedMethod(MethodHookParam methodHookParam) throws Throwable {
                float f = ((int) methodHookParam.args[0]) * 1F;
                float calc = 360.0f * ((f) / ((float)  TimeUnit.MINUTES.toMillis(2)));
                XposedObject o = new XposedObject(methodHookParam.thisObject);
                o.getXObj("_shutterButton").call("a", calc);
                o.getXObj("_videoTime").call("setText", XposedHelpers.callStaticMethod(XposedHelpers.findClass(kikRecordTextMgr, loadPackageParam.classLoader), "a", methodHookParam.args[0]));
                return null;
            }
        });

        /*
        Adjusts the onTick method to calculte with the new, modified time
         */
        XposedHelpers.findAndHookMethod(kikCameraTimer, loadPackageParam.classLoader, "onTick", long.class, new XC_MethodReplacement() {
            @Override
            protected Object replaceHookedMethod(MethodHookParam methodHookParam) throws Throwable {
                int tim = (int) Math.max(0, (int) TimeUnit.MINUTES.toMillis(2) - ((long) methodHookParam.args[0]));
                new XposedObject(methodHookParam.thisObject).getXObj("a").set("h", tim);
                new XposedObject(methodHookParam.thisObject).getXObj("a").getXObj("r").call("b", tim);
                return null;
            }
        });

        /*
        Modifies the CountDownTimer to use the modded time
         */
        XposedHelpers.findAndHookConstructor(kikCameraTimer, loadPackageParam.classLoader, kikCamObj, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                new XposedObject(param.thisObject).set("mMillisInFuture", (int) TimeUnit.MINUTES.toMillis(2));
                super.afterHookedMethod(param);
            }
        });
    }
}
