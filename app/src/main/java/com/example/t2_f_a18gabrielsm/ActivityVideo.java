package com.example.t2_f_a18gabrielsm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ActivityVideo extends AppCompatActivity {

    int RECORD_VIDEO_REQUEST = 0;

    File videosDirPath;
    File videosDir;

    MediaController mediaController;
    TextView tvRutaVideos;
    String selectedVideo;
    Spinner spnVideos;
    VideoView vvVideo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video);

        loadVideoDir();
        loadClicks();
        loadVideoView();

    }

    private void loadVideoView() {
        mediaController = new MediaController(this);

        vvVideo = findViewById(R.id.vvVideo);
        vvVideo.setMediaController(mediaController);
    }

    private void loadClicks() {
        Button btnRecordVideo = findViewById(R.id.btnRecordVideo);
        btnRecordVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
                Date date = new Date();
                String videoName = sdf.format(date);

                videosDir = new File(videosDirPath, videoName + ".mp4");
                Intent recordVideo = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                recordVideo.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(videosDir));
                startActivityForResult(recordVideo, RECORD_VIDEO_REQUEST);
            }
        });
    }

    private void loadVideoDir() {
        videosDirPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);

        tvRutaVideos = findViewById(R.id.tvRutaVideos);
        tvRutaVideos.setText(videosDirPath.toString());

        File[] videosFiles = videosDirPath.listFiles();
        ArrayList<String> videosList = new ArrayList<>();
        if (videosFiles != null){
            for (File video : videosFiles) {
                videosList.add(video.getName());
            }
        }

        ArrayAdapter<String> adapterVideos = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, videosList);
        spnVideos = findViewById(R.id.spnVideos);
        spnVideos.setAdapter(adapterVideos);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == RECORD_VIDEO_REQUEST){
            if (resultCode == RESULT_OK) {
                if (data != null){
                    vvVideo.setVideoURI(data.getData());
                    vvVideo.start();

                    loadVideoDir();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
