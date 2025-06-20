package com.qppd.fifov2.ui.history;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.qppd.fifov2.Classes.History;
import com.qppd.fifov2.Classes.Note;
import com.qppd.fifov2.Database.DBHandler;
import com.qppd.fifov2.Globals.UserGlobal;
import com.qppd.fifov2.Libs.DateTimez.DateTimeClass;
import com.qppd.fifov2.Libs.Functionz.UserFunctions;
import com.qppd.fifov2.Libs.SharedPreferencez.SharedPreferencesClass;
import com.qppd.fifov2.R;
import com.qppd.fifov2.databinding.FragmentHistoryBinding;
import com.qppd.fifov2.databinding.FragmentProfileBinding;
import com.qppd.fifov2.ui.note.NoteList;

import java.util.ArrayList;
import java.util.Map;

public class HistoryFragment extends Fragment {

    private FragmentHistoryBinding binding;

    private UserFunctions functions;
    private DateTimeClass dateTimeClass;
    private SharedPreferencesClass sharedPreferencesClass;

    private DBHandler dbHandler;

    private ListView listHistory;

    private int history_key;
    private ArrayList<String> history_keys;
    private ArrayList<History> history_list;

    private HistoryList historyAdapter;
    private History history;

    private View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHistoryBinding.inflate(inflater, container, false);
        root = binding.getRoot();

        functions = new UserFunctions(root.getContext());
        sharedPreferencesClass = new SharedPreferencesClass(getContext());
        dateTimeClass = new DateTimeClass("MM/dd/YYYY");
        dbHandler = new DBHandler(root.getContext());

        initComponents();
        loadHistory();
        return root;
    }

    private void loadHistory() {
        double income;
        double balance;
        double target;
        double total;

        history_list = new ArrayList<>();
        history_keys = new ArrayList<>();

        Map<String, Double[]> data = dbHandler.getTotalExpensesAndSavingsGroupedByMonthAndYear();
        for (Map.Entry<String, Double[]> entry : data.entrySet()) {
            String monthYear = entry.getKey();
            Double[] values = entry.getValue();
            double expenses = values[0];
            double savings = values[1];

            income = Double.parseDouble(dbHandler.getProfile(UserGlobal.getUser().getId()).getIncome());
            balance = income - expenses;
            target = Double.parseDouble(sharedPreferencesClass.getString("target", ""));
            total = savings;
            history_list.add(new History(monthYear, income, expenses, balance, target, balance));

        }

        historyAdapter = new HistoryList((Activity)root.getContext(), history_list);
        listHistory.setAdapter(historyAdapter);

    }

    private void initComponents() {
        functions = new UserFunctions(root.getContext());
        listHistory = root.findViewById(R.id.listHistory);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}