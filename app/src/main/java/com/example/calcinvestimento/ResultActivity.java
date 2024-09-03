package com.example.calcinvestimento;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.sql.Timestamp;
import java.time.Duration;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_result);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());

            final float scale = getResources().getDisplayMetrics().density;
            final int horizontalPadding = (int) (20 * scale + 0.5f);

            v.setPadding(horizontalPadding, systemBars.top, horizontalPadding, systemBars.bottom);
            return insets;
        });

        final double amountInvested = getIntent().getDoubleExtra("amount", 0);
        final long endDate = getIntent().getLongExtra("date", 0);

        final double amountReceived = calculateAmountReceived(amountInvested, endDate);

        TextView resultText = findViewById(R.id.result);
        resultText.setText(String.format("R$ %.2f", amountReceived));
    }

    private double calculateAmountReceived(double amountInvested, long endDate) {
        final Timestamp startDate = new Timestamp(System.currentTimeMillis());
        final Timestamp endTimestamp = new Timestamp(endDate);
        final Duration duration = Duration.between(startDate.toInstant(), endTimestamp.toInstant());
    }
}