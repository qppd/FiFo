package com.qppd.fifov2.ui.history;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.qppd.fifov2.Classes.History;
import com.qppd.fifov2.Classes.Note;
import com.qppd.fifov2.R;

import java.util.List;

public class HistoryList extends ArrayAdapter<History> {

    private Activity context;
    private List<History> historyList;

    public HistoryList(Activity context, List<History> historyList){
        super(context, R.layout.fragment_history, historyList);
        this.context = context;
        this.historyList = historyList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View ListViewItem = inflater.inflate(R.layout.layout_list_history, null, true);

        TextView history_date = (TextView)ListViewItem.findViewById(R.id.txtDate);
        TextView history_income = (TextView)ListViewItem.findViewById(R.id.txtIncome);
        TextView history_expenses = (TextView)ListViewItem.findViewById(R.id.txtExpenses);
        TextView history_balance = (TextView)ListViewItem.findViewById(R.id.txtBalance);
        TextView history_target = (TextView)ListViewItem.findViewById(R.id.txtTarget);
        TextView history_total = (TextView)ListViewItem.findViewById(R.id.txtTotal);

        History history = historyList.get(position);

        history_date.setText(history.getDate());

        history_income.setText("₱ " + String.format("%.2f", history.getIncome()));
        history_expenses.setText("₱ " + String.format("%.2f", history.getExpenses()));
        history_balance.setText("₱ " + String.format("%.2f", history.getBalance()));
        history_target.setText("₱ " + String.format("%.2f", history.getTarget()));
        history_total.setText("₱ " + String.format("%.2f", history.getBalance()));

        return ListViewItem;

    }

}
