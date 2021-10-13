package com.dtsoftware.pedometer.ui.home;

import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.dtsoftware.pedometer.R;
import com.dtsoftware.pedometer.databinding.FragmentHomeBinding;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    ProgressBar progreso;
    TextView tvSteps;
    private FragmentHomeBinding binding;
    private int steps;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        tvSteps = root.findViewById(R.id.tvSteps);
        progreso = root.findViewById(R.id.progress_circular_id);
        progreso.setMax(8000);

        homeViewModel.getText().observe(getViewLifecycleOwner(), this::update);


        progreso.setOnClickListener(v -> {

            ObjectAnimator progressAnimator;
            progressAnimator = ObjectAnimator.ofInt(progreso, "progress", 0, steps);
            progressAnimator.setDuration(500);
            progressAnimator.start();

        });


        return root;
    }

    private void update(int i) {
//        progreso.setProgress(i);
        tvSteps.setText(String.valueOf(i));
        ObjectAnimator progressAnimator;
        progressAnimator = ObjectAnimator.ofInt(progreso, "progress", steps, i);
        progressAnimator.setDuration(500);
        steps = i;
        progressAnimator.start();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}