package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MainScreen extends AppCompatActivity {
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainscreen);
        Button nextButton = findViewById(R.id.HomeWork);
        nextButton.setOnClickListener(view -> {
            makeIntent();
                });
        textView = findViewById(R.id.Schooldays);

        new Thread(() -> {
            StringBuilder displayText = new StringBuilder();

            // === School Days Section ===
            AppDatabase db = DatabaseClient.getInstance(getApplicationContext()).getDatabase();
            List<DateRange> ranges = db.dateRangeDao().getAllRanges();
            if (!ranges.isEmpty()) {
                DateRange latest = ranges.get(ranges.size() - 1);
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                try {
                    Date today = truncateTime(new Date());
                    Date lastDate = truncateTime(sdf.parse(latest.lastDate));

                    long diffMillis = lastDate.getTime() - today.getTime();
                    long daysLeft = diffMillis / (1000 * 60 * 60 * 24);

                    if (daysLeft >= 0) {
                        displayText.append(" School days left: ").append(daysLeft).append("\n\n");
                    } else {
                        displayText.append(" School is already over!\n\n");
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                    displayText.append(" Error parsing date.\n\n");
                }
            } else {
                displayText.append(" No date range found.\n\n");
            }

            // === Homework Section ===
            File file = new File(getFilesDir(), "homework.csv");
            if (file.exists()) {
                displayText.append("📚 Homework List:\n");

                try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                    String line;
                    boolean skipHeader = true;

                    while ((line = br.readLine()) != null) {
                        if (skipHeader) {
                            skipHeader = false; // skip the CSV header
                            continue;
                        }
                        String[] parts = line.split(",", -1);
                        if (parts.length >= 3) {
                            String dueDate = parts[0];
                            String name = parts[1];
                            String desc = parts[2];

                            displayText.append("• ").append(name).append(" (Due: ").append(dueDate).append(")\n")
                                    .append("   → ").append(desc).append("\n\n");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    displayText.append(" Failed to read homework file.\n");
                }
            } else {
                displayText.append("📝 No homework saved yet.");
            }

            runOnUiThread(() -> textView.setText(displayText.toString()));

        }).start();
    }

    private void makeIntent () {
        Intent intent = new Intent(MainScreen.this, HomeWork.class);
        startActivity(intent);
    }

    private Date truncateTime(Date date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String formatted = sdf.format(date);
            return sdf.parse(formatted);
        } catch (ParseException e) {
            e.printStackTrace();
            return date;
        }

    }
}
