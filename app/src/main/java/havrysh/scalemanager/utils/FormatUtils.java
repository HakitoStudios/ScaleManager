package havrysh.scalemanager.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class FormatUtils {
    private static final String MONEY_FORMAT = "%.2f грн";

    public static String formatMoney(float money) {
        return String.format(MONEY_FORMAT, money);
    }
}
