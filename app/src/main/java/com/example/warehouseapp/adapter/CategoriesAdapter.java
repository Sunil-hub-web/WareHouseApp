package com.example.warehouseapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.example.warehouseapp.R;
import com.example.warehouseapp.modelclass.Category_ModelClass;

import java.util.ArrayList;

public class CategoriesAdapter extends ArrayAdapter<Category_ModelClass> {

    private ArrayList<Category_ModelClass> myarrayList;

    public CategoriesAdapter(FragmentActivity activity, int textViewResourceId, ArrayList<Category_ModelClass> categories) {
        super(activity,textViewResourceId,categories);

        this.myarrayList = categories;
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, parent);
    }

    @Nullable
    @Override
    public Category_ModelClass getItem(int position) {
        return myarrayList.get(position);
    }

    @Override
    public int getCount() {
        int count = myarrayList.size();
        //return count > 0 ? count - 1 : count;
        return count;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, parent);
    }

    private View getCustomView(int position, ViewGroup parent) {
        Category_ModelClass model = getItem(position);

        View spinnerRow = LayoutInflater.from(parent.getContext()).inflate(R.layout.spinneritem, parent, false);

        TextView label = spinnerRow.findViewById(R.id.spinner_text);
        label.setText(String.format("%s", model != null ? model.getCatname() : ""));


        return spinnerRow;
    }
}
