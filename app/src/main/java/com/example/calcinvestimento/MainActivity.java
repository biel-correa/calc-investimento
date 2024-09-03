package com.example.calcinvestimento;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private long selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());

            final float scale = getResources().getDisplayMetrics().density;
            final int horizontalPadding = (int) (20 * scale + 0.5f);

            v.setPadding(horizontalPadding, systemBars.top, horizontalPadding, systemBars.bottom);
            return insets;
        });

        this.listenToCalendarChanges();
        this.listenToSubmitClick();
    }

    private void listenToCalendarChanges() {
        CalendarView calendarInput = findViewById(R.id.calendarInput);
        calendarInput.setOnDateChangeListener((CalendarView view, int year, int month, int dayOfMonth) -> {
            selectedDate = new Date(year, month, dayOfMonth).getTime();
        });
    }

    private void listenToSubmitClick() {
        Button submitButton = findViewById(R.id.submit);
        submitButton.setOnClickListener(v -> {
            TextView errorText = findViewById(R.id.errorText);
            errorText.setText("");
            errorText.setVisibility(TextView.INVISIBLE);

            EditText amountInput = findViewById(R.id.amountInput);
            if (amountInput.getText().toString().isEmpty()) {
                errorText.setText("O campo 'Valor' é obrigatório.");
                errorText.setVisibility(TextView.VISIBLE);
                return;
            }

            double amount = Double.parseDouble(amountInput.getText().toString());

            Intent intent = new Intent(this, ResultActivity.class);
            intent.putExtra("amount", amount);
            intent.putExtra("date", selectedDate);
            startActivity(intent);
        });
    }
}