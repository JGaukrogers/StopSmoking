package cat.judith.stopsmoking;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.DatePicker;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    // Todo: add a 0 for days and months > 10
    public void onDateSet(DatePicker view, int year, int month, int day) {
        String selDate = String.valueOf(year) + String.valueOf(month+1) + String.valueOf(day);

        SharedPreferences sharedPref = CigaretteCounter.getThisAppContext().getSharedPreferences(CommonConstants.PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(CommonConstants.SELECTED_DATE_KEY, selDate);
        editor.apply();
    }
}
