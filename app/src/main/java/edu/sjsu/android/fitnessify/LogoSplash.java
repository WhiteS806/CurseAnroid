package edu.sjsu.android.fitnessify;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class LogoSplash extends AppCompatActivity {

    // Класс logoSplash 3 секунды
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo_splash);
        new Handler().postDelayed(() -> {
            startActivity(new Intent(LogoSplash.this, MainActivity.class));
            finish();
        },3000);
    }
}
