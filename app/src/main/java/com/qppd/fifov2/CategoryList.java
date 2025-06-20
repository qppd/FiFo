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

import java.util.List;

public class CategoryList extends ArrayAdapter<Category> {

    private Activity context;
    private List<Category> categoryList;

    public CategoryList(Activity context, List<Category> categoryList){
        super(context, R.layout.fragment_category, categoryList);
        this.context = context;
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View ListViewItem = inflater.inflate(R.layout.layout_list_category, null, true);
        TextView name = (TextView)ListViewItem.findViewById(R.id.name);
        Category category = categoryList.get(position);
        name.setText(category.getName());
        return ListViewItem;

    }

}
