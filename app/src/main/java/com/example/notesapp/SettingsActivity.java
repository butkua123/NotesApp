package com.example.notesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class SettingsActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sharedPreferences = getSharedPreferences(MainActivity.PREFS_NAME, MODE_PRIVATE);

        // Text Size Spinner Setup
        Spinner textSizeSpinner = findViewById(R.id.spinnerTextSize);
        ArrayAdapter<CharSequence> textSizeAdapter = ArrayAdapter.createFromResource(this,
                R.array.text_size_options, android.R.layout.simple_spinner_item);
        textSizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        textSizeSpinner.setAdapter(textSizeAdapter);

        textSizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //Spinner'daki bir öğe seçildiğinde çağrılır. Seçilen öğeye göre işlemler gerçekleştirir.
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int textSize = Integer.parseInt(parent.getItemAtPosition(position).toString());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("textSize", textSize);
                editor.apply();
            }
            //Spinner'da hiçbir öğe seçilmediğinde çağrılır. Bu durumda yapılabilecek işlemleri tanımlar.
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        int currentTextSize = sharedPreferences.getInt("textSize", 16); // Default value
        textSizeSpinner.setSelection(textSizeAdapter.getPosition(String.valueOf(currentTextSize)));

        // Text Color Spinner Setup
        Spinner textColorSpinner = findViewById(R.id.spinnerTextColor);
        ArrayAdapter<CharSequence> textColorAdapter = ArrayAdapter.createFromResource(this,
                R.array.text_color_options, android.R.layout.simple_spinner_item);
        textColorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        textColorSpinner.setAdapter(textColorAdapter);

        textColorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedColor = parent.getItemAtPosition(position).toString();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("textColor", selectedColor);
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        String currentColor = sharedPreferences.getString("textColor", "Black");
        textColorSpinner.setSelection(textColorAdapter.getPosition(currentColor));
    }
}



