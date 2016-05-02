package cat.judith.stopsmoking;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by judith on 29/04/16.
 */
public class DateToString {

    public static String getTodaysDateAsString(){
        Calendar cAux = Calendar.getInstance(Locale.getDefault());
        return getDateAsString(cAux.get(Calendar.YEAR), cAux.get(Calendar.MONTH), cAux.get(Calendar.DAY_OF_MONTH));
    }

    public static String getDateAsString(int year, int month, int day){
        int todaysDate = year * 10000
                + (month + 1) * 100
                + day;
        return String.valueOf(todaysDate);
    }

}
