package cat.judith.stopsmoking;

import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class EditPastDaysActivity extends AppCompatActivity {

    private EditText cigsOnDateView;
    private String selDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_past_days);
        cigsOnDateView = (EditText) findViewById(R.id.numCigsOnDateView);

        // Load current cigs per day on current date
        selDate = DateToString.getTodaysDateAsString();
        displaySmokedCigsInThatDay(selDate);

    }

    private void insertToDatabase(int smoked, CigarettesSmokedDB dbHelper, String date) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(CigarettesSmokedDB.COLUMN_NAME_ENTRY_ID, date);
        values.put(CigarettesSmokedDB.COLUMN_NAME_SMOKED, smoked);

        // Insert the new row, returning the primary key value of the new row
        db.insert(
                CigarettesSmokedDB.TABLE_NAME,
                null,
                values);
    }

    private void updateToDatabase(int smoked, CigarettesSmokedDB dbHelper, String date) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(CigarettesSmokedDB.COLUMN_NAME_SMOKED, smoked);
        String[] where = {String.valueOf(date)};

        // Insert the new row, returning the primary key value of the new row
        db.update(
                CigarettesSmokedDB.TABLE_NAME,
                values,
                CigarettesSmokedDB.COLUMN_NAME_ENTRY_ID + "=?",
                where);
    }

    private void displaySmokedCigsInThatDay(String date){

        CigarettesSmokedDB dbHelper = new CigarettesSmokedDB(this.getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                CigarettesSmokedDB.COLUMN_NAME_ENTRY_ID,
                CigarettesSmokedDB.COLUMN_NAME_SMOKED
        };
        String selection = CigarettesSmokedDB.COLUMN_NAME_ENTRY_ID + " = ?";
        String[] selectionArgs = {
                date
        };
        Cursor c = db.query(
                CigarettesSmokedDB.TABLE_NAME,  // The table to query
                projection,                     // The columns to return
                selection,                      // The columns for the WHERE clause
                selectionArgs,                  // The values for the WHERE clause
                null,                           // don't group the rows
                null,                           // don't filter by row groups
                null                            // don't sort
        );
        int count = c.getCount();
        if( count == 0){
            // Add entry and show to user
            // Todo: for some reason, the date is sometimes "0". Find reason and fix it properly
            // maybe on pickup new date?
            if (date.equals("0")){
                cigsOnDateView.setText("0");
            }
            else {
                cigsOnDateView.setText("0");
                insertToDatabase(0, dbHelper, date);
            }
        }
        else{
            //Get entry and show to user
            c.moveToFirst();
            String itemId = c.getString(c.getColumnIndex(CigarettesSmokedDB.COLUMN_NAME_SMOKED));
            cigsOnDateView.setText(itemId);
        }
        c.close();

    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "date picker");

        // otherwise, we can only refresh the date once
        forceRefresh(v);
    }

    // Refresh with cigarettes smoked on selected date
    // Todo: doesn't seem to work very well with updateNewValue: fix it
    public void forceRefresh(View view) {
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(CommonConstants.PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        selDate = sharedPref.getString(CommonConstants.SELECTED_DATE_KEY, "0");

        displaySmokedCigsInThatDay(selDate);
    }

    public void updateNewValue(View view) {
        // Todo: save new non-negative number
        String newValueS = String.valueOf(cigsOnDateView.getText());
        int newValue = Integer.decode(newValueS);
        CigarettesSmokedDB dbHelper = new CigarettesSmokedDB(getApplicationContext());
        updateToDatabase(newValue, dbHelper, selDate);
    }
}
