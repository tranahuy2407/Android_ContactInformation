package stu.edu.vn.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import stu.edu.vn.myapplication.adapter.ContactAdapter;
import stu.edu.vn.myapplication.model.Users;

public class MainActivity2 extends AppCompatActivity {
    ListView mListView;
    ArrayList<Users> mList;
    ContactAdapter contactAdapter=null;
    ImageView imageView;
    FloatingActionButton fabThem;

     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        addControls();
        addEvents();

    }

    private void addEvents() {
        fabThem = findViewById(R.id.fabThem);
         fabThem.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent = new Intent(
                         MainActivity2.this,
                        MainActivity.class
                 );
                 startActivity(intent);
             }
         });
    }

    private void addControls() {
        mListView = findViewById(R.id.listView);
        mList =new ArrayList<>();
        contactAdapter = new ContactAdapter(this,R.layout.item_contact, mList);
        mListView.setAdapter(contactAdapter);
        Cursor cursor = MainActivity.mDbHelper.getData("SELECT * FROM contacts");
        mList.clear();
        while (cursor.moveToNext()){
            int id =cursor.getInt(0);
            String name =cursor.getString(1);
            String age =cursor.getString(2);
            String phone =cursor.getString(3);
            String social =cursor.getString(4);
            String mail =cursor.getString(5);
            byte[] image = cursor.getBlob(6);
            mList.add(new Users(id,name,age,phone,social,mail,image));

        }
        contactAdapter.notifyDataSetChanged();
        if(mList.size()==0){
            Toast.makeText(this,"Không tìm thấy...!",Toast.LENGTH_LONG).show();
        }
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final CharSequence[] items ={"Sửa","Xóa"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity2.this);
                dialog.setTitle("Bạn muốn ?");
                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which==0){
                            Cursor c = MainActivity.mDbHelper.getData("SELECT id FROM contacts");
                            ArrayList<Integer> arrID =new ArrayList<Integer>();
                            while (c.moveToNext()){
                                arrID.add(c.getInt(0));
                            }
                            showDialogUpdate(MainActivity2.this,arrID.get(position));
                        }
                        if(which==1){
                            Cursor c = MainActivity.mDbHelper.getData("SELECT id FROM contacts");
                            ArrayList<Integer> arrID = new ArrayList<Integer>();
                            while (c.moveToNext()){
                                arrID.add(c.getInt(0));
                            }
                            showDialogDelete(arrID.get(position));
                        }
                    }
                });
                dialog.show();
                return true;
            }
        });
    }
