package com.honeywell.sweetspot;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

//
// import android.support.v7.app.AppCompatActivity;

public class SweetSpotActivity extends Activity {

    Button btnGenerateBubble;
    SeekBar sbTransparency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sweetspot);

        SharedPreferences myPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor myEditor = myPref.edit();
        myEditor.putInt("Transparency",sbTransparency.get)

        btnGenerateBubble = (Button) findViewById(R.id.btnPutFloatingBubble);
        btnGenerateBubble.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SweetSpotActivity.this, FloatingBubbleService.class);
                startService(intent);

            }
        });

        /*Intent intent = new Intent(SweetSpotActivity.this, FloatingBubbleService.class);
        startService(intent);
        moveTaskToBack(true);*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*Intent intent = new Intent(SweetSpotActivity.this, FloatingBubbleService.class);
        startService(intent);
        moveTaskToBack(true);*/
    }
}
