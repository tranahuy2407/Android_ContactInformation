package stu.edu.vn.myapplication.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import stu.edu.vn.myapplication.R;
import stu.edu.vn.myapplication.model.Users;

public class ContactAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<Users> usersList;

    public ContactAdapter(Context context, int layout, ArrayList<Users> usersList) {
        this.context = context;
        this.layout = layout;
        this.usersList = usersList;
    }

    @Override
    public int getCount() {
        return usersList.size();
    }

    @Override
    public Object getItem(int position) {
        return usersList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public class ViewHolder{
        ImageView imageView;
        TextView txtName, txtAge,txtPhone,txtSocial,txtMail;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = new ViewHolder();
        if(row ==null){
            LayoutInflater inflater =(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);
            holder.txtName=row.findViewById(R.id.txtName);
            holder.txtAge=row.findViewById(R.id.txtAge);
            holder.txtPhone=row.findViewById(R.id.txtPhone);
            holder.txtSocial=row.findViewById(R.id.txtSocial);
            holder.txtMail=row.findViewById(R.id.txtMail);
            holder.imageView=row.findViewById(R.id.imgIcon);
            row.setTag(holder);
        }
        else{
            holder=(ViewHolder) row.getTag();
        }
        Users users = usersList.get(position);
        holder.txtName.setText(users.getName());
        holder.txtAge.setText(users.getAge());
        holder.txtPhone.setText(users.getPhone());
        holder.txtSocial.setText(users.getSocial());
        holder.txtMail.setText(users.getMail());
        byte[] img = users.getImage();
        Bitmap bitmap= BitmapFactory.decodeByteArray(img,0,img.length);
        holder.imageView.setImageBitmap(bitmap);
        return row;
    }
}
