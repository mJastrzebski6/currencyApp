package com.example.currencyapp;

import android.util.Log;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Formatter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CurrencyRate {
    private String code;
    private String fullName;
    private Double rate;
    private String amount_rate;
    private Double amount;
    private String targetCode;

    public CurrencyRate(String code, String fullName, String amountX, String targetCodeX) {
        this.code = code;
        this.fullName = fullName;
        this.targetCode = targetCodeX;

        try{
            amount = Double.parseDouble(amountX);
        }
        catch(Exception e){
            amount = 1D;
        }

        changeTargetCode();
    }

    public void changeAmountRate(){
        Double convertedAmount = rate * amount;
        convertedAmount = (double) Math.round(convertedAmount * 10000d) / 10000d;
        amount_rate = amount + " " + targetCode + " = " + convertedAmount +" "+ code;
    }

    public void changeTargetCode(){
        Double USDtoCode = Helpers.conversionRates.get(code.toLowerCase());
        Double USDtoTarget = Helpers.conversionRates.get(targetCode.toLowerCase());
        rate = USDtoCode/USDtoTarget;
        rate = (double) Math.round(rate * 10000d) / 10000d;
        changeAmountRate();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public String getAmount_rate() {
        return amount_rate;
    }

    public void setAmount_rate(String amount_rate) {
        this.amount_rate = amount_rate;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getTargetCode() {
        return targetCode;
    }

    public void setTargetCode(String targetCode) {
        this.targetCode = targetCode;
    }
}

//        Helpers.getRates(code, targetCode, new dOER() {
//            @Override
//            public void doJob(ExchangeRate rate) {
//                amount = rate.getConversionRate();
//                //Log.i("xxx", String.valueOf(amount));
//                amount_rate = "lol";
//            }
//        });