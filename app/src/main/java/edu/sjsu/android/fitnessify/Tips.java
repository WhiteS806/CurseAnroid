package edu.sjsu.android.fitnessify;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

public class Tips extends AppCompatActivity {

    WebView home_workout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips);
        // Что касается советов, когда пользователь нажимает кнопку «Советы», он перенаправляет их на веб-сайт NY Times.
        home_workout = findViewById(R.id.tips_web_view);
        // загрузка URL
        home_workout.loadUrl(getString(R.string.tips_url));
    }
}