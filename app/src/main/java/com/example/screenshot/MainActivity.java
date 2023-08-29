package com.example.screenshot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    Button takeImage,imageList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        takeImage = findViewById(R.id.screenshotBTN);
        imageList = findViewById(R.id.savedImageBTN);

        takeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            takeScreenshot();
            }
        });

        imageList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getImage = new Intent(MainActivity.this, savedImage.class);
                startActivity(getImage);
            }
        });
    }

    private void takeScreenshot() {
        View rootView = getWindow().getDecorView().getRootView();
        rootView.setDrawingCacheEnabled(true);

        Bitmap screenshot = Bitmap.createBitmap(rootView.getDrawingCache());
        rootView.setDrawingCacheEnabled(false);

        saveScreenshot(screenshot);
    }

    private void saveScreenshot(Bitmap screenshot) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "Screenshot_" + timeStamp + ".jpg";

        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File imageFile = new File(storageDir, imageFileName);

        try {
            FileOutputStream fos = new FileOutputStream(imageFile);
            screenshot.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            fos.close();

            Toast.makeText(this, "Screenshot saved to " + imageFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to save screenshot", Toast.LENGTH_SHORT).show();
        }
    }

}