package com.example.todaydrink;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DutchPayPictureActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView imageView;
    TextView imageViewText;
    Button albumBtn;
    Button cameraBtn;
    Bitmap albumBitmap;
    Bitmap cameraBitmap;

    String currentUser;
    int groupNumber;

    Bitmap image;
    private TessBaseAPI mTess;
    String datapath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dutch_pay_picture);

        Intent intent = getIntent();
        currentUser = intent.getStringExtra("currentUser");
        groupNumber = intent.getIntExtra("groupNumber", 1);

        imageViewText=(TextView)findViewById(R.id.imageViewText);
        albumBtn = (Button)findViewById(R.id.albumBtn);
        albumBtn.setOnClickListener(this);
        cameraBtn = (Button)findViewById(R.id.cameraBtn);
        cameraBtn.setOnClickListener(this);

        imageView = findViewById(R.id.imageView);
    }

    public void onClick(View view){
        if(view == cameraBtn) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, 0);
        }
        else if(view == albumBtn){
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, 1);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            albumBitmap = (Bitmap)extras.get("data");
            imageView.setImageBitmap(albumBitmap);

        }
        else if (requestCode == 1 && resultCode == RESULT_OK){
            try{
                InputStream in = getContentResolver().openInputStream(data.getData());
                cameraBitmap = BitmapFactory.decodeStream(in);
                in.close();
                imageView.setImageBitmap(cameraBitmap);
            }catch(Exception e){
            }
        }
        imageViewText.setVisibility(View.INVISIBLE);
    }

    public void processImage(View view){
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        image = drawable.getBitmap();
        datapath = getFilesDir()+"/tesseract/";
        checkFile(new File(datapath + "tessdata/"));
        String lang = "kor";
        mTess = new TessBaseAPI();
        mTess.init(datapath, lang);

        String OCRresult = null;
        mTess.setImage(image);
        OCRresult = mTess.getUTF8Text();
        String[] menu_list = OCRresult.split("\\n");

        Intent intent = new Intent(this, DutchPayListActivity.class);
        intent.putExtra("menu", menu_list);
        intent.putExtra("currentUser", currentUser);
        intent.putExtra("groupNumber", groupNumber);
        startActivity(intent);
    }

    private void copyFiles(){
        try{
            String filepath = datapath + "/tessdata/kor.traineddata";
            AssetManager assetManager = getAssets();
            InputStream inStream = assetManager.open("tessdata/kor.traineddata");
            OutputStream outStream = new FileOutputStream(filepath);
            byte[] buffer = new byte[1024];
            int read;
            while ((read = inStream.read(buffer)) != -1){
                outStream.write(buffer, 0, read);
            }
            outStream.flush();
            outStream.close();
            inStream.close();
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
            String datafilePath = datapath + "/tessdata/kor.traineddata";
            File datafile = new File(datafilePath);
            if(!datafile.exists()){
                copyFiles();
            }
        }
    }
}