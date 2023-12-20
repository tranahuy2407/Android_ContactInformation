package stu.edu.vn.myapplication.DB;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;


public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context,
                    String name,
                    SQLiteDatabase.CursorFactory factory,
                    int version){
        super(context,name,factory,version);
    }
    public void queryData(String sql){
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
    }
    public void insertData(String name, String age, String phone,String social, String mail, byte[] image){
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO contacts VALUES(NULL,?,?,?,?,?,?)";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindString(1,name);
        statement.bindString(2,age);
        statement.bindString(3,phone);
        statement.bindString(4,social);
        statement.bindString(5,mail);
        statement.bindBlob(6,image);
        statement.executeInsert();
    }
    public void updateData(String name, String age, String phone,String social, String mail, byte[] image,int id){
        SQLiteDatabase database = getWritableDatabase();
        String sql = "UPDATE contacts SET name=?, age=?,phone=?,social=?,mail=?,image=? WHERE id=?";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.bindString(1,name);
        statement.bindString(2,age);
        statement.bindString(3,phone);
        statement.bindString(4,social);
        statement.bindString(5,mail);
        statement.bindBlob(6,image);
        statement.bindDouble(7, id);
        statement.execute();
        database.close();
    }
    public void deleteData(int id){
        SQLiteDatabase database = this.getWritableDatabase();
        String sql ="DELETE FROM contacts WHERE id = ?";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.bindDouble(1,(double) id );
        statement.execute();
        database.close();
    }
    public Cursor getData(String sql){
        SQLiteDatabase database = getReadableDatabase();
        return  database.rawQuery(sql, null);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}