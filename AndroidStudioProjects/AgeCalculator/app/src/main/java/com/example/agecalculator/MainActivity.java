package com.example.agecalculator;
import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.Period;

public class MainActivity extends AppCompatActivity {
    //Input Fields
    private EditText inputFirstName;
    private EditText inputLastName;
    private EditText inputBirthDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        //Get the fields info and create button
        inputFirstName = findViewById(R.id.firstName);
        inputLastName = findViewById(R.id.lastName);
        inputBirthDate = findViewById(R.id.birthDate);
        Button calculateAge = findViewById(R.id.ageCalculate);

        //Date Picker
        inputBirthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
        inputBirthDate.setKeyListener(null);

        //Button action for calculating the user's age
        calculateAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get input fields and turn into strings
                String firstName = inputFirstName.getText().toString();
                String lastName = inputLastName.getText().toString();
                String birthDate = inputBirthDate.getText().toString();

                //making sure all the fields are not empty before proceeding; returns a toast if empty
                if (checkNull(inputFirstName) || checkNull(inputLastName) || checkNull(inputBirthDate)) {
                    Toast.makeText(MainActivity.this, "Error: Please fill in all fields.", Toast.LENGTH_LONG).show();
                    return;
                }

                Log.i("input", birthDate);

                //calculate age and check if date is valid
                int age = getAge(birthDate);
                if (age < 0) {
                    Toast.makeText(MainActivity.this, "Something went wrong. Please select a valid date.", Toast.LENGTH_LONG).show();
                    return;
                }

                //Display age in toast
                Log.i("input", Integer.toString(age));
                String toastText = firstName + " " + lastName + " your age is " + Integer.toString(age);
                Toast.makeText(MainActivity.this, toastText, Toast.LENGTH_LONG).show();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    /* Shows the Date Picker */
    private void showDatePicker() {
        LocalDate now = LocalDate.now();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                MainActivity.this,
                (view, year, month, dayOfMonth) -> {
                    String date = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year);
                    inputBirthDate.setText(date);
                },
                now.getYear(),
                now.getMonthValue() - 1,
                now.getDayOfMonth()
        );
        datePickerDialog.show();
    }

    /* Calculates the Age */
    private int getAge(String inputBirthDate) {
        try {
            int[] birthDateParsed = parseDate(inputBirthDate);
            //Parsed date format is day/month/year but Local Date is month/day/year
            LocalDate birthDate = LocalDate.of(birthDateParsed[2], birthDateParsed[1], birthDateParsed[0]);
            LocalDate currentDate = LocalDate.now();
            return Period.between(birthDate, currentDate).getYears();
        } catch (Exception e) {
            Log.e("AgeCalculator", "Invalid birth date format: " + inputBirthDate, e);
            return -1; // Sentinel value for invalid age
        }
    }

    /* Checking if the input fields be empty or not */
    private boolean checkNull(EditText inputField) {
        return inputField.getText().toString().trim().isEmpty();
    }

    /* Parsing the Input Date to be in the same format as the local date */
    private int[] parseDate(String date) throws Exception {
        String[] parseDate = date.split("/");
        if (parseDate.length != 3) throw new Exception("Invalid date format");
        int[] intDate = new int[3];
        for (int i = 0; i < 3; i++) {
            intDate[i] = Integer.parseInt(parseDate[i]);
        }
        return intDate;
    }
}