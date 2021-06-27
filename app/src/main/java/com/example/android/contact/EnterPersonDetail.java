package com.example.android.contact;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.contact.data.MyDbHelper;
import com.example.android.contact.model.Contact;
import com.example.android.contact.utils.DbBitmapUtility;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class EnterPersonDetail extends AppCompatActivity {

    MyDbHelper db;
    Contact contact;
    String fullName;
    String mobileNumber;
    byte[] imageByte;
    EditText editFirstName;
    EditText editMobileNumber;
    ImageView profileImage;
    private Bitmap scaleBitmap;
    private boolean isImageSelected = false;
    Uri uri;
    private static int PICK_PHOTO = 100;
    public static int RESULT_LOAD_IMAGE=1;
    public static final int REQUEST_CODE_GALLERY =999;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_person_detail);

        db = new MyDbHelper(EnterPersonDetail.this);


        editFirstName = (EditText)findViewById(R.id.first_name);


        editMobileNumber = (EditText)findViewById(R.id.mobile_number);

        contact = new Contact();

        profileImage=(ImageView)findViewById(R.id.profile_photo);
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(EnterPersonDetail.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},PICK_PHOTO);
            }
        });

        Button saveButton = (Button)findViewById(R.id.save_data);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    fullName = editFirstName.getText().toString();
                    mobileNumber = editMobileNumber.getText().toString();
                    if(isImageSelected) {
                        if (mobileNumber.length() == 10) {
                            imageByte = DbBitmapUtility.getBytes(scaleBitmap);
                            contact.setImage(imageByte);
                            contact.setName("" + fullName);
                            contact.setPhoneNumber("" + mobileNumber);
                            db.addContact(contact);
                            Intent intent = new Intent(EnterPersonDetail.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(EnterPersonDetail.this, "Please Enter Correct Number", Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(EnterPersonDetail.this, "Please Upload Image", Toast.LENGTH_LONG).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == PICK_PHOTO){
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent , PICK_PHOTO);
            }
            else {

                Toast.makeText(getApplicationContext(),"You dont have permission to acess this media",Toast.LENGTH_SHORT);
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            Uri selectedImage =  data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(selectedImage);
                Bitmap bitmap =  BitmapFactory.decodeStream(inputStream);
                profileImage.setImageBitmap(bitmap);
                BitmapDrawable drawable = (BitmapDrawable) profileImage.getDrawable();
                bitmap = drawable.getBitmap();
                scaleBitmap = Bitmap.createScaledBitmap(bitmap, 160, 160, false);
                isImageSelected=true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }
}