package com.mobile.agri10x;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.ArrayMap;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.mobile.agri10x.Connection.POSTRequest;
import com.mobile.agri10x.Model.AddStockData;
import com.mobile.agri10x.Model.Main;
import com.mobile.agri10x.Model.SecurityData;
import com.mobile.agri10x.Model.UserId;
import com.mobile.agri10x.SessionManagment.SessionManager;
import com.mobile.agri10x.models.GetTradeCommodity;
import com.mobile.agri10x.models.GetTradeVariety;
import com.mobile.agri10x.retrofit.AgriInvestor;
import com.mobile.agri10x.retrofit.ApiHandler;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickCancel;
import com.vansuita.pickimage.listeners.IPickResult;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import gr.escsoft.michaelprimez.searchablespinner.SearchableSpinner;
import gr.escsoft.michaelprimez.searchablespinner.interfaces.OnItemSelectedListener;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddStock extends AppCompatActivity {
    List<GetTradeCommodity> getTradeCommodityArrayList = new ArrayList<>();
    List<GetTradeVariety> getTradeVarietyArrayList = new ArrayList<>();
    ArrayList<String> Commoditycategory = new ArrayList<>();
    SimpleListAdapter mSimpleListAdapter;
    ArrayList<String> Varietycategory = new ArrayList<>();;
    private static final String[] Value = new String[]{
            "Yes", "No"
    };

    private static final String[] Unit = new String[]{
            "10", "20", "30", "40", "50"
    };

    ImageView stock_image;
    EditText quantity;
    SearchableSpinner commodity;
    MaterialBetterSpinner commodityunit, perishable, coldstorage,Variety;
    String Quan, Cold, Comm, Peri, unit;
    Bitmap stockbitmap;
    private Button back;
    String commid,varietyid,strcomname;


    //Toolbar toolbar;
    private UserId userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_stock);

        /*
        toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_name);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //SessionManager sess = new SessionManager(getApplicationContext());
                finish();
            }
        });*/
callapi();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userId=new UserId();
            userId.setUserid(extras.getString("Userid"));
        }

        back=findViewById(R.id.but_back_add);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddStock.this.finish();
            }
        });

        stock_image = findViewById(R.id.Stock_Image);
        Button button = findViewById(R.id.addStock_button);

        commodity = findViewById(R.id.Commodity);
        Variety = findViewById(R.id.Variety);
        quantity = findViewById(R.id.comm_quattity);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, Unit);
        commodityunit = findViewById(R.id.CommodityUnit);
        commodityunit.setAdapter(adapter1);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, Value);
        perishable = findViewById(R.id.perishable);
        perishable.setAdapter(adapter2);

        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, Value);
        coldstorage = findViewById(R.id.ColdStorage);
        coldstorage.setAdapter(adapter3);

//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddStock.this,
//                android.R.layout.simple_dropdown_item_1line, getTradeCommodityArrayList);
//        commodity.setAdapter(adapter);

commodity.setOnItemSelectedListener(new OnItemSelectedListener() {
    @Override
    public void onItemSelected(View view, int position, long id) {


     //   Toast.makeText(AddStock.this, "Item on position " + position + " : " + mSimpleListAdapter.getItem(position) + " Selected", Toast.LENGTH_SHORT).show();
String pos = commodity.getSelectedItem().toString();
Log.d("selectedcommo",pos);
for(int i= 0 ;i < getTradeCommodityArrayList.size() ; i++){
    if(pos.equals(getTradeCommodityArrayList.get(i).getCommodity())){
        commid = getTradeCommodityArrayList.get(i).getId();
        strcomname = getTradeCommodityArrayList.get(i).getCommodity();
    }
}
//                    commid = getComId(position);
//                    strcomname = getComName(position);


                    Log.d("getcommidnname",commid+" "+strcomname);
                    callvariety(commid);
    }

    @Override
    public void onNothingSelected() {

    }
});

