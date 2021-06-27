package com.example.android.contact;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.contact.data.MyDbHelper;
import com.example.android.contact.model.Contact;


public class ContactDetail extends AppCompatActivity {

    private static final int REQUEST_CALL=1;
    String myNumber;
    String myName;
    ImageView personImage;
    private byte[] imageByte;
    int myId;
    MyDbHelper db;
    Contact contact;
    Uri imageUri;
    private Bitmap scaleBitmap;
    private static int PICK_PHOTO = 100;
    public static int RESULT_LOAD_IMAGE=1;
    public static final int REQUEST_CODE_GALLERY =999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);


        db = new MyDbHelper(this);
        contact = new Contact();

        ImageView imageView1 = (ImageView)findViewById(R.id.back_button);
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        myNumber = getIntent().getExtras().getString("MyNumber");
        myName = getIntent().getExtras().getString("MyName");
        myId= getIntent().getExtras().getInt("MyId");

        Bundle extras = getIntent().getExtras();
        byte[] byteArray = extras.getByteArray("MyImage");

        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        ImageView image = (ImageView) findViewById(R.id.image_person_photo);

        image.setImageBitmap(bmp);


        TextView textView1 = (TextView) findViewById(R.id.call_number);
        textView1.setText(myNumber);

        ImageView typeMessage = (ImageView)findViewById(R.id.person_contact_message);

        TextView textView2 = (TextView) findViewById(R.id.contact_person_name);
        textView2.setText(myName);

        TextView whatsAppMessage = (TextView)findViewById(R.id.whatsapp_message);
        whatsAppMessage.setText(myNumber);

        TextView textView4 = (TextView)findViewById(R.id.whatsapp_voice_call);
        textView4.setText(myNumber);

        TextView textView5 = (TextView)findViewById(R.id.whatsapp_video_call);
        textView5.setText(myNumber);

        ImageView callImageView = (ImageView)findViewById(R.id.call);
        callImageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                if (ContextCompat.checkSelfPermission(ContactDetail.this,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ContactDetail.this,
                            new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
                } else {
                    String dial = "tel:" + myNumber;
                    startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
                }
            }
        });
        Log.d("Hello","printNumber" + myNumber);


        typeMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW,Uri.fromParts("sms", myNumber, null));
                startActivity(intent);
            }
        });

        ImageView deleteTextView = (ImageView) findViewById(R.id.delete_contact);
        deleteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.deleteContactById(myId);
                Intent intent= new Intent(ContactDetail.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


        TextView whatsAppMessageText = (TextView)findViewById(R.id.whatsapp_message_text);
        whatsAppMessageText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("smsto:" + myNumber);
                Intent intent = new Intent(Intent.ACTION_SENDTO,uri);
                intent.setPackage("com.whatsapp");
                startActivity(intent);
            }
        });

        whatsAppMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("smsto:" + myNumber);
                Intent intent = new Intent(Intent.ACTION_SENDTO,uri);
                intent.setPackage("com.whatsapp");
                startActivity(intent);
            }
        });
    }

}