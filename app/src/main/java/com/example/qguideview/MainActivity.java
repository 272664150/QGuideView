package com.example.qguideview;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (!hasFocus) {
            return;
        }

        new QGuideView.Builder(this)
                .setOnlyOnceTag(this.getClass().getSimpleName())
                .setBackgroundColor(Color.parseColor("#80000000"))
//                .setTargetView(findViewById(R.id.hello_tv))
                .addDrawable(R.drawable.ic_launcher_foreground)
                .addDrawable(R.drawable.ic_launcher_background)
                .setOnDisplayListener(new QGuideView.OnDisplayListener() {
                    @Override
                    public void onShow(QGuideView guideView) {
                    }

                    @Override
                    public void onReappear(boolean isReappear) {
                    }

                    @Override
                    public void onDismiss() {
                    }
                })
                .build().show();
    }
}
