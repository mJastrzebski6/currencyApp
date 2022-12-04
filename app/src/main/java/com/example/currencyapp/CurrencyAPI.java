package com.example.currencyapp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CurrencyAPI {


    @GET("symbols")
    Call<Symbols> getCurrencies();

    @GET("latest&base=USD")
    Call<USDCurrencies> getAllUSDRates();

    @GET("timeseries")
    Call<TimeSeries> getTimeSeries(
            @Query("base") String base,
            @Query("symbols") String symbols,
            @Query("start_date") String start_date,
            @Query("end_date") String end_date
    );
}



