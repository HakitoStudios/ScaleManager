package havrysh.scalemanager.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FormatUtils {
    private static final String MONEY_FORMAT = "%.2f грн";
    private static DateFormat dateFormat = new SimpleDateFormat();

    public static String formatDate(long date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        return dateFormat.format(calendar);
    }

    public static String formatMoney(float money) {
        return String.format(MONEY_FORMAT, money);
    }
}