//        commodity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long id) {
//                Log.d("dsfdsfds", String.valueOf(i));
//
//                String selectedItem = adapterView.getItemAtPosition(i).toString();
//
//                Log.d("kjjjjjkkjkjjk", selectedItem);
//                if (selectedItem.equals("Select")) {
//                    commid = "";
//                } else {
//
//                    commid = getComId(i - 1);
//                }
//
//                Log.d("checkcommid",commid);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

//        commodity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//
//            @Override
//            public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
//                // On selecting a spinner item
//                ((TextView) adapter.getChildAt(0)).setTextColor(Color.BLACK);
//                Log.d("commodityval", String.valueOf(position));
//
//                String selectedItem = adapter.getItemAtPosition(position).toString();
//
//                Log.d("kjjjjjkkjkjjk", selectedItem);
//                if (selectedItem.equals("Select")) {
//                    commid = "";
//                } else {
//
//                    commid = getComId(position);
//                    strcomname = getComName(position);
//
//                    callvariety(commid);
//                }
//
//                Log.d("checkcommid",commid);
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> arg0) {
//                // TODO Auto-generated method stub
//            }
//        });
//        commodity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Log.d("commodityval", String.valueOf(i));
//
//                String selectedItem = adapterView.getItemAtPosition(i).toString();
//
//                Log.d("kjjjjjkkjkjjk", selectedItem);
//                if (selectedItem.equals("Select")) {
//                    commid = "";
//                } else {
//
//                    commid = getComId(i);
//                    strcomname = getComName(i);
//
//                    callvariety(commid);
//                }
//
//                Log.d("checkcommid",commid);
//            }
//        });
        Variety.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("Varietyval", String.valueOf(i));

                String selectedItem = adapterView.getItemAtPosition(i).toString();

                Log.d("kjjjjjkkjkjjk", selectedItem);
                if (selectedItem.equals("Select")) {
                    varietyid = "";
                } else {

                    varietyid = getVarietyId(i);


                }

                Log.d("checkvarietyid",varietyid);
            }
        });
        stock_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PickImageDialog.build(new PickSetup())
                        .setOnPickResult(new IPickResult() {
                            @Override
                            public void onPickResult(PickResult r) {
                                stockbitmap = r.getBitmap();
                                stock_image.setImageBitmap(r.getBitmap());

                            }
                        })
                        .setOnPickCancel(new IPickCancel() {
                            @Override
                            public void onCancelClick() {

                            }
                        }).show(getSupportFragmentManager());


            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unit = commodityunit.getText().toString().trim();
                Cold = coldstorage.getText().toString().trim();
                Comm = commid;
                Peri = perishable.getText().toString().trim();
                Quan = quantity.getText().toString().trim();
