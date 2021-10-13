package com.dtsoftware.pedometer.ui.dashboard;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.dtsoftware.pedometer.AppRepository;
import com.dtsoftware.pedometer.R;
import com.dtsoftware.pedometer.Steps;
import com.dtsoftware.pedometer.databinding.FragmentDashboardBinding;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private FragmentDashboardBinding binding;
    BarChart chart;
    TextView tvAvg;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        chart = root.findViewById(R.id.chart);
        tvAvg = root.findViewById(R.id.tvAvg);

        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.getDescription().setEnabled(false);
        chart.getLegend().setEnabled(false);
        chart.animateY(500);
        chart.getAxisRight().setEnabled(false);
        chart.getAxisLeft().setEnabled(false);
        chart.getAxisLeft().setDrawGridLines(false);
        chart.getXAxis().setDrawGridLines(false);
        chart.getXAxis().setDrawAxisLine(false);
        chart.setTouchEnabled(false);

        dashboardViewModel.getWeekelySteps().observe(getViewLifecycleOwner(), new Observer<List<Steps>>() {
            @Override
            public void onChanged(List<Steps> steps) {
                setWeekelyAverage(steps);
                setXAxisLabels();
                setBarChart(steps);
            }
        });

        return root;
    }

    private void setWeekelyAverage(List<Steps> weekelySteps) {
        int avg = 0;

        for (Steps s : weekelySteps) {
            avg += s.getTotalSteps();
        }

        avg /= weekelySteps.size();

        tvAvg.setText(String.valueOf(avg));

    }


    private void setXAxisLabels() {
        Calendar cal = Calendar.getInstance();
        String[] xAxisLabels = new String[7];
        cal.setTime(new Date());
        int todayDayIndex = cal.get(Calendar.DAY_OF_WEEK);

        xAxisLabels[6] = getDayString(todayDayIndex);
        for (int i = 1; i <= 6; i++) {
            xAxisLabels[6 - i] = getDayString((7 + todayDayIndex - i) % 7);
        }

        chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xAxisLabels));

    }

    private String getDayString(int dayIndex) {
        switch (dayIndex) {
            case Calendar.MONDAY:
                return "MON";
            case Calendar.TUESDAY:
                return "TUE";
            case Calendar.WEDNESDAY:
                return "WED";
            case Calendar.THURSDAY:
                return "THU";
            case Calendar.FRIDAY:
                return "FRI";
            case Calendar.SATURDAY:
            case 0:
                return "SAT";
            case Calendar.SUNDAY:
                return "SUN";
            default:
                return "XXX";
        }

    }


    private void setBarChart(List<Steps> weekelySteps) {

        BarData data = new BarData(getDataSet(weekelySteps));

        chart.setData(data);
        chart.invalidate();


    }


    private ArrayList<IBarDataSet> getDataSet(List<Steps> weekelySteps) {
        ArrayList<IBarDataSet> dataSets = null;
        ArrayList<BarEntry> valueSet1 = new ArrayList<>();


        LocalDate localDate =  LocalDate.now();
        Log.d("PEDOMETER", "numero de registros=" + weekelySteps.size() );

        int i = 0;
        for (Steps s : weekelySteps) {


            valueSet1.add(new BarEntry(i, s.getTotalSteps()));


            i++;
        }

        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Steps");
        barDataSet1.setColor(Color.parseColor("#007DD6"));
        dataSets = new ArrayList<>();
        dataSets.add(barDataSet1);
        return dataSets;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}