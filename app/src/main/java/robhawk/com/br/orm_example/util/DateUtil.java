package robhawk.com.br.orm_example.util;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {
    private static final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    public static String formatPtBrNow() {
        return formatter.format(now());
    }

    public static String formatPtBr(Date date) {
        return formatter.format(date);
    }

    public static Date parsePtBr(String date) {
        try {
            return formatter.parse(date);
        } catch (ParseException e) {
            Log.e("ParseException", e.getMessage());
            return now();
        }
    }

    public static Date now() {
        return Calendar.getInstance().getTime();
    }
}
