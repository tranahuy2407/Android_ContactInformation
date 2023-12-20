package stu.edu.vn.myapplication;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;

import stu.edu.vn.myapplication.DB.DBHelper;

public class MainActivity extends AppCompatActivity {
    EditText mEditName,mEditAge,mEditPhone,mEditSocial,mEditMail;
    Button mbtnAdd,mbtnList;
    FloatingActionButton mbtnCapture;
    ImageView mImageView;

    private static final int REQUEST_CODE_CAMERA = 123;

    final int REQUEST_CODE_GALLERY = 999;

    public static DBHelper mDbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addControls();
        addEvents();
        mDbHelper = new DBHelper(this,"CONTACT.db",null, 1);
        mDbHelper.queryData("CREATE TABLE IF NOT EXISTS contacts(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name VARCHAR, age VARCHAR, phone VARCHAR, social VARCHAR, mail TEXT, image BLOB)");
    }

    private void addEvents() {
        mImageView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ActivityCompat.requestPermissions(
                    MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_CODE_GALLERY
            );

        }
    });
        mbtnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mở camera để chụp ảnh
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_CODE_CAMERA);
            }
        });

        mbtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    mDbHelper.insertData(
                            mEditName.getText().toString().trim(),
                            mEditAge.getText().toString().trim(),
                            mEditPhone.getText().toString().trim(),
                            mEditSocial.getText().toString().trim(),
                            mEditMail.getText().toString().trim(),
                            imageViewToByte(mImageView)
                    );
                    Toast.makeText(MainActivity.this, "Đã thêm vào danh sách bạn bè !", Toast.LENGTH_LONG).show();
                    mEditName.setText("");
                    mEditAge.setText("");
                    mEditPhone.setText("");
                    mEditSocial.setText("");
                    mEditMail.setText("");
                    mImageView.setImageResource(R.drawable.ic_add_photo);
                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
        mbtnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MainActivity2.class));

            }
        });

    }

    public static byte[] imageViewToByte(ImageView mImageView) {
        Bitmap bitmap=((BitmapDrawable) mImageView.getDrawable() ).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    private void addControls() {
        mEditName = findViewById(R.id.edtName);
        mEditAge = findViewById(R.id.edtAge);
        mEditPhone = findViewById(R.id.edtPhone);
        mEditSocial = findViewById(R.id.edtSocial);
        mEditMail = findViewById(R.id.edtMail);
        mbtnAdd = findViewById(R.id.btnAdd);
        mbtnList = findViewById(R.id.btnList);
        mImageView = findViewById(R.id.imageView);
        mbtnCapture = findViewById(R.id.btnCapture);

    }
public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

    if (requestCode == REQUEST_CODE_GALLERY) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Intent galeryIntent= new Intent(Intent.ACTION_GET_CONTENT);
            galeryIntent.setType("image/*");
            startActivityForResult(galeryIntent, REQUEST_CODE_GALLERY);
        }
        else{
            Toast.makeText(this,"Không có quyền truy cập!",Toast.LENGTH_LONG).show();
        }
        return;
    }
    else if (requestCode == REQUEST_CODE_CAMERA) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, REQUEST_CODE_CAMERA);
        } else {
            Toast.makeText(this, "Không có quyền truy cập Camera!", Toast.LENGTH_LONG).show();
        }
    }
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
}
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK){
            Uri imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);
        }
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result =CropImage.getActivityResult(data);
            if(resultCode==RESULT_OK){
                Uri resultUri = result.getUri();
                mImageView.setImageURI(resultUri);
            }
            else if(resultCode==CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception error = result.getError();
            }
        }
    if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK) {
        Bitmap photo = (Bitmap) data.getExtras().get("data");
        mImageView.setImageBitmap(photo);
    }
    super.onActivityResult(requestCode, resultCode, data);
}

}