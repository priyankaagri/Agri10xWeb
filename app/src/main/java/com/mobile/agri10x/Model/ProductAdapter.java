package com.mobile.agri10x.Model;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mobile.agri10x.Connection.LoadImage;
import com.mobile.agri10x.R;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    //this context we will use to inflate the layout
    private final Context mCtx;

    //we are storing all the products in a list
    private final List<Product> productList;

    private final String activityName ;

    ProductViewHolder Holder;
    //getting the context and product list with constructor
    public ProductAdapter(Context mCtx, List<Product> productList) {
        this.mCtx = mCtx;
        this.productList = productList;
        activityName = mCtx.getClass().getSimpleName();
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_products, null);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        //getting the product of the specified position
        Product product = productList.get(position);

//        MaxBid, MinBid, AvgBid, Quality,Unit,SellOrderQuantity,SellingPrice,Status,BidCount,Commodity;

        if(product!=null) {
            if (product.get_id().getCommodity() != null)
                holder.Commodity.setText(product.get_id().getCommodity());
            else{
                ErrorLog errorLog = new ErrorLog(Main.getIp()+"/getFarmerBidStatus","_id.Commodity","String",null,this.getClass().getName()+"->onBindViewHolder",mCtx.getResources().getString(R.string.DeviceName),activityName);
                Main.addErrorReportRequest(errorLog,mCtx);
            }

            if (product.get_id().getQuality() != null)
                holder.Quality.setText(product.get_id().getQuality());
            else{
                ErrorLog errorLog = new ErrorLog(Main.getIp()+"/getFarmerBidStatus","_id.Quality","String",null,this.getClass().getName()+"->onBindViewHolder",mCtx.getResources().getString(R.string.DeviceName),activityName);
                Main.addErrorReportRequest(errorLog,mCtx);
            }




            if(product.getMaxBid()==0){
                ErrorLog errorLog = new ErrorLog(Main.getIp()+"/getFarmerBidStatus","MaxBid","Int",null,this.getClass().getName()+"->onBindViewHolder",mCtx.getResources().getString(R.string.DeviceName),activityName);
                Main.addErrorReportRequest(errorLog,mCtx);
            }
            holder.MaxBid.setText(Integer.toString(product.getMaxBid()));


            if(product.getMinBid()==0){
                ErrorLog errorLog = new ErrorLog(Main.getIp()+"/getFarmerBidStatus","MinBid","Int",null,this.getClass().getName()+"->onBindViewHolder",mCtx.getResources().getString(R.string.DeviceName),activityName);
                Main.addErrorReportRequest(errorLog,mCtx);
            }
            holder.MinBid.setText(Integer.toString(product.getMinBid()));


            if(product.getAvgBid()==0){
                ErrorLog errorLog = new ErrorLog(Main.getIp()+"/getFarmerBidStatus","AvgBid","Int",null,this.getClass().getName()+"->onBindViewHolder",mCtx.getResources().getString(R.string.DeviceName),activityName);
                Main.addErrorReportRequest(errorLog,mCtx);
            }
            holder.AvgBid.setText(String.valueOf(product.getAvgBid()));


            if(product.get_id().getUnit()==0){
                ErrorLog errorLog = new ErrorLog(Main.getIp()+"/getFarmerBidStatus","_id.Unit","Int",null,this.getClass().getName()+"->onBindViewHolder",mCtx.getResources().getString(R.string.DeviceName),activityName);
                Main.addErrorReportRequest(errorLog,mCtx);
                holder.Unit.setText("Unit : " + product.get_id().getUnit());
            }
            holder.Unit.setText("Unit : " + product.get_id().getUnit());


            if(product.get_id().getSellOrderQuantity()==0){
                ErrorLog errorLog = new ErrorLog(Main.getIp()+"/getFarmerBidStatus","_id.SellOrderQuantity","Int",null,this.getClass().getName()+"->onBindViewHolder",mCtx.getResources().getString(R.string.DeviceName),activityName);
                Main.addErrorReportRequest(errorLog,mCtx);
            }
            holder.SellOrderQuantity.setText("Sell-Order-Quantity: " + product.get_id().getSellOrderQuantity());


//            if(product.get_id().getSellinPrice()==0){
//                ErrorLog errorLog = new ErrorLog(Main.getIp()+"/getFarmerBidStatus","_id.SellingPrice","Int",null,this.getClass().getName()+"->onBindViewHolder",mCtx.getResources().getString(R.string.DeviceName),activityName);
//                Main.addErrorReportRequest(errorLog,mCtx);
//            }
            holder.SellingPrice.setText("SellingPrice : " + product.get_id().getSellinPrice());


            if(product.getBidCount()==0)
            {
                ErrorLog errorLog = new ErrorLog(Main.getIp()+"/getFarmerBidStatus","BidCount","Int",null,this.getClass().getName()+"->onBindViewHolder",mCtx.getResources().getString(R.string.DeviceName),activityName);
                Main.addErrorReportRequest(errorLog,mCtx);
            }
            holder.BidCount.setText(Integer.toString(product.getBidCount()));


            if (product.get_id().getProductImage() != null) {
                new downloadImage().execute(new StrViewHolder(holder, Main.getOldUrl() + product.get_id().getProductImage()));
            }
        }


    }

    // [
//      {
//      "_id":
//              {"Status":"Accepted",//not showing
//               "Commodity":"RAISINS",
//               "SellingPrice":1750,
//               "SellOrderQuantity":1000,
//              "Quality":"GRADE AAA",
//               "Unit":10},
//      "BidCount":1,
//      "MaxBid":1777,
//      "MinBid":1777,
//      "AvgBid":1777
//      }
// ]

    class downloadImage extends AsyncTask<StrViewHolder, Integer, Bitmap> {

        ProductViewHolder hold;
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
                bitmap = LoadImage.fetchUserData(obj[0].getImageUrl(), mCtx);
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

    public void delete(int position) { //removes the row
        productList.remove(position);

    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView MaxBid, MinBid, AvgBid, Quality,Unit,SellOrderQuantity,SellingPrice,Status,BidCount,Commodity;
        ImageView imageView;

        public ProductViewHolder(View itemView) {
            super(itemView);

            SellOrderQuantity= itemView.findViewById(R.id.SellOrderQuantity);
            MaxBid = itemView.findViewById(R.id.MaxBid);
            MinBid = itemView.findViewById(R.id.MinBid);
            AvgBid = itemView.findViewById(R.id.AvgBid);
            Quality = itemView.findViewById(R.id.Quality);
            Unit = itemView.findViewById(R.id.Unit);
            SellingPrice = itemView.findViewById(R.id.SellingPrice);
//            Status = itemView.findViewById(R.id.Status);
            BidCount = itemView.findViewById(R.id.BidCount);
            Commodity = itemView.findViewById(R.id.Commodity);
            imageView = itemView.findViewById(R.id.imageView_card);

        }
    }

    // [
//      {
//      "_id":
//              {"Status":"Accepted",
//               "Commodity":"RAISINS",
//               "SellingPrice":1750,
//               "SellOrderQuantity":1000,
//              "Quality":"GRADE AAA",
//               "Unit":10},
//      "BidCount":1,
//      "MaxBid":1777,
//      "MinBid":1777,
//      "AvgBid":1777
//      }
// ]


    class StrViewHolder{
        ProductViewHolder holder;
        String imageUrl;

        public StrViewHolder(ProductViewHolder holder, String imageUrl) {
            this.holder = holder;
            this.imageUrl = imageUrl;
        }

        public ProductViewHolder getHolder() {
            return holder;
        }

        public void setHolder(ProductViewHolder holder) {
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
