package com.example.todaydrink;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DutchPayPictureActivity extends AppCompatActivity {

    private static final int PICK_FROM_ALBUM = 1;
    Uri uri;
    ImageView imageView;
    Bitmap image;
    private TessBaseAPI mTess;
    String datapath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dutch_pay_picture);

        imageView = findViewById(R.id.imageView);
        image = BitmapFactory.decodeResource(getResources(), R.drawable.sample_eng3);
        datapath = getFilesDir()+"/tesseract/";

        checkFile(new File(datapath + "tessdata/"));

        String lang = "kor";

        mTess = new TessBaseAPI();
        mTess.init(datapath, lang);
    }

    public void uploadImage(View view){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, RESULT_OK);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_OK && data != null) {
            uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                imageView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void processImage(View view){
        String OCRresult = null;
        mTess.setImage(image);
        OCRresult = mTess.getUTF8Text();
        String[] menu_list = OCRresult.split("\\n");
        TextView OCRTextView = (TextView)findViewById(R.id.OCRTextView);
        OCRTextView.setText(OCRresult);

        Intent intent = new Intent(this, DutchPayListActivity.class);
        intent.putExtra("menu", menu_list);
        startActivity(intent);
    }

    private void copyFiles(){
        try{
            String filepath = datapath + "/tessdata/kor.traineddata";
            AssetManager assetManager = getAssets();
            InputStream instream = assetManager.open("tessdata/kor.traineddata");
            OutputStream outstream = new FileOutputStream(filepath);
            byte[] buffer = new byte[1024];
            int read;
            while ((read = instream.read(buffer)) != -1){
                outstream.write(buffer, 0, read);
            }
            outstream.flush();
            outstream.close();
            instream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkFile(File dir){
        if(!dir.exists() && dir.mkdirs()){
            copyFiles();
        }
        if((dir.exists())){
            String datafilepath = datapath + "/tessdata/kor.traineddata";
            File datafile = new File(datafilepath);
            if(!datafile.exists()){
                copyFiles();
            }
        }
    }
}