package com.qppd.fifov2;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.qppd.fifov2.Classes.Category;
import com.qppd.fifov2.Classes.Expense;
import com.qppd.fifov2.Classes.Saving;

import java.util.List;

public class SavingList extends ArrayAdapter<Saving> {

    private Activity context;
    private List<Saving> savingList;

    public SavingList(Activity context, List<Saving> savingList){
        super(context, R.layout.fragment_saving, savingList);
        this.context = context;
        this.savingList = savingList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View ListViewItem = inflater.inflate(R.layout.layout_list_saving, null, true);


        TextView saving_date = (TextView)ListViewItem.findViewById(R.id.saving_date);
        TextView saving_price = (TextView)ListViewItem.findViewById(R.id.saving_price);

        Saving saving = savingList.get(position);
        saving_date.setText(saving.getDate());
        saving_price.setText("₱ " + String.format("%.2f", saving.getPrice()));
        return ListViewItem;

    }

}
