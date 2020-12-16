package alroy214.xkik.xposed;

import alroy214.xkik.utilities.Utils;
import de.robv.android.xposed.XposedHelpers;

public class XposedObject {

    private Object obj;

    public XposedObject(Object obj) {
        this.obj = obj;
    }

    public Object getSelf(){
        return obj;
    }

    public Object get(String var) {
        return Utils.getObjField(obj, var);
    }

    XposedObject getXObj(String var) {
        return new XposedObject(Utils.getObjField(obj, var));
    }

    void set(String var, Object value) {
        XposedHelpers.setObjectField(obj, var, value);
    }

    Object call(String func, Object... args) {
        return XposedHelpers.callMethod(obj, func, args);
    }

}
