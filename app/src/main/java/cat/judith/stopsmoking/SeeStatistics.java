package cat.judith.stopsmoking;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SeeStatistics extends AppCompatActivity {

    private double price = 5;
    private int cigsPerPacket = 20;
    private String currency = "€";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_statistics);

        getSavedPreferences();

        TextView cigsSmokedView = (TextView) findViewById(R.id.cigsSmokedView);
        TextView moneySmokedView = (TextView) findViewById(R.id.moneySmokedView);
        TextView averageSmokedView = (TextView) findViewById(R.id.avgSmokedView);

        int totalCigsSmoked = getTotalCigarettesSmoked();
        cigsSmokedView.setText(String.format("%d%s", totalCigsSmoked, getString(R.string.cigarettesSmoked)));
        moneySmokedView.setText(String.format("%s%s%s%s", getTotalMoneySmoked(totalCigsSmoked), getString(R.string.singleSpace), currency, getString(R.string.smoked)));
        averageSmokedView.setText(String.format("%s%s", getAverageCigarettesSmoked(totalCigsSmoked), getString(R.string.cigarettesPerDay)));

        // Init. and set graph
        GraphView graph = (GraphView) findViewById(R.id.graphView);
        BarGraphSeries<DataPoint> graphCigsDay = getCigsPerDayPoints();

        graph.addSeries(graphCigsDay);

        java.text.DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY);
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getApplicationContext(), dateFormat));
        graph.getGridLabelRenderer().setVerticalAxisTitle("Cigarettes Smoked");
        graph.getGridLabelRenderer().setNumHorizontalLabels(3);

    }

    private void getSavedPreferences(){
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(Settings.PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        price = sharedPref.getFloat(Settings.PREFERENCE_PRICE, 5f);
        cigsPerPacket = sharedPref.getInt(Settings.PREFERENCE_NUM_CIGARETTES, 20);
        currency = sharedPref.getString(Settings.PREFERENCE_CURRENCY, "€");
    }

    private int getTotalCigarettesSmoked() {

        int sum_cigs_smoked;

        CigarettesSmokedDB dbHelper = new CigarettesSmokedDB(this.getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor c = db.rawQuery(CigarettesSmokedDB.SQL_GET_SUM, null);
        if(c.moveToFirst())
            sum_cigs_smoked = c.getInt(0);
        else
            sum_cigs_smoked = -1;
        c.close();
        db.close();

        return sum_cigs_smoked;
    }

    private double getTotalMoneySmoked(int totalCigarettes) {
        return getPriceForSingleCig() * totalCigarettes;
    }

    private double getPriceForSingleCig() {
        return price / cigsPerPacket;
    }

    private int getSumDaysSmoked(){

        int sum_days_smoked;

        CigarettesSmokedDB dbHelper = new CigarettesSmokedDB(this.getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor c = db.rawQuery(CigarettesSmokedDB.SQL_GET_DAYS_SMOKED, null);
        if(c.moveToFirst()) {
            sum_days_smoked = c.getInt(0);
        }
        else {
            sum_days_smoked = -1;
        }
        c.close();
        db.close();

        return sum_days_smoked;
    }

    private float getAverageCigarettesSmoked(int totalCigarettes){

        float daysSmoked = getSumDaysSmoked();
        float avg_cigs_smoked = 0;

        if(daysSmoked != 0){
            avg_cigs_smoked = totalCigarettes / daysSmoked;
        }
        return avg_cigs_smoked;
    }

    private BarGraphSeries<DataPoint> getCigsPerDayPoints() {

        CigarettesSmokedDB dbHelper = new CigarettesSmokedDB(this.getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM " + CigarettesSmokedDB.TABLE_NAME + " ORDER BY " + CigarettesSmokedDB.COLUMN_NAME_ENTRY_ID + " ASC ", null);

        c.moveToFirst();
        DataPoint dataPointArray[] = new DataPoint[c.getCount()];

        for (int i = 0; i < c.getCount(); i++){
            DataPoint dpAux = new DataPoint(convertToDate(c.getInt(1)), c.getInt(2));
            dataPointArray[i] = dpAux;
            c.moveToNext();
        }
        c.close();
        BarGraphSeries<DataPoint> graphCigsDay = new BarGraphSeries<>(dataPointArray);
        graphCigsDay.setSpacing(20);

        return graphCigsDay;
    }

    @SuppressWarnings("deprecation")
    private Date convertToDate(int iDate){


        int year = (iDate / 10000) - 1900;
        int month = ((iDate / 100) % 100) - 1;
        int day = iDate % 100;

        return new Date(year, month, day);

    }

}
