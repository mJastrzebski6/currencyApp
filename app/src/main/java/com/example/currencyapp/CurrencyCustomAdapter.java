package com.example.currencyapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class CurrencyCustomAdapter extends ArrayAdapter {

    private ArrayList<Currency> _list;
    private Context _context;
    private int _resource;
    private Activity ac;

    public CurrencyCustomAdapter(@NonNull Context context, Activity ac, int resource, ArrayList<Currency> list) {
        super(context, resource, list);

        this._list= list;
        this._context = context;
        this._resource = resource;
        this.ac = ac;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(_resource, null);

        TextView fullNameTV = convertView.findViewById(R.id.currency_name_tv);
        ImageView iv = convertView.findViewById(R.id.currency_code_iv);

        Bitmap bitmap = Helpers.getCurrencyBitmapFromCode(_context, _list.get(position).getCode());
        iv.setImageBitmap(bitmap);

        fullNameTV.setText(_list.get(position).getFullName());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = ac.getIntent();

                Intent intent = new Intent();
                intent.putExtra("purpose", it.getStringExtra("purpose"));
                intent.putExtra("code", _list.get(position).getCode());
                intent.putExtra("name", _list.get(position).getFullName());
                ac.setResult(Activity.RESULT_OK, intent);
                ac.finish();
            }
        });

        return convertView;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }
}
