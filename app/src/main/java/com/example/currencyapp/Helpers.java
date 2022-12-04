package com.example.currencyapp;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Helpers {
    public static Map<String, Double> conversionRates = new TreeMap<>();

    public static ArrayList<Currency> currencyArrayList;

    public static void downloadLatestUSDCurrencies(){
        CurrencyAPI currencyAPI = RetrofitCurrency.getClient().create(CurrencyAPI.class);

        try {
            currencyAPI.getAllUSDRates().enqueue(new Callback<USDCurrencies>() {
                @Override
                public void onResponse(Call<USDCurrencies> call, Response<USDCurrencies> response) {
                    assert response.body() != null;

                    Rates cr = response.body().getRates();

                    for (Field field : cr.getClass().getDeclaredFields()) {
                        field.setAccessible(true);
                        Object value = null;
                        try {
                            value = field.get(cr);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                        conversionRates.put(field.getName(), new Double(value.toString()));
                    }
                }

                @Override
                public void onFailure(Call<USDCurrencies> call, Throwable t) {
                    //Toast.makeText(ac, "Api error", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }


    public static void downloadCurrencyArrayList(){
        CurrencyAPI currencyAPI = RetrofitCurrency.getClient().create(CurrencyAPI.class);

        try {
            currencyAPI.getCurrencies().enqueue(new Callback<Symbols>() {
                @Override
                public void onResponse(Call<Symbols> call, Response<Symbols> response) {
                    assert response.body() != null;

                    ArrayList<Currency> currencyArrayList = new ArrayList<>();

                    Map<String, Symbol> map = response.body().getSymbols();

                    for (Map.Entry<String, Symbol> entry : map.entrySet()) {
                        currencyArrayList.add(new Currency(entry.getKey(),entry.getValue().getDescription()));
                    }
                    setCurrencyArrayList(currencyArrayList);
                }

                @Override
                public void onFailure(Call<Symbols> call, Throwable t) {
                    //Toast.makeText(ac, "Api error", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }

    public static Bitmap getCurrencyBitmapFromCode(Context _context, String code){

        AssetManager am = _context.getResources().getAssets();
        try {
            InputStream is = am.open("flags/"+ code.toLowerCase() +".png");
            Bitmap bitmap1 = BitmapFactory.decodeStream(is);
            return bitmap1;

        } catch (IOException e) {

            //e.printStackTrace();
            InputStream is = null;

            try {
                is = am.open("flags/null.png");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return BitmapFactory.decodeStream(is);
        }
    }

    public static ArrayList<Currency> getCurrencyArrayList() {
        return currencyArrayList;
    }

    public static void setCurrencyArrayList(ArrayList<Currency> currencyArrayList) {
        Helpers.currencyArrayList = currencyArrayList;
    }

}

