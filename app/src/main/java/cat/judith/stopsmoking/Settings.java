package cat.judith.stopsmoking;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class Settings extends AppCompatActivity {

    public static final String PREFERENCES_FILE_NAME = "preferences";
    public static final String PREFERENCE_PRICE = "PRICE";
    public static final String PREFERENCE_NUM_CIGARETTES = "NUM_CIGS";
    public static final String PREFERENCE_CURRENCY = "CURRENCY";

    private EditText cigsPerPacketText;
    private EditText priceText;
    private Spinner spinner;
    private boolean first_run = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        cigsPerPacketText = (EditText) findViewById(R.id.cigsPerPacketText);
        priceText = (EditText) findViewById(R.id.priceText);

        // Populate spinner
        spinner = (Spinner) findViewById(R.id.currencySpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.currencies_list, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        getSavedPreferences();

        // Save new value when editing #cigarettes / packet
        cigsPerPacketText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putInt(PREFERENCE_NUM_CIGARETTES, Integer.decode(String.valueOf(cigsPerPacketText.getText())));
                    editor.apply();
                }
            }
        });

        // Save new value when editing price per packet
        priceText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putFloat(PREFERENCE_PRICE,
                            Float.valueOf(String.valueOf(priceText.getText())));
                    editor.apply();
                }
            }
        });

        // Save new value when picking up new currency
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (first_run){
                    first_run = false;
                }
                else {
                    SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString(PREFERENCE_CURRENCY, (String.valueOf(spinner.getSelectedItem())));
                    editor.apply();
                }
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void getSavedPreferences(){
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(Settings.PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        float price = sharedPref.getFloat(Settings.PREFERENCE_PRICE, 5f);
        int cigsPerPacket = sharedPref.getInt(Settings.PREFERENCE_NUM_CIGARETTES, 20);
        String currency = sharedPref.getString(Settings.PREFERENCE_CURRENCY, "€");

        cigsPerPacketText.setText(String.valueOf(cigsPerPacket));
        priceText.setText(String.valueOf(price));

        String[] aux = getResources().getStringArray(R.array.currencies_list);
        for (int i = 0; i < aux.length; i++) {
            if (aux[i].equals(currency)) {
                spinner.setSelection(i);
            }
        }
    }
}
