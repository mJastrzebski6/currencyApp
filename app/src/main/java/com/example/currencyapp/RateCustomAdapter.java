package com.example.currencyapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class RateCustomAdapter extends ArrayAdapter {
    private ArrayList<CurrencyRate> _list;
    private Context _context;
    private int _resource;
    private Activity ac;

    public RateCustomAdapter(@NonNull Context context, Activity ac, int resource, ArrayList<CurrencyRate> list) {
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

        TextView currency_code_tv = convertView.findViewById(R.id.currency_code_tv);
        TextView currency_name_tv = convertView.findViewById(R.id.currency_name_tv);
        TextView currency_rate_tv = convertView.findViewById(R.id.currency_rate_tv);
        TextView currency_amount_rate_tv = convertView.findViewById(R.id.currency_amount_rate_tv);
        ImageView flag_iv = convertView.findViewById(R.id.flag_iv);

        currency_code_tv.setText(_list.get(position).getCode());
        currency_name_tv.setText(_list.get(position).getFullName());
        currency_rate_tv.setText(String.valueOf(_list.get(position).getRate()));
        currency_amount_rate_tv.setText(_list.get(position).getAmount_rate());

        flag_iv.setImageBitmap(Helpers.getCurrencyBitmapFromCode(getContext(), _list.get(position).getCode()));

        return convertView;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }
}
