package com.example.currencyapp.ui.converter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.currencyapp.ChooseCurrencyActivity;
import com.example.currencyapp.CurrencyRate;
import com.example.currencyapp.Helpers;
import com.example.currencyapp.RateCustomAdapter;
import com.example.currencyapp.databinding.FragmentConverterBinding;

import com.example.currencyapp.R;

import java.util.ArrayList;
import java.util.Objects;

public class ConverterFragment extends Fragment {

    View root;
    Context ct;
    ArrayList<CurrencyRate> currencyArrayList = new ArrayList<>();
    ListView currencies_lv;
    EditText amount_ed;
    Button chooseCurrencyBt;
    String currentCurrency = "";



    private FragmentConverterBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ConverterViewModel converterViewModel = new ViewModelProvider(this).get(ConverterViewModel.class);

        binding = FragmentConverterBinding.inflate(inflater, container, false);
        root = binding.getRoot();
        chooseCurrencyBt = root.findViewById(R.id.choose_currency_button);
        currencies_lv = root.findViewById(R.id.currencies_list);
        amount_ed = root.findViewById(R.id.amount_ed);

        chooseCurrencyBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chooseActivityIntent = new Intent(getActivity(), ChooseCurrencyActivity.class);
                chooseActivityIntent.putExtra("purpose","getConverterBase");
                someActivityResultLauncher.launch(chooseActivityIntent);
            }
        });

        ct = getActivity();

        View fab = root.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newCurrencyIntent = new Intent(ct, ChooseCurrencyActivity.class);
                newCurrencyIntent.putExtra("purpose","getConverterTarget");
                someActivityResultLauncher.launch(newCurrencyIntent);
            }
        });

        amount_ed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String amount = editable.toString();
                Double amountD;
                try{
                    amountD = Double.parseDouble(amount);
                }
                catch(Exception e){
                    amountD = 1D;
                }

                for(CurrencyRate rate: currencyArrayList){
                    rate.setAmount(amountD);
                    rate.changeAmountRate();
                }

                updateCurrenciesList();
            }
        });

        setDefaultCurrency();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void setDefaultCurrency(){
        currentCurrency = "USD";
        chooseCurrencyBt.setText(currentCurrency);
        Bitmap bm = Helpers.getCurrencyBitmapFromCode(ct, currentCurrency);
        Drawable d = new BitmapDrawable(getResources(), bm);

        chooseCurrencyBt.setCompoundDrawablesWithIntrinsicBounds(d, null,null,null);
    }

    public void updateCurrenciesList(){
        RateCustomAdapter adapter = new RateCustomAdapter (
                getContext(),
                getActivity(),
                R.layout.rate_layout,
                currencyArrayList
        );
        currencies_lv.setAdapter(adapter);
    }


    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();

                if (data == null) return;
                String purpose = data.getStringExtra("purpose");

                if(Objects.equals(purpose, "getConverterBase")){
                    String code = data.getStringExtra("code");
                    chooseCurrencyBt.setText(code);
                    currentCurrency = code;
                    Bitmap bm = Helpers.getCurrencyBitmapFromCode(ct, code);
                    Drawable d = new BitmapDrawable(getResources(), bm);
                    chooseCurrencyBt.setCompoundDrawablesWithIntrinsicBounds(d, null,null,null);
                    changeActiveCurrency();
                }

                else if(purpose.equals("getConverterTarget")){
                    String code = data.getStringExtra("code");
                    String name = data.getStringExtra("name");
                    currencyArrayList.add(new CurrencyRate(code, name, amount_ed.getText().toString(), currentCurrency));
                    updateCurrenciesList();
                }
            }
        }
    });

    public void changeActiveCurrency(){
        for(CurrencyRate rate: currencyArrayList){
            rate.setTargetCode(currentCurrency);
            rate.changeTargetCode();
        }
        updateCurrenciesList();
    }

//    ActivityResultLauncher<Intent> someActivityResultLauncher2 = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
//        @Override
//        public void onActivityResult(ActivityResult result) {
//            if (result.getResultCode() == Activity.RESULT_OK) {
//                Intent data = result.getData();
//                if (data != null) {
//                        String code = data.getStringExtra("code");
//                        String name = data.getStringExtra("name");
//
//                        currencyArrayList.add(new CurrencyRate(code, name, amount_ed.getText().toString(), currentCurrency));
//                        updateCurrenciesList();
//                }
//            }
//        }
//    });


}