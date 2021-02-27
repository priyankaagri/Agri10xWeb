package com.mobile.agri10x.Model;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.mobile.agri10x.Connection.LoadImage;
import com.mobile.agri10x.Farmer;
import com.mobile.agri10x.OnlyWebPage;
import com.mobile.agri10x.R;
import com.squareup.picasso.Picasso;


import java.io.InputStream;
import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class StockListAdapter extends RecyclerView.Adapter<StockListAdapter.StockListViewHolder>  {

    private final Context mCtx;

    private final List<StockList> productList;

    private final String activityName ;

    boolean flag = false;

    String username;

    PopupWindow popupWindow;

    EditText price;
    TextView qty,commodityname;

    public StockListAdapter(Context mCtx, List<StockList> productList, String username) {
        this.mCtx = mCtx;
        this.productList = productList;
        this.activityName = mCtx.getClass().getSimpleName();
        this.username = username;
    }

    @Override
    public StockListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.stock_list_card_new, null);//stock_list_card   my_stock_list
        return new StockListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StockListViewHolder holder, int position) {
        //getting the product of the specified position

        StockList product = productList.get(position);

//holder.cardview.setBackgroundResource(R.drawable.card_view_border);
        if(product.getCommodityname()!=null)
            holder.Commodityname.setText(product.getCommodityname());
        else{
            ErrorLog errorLog = new ErrorLog(Main.getOldUrl()+"/getMyStockList","commodityname","String",null,this.getClass().getName()+"->onBindViewHolder",mCtx.getResources().getString(R.string.DeviceName),activityName);
            Main.addErrorReportRequest(errorLog,mCtx);
        }

        if(product.getUnit()==0)
        {
            ErrorLog errorLog = new ErrorLog(Main.getOldUrl()+"/getMyStockList","unit","Int",null,this.getClass().getName()+"->onBindViewHolder",mCtx.getResources().getString(R.string.DeviceName),activityName);
            Main.addErrorReportRequest(errorLog,mCtx);
        }
        holder.Unit.setText("Unit: " + product.getUnit());


        if(product.getQuality()!=null){
            //  holder.Quality.setText(product.getQuality());
        }
        else{
            ErrorLog errorLog = new ErrorLog(Main.getOldUrl()+"/getMyStockList","quality","String",null,this.getClass().getName()+"->onBindViewHolder",mCtx.getResources().getString(R.string.DeviceName),activityName);
            Main.addErrorReportRequest(errorLog,mCtx);
        }



        if(product.getQuantity()==0)
        {
            ErrorLog errorLog = new ErrorLog(Main.getOldUrl()+"/getMyStockList","quantity","Int",null,this.getClass().getName()+"->onBindViewHolder",mCtx.getResources().getString(R.string.DeviceName),activityName);
            Main.addErrorReportRequest(errorLog,mCtx);
        }
        holder.Quantity.setText("Quantity: "+ product.getQuantity());



//        if(product.getEntryTime()!=null)
//            holder.Entrytime.setText(product.getEntryTime());
//        else{
//            ErrorLog errorLog = new ErrorLog(Main.getOldUrl()+"/getMyStockList","EntryTime","String",null,this.getClass().getName()+"->onBindViewHolder",mCtx.getResources().getString(R.string.DeviceName),activityName);
//            Main.addErrorReportRequest(errorLog,mCtx);
//        }

        if(product.getQuality()!=null){
          //  Log.d("getqual",product.getQuality());
         String   content = product.getQuality();
         content = content.replace("Grade","");
            holder.Entrytime.setText("Grade:"+content);
        }

        else{
/* ErrorLog errorLog = new ErrorLog(Main.getOldUrl()+"/getMyStockList","EntryTime","String",null,this.getClass().getName()+"->onBindViewHolder",mCtx.getResources().getString(R.string.DeviceName),activityName);
Main.addErrorReportRequest(errorLog,mCtx);*/

            ErrorLog errorLog = new ErrorLog(Main.getOldUrl()+"/getMyStockList","quality","String",null,this.getClass().getName()+"->onBindViewHolder",mCtx.getResources().getString(R.string.DeviceName),activityName);
            Main.addErrorReportRequest(errorLog,mCtx);
        }

        if(product.isNeedColdStorage())
        {
            holder.ColdStorage.setText("Cold Storage:"+" YES");
        }
        else
        {
            holder.ColdStorage.setText("Cold Storage:"+" NO");
        }
        if(product.isPerishable())
        {
            holder.Perishable.setText("Perishable:"+" YES");
        }
        else
        {
            holder.Perishable.setText("Perishable:"+" NO");
        }



//        if(product.getBlockedquantity()==0)
//        {
//            ErrorLog errorLog = new ErrorLog(Main.getOldUrl()+"/getMyStockList","Blockedquantity","Int",null,this.getClass().getName()+"->onBindViewHolder",mCtx.getResources().getString(R.string.DeviceName),activityName);
//            Main.addErrorReportRequest(errorLog,mCtx);
//        }
        holder.Blockquantity.setText("BlockQuantity: "+ product.getBlockedquantity());


        if(product.getProductImage()!=null)
        {
            new downloadImage().execute(new StrViewHolder(holder,Main.getBaseUrl()+product.getProductImage()));
            Log.d("imgurl",Main.getBaseUrl()+product.getProductImage());
            Picasso.with(mCtx)
                    .load(Main.getBaseUrl()+product.getProductImage())
                    .into(holder.imageView);

            //setborder

          /*  holder.imageView.setBorderWidth(10f);
            holder.imageView.setBorderColor(Color.BLACK);
// or with gradient
            holder.imageView.setBorderColorStart(Color.BLACK);
            holder.imageView.setBorderColorEnd(Color.RED);
            holder.imageView.setBorderColorDirection(CircularImageView.GradientDirection.TOP_TO_BOTTOM);
*/

            //     Picasso.with(mCtx).load(Main.getBaseUrl()+product.getProductImage()).into(holder.imageView);

        }

        else{
            String uri = "@drawable/farmer";
            int imageResource = mCtx.getResources().getIdentifier(uri, null, mCtx.getPackageName());
            Drawable res = mCtx.getResources().getDrawable(imageResource);
            holder.imageView.setImageDrawable(res);
        }

        holder.sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                callPopup("Quantity: "+ product.getQuantity(),product.getCommodityname());
            }
        });
