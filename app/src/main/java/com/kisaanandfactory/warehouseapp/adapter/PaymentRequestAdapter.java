package com.kisaanandfactory.warehouseapp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.kisaanandfactory.warehouseapp.R;
import com.kisaanandfactory.warehouseapp.modelclass.PaymentHistory_ModelClass;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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

        String utcDateString = payment_request.getDatetime();

        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {

                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
                Date date = dateFormat.parse(utcDateString);//You will get date object relative to server/client timezone wherever it is parsed
                DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); //If you need time just put specific format for time like 'HH:mm:ss'
                DateFormat formatter1 = new SimpleDateFormat("HH:mm"); //If you need time just put specific format for time like 'HH:mm:ss'
                String dateStr = formatter.format(date);
                String dateStr1 = formatter1.format(date);

                Log.d("dateStr", dateStr + "  "+dateStr1);

                holder.time.setText(dateStr+"("+dateStr1+")");
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

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
