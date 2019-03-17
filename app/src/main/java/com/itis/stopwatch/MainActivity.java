package com.itis.stopwatch;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private TextView timeView;
    private ImageButton controlButton;
    private ImageButton resetButton;

    private Timer timer;

    private long startTime;

    private static final long DELAY  = 0;
    private static final long PERIOD = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timeView      = findViewById(R.id.time_view);

        controlButton = findViewById(R.id.control_button);
        controlButton.setOnClickListener(controlButtonClickListener);

        resetButton   = findViewById(R.id.reset_button);
        resetButton.setOnClickListener(resetButtonClickListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (timer != null)
            timer.cancel();
    }

    private void startTimer() {
        controlButton.setSelected(true);
        resetButton.setEnabled(false);

        timer = new Timer();
        startTime = SystemClock.uptimeMillis();

        TimerTask stopwatch = new TimerTask() {
            @Override
            public void run() {

                long mills = SystemClock.uptimeMillis() - startTime;
                SimpleDateFormat outputTimeFormat = new SimpleDateFormat(
                        getString(R.string.time_format_pattern)
                );
                final String timeString = outputTimeFormat.format(mills);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        timeView.setText(timeString);
                    }
                });
            }
        };

        timer.schedule(stopwatch, DELAY, PERIOD);
    }

    private void stopTimer() {
        controlButton.setSelected(false);
        resetButton.setEnabled(true);
        timer.cancel();
    }

    private void setIconButtonControl(int id) {
        controlButton.setImageDrawable(
                ContextCompat.getDrawable(getApplicationContext(), id)
        );
    }

    View.OnClickListener controlButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(controlButton.isSelected()) {
                stopTimer();
                setIconButtonControl(R.drawable.ic_play);
            }
            else {
                startTimer();
                setIconButtonControl(R.drawable.ic_stop);
            }
        }
    };

    View.OnClickListener resetButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            timeView.setText(getString(R.string.null_time));
        }
    };
}
