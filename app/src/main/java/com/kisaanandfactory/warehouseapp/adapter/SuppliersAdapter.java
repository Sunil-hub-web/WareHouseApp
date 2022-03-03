package com.kisaanandfactory.warehouseapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.kisaanandfactory.warehouseapp.R;
import com.kisaanandfactory.warehouseapp.modelclass.Suppliers_ModelClass;

import java.util.ArrayList;

public class SuppliersAdapter extends RecyclerView.Adapter<SuppliersAdapter.ViewHolder> {

    Context context;
    ArrayList<Suppliers_ModelClass> suppelers;

    public SuppliersAdapter(FragmentActivity activity, ArrayList<Suppliers_ModelClass> suppliers) {

        this.context = activity;
        this.suppelers = suppliers;
    }

    @NonNull
    @Override
    public SuppliersAdapter.ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.suppliers_details,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  SuppliersAdapter.ViewHolder holder, int position) {

        Suppliers_ModelClass suppliers_details = suppelers.get(position);

        holder.order_id.setText(suppliers_details.getOrderId());
        holder.status_Name.setText(suppliers_details.getOrderStatus());

    }

    @Override
    public int getItemCount() {
        return suppelers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView order_id,status_Name;

        public ViewHolder(@NonNull  View itemView) {
            super(itemView);

            status_Name = itemView.findViewById(R.id.status_Name);
            order_id = itemView.findViewById(R.id.order_id);

        }
    }
}
