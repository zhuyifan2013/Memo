package our.memo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Util {

    public final static String DATE_FORMAT_EDIT_PAGE = "yyyy年MM月dd日 HH:mm:ss";
    public final static String DATE_FORMAT_NOTE_ITEM = "MM/dd";

    //get current time, string type
    public static String getCurrentTime() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_EDIT_PAGE,
                Locale.getDefault());
        return dateFormat.format(date);
    }

    public static String formatTime(String format, long time){
        SimpleDateFormat dateFormat = new SimpleDateFormat(format,
                Locale.getDefault());
        return dateFormat.format(time);
    }
}