//        holder.Commodityname.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(flag){
//                    flag = false;
//                    holder.hari.setVisibility(View.INVISIBLE);
//                }
//                else
//                    {
//                        flag = true;
//                        holder.hari.setVisibility(View.VISIBLE);
//                }
//
//            }
//        });
    }

    private void callPopup(String strqty, String strcommodityname) {

        LayoutInflater layoutInflater = (LayoutInflater)mCtx
                .getSystemService(LAYOUT_INFLATER_SERVICE);

        View popupView = layoutInflater.inflate(R.layout.popup, null);

        popupWindow=new PopupWindow(popupView,
               800, 550,
                true);

        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);

        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
        price = (EditText) popupView.findViewById(R.id.price);
        qty = (TextView) popupView.findViewById(R.id.qty);
        commodityname = (TextView) popupView.findViewById(R.id.commodityname);

        qty.setText(strqty);
        commodityname.setText(strcommodityname);



        ((TextView) popupView.findViewById(R.id.submit))
                .setOnClickListener(new View.OnClickListener() {


                    public void onClick(View arg0) {
              callapi();

                        popupWindow.dismiss();
                    }
                });


    }

    private void callapi() {


    }


//    private int quantity;//quantity
//    private int unit;//unit
//    private int Blockedquantity;//Blockedquantity
//    private String commodityname;//commodityname
//    private boolean Perishable;//Perishable
//    private boolean NeedColdStorage;//NeedColdStorage
//    private String ProductImage;//ProductImage
//    private String EntryTime;//EntryTime
//    private String quality;//quality


    class downloadImage extends AsyncTask<StrViewHolder, Integer, Bitmap> {

        StockListViewHolder hold;
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if(bitmap!=null){
                hold.imageView.setImageBitmap(bitmap);
            }
        }

        @Override
        protected Bitmap doInBackground(StrViewHolder... obj) {
            Bitmap bitmap;
            try {
                hold = obj[0].getHolder();
                //bitmap = LoadImage.fetchUserData(obj[0].getImageUrl(), mCtx);
                InputStream in = new java.net.URL(obj[0].getImageUrl()).openStream();
                bitmap = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                return null;
            }
            return bitmap;
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }



    class StockListViewHolder extends RecyclerView.ViewHolder {
        LinearLayout hari;
        TextView Unit, Quality, Quantity,Commodityname,Blockquantity,Perishable,ColdStorage,Entrytime,sell;
        ImageView imageView,arrowdown,arrowup;
        CardView cardview;
        RelativeLayout rel;

        public StockListViewHolder(View itemView) {
            super(itemView);


            //cardview = itemView.findViewById(R.id.cardview);
            sell = itemView.findViewById(R.id.sell);
            arrowup = itemView.findViewById(R.id.arrowup);
            arrowdown = itemView.findViewById(R.id.arrowdown);
            Commodityname= itemView.findViewById(R.id.commoditynamestocklist);
            Quality = itemView.findViewById(R.id.qualitystocklist);
            Quantity = itemView.findViewById(R.id.quantitystocklist);
            Unit = itemView.findViewById(R.id.unitstocklist);
            Blockquantity = itemView.findViewById(R.id.blockQuantity_stock_list);
            Perishable = itemView.findViewById(R.id.perishable_stock_list);
            ColdStorage = itemView.findViewById(R.id.coldStorage_stock_list);
            Entrytime = itemView.findViewById(R.id.entrytime_stock_list);
            imageView = itemView.findViewById(R.id.imageView);
            rel = itemView.findViewById(R.id.rel);

        }
    }


    class StrViewHolder{
        StockListViewHolder holder;
        String imageUrl;

        public StrViewHolder(StockListViewHolder holder, String imageUrl) {
            this.holder = holder;
            this.imageUrl = imageUrl;
        }

        public StockListViewHolder getHolder() {
            return holder;
        }

        public void setHolder(StockListViewHolder holder) {
            this.holder = holder;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }
    }

}
