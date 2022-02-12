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
import com.example.warehouseapp.modelclass.SubCategory_ModelClass;

import java.util.ArrayList;

public class SubCategoriesAdapter extends ArrayAdapter<SubCategory_ModelClass> {

    private ArrayList<SubCategory_ModelClass> myarrayList;

    public SubCategoriesAdapter(FragmentActivity activity, int textViewResourceId, ArrayList<SubCategory_ModelClass> categories) {
        super(activity,textViewResourceId,categories);

        this.myarrayList = categories;
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, parent);
    }

    @Nullable
    @Override
    public SubCategory_ModelClass getItem(int position) {
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
        SubCategory_ModelClass model = getItem(position);

        View spinnerRow = LayoutInflater.from(parent.getContext()).inflate(R.layout.spinneritem, parent, false);

        TextView label = spinnerRow.findViewById(R.id.spinner_text);
        label.setText(String.format("%s", model != null ? model.getSubcatName() : ""));


        return spinnerRow;
    }
}
