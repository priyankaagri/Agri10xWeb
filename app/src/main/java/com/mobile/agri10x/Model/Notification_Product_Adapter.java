package com.mobile.agri10x.Model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mobile.agri10x.Interface.ClickHandler;
import com.mobile.agri10x.R;

import java.util.List;

public class Notification_Product_Adapter extends RecyclerView.Adapter<Notification_Product_Adapter.NotificationProductViewHolder> {


    private final ClickHandler clickHandler;
    private final String isItMyInterestPage;
    //this context we will use to inflate the layout
    private final Context mCtx;
    private final String activityName ;

    //we are storing all the products in a list
    private final List<Notification_Product> productList;


    public Notification_Product_Adapter(Context mCtx, List<Notification_Product> productList,final ClickHandler clickHandler,String isItMyInterestPage) {
        this.mCtx = mCtx;
        this.productList = productList;
        this.clickHandler = clickHandler;
        this.isItMyInterestPage = isItMyInterestPage;
        this.activityName = mCtx.getClass().getSimpleName();
    }

    @Override
    public NotificationProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.active_trade_list_item, null);

        return new NotificationProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NotificationProductViewHolder holder, int position) {

        Notification_Product product = productList.get(position);
        holder.clickHandler = this.clickHandler;

        if(product!=null){
            if(product.getCommodityname()!=null)
                holder.Commodityname.setText(product.getCommodityname());
            else{
                ErrorLog errorLog = new ErrorLog(Main.getNegiUrl()+"/notificationData","commodityname","String",null,this.getClass().getName()+"->onBindViewHolder",mCtx.getResources().getString(R.string.DeviceName),activityName);
                Main.addErrorReportRequest(errorLog,mCtx);
            }
            if(product.getQuality()!=null)
                holder.Quality.setText(product.getQuality());
            else{
                ErrorLog errorLog = new ErrorLog(Main.getNegiUrl()+"/notificationData","quality","String",null,this.getClass().getName()+"->onBindViewHolder",mCtx.getResources().getString(R.string.DeviceName),activityName);
                Main.addErrorReportRequest(errorLog,mCtx);
            }
            if(product.getQuantity()!=0)
                holder.Quantity.setText(Integer.toString(product.getQuantity()));
            else{
                ErrorLog errorLog = new ErrorLog(Main.getNegiUrl()+"/notificationData","quantity","Int",null,this.getClass().getName()+"->onBindViewHolder",mCtx.getResources().getString(R.string.DeviceName),activityName);
                Main.addErrorReportRequest(errorLog,mCtx);
                holder.Quantity.setText(Integer.toString(product.getQuantity()));
            }
            if(product.getUnit()!=0)
                holder.Unit.setText(String.valueOf(product.getUnit()));
            else{
                ErrorLog errorLog = new ErrorLog(Main.getNegiUrl()+"/notificationData","unit","Int",null,this.getClass().getName()+"->onBindViewHolder",mCtx.getResources().getString(R.string.DeviceName),activityName);
                Main.addErrorReportRequest(errorLog,mCtx);
                holder.Unit.setText(String.valueOf(product.getUnit()));
            }
            if(product.getValidFrom()!=null)
                holder.ValidFrom.setText(product.getValidFrom());
            else{
                ErrorLog errorLog = new ErrorLog(Main.getNegiUrl()+"/notificationData","ValidFrom","String",null,this.getClass().getName()+"->onBindViewHolder",mCtx.getResources().getString(R.string.DeviceName),activityName);
                Main.addErrorReportRequest(errorLog,mCtx);
            }
            if(product.getValidTill()!=null)
                holder.ValidTill.setText(product.getValidTill());
            else{
                ErrorLog errorLog = new ErrorLog(Main.getNegiUrl()+"/notificationData","ValidTill","String",null,this.getClass().getName()+"->onBindViewHolder",mCtx.getResources().getString(R.string.DeviceName),activityName);
                Main.addErrorReportRequest(errorLog,mCtx);
            }
        }

        /*
        {
        "_id" : ObjectId("5d1e452da68e3e18542b9b5e"),
        "role" : "Farmer",
        "quantity" : 20,
        "unit" : 5,
        "interested" : true,
        "quality" : "req.quality",
        "UserRef" : ObjectId("5d1e452da68e3e18542b9b5d"),
        "commodityname" : "req.body.commodityname",
        "ValidFrom" : ISODate("2019-07-04T18:27:57.287Z"),
        "ValidTill" : ISODate("2019-07-04T18:27:57.287Z"),
        "__v" : 0
        }*/
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public Notification_Product getItem(int position){
        return productList.get(position);
    }

    class NotificationProductViewHolder extends RecyclerView.ViewHolder {

        TextView Unit, Quality, Quantity,Commodityname,ValidFrom,ValidTill;
        Button interested ;
        private ClickHandler clickHandler;
        public NotificationProductViewHolder(View itemView) {
            super(itemView);
            Commodityname= itemView.findViewById(R.id.commodityname);
            Quality = itemView.findViewById(R.id.quality_noti);
            Quantity = itemView.findViewById(R.id.quantity_noti);
            Unit = itemView.findViewById(R.id.unit_noti);
            ValidFrom = itemView.findViewById(R.id.validFrom);
            ValidTill = itemView.findViewById(R.id.validTill);
            interested = itemView.findViewById(R.id.Interested_button);
            if(isItMyInterestPage!=null && isItMyInterestPage.equals("MyInterest")){
                //my interest page
                interested.setText("Not Interested");
                interested.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (clickHandler != null) {
                            clickHandler.onInterestedClick(v,getAdapterPosition());
                        }
                    }
                });
            }
            interested.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickHandler != null) {
                        clickHandler.onInterestedClick(v,getAdapterPosition());

                    }
                }
            });


        }
    }


}
