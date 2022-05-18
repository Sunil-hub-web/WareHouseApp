package com.kisaanandfactory.warehouseapp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.kisaanandfactory.warehouseapp.R;
import com.kisaanandfactory.warehouseapp.modelclass.Image_ModelClass;
import com.kisaanandfactory.warehouseapp.modelclass.OrderRequest_ModelClass;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class OrderRequestAdapter extends RecyclerView.Adapter<OrderRequestAdapter.ViewHolder> {

    Context context;
    ArrayList<OrderRequest_ModelClass> orderRequest;

    int index;

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

        holder.productName.setText(order_request.getTitle());
        holder.producr_qty.setText(order_request.getProductQuantity());
        holder.total_price.setText(order_request.getTotalAmount());
        holder.stauesName.setText(order_request.getStatus());
        holder.text_weight.setText(order_request.getStr_weight());

        ArrayList<Image_ModelClass> image_modelClass = order_request.getImage_modelClasses();
        String image = "https://kisaanandfactory.com/static_file/"+image_modelClass.get(0);
        Log.d("ranj_adapter_image",image);
        Log.d("ranj_adapter_image",image_modelClass.get(0)+"");
        Picasso.with(context).load(image).into(holder.productImage);

        holder.customerName.setText(order_request.getCoustomerName());
        holder.phoneNo.setText(order_request.getContacts());
        holder.paymentStatues.setText(order_request.getPaymentMethod());
        holder.totalAmount.setText(order_request.getTotalAmount());
        holder.deliveryAddress.setText(order_request.getHouse()+","+order_request.getStreet()+","+order_request.getLocality()
                +","+order_request.getCity()+","+order_request.getState()+","+order_request.getCountry()+","+order_request.getZip());

        boolean isExpand = order_request.isExpanded();
        holder.expandableLayout.setVisibility(isExpand ? View.VISIBLE : View.GONE);

        /*holder.rel_orderdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                index = position;
                notifyDataSetChanged();
            }
        });*/

       /* if(index == position){

            holder.expandableLayout.setVisibility(View.VISIBLE);

        }else{

            holder.expandableLayout.setVisibility(View.GONE);
        }
        */

        String utcDateString = order_request.getOrderDate();

       /* try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {

                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
                Date date = dateFormat.parse(utcDateString);//You will get date object relative to server/client timezone wherever it is parsed
                DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); //If you need time just put specific format for time like 'HH:mm:ss'
                DateFormat formatter1 = new SimpleDateFormat("HH:mm"); //If you need time just put specific format for time like 'HH:mm:ss'
                String dateStr = formatter.format(date);
                String dateStr1 = formatter1.format(date);

                Log.d("dateStr", dateStr + "  "+dateStr1);

               // holder.datetime.setText(dateStr+"("+dateStr1+")");
            }


        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public int getItemCount() {

        return orderRequest.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView productName,producr_qty,total_price,stauesName,customerName,phoneNo,deliveryAddress,
                paymentStatues,totalAmount,text_weight;
        ImageView productImage;
        RelativeLayout rel_orderdetails;
        LinearLayout expandableLayout;

        public ViewHolder(@NonNull  View itemView) {
            super(itemView);

            totalAmount = itemView.findViewById(R.id.totalAmount);
            productName = itemView.findViewById(R.id.productName);
            producr_qty = itemView.findViewById(R.id.producr_qty);
            total_price = itemView.findViewById(R.id.total_price);
            stauesName = itemView.findViewById(R.id.stauesName);
            customerName = itemView.findViewById(R.id.customerName);
            phoneNo = itemView.findViewById(R.id.phoneNo);
            deliveryAddress = itemView.findViewById(R.id.deliveryAddress);
            paymentStatues = itemView.findViewById(R.id.paymentStatues);
            productImage = itemView.findViewById(R.id.productImage);
            rel_orderdetails = itemView.findViewById(R.id.rel_orderdetails);
            expandableLayout = itemView.findViewById(R.id.expandableLayout);
            text_weight = itemView.findViewById(R.id.text_weight);


            rel_orderdetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    OrderRequest_ModelClass order_request = orderRequest.get(getAdapterPosition());
                    order_request.setExpanded(!order_request.isExpanded());
                    notifyItemChanged(getAdapterPosition());
                }
            });

        }

    }
}
