package com.kisaanandfactory.warehouseapp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kisaanandfactory.warehouseapp.R;
import com.kisaanandfactory.warehouseapp.modelclass.AllProduct_ModelClass;
import com.kisaanandfactory.warehouseapp.modelclass.Image_ModelClass;
import com.kisaanandfactory.warehouseapp.modelclass.Weight_ModelClass;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AllProductAdapter extends RecyclerView.Adapter<AllProductAdapter.ViewHolder> {

    Context context;
    ArrayList<AllProduct_ModelClass> vegetable;
    ArrayList<Weight_ModelClass> vegetableweight;
    ArrayList<Image_ModelClass> vegetableimage;

    String token, str_quantity, productId;
    int quantity;

    public AllProductAdapter(Context context, ArrayList<AllProduct_ModelClass> vegetable, ArrayList<Image_ModelClass> vegetableimage) {

        this.context = context;
        this.vegetable = vegetable;
        this.vegetableimage = vegetableimage;
    }

    @NonNull
    @Override
    public AllProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product, parent, false);
        return new AllProductAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllProductAdapter.ViewHolder holder, int position) {

        AllProduct_ModelClass allProduct = vegetable.get(position);

        holder.productName.setText(allProduct.getTitle());
        holder.stock.setText(allProduct.getInStock());
        holder.productType.setText(allProduct.getType());

        String utcDateString = allProduct.getCreatedAt();

        Log.d("utcDateString", utcDateString);

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
        return vegetable.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView productName, stock, productType, datetime;
        ImageView productImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            stock = itemView.findViewById(R.id.stock);
            productType = itemView.findViewById(R.id.productType);
            datetime = itemView.findViewById(R.id.datetime);

        }
    }
}
