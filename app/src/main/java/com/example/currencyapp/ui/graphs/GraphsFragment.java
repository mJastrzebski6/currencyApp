package com.example.currencyapp.ui.graphs;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.currencyapp.ChooseCurrencyActivity;
import com.example.currencyapp.Currency;
import com.example.currencyapp.CurrencyAPI;
import com.example.currencyapp.Helpers;
import com.example.currencyapp.RetrofitCurrency;
import com.example.currencyapp.Symbol;
import com.example.currencyapp.Symbols;
import com.example.currencyapp.TimeSeries;
import com.example.currencyapp.TimeSeriesElement;
import com.example.currencyapp.databinding.FragmentGraphsBinding;
import com.example.currencyapp.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.sql.Time;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GraphsFragment extends Fragment {

    View root;
    LinearLayout graph_ll;

    Button base_button;
    Button target_button;

    TextView one_week_tv;
    TextView one_month_tv;
    TextView one_year_tv;

    Boolean currentGraphType = true;
    String currentBaseCode = "USD";
    String currentTargetCode = "USD";
    String currentStartDate = "2020-01-01";

    private FragmentGraphsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        GraphsViewModel graphsViewModel = new ViewModelProvider(this).get(GraphsViewModel.class);
        setHasOptionsMenu(true);


        binding = FragmentGraphsBinding.inflate(inflater, container, false);
        root = binding.getRoot();
        graph_ll  = root.findViewById(R.id.graph_ll);

        base_button = root.findViewById(R.id.choose_base_currency_button);
        target_button = root.findViewById(R.id.choose_target_currency_button);

        one_week_tv = root.findViewById(R.id.one_week_tv);
        one_month_tv = root.findViewById(R.id.one_month_tv);
        one_year_tv = root.findViewById(R.id.one_year_tv);

        one_week_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DAY_OF_YEAR, -7);
                Log.i("xxx", "week: " + calToString(cal));
                currentStartDate = calToString(cal);
                getData();
            }
        });

        one_month_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cal = Calendar.getInstance();
                cal.add(Calendar.MONTH, -1);
                Log.i("xxx", "mont: " + calToString(cal));
                currentStartDate = calToString(cal);
                getData();
            }
        });

        one_year_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cal = Calendar.getInstance();
                cal.add(Calendar.YEAR, -1);
                Log.i("xxx", "year: " + calToString(cal));
                currentStartDate = calToString(cal);
                getData();
            }
        });

        base_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chooseBaseActivityIntent = new Intent(getActivity(), ChooseCurrencyActivity.class);
                chooseBaseActivityIntent.putExtra("purpose","getGraphBase");
                someActivityResultLauncher.launch(chooseBaseActivityIntent);
            }
        });
        target_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chooseTargetActivityIntent = new Intent(getActivity(), ChooseCurrencyActivity.class);
                chooseTargetActivityIntent.putExtra("purpose","getGraphTarget");
                someActivityResultLauncher.launch(chooseTargetActivityIntent);
            }
        });
        setDefaultCurrency();

        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -7);
        currentStartDate = calToString(cal);
        Log.i("xxx", "star: " + currentStartDate);

        getData();
        return root;
    }

    public String calToString(Calendar cal){
        String res = String.valueOf(cal.get(Calendar.YEAR)) + "-" ;

        if(cal.get(Calendar.MONTH)+1 < 10){
            res += "0" + (cal.get(Calendar.MONTH)+1) + "-";
        }
        else res += (cal.get(Calendar.MONTH)+1) + "-";

        if(cal.get(Calendar.DAY_OF_MONTH) < 10){
            res += "0" + cal.get(Calendar.DAY_OF_MONTH);
        }
        else res += cal.get(Calendar.DAY_OF_MONTH);

        return res;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        //super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.charts_menu, menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_line_chart:  {
                currentGraphType = false;
                getData();
                return true;
            }
            case R.id.action_bar_chart: {
                currentGraphType = true;
                getData();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void setDefaultCurrency(){
        base_button.setText(currentBaseCode);
        Bitmap bm = Helpers.getCurrencyBitmapFromCode(getContext(), currentBaseCode);
        Drawable d = new BitmapDrawable(getResources(), bm);
        base_button.setCompoundDrawablesWithIntrinsicBounds(d, null,null,null);

        target_button.setText(currentTargetCode);
        bm = Helpers.getCurrencyBitmapFromCode(getContext(), currentTargetCode);
        d = new BitmapDrawable(getResources(), bm);
        target_button.setCompoundDrawablesWithIntrinsicBounds(d, null,null,null);
    }

    public void getData(){
        CurrencyAPI currencyAPI = RetrofitCurrency.getClient().create(CurrencyAPI.class);

        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        String endDate = calToString(cal);

        try {
            currencyAPI.getTimeSeries(currentBaseCode,currentTargetCode,currentStartDate,endDate).enqueue(new Callback<TimeSeries>() {
                @Override
                public void onResponse(Call<TimeSeries> call, Response<TimeSeries> response) {
                    assert response.body() != null;
                    updateChart(response.body().getRates());
                }

                @Override
                public void onFailure(Call<TimeSeries> call, Throwable t) {
                    //Toast.makeText(ac, "Api error", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void updateChart(Map<String, Map<String, Double>> rates){
        if(!currentGraphType){
            setLinearGraph(rates);
        }
        else{
            setBarGraph(rates);
        }
    }

    public void setBarGraph(Map<String, Map<String, Double>> rates){
        List<BarEntry> entries = new ArrayList<BarEntry>();

        Integer i = 0;
        for (Map.Entry<String, Map<String, Double>> entry : rates.entrySet()) {
            Float y = entry.getValue().get(currentTargetCode.toUpperCase()).floatValue();
            entries.add(new BarEntry(i,y));
            i++;
        }

        BarChart chart = new BarChart(getContext());
        chart.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        graph_ll.removeAllViews();
        graph_ll.addView(chart);

        BarDataSet dataSet = new BarDataSet(entries, currentBaseCode + " to " + currentTargetCode);
        //dataSet.setDrawCircles(false);
        dataSet.setColor(Color.parseColor("#ff0000"));

        BarData lineData = new BarData(dataSet);
        chart.setData(lineData);
        chart.invalidate();
    }

    public void setLinearGraph(Map<String, Map<String, Double>> rates){
        List<Entry> entries = new ArrayList<Entry>();

        Integer i = 0;
        for (Map.Entry<String, Map<String, Double>> entry : rates.entrySet()) {
            //Log.i("xxx", String.valueOf(entry.getValue()));

            Float y = entry.getValue().get(currentTargetCode.toUpperCase()).floatValue();
            entries.add(new Entry(i,y));
            i++;
        }

        LineChart chart = new LineChart(getContext());
        chart.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        graph_ll.removeAllViews();
        graph_ll.addView(chart);

        LineDataSet dataSet = new LineDataSet(entries, currentBaseCode + " to " + currentTargetCode);
        dataSet.setDrawCircles(false);
        dataSet.setColor(Color.parseColor("#ff0000"));
        //dataSet.setValueTextColor();

        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        chart.invalidate();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                if (data == null)  return;

                String purpose = data.getStringExtra("purpose");

                if(purpose.equals("getGraphBase")){
                    String code = data.getStringExtra("code");
                    base_button.setText(code);
                    currentBaseCode = code;
                    Bitmap bm = Helpers.getCurrencyBitmapFromCode(getContext(), code);
                    Drawable d = new BitmapDrawable(getResources(), bm);
                    base_button.setCompoundDrawablesWithIntrinsicBounds(d, null,null,null);

                }
                else if(purpose.equals("getGraphTarget")){
                    String code = data.getStringExtra("code");
                    target_button.setText(code);
                    currentTargetCode = code;
                    Bitmap bm = Helpers.getCurrencyBitmapFromCode(getContext(), code);
                    Drawable d = new BitmapDrawable(getResources(), bm);
                    target_button.setCompoundDrawablesWithIntrinsicBounds(d, null,null,null);
                }

                getData();
            }
        }
    });

}