//    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
//        super.onCreateContextMenu(menu, v, menuInfo);
//        if(v.getId() == R.id.listView){
//            getMenuInflater().inflate(R.menu.context_menu, menu);
//        }
//    }
//
//
//    @Override
//    public boolean onContextItemSelected(@NonNull MenuItem item) {
//        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
//        int index = info.position;
//        if(item.getItemId() == R.id.btnSua){
//            Cursor c = MainActivity.mDbHelper.getData("SELECT id FROM contacts");
//            ArrayList<Integer> arrID =new ArrayList<Integer>();
//            while (c.moveToNext()){
//                arrID.add(c.getInt(0));
//            }
//            showDialogUpdate(MainActivity2.this,arrID.get(index));
//        } else if (item.getItemId()==R.id.btnXoa) {
//            Cursor c = MainActivity.mDbHelper.getData("SELECT * FROM contacts");
//            ArrayList<Integer> arrID = new ArrayList<Integer>();
//            while (c.moveToNext()){
//                arrID.add(c.getInt(0));
//            }
//            showDialogDelete(arrID.get(index));
//
//        }
//        return super.onContextItemSelected(item);
//    }

    private void showDialogDelete(int idRecord) {
         AlertDialog.Builder dialogDelete = new AlertDialog.Builder(MainActivity2.this);
         dialogDelete.setTitle("Warning!!");
         dialogDelete.setMessage("Bạn có chắc muốn xóa!");
         dialogDelete.setPositiveButton("OK", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int which) {
                 try{
                     MainActivity.mDbHelper.deleteData(idRecord);
                     Toast.makeText(MainActivity2.this,"Xóa thành công!", Toast.LENGTH_SHORT).show();

                 }catch (Exception e){
                    Log.e("Lỗi",e.getMessage());
                 }
                 updateRecordList();
             }
         });
         dialogDelete.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int which) {
                 dialog.dismiss();
             }
         });
         dialogDelete.show();
    }

    private void showDialogUpdate(Activity activity, final int position){
         final Dialog dialog= new Dialog(activity);
         dialog.setContentView(R.layout.update_dialog);
         dialog.setTitle("Cập nhật");
         imageView = dialog.findViewById(R.id.imgViewRecord);
        final EditText editName = dialog.findViewById(R.id.edtName);
        final EditText editAge = dialog.findViewById(R.id.edtAge);
        final EditText editPhone = dialog.findViewById(R.id.edtPhone);
        final EditText editSocial = dialog.findViewById(R.id.edtSocial);
        final EditText editMail = dialog.findViewById(R.id.edtMail);
        Button btnUpdate = dialog.findViewById(R.id.btnUpdate);

        Cursor cursor = MainActivity.mDbHelper.getData("SELECT * FROM contacts WHERE id ="+position);
        mList.clear();
        while (cursor.moveToNext()){
            int id =cursor.getInt(0);
            String name =cursor.getString(1);
            editName.setText(name);
            String age =cursor.getString(2);
            editAge.setText(age);
            String phone =cursor.getString(3);
            editPhone.setText(phone);
            String social =cursor.getString(4);
            editSocial.setText(social);
            String mail =cursor.getString(5);
            editMail.setText(mail);
            byte[] image = cursor.getBlob(6);
            imageView.setImageBitmap(BitmapFactory.decodeByteArray(image,0,image.length));
            mList.add(new Users(id,name,age,phone,social,mail,image));
        }
        int width = (int)(activity.getResources().getDisplayMetrics().widthPixels*0.95);
        int height =(int)(activity.getResources().getDisplayMetrics().heightPixels*0.7);
        dialog.getWindow().setLayout(width,height);
        dialog.show();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(
                        MainActivity2.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},888
                );
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    MainActivity.mDbHelper.updateData(
                            editName.getText().toString().trim(),
                            editAge.getText().toString().trim(),
                            editPhone.getText().toString().trim(),
                            editSocial.getText().toString().trim(),
                            editMail.getText().toString().trim(),
                            MainActivity.imageViewToByte(imageView),
                            position
                    );
                    dialog.dismiss();
                    Toast.makeText(activity,"Cập nhật thành công",Toast.LENGTH_LONG).show();
                }
                catch (Exception e){
                    Log.e("Lỗi cập nhật",e.getMessage());
                }
                updateRecordList();
            }
        });
    }

    private void updateRecordList() {
        Cursor cursor = MainActivity.mDbHelper.getData("SELECT * FROM contacts");
        mList.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String age = cursor.getString(2);
            String phone = cursor.getString(3);
            String social = cursor.getString(4);
            String mail = cursor.getString(5);
            byte[] image = cursor.getBlob(6);
            mList.add(new Users(id, name, age, phone, social, mail, image));
        }
        contactAdapter.notifyDataSetChanged();
    }

    public static byte[] imageViewToByte(ImageView mImageView) {
        Bitmap bitmap=((BitmapDrawable) mImageView.getDrawable() ).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }


    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 888) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent galeryIntent= new Intent(Intent.ACTION_GET_CONTENT);
                galeryIntent.setType("image/*");
                startActivityForResult(galeryIntent, 888);
            }
            else{
                Toast.makeText(this,"Không có quyền truy cập!",Toast.LENGTH_LONG).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 888 && resultCode == RESULT_OK){
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
                imageView.setImageURI(resultUri);
            }
            else if(resultCode==CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception error = result.getError();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}