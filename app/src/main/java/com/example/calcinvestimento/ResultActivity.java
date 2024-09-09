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
import java.time.Instant;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        try {
            getSupportActionBar().hide();
        } catch (NullPointerException ignored) {}

        setContentView(R.layout.activity_result);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());

            final float scale = getResources().getDisplayMetrics().density;
            final int horizontalPadding = (int) (20 * scale + 0.5f);

            v.setPadding(horizontalPadding, systemBars.top, horizontalPadding, systemBars.bottom);
            return insets;
        });

        this.listenToBackButton();

        final double amountInvested = getIntent().getDoubleExtra("amount", 0);
        final double cdiPercentage = getIntent().getDoubleExtra("cdiPercentage", 0);
        final Timestamp endDate = new Timestamp(getIntent().getLongExtra("date", 0));
        final Duration duration = getDuration(endDate);

        final double amountReceived = calculateAmountReceived(amountInvested, duration, cdiPercentage);
        final double taxRate = getTaxRate(duration);
        final double tax = amountReceived * taxRate;
        final double oneMonthAmountReceivedPercentage = getOneMonthAmountReceivedPercentage(amountInvested, Duration.ofDays(30), cdiPercentage);
        final double oneYearAmountReceivedPercentage = getOneMonthAmountReceivedPercentage(amountInvested, Duration.ofDays(365), cdiPercentage);
        final double selectedTimeAmountReceivedPercentage = getOneMonthAmountReceivedPercentage(amountInvested, duration, cdiPercentage);

        setText(R.id.result, String.format("R$ %.2f", amountReceived + amountInvested));
        setText(R.id.totalEarnings, String.format("R$ %.2f", amountReceived));
        setText(R.id.initialValue, String.format("R$ %.2f", amountInvested));
        setText(R.id.grossValue, String.format("R$ %.2f", amountReceived + amountInvested));
        setText(R.id.earningsValue, String.format("R$ %.2f", amountReceived));
        setText(R.id.irValue, String.format("R$ %.2f(%.2f%%)", tax, taxRate * 100));
        setText(R.id.netValue, String.format("R$ %.2f", amountReceived + amountInvested - tax));
        setText(R.id.redeemDate, endDate.toString().substring(8, 10) + "/" + endDate.toString().substring(5, 7) + "/" + endDate.toString().substring(0, 4));
        setText(R.id.daysElapsed, String.valueOf(duration.toDays()));
        setText(R.id.monthlyEarnings, String.format("R$ %.2f%%", oneMonthAmountReceivedPercentage));
        setText(R.id.annualProfitability, String.format("R$ %.2f%%", oneYearAmountReceivedPercentage));
        setText(R.id.periodProfitability, String.format("R$ %.2f%%", selectedTimeAmountReceivedPercentage));
        setText(R.id.cdiPercentage, String.format("R$ %.2f%%", cdiPercentage));
    }

    private double getOneMonthAmountReceivedPercentage(double amountInvested, Duration oneMonthDuration, double cdiPercentage) {
        final double received = calculateAmountReceived(amountInvested, oneMonthDuration, cdiPercentage);
        return (received / amountInvested) * 100;
    }

    private void listenToBackButton() {
        findViewById(R.id.back).setOnClickListener(v -> {
            finish();
        });
    }

    private Duration getDuration(Timestamp endDate) {
        final Instant startDate = new Timestamp(System.currentTimeMillis()).toInstant();
        return Duration.between(startDate, endDate.toInstant());
    }

    private double getTaxRate(Duration duration) {
        final long days = duration.toDays();

        if (days <= 180) {
            return 0.175;
        } else if (days <= 360) {
            return 0.15;
        } else if (days <= 720) {
            return 0.1;
        }

        return 0.05;
    }

    private void setText(int id, String text) {
        TextView textView = findViewById(id);
        textView.setText(text);
    }

    private double calculateAmountReceived(double amountInvested, Duration duration, double cdiPercentage) {
        double years = duration.toDays() / 365.0;
        double annualRate = 0.105 * (cdiPercentage / 100);
        return (amountInvested * Math.pow(1 + annualRate, years)) - amountInvested;
    }
}