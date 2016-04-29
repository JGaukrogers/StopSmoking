package cat.judith.stopsmoking;

import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class EditPastDaysActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_past_days);

        // Todo: load current cigs per day on current date

    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "date picker");

        // otherwise, we can only refresh the date once
        forceRefresh(v);
    }

    // Todo: refresh with cigarettes smoked on selected date
    // Todo: add and update text view with selected date
    public void forceRefresh(View view) {
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(CommonConstants.PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        String selDate = sharedPref.getString(CommonConstants.SELECTED_DATE_KEY, "0");

        TextView tv = (TextView) findViewById(R.id.numCigsOnDateView);
        tv.setText(selDate);
    }
}
