package alroy214.xkik.settings;


import android.graphics.Color;

import alroy214.xkik.enums.Colors;

/**
 * Setting for changing color resource
 */

public class ColorSetting {

    public String label;
    public String[] id;
    public int defval;


    /**
     * @param label  Color label shown in settings page
     * @param id     Color resource id
     * @param defval default value
     */
    public ColorSetting(String label, String id, int defval) {
        this.label = label;
        this.id = new String[]{id};
        this.defval = defval;
    }
    /**
     * @param label  Color label shown in settings page
     * @param id     Color resource id
     * @param defval default value
     */
    public ColorSetting(String label, Colors color, String defval) {
        this.label = label;
        this.id = new String[]{color.getColor()};
        this.defval = Color.parseColor(defval);
    }

    /**
     * @param label  Color label shown in settings page
     * @param id     Color resource id
     * @param defval default value
     */
    public ColorSetting(String label, String id, String defval) {
        this.label = label;
        this.id = new String[]{id};
        this.defval = Color.parseColor(defval);
    }

    /**
     * @param label  Color label shown in settings page
     * @param id     Color resource id's
     * @param defval default value
     */
    public ColorSetting(String label, String[] id, String defval) {
        this.label = label;
        this.id = id;
        this.defval = Color.parseColor(defval);
    }


}
