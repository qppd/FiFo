package com.qppd.fifov2;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.qppd.fifov2.Classes.CategoryExpense;
import com.qppd.fifov2.Classes.Expense;
import com.qppd.fifov2.Classes.Saving;
import com.qppd.fifov2.Database.DBHandler;
import com.qppd.fifov2.Globals.UserGlobal;
import com.qppd.fifov2.Libs.DateTimez.DateTimeClass;
import com.qppd.fifov2.Libs.Functionz.UserFunctions;
import com.qppd.fifov2.Libs.SharedPreferencez.SharedPreferencesClass;
import com.qppd.fifov2.databinding.FragmentSavingActualBinding;
import com.qppd.fifov2.databinding.FragmentSavingBinding;

import java.util.ArrayList;

public class SavingActualFragment extends Fragment implements View.OnClickListener {

    private FragmentSavingActualBinding binding;
    private View root;

    private UserFunctions functions;
    private DateTimeClass dateTimeClass;
    private SharedPreferencesClass sharedPreferencesClass;

    private DBHandler dbHandler;

    private TextView txtDate;
    private TextView txtIncome;
    private TextView txtExpenses;
    private TextView txtBalance;
    private TextView txtTarget;
    private TextView txtTotal;

    PieChart chartTotalSavings;

    private Button btnBack;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSavingActualBinding.inflate(inflater, container, false);
        root = binding.getRoot();

        functions = new UserFunctions(root.getContext());
        sharedPreferencesClass = new SharedPreferencesClass(getContext());
        dateTimeClass = new DateTimeClass("MM/dd/YYYY");
        dbHandler = new DBHandler(root.getContext());

        initComponents();

        ArrayList<CategoryExpense> arrayList = new ArrayList<>();
        arrayList = dbHandler.getCategoriesWithTotalExpenseCurrentMonthHighestToLowest();

        LinearLayout llCategories = root.findViewById(R.id.llCategories);

        boolean isGreen = true;
        for(int x=0; x< arrayList.size(); x++){

            View view = inflater.inflate(R.layout.layout_categories_highest_to_lowest, null);
            LinearLayout lLayout = view.findViewById(R.id.lLayout);
            TextView category_name = view.findViewById(R.id.category_name);
            TextView category_price = view.findViewById(R.id.category_price);

            if(isGreen){
                isGreen = false;
            }
            else if(!isGreen){
                isGreen = true;
                lLayout.setBackgroundColor(Color.WHITE);
                category_name.setTextColor(Color.BLACK);
                category_price.setTextColor(Color.BLACK);
            }
            category_name.setText(arrayList.get(x).getCategory_name());
            category_price.setText("₱" + arrayList.get(x).getExpense_total());
            llCategories.addView(view);

            //functions.showMessage(arrayList.get(x).getCategory_name() + "-" + arrayList.get(x).getExpense_total());
        }

        ArrayList<Expense> arrayListPriorities = new ArrayList<>();
        arrayListPriorities = dbHandler.getCurrentMonthPriorityTotalExpenses();

        LinearLayout llPriorities = root.findViewById(R.id.llPriority);

        for(int x=0; x < arrayListPriorities.size(); x++){

            View view = inflater.inflate(R.layout.layout_priority, null);
            LinearLayout lLayoutPrio = view.findViewById(R.id.lLayoutPrio);
            TextView priority_name = view.findViewById(R.id.priority_name);
            TextView priority_price = view.findViewById(R.id.priority_price);

            if(isGreen){
                isGreen = false;
            }
            else if(!isGreen){
                isGreen = true;
                lLayoutPrio.setBackgroundColor(Color.WHITE);
                priority_name.setTextColor(Color.BLACK);
                priority_price.setTextColor(Color.BLACK);
            }
            priority_name.setText(arrayListPriorities.get(x).getProduct());
            priority_price.setText("₱" + arrayListPriorities.get(x).getPrice());
            llPriorities.addView(view);

            //functions.showMessage(arrayListPriorities.get(x).getProduct() + "-" + arrayListPriorities.get(x).getPriority() + "-" + arrayListPriorities.get(x).getPrice());
        }
        loadData();


