package Classes;

import android.util.Log;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
public class DateUtils {
    private static final String API_DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss z";
    private static final String DISPLAY_DATE_FORMAT = "dd/MM/yyyy";
    private static final String API_REQUEST_DATE_FORMAT = "yyyy-MM-dd";

    // convert API date format to normal
    public static Date convertDate(String apiDate) {
        if (apiDate == null || apiDate.isEmpty()) {
            return null;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(API_DATE_FORMAT, Locale.US);
        try {
            return sdf.parse(apiDate);
        } catch (ParseException e) {
            Log.e("API", "Error parsing API date: " + apiDate + ", Error: " + e.getMessage());
            return null;
        }
    }

    // format date to a readable display format
    public static String showDate(Date date) {
        if (date == null) {
            return "";
        }

        SimpleDateFormat sdf = new SimpleDateFormat(DISPLAY_DATE_FORMAT, Locale.UK);
        return sdf.format(date);
    }

    // date to api request
    public static String apiDate(Date date) {
        if (date == null) {
            return "";
        }

        SimpleDateFormat sdf = new SimpleDateFormat(API_REQUEST_DATE_FORMAT, Locale.US);
        return sdf.format(date);
    }

    // get crrent date in API request format
    public static String today() {
        return apiDate(new Date());
    }

    // add days to a date and return in API format - calculatuions
    public static String addDays(String apiDate, int days) {
        Date date = convertDate(apiDate);
        if (date == null) {
            Log.w("API", "Could not parse date: " + apiDate + ", using current date");
            return today();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, days);

        Date newDate = calendar.getTime();
        return apiDate(newDate);
    }
}