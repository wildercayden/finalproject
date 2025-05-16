package com.example.myapplication;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import java.text.SimpleDateFormat;





public class SetupActivity extends AppCompatActivity {
    private CalendarView calendarView;
    private long firstSelectedDate = 0;
    private long lastSelectedDate = 0;
    private long previousFirstDate = 0; // Store the previous first selected date
    private long previousLastDate = 0; // Store the previous last selected date
    private String firstString;
    private String lastString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        TextView TextView2 = findViewById(R.id.textView2);


        calendarView = findViewById(R.id.calendarView);
        Button nextButton = findViewById(R.id.HomeWork);
        Button undoButton = findViewById(R.id.Undo);

        // On first selection, store the first selected date
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            if (firstSelectedDate == 0) {
                // Store the previous first date before updating
                previousFirstDate = firstSelectedDate;
                firstSelectedDate = calendarView.getDate();
                Toast.makeText(this, "First date selected", Toast.LENGTH_SHORT).show();
                TextView2.setText("Now select the last day of school");
                TextView2.setTypeface(null, Typeface.BOLD);
            } else {
                // Store the previous second (last) date before updating
                previousLastDate = lastSelectedDate;
                lastSelectedDate = calendarView.getDate();
                Toast.makeText(this, "Last date selected", Toast.LENGTH_SHORT).show();
            }
        });

        // Undo functionality: revert the date selection
        undoButton.setOnClickListener(view -> {
            if (firstSelectedDate != 0 && lastSelectedDate != 0) {
                // Revert to the previous date selection
                firstSelectedDate = previousFirstDate;
                lastSelectedDate = previousLastDate;

                // Update the UI and inform the user
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                firstString = sdf.format(firstSelectedDate);
                lastString = sdf.format(lastSelectedDate);
                Toast.makeText(getApplicationContext(), "Selection undone", Toast.LENGTH_SHORT).show();
                TextView2.setText("Pless Select the first day of school");
            } else {
                Toast.makeText(getApplicationContext(), "No previous selection to undo", Toast.LENGTH_SHORT).show();
            }
        });

        // Next button to proceed
        nextButton.setOnClickListener(view -> {
            if (firstSelectedDate != 0 && lastSelectedDate != 0) {
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                firstString = sdf.format(firstSelectedDate);
                lastString = sdf.format(lastSelectedDate);

                if (firstSelectedDate > lastSelectedDate) {
                    Toast.makeText(getApplicationContext(), "First date must be before last date", Toast.LENGTH_SHORT).show();
                } else {
                    // Insert into Room
                    new Thread(() -> {
                        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "dates-db")
                                .build();

                        DateRange range = new DateRange(firstString, lastString);
                        db.dateRangeDao().insert(range);

                        runOnUiThread(this::makeIntent);
                    }).start();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Please select both dates", Toast.LENGTH_SHORT).show();
            }
        });
    }
        private void BackIntent () {
            Intent intent = new Intent(SetupActivity.this, MainActivity.class);
            startActivity(intent);
        }

        private void makeIntent () {
            Intent intent = new Intent(SetupActivity.this, MainScreen.class);
            startActivity(intent);
        }
    }