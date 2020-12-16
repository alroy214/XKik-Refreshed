package alroy214.xkik.utilities;

import java.util.regex.Pattern;

public class XposedStatics {
    public static long WEEK_IN_MILLISECONDS = 604800000L;
    public static Pattern useridPattern = Pattern.compile("(.*)_[^_]*");
    public static Pattern groupPattern = Pattern.compile("g jid=\"(.*?)\"");
}
