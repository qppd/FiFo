package com.qppd.fifov2;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.qppd.fifov2.Classes.Category;
import com.qppd.fifov2.Classes.Expense;

import java.util.List;

public class ExpenseList extends ArrayAdapter<Expense> {

    private Activity context;
    private List<Expense> expenseList;

    private LinearLayout list_background;
    private String previous_date = "-";
    private String current_date = "-";

    public ExpenseList(Activity context, List<Expense> expenseList){
        super(context, R.layout.fragment_expense, expenseList);
        this.context = context;
        this.expenseList = expenseList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View ListViewItem = inflater.inflate(R.layout.layout_list_expense, null, true);
        TextView expense_date = (TextView)ListViewItem.findViewById(R.id.expense_date);
        TextView expense_title = (TextView)ListViewItem.findViewById(R.id.expense_title);
        TextView expense_price = (TextView)ListViewItem.findViewById(R.id.expense_price);
        LinearLayout linearLayout = (LinearLayout)ListViewItem.findViewById(R.id.list_background);


        Expense expense = expenseList.get(position);
        current_date = expense.getDate();
        if(previous_date.equals(current_date)){
            previous_date = current_date;
            expense_date.setVisibility(View.GONE);
        }
        else{
            previous_date = current_date;
            expense_date.setText(expense.getDate());
        }

        expense_title.setText(expense.getProduct());
        expense_price.setText("₱ " + String.format("%.2f", expense.getPrice()));
        return ListViewItem;

    }

}
