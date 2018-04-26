package robhawk.com.br.orm_example.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {
    private static final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    public static String formatPtBrNow() {
        Date now = Calendar.getInstance().getTime();
        return formatter.format(now);
    }

    public static String formatPtBr(Date date) {
        return formatter.format(date);
    }
}
