package com.example.t_sashar.persona;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.graphics.BitmapCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class DisplayUserQR extends AppCompatActivity {

    ImageView imageView;
    final String PREFS_NAME = "MyPrefsFile";
    String user_id = "";
    SharedPreferences preferences;
    public static final int white = 0xFFFFFFFF;
    public static final int black = 0xFF000000;
    public final static int WIDTH = 500;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_user_qr);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        imageView = (ImageView) findViewById(R.id.qr_image);
        preferences = getSharedPreferences(PREFS_NAME, 0);
//        user_id = preferences.getString("user_id", "0");

        try {
            textToImageEncode("Brian-Ikenna");
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    public void textToImageEncode(String str) throws WriterException {
        BitMatrix bitMatrix = null;
        try {
            bitMatrix = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, WIDTH, WIDTH, null);

        } catch (IllegalArgumentException e) {

            e.printStackTrace();
        }

        // add the dimensions to the bitMatrix
        int bitMatrixWidth = bitMatrix.getWidth();
        int bitMatrixHeight = bitMatrix.getHeight();
        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;
            for (int x = 0; x < bitMatrixWidth; x++) {
                pixels[offset + x] = bitMatrix.get(x, y) ?
                        black : white;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);
        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);
        if(bitmap == null){
            Toast.makeText(getApplicationContext(), "Could not create QR", Toast.LENGTH_SHORT).show();
        }
        this.bitmap = bitmap;

        imageView.setImageBitmap(bitmap);
    }

}

