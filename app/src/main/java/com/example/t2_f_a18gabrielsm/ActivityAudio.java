package com.example.t2_f_a18gabrielsm;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ActivityAudio extends AppCompatActivity {

    AssetManager assetManager;
    SharedPreferences prefs;
    String audiosDirPath;
    File audiosDir;

    AlertDialog.Builder builder;
    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;
    String selectedAudio;
    Spinner spnAudios;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audio);

        mediaPlayer = new MediaPlayer();
        loadAudios();
        loadClicks();

    }

    private void loadClicks() {
        spnAudios.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedAudio = String.valueOf(spnAudios.getSelectedItem());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button btnPlay = findViewById(R.id.btnPlay);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer != null || mediaPlayer.isPlaying()){
                    mediaPlayer.reset();
                }

                mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mediaPlayer.setDataSource(audiosDirPath + selectedAudio);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        Button btnStop = findViewById(R.id.btnStop);
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                } else {
                    Toast.makeText(ActivityAudio.this, R.string.noAudioToast, Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button btnRecord = findViewById(R.id.btnRecord);
        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
                Date date = new Date();
                String recordDate = sdf.format(date);

                mediaRecorder = new MediaRecorder();
                mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
                mediaRecorder.setOutputFile(audiosDirPath + "record_" + recordDate + ".3gp");
                mediaRecorder.setMaxDuration(7000);
                try {
                    mediaRecorder.prepare();
                    mediaRecorder.start();
                } catch (Exception e) {
                    e.printStackTrace();
                    mediaRecorder.reset();
                }

                builder = new AlertDialog.Builder(ActivityAudio.this);
                builder.setMessage(R.string.recordingDialog);
                builder.setPositiveButton(R.string.recordingDialogStop, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mediaRecorder.stop();
                        mediaRecorder.release();
                        mediaRecorder = null;
                        loadAudios();
                    }
                });

                builder.setCancelable(false);
                builder.show();

            }
        });
    }

    private void loadAudios() {

        prefs = getSharedPreferences("PREFS", MODE_PRIVATE);
        audiosDirPath = getExternalFilesDir(null) + File.separator + "AUDIO" + File.separator + prefs.getString("NAME", "") + File.separator;
        audiosDir = new File(audiosDirPath);
        audiosDir.mkdirs();

        assetManager = getAssets();

        try {
            String[] assetList = assetManager.list("");

            if (assetList != null) {
                for (String fileName : assetList) {

                    InputStream is;
                    OutputStream os;

                    is = assetManager.open(fileName);
                    File outFile = new File(audiosDir, fileName);
                    os = new FileOutputStream(outFile);

                    byte[] buffer = new byte[1024];
                    int read;
                    while((read = is.read(buffer)) != -1){
                        os.write(buffer, 0, read);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        File[] audiosFiles = audiosDir.listFiles();
        ArrayList<String> audiosList = new ArrayList<>();
        if (audiosFiles != null) {
            for (File audio : audiosFiles) {
                audiosList.add(audio.getName());
            }
        }

        ArrayAdapter<String> adapterAudios = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, audiosList);
        spnAudios = findViewById(R.id.spnAudios);
        spnAudios.setAdapter(adapterAudios);

    }

    @Override
    protected void onStop() {
        if (mediaPlayer != null || mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
        super.onStop();
    }
}
