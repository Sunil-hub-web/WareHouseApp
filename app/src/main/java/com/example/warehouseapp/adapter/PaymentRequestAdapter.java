package com.example.warehouseapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.warehouseapp.R;
import com.example.warehouseapp.modelclass.PaymentHistory_ModelClass;

import java.util.ArrayList;

public class PaymentRequestAdapter extends RecyclerView.Adapter<PaymentRequestAdapter.ViewHolder> {

    Context context;
    ArrayList<PaymentHistory_ModelClass> payment;

    public PaymentRequestAdapter(ArrayList<PaymentHistory_ModelClass> payment, FragmentActivity activity) {

        this.context = activity;
        this.payment = payment;

    }

    @NonNull
    @Override
    public PaymentRequestAdapter.ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_request,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  PaymentRequestAdapter.ViewHolder holder, int position) {

        PaymentHistory_ModelClass payment_request = payment.get(position);

        holder.idname.setText(payment_request.getOrderId());
        holder.text_Price.setText(payment_request.getAmount());
        holder.time.setText(payment_request.getDatetime());

    }

    @Override
    public int getItemCount() {
        return payment.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView text_Price,idname,time;

        public ViewHolder(@NonNull  View itemView) {
            super(itemView);

            time = itemView.findViewById(R.id.time);
            text_Price = itemView.findViewById(R.id.text_Price);
            idname = itemView.findViewById(R.id.idname);
        }
    }
}