        return root;
    }

    private void loadData() {
        double income, expenses, balance, target, total;
        income = Double.parseDouble(dbHandler.getProfile(UserGlobal.getUser().getId()).getIncome());
        expenses = dbHandler.getCurrentMonthTotalExpenses(Integer.parseInt(
                new DateTimeClass("M").getFormattedTime()));
        balance = income - expenses;
        target = Double.parseDouble(sharedPreferencesClass.getString("target", ""));
        total = dbHandler.getCurrentMonthTotalSaving(Integer.parseInt(
                new DateTimeClass("M").getFormattedTime()));

        txtDate.setText(dateTimeClass.getFormattedTime());

        txtIncome.setText("₱ " + String.format("%.2f", income));
        txtExpenses.setText("₱ " + String.format("%.2f", expenses));
        txtBalance.setText("₱ " + String.format("%.2f", balance));
        txtTarget.setText("₱ " + String.format("%.2f", target));
        txtTotal.setText("₱ " + String.format("%.2f", balance));

        loadPieChart(income, balance);
    }


    private void initComponents() {

        txtDate = root.findViewById(R.id.txtDate);
        txtIncome = root.findViewById(R.id.txtIncome);
        txtExpenses = root.findViewById(R.id.txtExpenses);
        txtBalance = root.findViewById(R.id.txtBalance);

        txtTarget = root.findViewById(R.id.txtTarget);
        txtTotal = root.findViewById(R.id.txtTotal);

        chartTotalSavings = root.findViewById(R.id.chartTotalSavings);

        btnBack = root.findViewById(R.id.btnSavingActualBack);
        btnBack.setOnClickListener(this);


    }

    private void loadPieChart(double income, double balance) {

        ArrayList<PieEntry> entries = new ArrayList<>();

        entries.add(new PieEntry(Float.parseFloat(String.valueOf(income)), "Income"));
        entries.add(new PieEntry(Float.parseFloat(String.valueOf(balance)), "Savings"));

        ArrayList<CategoryExpense> categoryExpenseList = dbHandler.getCategoriesWithTotalExpenseCurrentMonth();

        for(CategoryExpense categoryExpense : categoryExpenseList){
            entries.add(new PieEntry(Float.parseFloat(String.valueOf(categoryExpense.getExpense_total())), categoryExpense.getCategory_name()));
        }

        int[] colors = {
                ContextCompat.getColor(getActivity().getApplicationContext(),
                        R.color.teal_700),
                ContextCompat.getColor(getActivity().getApplicationContext(),
                        R.color.aqua),
                ContextCompat.getColor(getActivity().getApplicationContext(),
                        R.color.yellow),
                ContextCompat.getColor(getActivity().getApplicationContext(),
                        R.color.teal_200),
                ContextCompat.getColor(getActivity().getApplicationContext(),
                        R.color.light_blue),
                ContextCompat.getColor(getActivity().getApplicationContext(),
                        R.color.teal),
                ContextCompat.getColor(getActivity().getApplicationContext(),
                        R.color.colorMain),
                ContextCompat.getColor(getActivity().getApplicationContext(),
                        R.color.colorAccent),
                ContextCompat.getColor(getActivity().getApplicationContext(),
                        R.color.orange),
        };


        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(colors);
        dataSet.setSliceSpace(2);
        dataSet.setValueTextSize(14);
        dataSet.setXValuePosition(PieDataSet.ValuePosition.INSIDE_SLICE);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.getSelectionShift();
        dataSet.setValueLineColor(Color.GREEN);

        int color;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // If the SDK version is Marshmallow (API 23) or higher, use ContextCompat.getColor()
            color = ContextCompat.getColor(getContext(), R.color.colorMainDark);
        } else {
            // For SDK versions lower than Marshmallow, use getResources().getColor()
            color = getResources().getColor(R.color.colorMainDark);
        }

        chartTotalSavings.setBackgroundColor(color);
        //chartTotalSavings.setCenterText(dateTimeClass.getFormattedTime());
//        chartTotalSavings.setCenterTextColor(Color.BLACK);
//        chartTotalSavings.setCenterTextSize(20);

        chartTotalSavings.setHoleColor(color);
        chartTotalSavings.setHoleRadius(2);
        chartTotalSavings.setDrawHoleEnabled(false);

        PieData data = new PieData(dataSet);
        chartTotalSavings.setData(data);

        // Description
        chartTotalSavings.getDescription().setEnabled(true);
        chartTotalSavings.getDescription().setText("Monthly Savings Report");

        // Legend
        chartTotalSavings.getLegend().setEnabled(true);
        chartTotalSavings.animateXY(1500, 1500);



        chartTotalSavings.invalidate();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnSavingActualBack:
                getParentFragmentManager().popBackStack();
                break;
        }
    }
}