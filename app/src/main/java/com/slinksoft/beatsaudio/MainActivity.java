package com.slinksoft.beatsaudio;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.audiofx.AudioEffect;
import android.media.audiofx.BassBoost;
import android.media.audiofx.LoudnessEnhancer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {
    int sessionID= 0;
    int version = 1;
    int revision = 8;
    Switch aSwitch;
    LoudnessEnhancer BAEffect;
    TextView verDisp;
    Button eq;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle(Html.fromHtml("<font color='#ff0000'>Beats Audio: By SlinkSoft </font>"));
        setContentView(R.layout.activity_main);
        aSwitch = findViewById(R.id.beatsAudioSwitch);
        BAEffect = new LoudnessEnhancer(sessionID);
        verDisp = findViewById(R.id.versionDisplay);
        eq = findViewById(R.id.equalizerButton);
        verDisp.setText("Version: " + version + "." + revision);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (aSwitch.isChecked())
                {
                    eq.setEnabled(false);
                    eq.setVisibility(View.INVISIBLE);
                    applyBeatsAudio();
                    startService();

                }
                else if (!aSwitch.isChecked())
                {
                    eq.setEnabled(true);
                    eq.setVisibility(View.VISIBLE);
                    stopService();
                    disableBeatsAudio();
                }

            }
        });


    createNotificationChannel();

    }

    public void startService() {


        Intent serviceIntent = new Intent(this, ForegroundService.class);
        serviceIntent.putExtra("inputExtra", "Beats Audio");

        ContextCompat.startForegroundService(this, serviceIntent);
    }

    public void stopService() {
        Intent serviceIntent = new Intent(this, ForegroundService.class);
        stopService(serviceIntent);
    }

    private void applyBeatsAudio()
    {
        BAEffect.setTargetGain(860);
        BAEffect.setEnabled(true);
    }

    private void disableBeatsAudio()
    {
        BAEffect.setEnabled(false);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    "BA",
                    "Beats Audio Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    public void about(View v)
    {
        AlertDialog note = new AlertDialog.Builder(MainActivity.this).create();
        note.setTitle("About");
        note.setMessage("This app simply enhances sound and slightly increases bass, but not too much. " +
                "This was the original purpose of Beats Audio during the Monster/HTC days. This app also " +
                "allows you to directly open up your built-in equalizer (finding this feature in settings can be" +
                " confusing, so you can access it from here). Finally, I wanted to get that Beats Audio feel back, not" +
                " only for a slightly better Hip-Hop audio experience, but also have that feeling of having such a feature, " +
                "just like during the Monster/HTC days. This app was made purely for fun.");
        note.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        note.show();
    }

    public void onCredits(View v)
    {
        AlertDialog credits = new AlertDialog.Builder(MainActivity.this).create();
        credits.setTitle("Credits");
        credits.setMessage("Developed By: Slink (Dan)\nVisit:\nhttps://realslinksoft.wixsite.com/slink-soft-portfolio" +
                "\nand\nhttp://www.YouTube.Com/ReTrOSlink\nThank you for using this app!\n\n- Slink");
        credits.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        credits.setButton(AlertDialog.BUTTON_POSITIVE, "Visit SlinkSoft",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://realslinksoft.wixsite.com/slink-soft-portfolio"));
                        startActivity(browserIntent);
                        dialogInterface.dismiss();
                    }
                });

        credits.show();
    }

    public void openEqualizer(View v)
    {
        Intent intent = new Intent(AudioEffect
                .ACTION_DISPLAY_AUDIO_EFFECT_CONTROL_PANEL);

        if ((intent.resolveActivity(getPackageManager()) != null)) {
            startActivityForResult(intent, 1);
        } else {
            AlertDialog note = new AlertDialog.Builder(MainActivity.this).create();
            note.setTitle("Error");
            note.setMessage("Cannot find the system's built-in equalizer! Perhaps your Android OS " +
                    "and/or phone does not provide one, which is very uncommon. Look up your phone model " +
                    "and Android version to see if an equalizer is provided (you should be able to open it " +
                    "within your \"Sound & Notification\" or \"Sound & Vibration\" menu in your settings. Once " +
                    "there, click \"Audio Effects\" or a similar button. Most phones SHOULD have a built-in equalizer.");
            note.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
            note.show();
        }
    }
}
