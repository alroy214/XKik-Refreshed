package alroy214.xkik.data_types;

import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import alroy214.xkik.xposed.XposedObject;
import alroy214.xkik.xposed.hooks;
import de.robv.android.xposed.XposedBridge;


public class messageText {

    private static Pattern unameparser = Pattern.compile("(.*)_[^_]*");

    private String content;
    private String fromGroup;
    private String fromUser;
    private String UUID;
    private Long timestamp;
    private Boolean isgroup;

    public messageText(Object o) {
        this(new XposedObject(o));
    }

    public messageText(XposedObject o) {
        Vector msgdata = (Vector) o.get("i");
        if(msgdata != null &&
                msgdata.get(0).getClass().getName().equals(hooks.DATATYPE_MSG_TEXT)) {
            content = (String) new XposedObject(msgdata.get(0)).get("a");
        }
        else {
            UUID = "";
            XposedBridge.log("Vector is null");
        }

        XposedObject msgobj = new XposedObject(o.getSelf());
        fromGroup = (String) msgobj.get("a");
        fromUser = (String) msgobj.get("b");
        UUID = (String) msgobj.get("f");
        XposedBridge.log("From group / content: " + fromGroup + " with UUID: " + UUID);
        timestamp = (Long) msgobj.get("n");
        if(fromGroup != null) {
            isgroup = fromGroup.contains("groups.kik.com");
        }
    }

    public Boolean getIsgroup() {
        return isgroup;
    }

    public String getContent() {
        return content;
    }

    public String getFromGroup() {
        return fromGroup;
    }

    public String getFromUser() {
        return fromUser;
    }

    public String getFromUserParsed() {
        Matcher m = unameparser.matcher(getFromUser());
        if (m.find()){
            return m.group(1);
        }
        return null;
    }

    public String getUUID() {
        return UUID;
    }

    public Long getTimestamp() {
        return timestamp;
    }
}