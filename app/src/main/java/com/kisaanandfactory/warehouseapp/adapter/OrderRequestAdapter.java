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
import com.kisaanandfactory.warehouseapp.modelclass.OrderRequest_ModelClass;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class OrderRequestAdapter extends RecyclerView.Adapter<OrderRequestAdapter.ViewHolder> {

    Context context;
    ArrayList<OrderRequest_ModelClass> orderRequest;

    public OrderRequestAdapter(FragmentActivity activity, ArrayList<OrderRequest_ModelClass> orderrequest) {

        this.context = activity;
        this.orderRequest = orderrequest;
    }

    @NonNull
    @Override
    public OrderRequestAdapter.ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orderrequest,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  OrderRequestAdapter.ViewHolder holder, int position) {

        OrderRequest_ModelClass order_request = orderRequest.get(position);

        holder.customerName.setText(order_request.getCoustomerName());
        holder.orderId.setText(order_request.getOrderId());
        holder.paymentStatues.setText(order_request.getPaymentStatus());
        holder.status.setText(order_request.getStatus());
        holder.totalAmount.setText(order_request.getTotalAmount());

        String utcDateString = order_request.getOrderDate();

        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {

                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
                Date date = dateFormat.parse(utcDateString);//You will get date object relative to server/client timezone wherever it is parsed
                DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); //If you need time just put specific format for time like 'HH:mm:ss'
                DateFormat formatter1 = new SimpleDateFormat("HH:mm"); //If you need time just put specific format for time like 'HH:mm:ss'
                String dateStr = formatter.format(date);
                String dateStr1 = formatter1.format(date);

                Log.d("dateStr", dateStr + "  "+dateStr1);

                holder.datetime.setText(dateStr+"("+dateStr1+")");
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {

        return orderRequest.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView orderId,customerName,datetime,paymentStatues,status,totalAmount;

        public ViewHolder(@NonNull  View itemView) {
            super(itemView);

            totalAmount = itemView.findViewById(R.id.totalAmount);
            orderId = itemView.findViewById(R.id.orderId);
            customerName = itemView.findViewById(R.id.customerName);
            datetime = itemView.findViewById(R.id.datetime);
            paymentStatues = itemView.findViewById(R.id.paymentStatues);
            status = itemView.findViewById(R.id.status);
        }
    }
}
