package com.mobile.agri10x.Model.UnverifiedUserModels;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobile.agri10x.R;

import java.util.List;

public class Information_Adapter extends RecyclerView.Adapter<Information_Adapter.InformationViewHolder> {

    private final Context mCtx;
    //we are storing all the products in a list
    private final List<Information> productList;

    //getting the context and product list with constructor
    public Information_Adapter(Context mCtx, List<Information> productList) {
        this.mCtx = mCtx;
        this.productList = productList;
    }


    @Override
    public InformationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.information_card, null);
        return new InformationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(InformationViewHolder holder, int position) {
        Information product = productList.get(position);
        holder.Feature.setText(product.getFeature());
        holder.text.setText(product.getText());

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }


    class InformationViewHolder extends RecyclerView.ViewHolder {

        TextView Feature,text;

        public InformationViewHolder(View itemView) {
            super(itemView);

            Feature = itemView.findViewById(R.id.Feature);
            text = itemView.findViewById(R.id.text_information);

        }
    }

}
