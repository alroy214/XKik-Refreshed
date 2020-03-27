package alroy214.xkik.settings;

import java.util.ArrayList;

/**
 * Created by Dylan on 9/17/2017.
 */

public class LongStringArray {

    private long lng;
    ArrayList<String> strArr;

    public LongStringArray(long l, ArrayList<String> str){
        lng = l;
        strArr = str;
    }

    public long getLong() {
        return lng;
    }

    public void setLong(long lng) {
        this.lng = lng;
    }

    public ArrayList<String> getStrArr() {
        return strArr;
    }

    public void addStrArr(String value){
        strArr.add(value);
    }

    public void delete(String value){
        strArr.remove(value);
    }

    public boolean contains (String value){
        return strArr.contains(value);
    }

}
