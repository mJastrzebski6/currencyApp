package com.example.currencyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChooseCurrencyActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_currency);
        getCurrencies();
    }

    public void getCurrencies(){

        createList();
    }

    public void createList(){
        ListView currencyLV = this.findViewById(R.id.currency_lv);

        CurrencyCustomAdapter adapter = new CurrencyCustomAdapter (
                this,
                this,
                R.layout.currency_layout,
                Helpers.getCurrencyArrayList()
        );
        currencyLV.setAdapter(adapter);
    }

}