Log.d("checkparms",unit+ " "+ Cold +" "+ Comm +" "+Peri+ ""+Quan);
                if (unit.length() > 0 && Cold.length() > 0 && Comm.length() > 0 && Peri.length() > 0 && Quan.length() > 0 ) { //  && varietyid.length() > 0 && stockbitmap != null
                    int unitInt = Integer.valueOf(unit);
                    int quan = Integer.valueOf(Quan);
                    if (unitInt > 0 && quan > 0) {
                        new AddStockPOST().execute();
                    } else if (unitInt <= 0) {
                        Toast.makeText(AddStock.this, "Add Valid Value of Unit", Toast.LENGTH_LONG).show();
                    } else if (quan <= 0) {
                        Toast.makeText(AddStock.this, "Add Valid Value of Quantity", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(AddStock.this, "Add Valid Value of Quantity And Unit", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(AddStock.this, "Fill All Details!!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }



    private void callvariety(String commid) {
        Varietycategory.clear();
        Map<String, Object> jsonParams = new ArrayMap<>();
//put something inside the map, could be null
        jsonParams.put("id", commid);

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),(new JSONObject(jsonParams)).toString());

        AgriInvestor apiService = ApiHandler.getApiService();
        final Call<List<GetTradeVariety>> loginCall = apiService.wsgetTradeVariety(
                "123456",body);
        loginCall.enqueue(new Callback<List<GetTradeVariety>>() {
            @SuppressLint("WrongConstant")
            @Override
            public void onResponse(Call<List<GetTradeVariety>> call,
                                   Response<List<GetTradeVariety>> response) {
                Log.d("GetTradeVariety",response.toString());
                if (response.isSuccessful()) {
                    getTradeVarietyArrayList = response.body();
                    Log.d("getresponse", String.valueOf(getTradeVarietyArrayList.size()));

if(!getTradeVarietyArrayList.isEmpty()){
    Variety.setDropDownHeight(WindowManager.LayoutParams.WRAP_CONTENT);
Variety.setClickable(true);
    Variety.setFocusable(true);
    for(int i=0; i < getTradeVarietyArrayList.size();i++){
        Varietycategory.add(getTradeVarietyArrayList.get(i).getVariety());
    }
    Log.d("Varietycategory", String.valueOf(Varietycategory.size()));

    Variety.setAdapter(new ArrayAdapter<String>(AddStock.this, android.R.layout.simple_dropdown_item_1line, Varietycategory));
}else{
    Variety.setDropDownHeight(0);

    Varietycategory.add("No Data");
    Variety.setAdapter(new ArrayAdapter<String>(AddStock.this, android.R.layout.simple_dropdown_item_1line, Varietycategory));
}


                }
            }

            @Override
            public void onFailure(Call<List<GetTradeVariety>> call,
                                  Throwable t) {
                Toast.makeText(AddStock.this,"Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private String getVarietyId(int i) {
        String Vvarirtyid = "";


        Vvarirtyid = getTradeVarietyArrayList.get(i).getVariety();//getId()

        Log.d("checkid",Vvarirtyid);
        //Returning the id
        return Vvarirtyid;
    }
    private String getComName(int i) {
        String Ccomname = "";


        Ccomname = getTradeCommodityArrayList.get(i).getCommodity();//

        Log.d("checkid",Ccomname);
        //Returning the id
        return Ccomname;
    }

    private String getComId(int i) {
        String Ccomid = "";


        Ccomid = getTradeCommodityArrayList.get(i).getId();//

Log.d("checkid",Ccomid);
        //Returning the id
        return Ccomid;
    }

    private void callapi() {

        AgriInvestor apiService = ApiHandler.getApiService();
        final Call<List<GetTradeCommodity>> loginCall = apiService.wsgetTradeCommodity(
                "123456");
        loginCall.enqueue(new Callback<List<GetTradeCommodity>>() {
            @SuppressLint("WrongConstant")
            @Override
            public void onResponse(Call<List<GetTradeCommodity>> call,
                                   Response<List<GetTradeCommodity>> response) {
                Log.d("GetTradeCommodity",response.toString());
                if (response.isSuccessful()) {
                    getTradeCommodityArrayList = response.body();
                    Log.d("getresponse", String.valueOf(getTradeCommodityArrayList.size()));





                    if(!getTradeCommodityArrayList.isEmpty()){

                        commodity.setClickable(true);
                        commodity.setFocusable(true);
                        //                    Commoditycategory.add("Select");
                        for(int i=0; i < getTradeCommodityArrayList.size();i++){
                            Commoditycategory.add(getTradeCommodityArrayList.get(i).getCommodity());
                        }
                        Log.d("Commoditycategory", String.valueOf(Commoditycategory.size()));

                        mSimpleListAdapter = new SimpleListAdapter(AddStock.this, Commoditycategory);
                        commodity.setAdapter(new ArrayAdapter<String>(AddStock.this, android.R.layout.simple_dropdown_item_1line, Commoditycategory));
//commodity.setAdapter(mSimpleListAdapter);

                    }else{

                        Commoditycategory.add("No Data");
                        mSimpleListAdapter = new SimpleListAdapter(AddStock.this, Commoditycategory);
                        commodity.setAdapter(new ArrayAdapter<String>(AddStock.this, android.R.layout.simple_dropdown_item_1line, Commoditycategory));
                    }
                }
            }

            @Override
            public void onFailure(Call<List<GetTradeCommodity>> call,
                                  Throwable t) {
                Toast.makeText(AddStock.this,"Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

    }


    class AddStockPOST extends AsyncTask<Void, Integer, AddStockData> {
        AlertDialog dialog;

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected AddStockData doInBackground(Void... voids) {
            AddStockData stock = new AddStockData();
            stock.setQuan(Quan);
            stock.setVariety(varietyid);
            if (Peri.equals("Yes")) {
                stock.setPeri("1");
            } else {
                stock.setPeri("0");
            }
            stock.setComm(strcomname);
            stock.setEnt(SessionManager.getUsername());
            if (Cold.equals("Yes")) {
                stock.setCold("1");
            } else {
                stock.setCold("0");
            }
            stock.setDura("");
            stock.setUnit(unit);
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            stockbitmap.compress(Bitmap.CompressFormat.PNG, 20, baos);
//            byte[] b = baos.toByteArray();
//            String s = Base64.encodeToString(b, Base64.DEFAULT);
//            s.replaceAll("/^[^,]*,/", "");
//            stock.setFile(s);
            stock.setUserid(userId.getUserid());
            return stock;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        //    dialog = new Alert().pleaseWait();
        }

        @Override
        protected void onPostExecute(AddStockData s) {
            super.onPostExecute(s);

        //    dialog.dismiss();
            //Toast.makeText(AddStock.this, "Data from server: " + s, Toast.LENGTH_LONG).show();
            new networkPOST().execute(Main.getOldUrl() + "/addstock", new Gson().toJson(s));

        }
    }

    class networkPOST extends AsyncTask<String, Integer, String> {
        AlertDialog dialog;

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new Alert().pleaseWait();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            dialog.dismiss();
            if (s != null) {
                if (s.equals("network")) {
                   new Alert().alert("Network !!!", getResources().getString(R.string.network_error_message));
                } else {
//                  Toast.makeText(AddStock.this, s, Toast.LENGTH_LONG).show();
     //              commodity.setText("");
                 //   commodity.setSelectedItem(-1);

                    Variety.setText("");
                    commodityunit.setText("");
                    perishable.setText("");
                    coldstorage.setText("");
                    quantity.setText("");
//                    String uri = "@drawable/image6";
//                    int imageResource = getResources().getIdentifier(uri, null, getPackageName());
//                    Drawable res = getResources().getDrawable(imageResource);
//                    stock_image.setImageDrawable(res);
                    new Alert().alert("Stock!!", "Successfully Added");
                }
            }

        }

        @Override
        protected String doInBackground(String... strings) {
            String str = null;
            try {
                str = POSTRequest.fetchUserData(strings[0], strings[1], AddStock.this);
            } catch (Exception e) {
                return "network";
            }
            return str;
        }
    }

    class Alert {
        public void alert(String title, String body) {
            final AlertDialog.Builder Alert = new AlertDialog.Builder(AddStock.this);
            Alert.setCancelable(false)
                    .setTitle(title)
                    .setMessage(body);
            Alert.setNegativeButton("Okay", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                    finish();
                    startActivity(getIntent());
                }
            });
            Alert.create().show();
        }

        public AlertDialog pleaseWait() {
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(AddStock.this);
            View mView = getLayoutInflater().inflate(R.layout.alert_progress_spinning, null);
            ProgressBar pb = mView.findViewById(R.id.progressBar);
            mBuilder.setView(mView);
            mBuilder.setCancelable(false);
            final AlertDialog dialog = mBuilder.create();
            dialog.show();
            return dialog;
        }

    }



}
