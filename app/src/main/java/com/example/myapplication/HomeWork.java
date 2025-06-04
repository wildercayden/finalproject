package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class HomeWork extends AppCompatActivity {

    EditText editDate, editName, editDesc;
    Button btnSave;
    final String fileName = "homework.csv";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_work);

        // Handle insets for edge-to-edge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Connect UI elements
        editDate = findViewById(R.id.editDate);
        editName = findViewById(R.id.editName);
        editDesc = findViewById(R.id.editDesc);
        btnSave = findViewById(R.id.btnSave);

        // Save button action
        btnSave.setOnClickListener(view -> {
            String date = editDate.getText().toString().trim();
            String name = editName.getText().toString().trim();
            String desc = editDesc.getText().toString().trim();

            saveToCSV(date, name, desc);
        });
    }

    private void saveToCSV(String date, String name, String desc) {
        File file = new File(getFilesDir(), fileName);
        boolean fileExists = file.exists();

        try {
            FileWriter writer = new FileWriter(file, true); // append mode
            if (!fileExists) {
                writer.append("Due Date,Name,Description\n"); // header
            }
            writer.append(date).append(",")
                    .append(name).append(",")
                    .append(desc).append("\n");
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
