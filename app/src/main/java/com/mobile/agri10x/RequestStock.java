package com.mobile.agri10x;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.mobile.agri10x.Connection.POSTRequest;
import com.mobile.agri10x.Model.AddRequest;
import com.mobile.agri10x.Model.Main;
import com.mobile.agri10x.Model.SecurityData;
import com.mobile.agri10x.Model.UserId;
import com.mobile.agri10x.SessionManagment.SessionManager;
import com.mobile.agri10x.models.GetRequestedCommodity;
import com.mobile.agri10x.models.GetRequestedVariety;
import com.mobile.agri10x.models.GetTradeVariety;
import com.mobile.agri10x.retrofit.AgriInvestor;
import com.mobile.agri10x.retrofit.ApiHandler;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import gr.escsoft.michaelprimez.searchablespinner.SearchableSpinner;
import gr.escsoft.michaelprimez.searchablespinner.interfaces.OnItemSelectedListener;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestStock extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String[] Value = new String[] {
            "Yes", "No"
    };

    private static final String[] Unit = new String[] {
            "10","15","20","25","30","35","40","45","50","55","60","65","70","75","80","85","90","95","100"
    };

    private static final String[] comm = new String[]{
            "wheat","almond","potato","onion"
    };
    SimpleListAdapter1 mSimpleListAdapter;
    String Comm="",unit="",Quan="",vlfrom="",vlTill="",qual="",role="",Organic="",ResidueFree="",UserType="",variety="",commid,strcomname,varietyid;
    List<GetRequestedCommodity> getRequestedCommodityArrayList = new ArrayList<>();
    List<GetRequestedVariety> getRequestedVarietyArrayList = new ArrayList<>();
    ArrayList<String> ReqCommoditycategory = new ArrayList<>();
    ArrayList<String> ReqVarietycategory = new ArrayList<>();;
    SessionManager session;
    SearchableSpinner commodity;
    MaterialBetterSpinner Variety;
    MaterialBetterSpinner commodityunit;
    EditText quantity,quality;
    Button btn,back;
    private DatePickerDialog.OnDateSetListener VelidFromDateListner;
    private DatePickerDialog.OnDateSetListener VelidTilDateListner;
    RadioGroup request_group,organic_group,residue_group;

    private Spinner quality_spinner;

    private TextView velidFrom,velidTill;
    //Toolbar toolbar;

    Long ddfrom;
    private UserId userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_stock);
        commodityunit =(MaterialBetterSpinner) findViewById(R.id.CommodityUnit);
        quality_spinner=(Spinner) findViewById(R.id.quality_spinner);
        Variety = (MaterialBetterSpinner) findViewById(R.id.Variety);
        commodity =  findViewById(R.id.Commodity);
        quantity = (EditText) findViewById(R.id.comm_quantity);
        btn =(Button) findViewById(R.id.addrequest_button);
        //toolbar = findViewById(R.id.toolbar_request_code);
        velidFrom = (TextView) findViewById(R.id.validFrom);
        velidTill =  (TextView) findViewById(R.id.validTill);
        request_group= (RadioGroup) findViewById(R.id.request_group);
        organic_group = (RadioGroup) findViewById(R.id.organic_group);
        residue_group =(RadioGroup)  findViewById(R.id.residue_group);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userId=new UserId();
            userId.setUserid(extras.getString("Userid"));
            UserType=extras.getString("user_type");
        }



        List<String> type = new ArrayList<String>();
        type.add("Grade A");
        type.add("Grade B");
        type.add("Grade C");
        type.add("Grade D");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,type);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        quality_spinner.setAdapter(dataAdapter);
        quality_spinner.setOnItemSelectedListener(RequestStock.this);

        callapi();
        /*
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_name         );
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //SessionManager sess = new SessionManager(getApplicationContext());
                finish();
            }
        });*/



//        ArrayAdapter<String> commodity_adapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_dropdown_item_1line, SecurityData.getCommodity());
//        commodity = findViewById(R.id.Commodity);
//        commodity.setAdapter(commodity_adapter);

//        ArrayAdapter<String> variety_adapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_dropdown_item_1line, SecurityData.getCommodity());
//        Variety = findViewById(R.id.Variety);
//        Variety.setAdapter(variety_adapter);


        ArrayAdapter<String> comm_unit = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, Unit);

        commodityunit.setAdapter(comm_unit);


        request_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                MaterialRadioButton rb = (MaterialRadioButton) group.findViewById(checkedId);
                if (rb.isChecked()) {
                    role = rb.getText().toString();
                    // Toast.makeText(RequestStock.this, rb.getText(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        residue_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                MaterialRadioButton rb = (MaterialRadioButton) group.findViewById(checkedId);
                if (rb.getText().equals("Yes")) {
                    ResidueFree = "true";
                    //   Toast.makeText(RequestStock.this, rb.getText().toString(), Toast.LENGTH_SHORT).show();
                }
                else {
                    ResidueFree = "false";
                    //   Toast.makeText(RequestStock.this, rb.getText().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        organic_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                MaterialRadioButton rb = (MaterialRadioButton) group.findViewById(checkedId);
                if (rb.getText().equals("Yes")) {
                    Organic="true";
                    //  Toast.makeText(RequestStock.this, rb.getText().toString(), Toast.LENGTH_SHORT).show();
                }
                else {
                    Organic = "false";
                    //   Toast.makeText(RequestStock.this, rb.getText().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        session=new SessionManager(getApplicationContext());

        velidFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        RequestStock.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        VelidFromDateListner,
                        year,month,day);
                DatePicker datePicker = dialog.getDatePicker();
                long now = System.currentTimeMillis() - 1000;
                datePicker.setMinDate(now);
                datePicker.setMaxDate(now+1000*60*60*24*SecurityData.getDateLimit());
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        VelidFromDateListner = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month++;
                String mon = Integer.toString(month);
                String yyyy = Integer.toString(year);
                String dd = Integer.toString(day);
                vlfrom = yyyy+"/"+mon+"/"+dd;
                DateFormat sd = new SimpleDateFormat("yyyy/MM/dd");
                Date d = null;
                try {
                    d = sd.parse(vlfrom);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                ddfrom = d.getTime();
                // parse the date string into Date object
                velidFrom.setText(dd+" "+getmonth(month)+" "+yyyy);
            }
        };
        velidTill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(vlfrom.length()>0) {
                    Calendar cal = Calendar.getInstance();
                    int year = cal.get(Calendar.YEAR);
                    int month = cal.get(Calendar.MONTH);
                    int day = cal.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog dialog = new DatePickerDialog(
                            RequestStock.this,
//                            android.R.style.Cal
                            android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                            VelidTilDateListner,
                            year, month, day);
                    DatePicker datePicker = dialog.getDatePicker();
                    long now = System.currentTimeMillis() - 1000;
                    long x = now + ddfrom;
                    Log.e("value x :",Long.toString(ddfrom));
                    datePicker.setMinDate(ddfrom);
                    datePicker.setMaxDate(System.currentTimeMillis() + (1000 * 60 * 60 * 24 * SecurityData.getDateLimit()));
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();
                }
                else{
                    Toast.makeText(RequestStock.this,"First Enter Validity From",Toast.LENGTH_LONG).show();
                }
            }
        });
        VelidTilDateListner = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month++;
                String mon = Integer.toString(month);
                String yyyy = Integer.toString(year);
                String dd = Integer.toString(day);
                vlTill = yyyy+"/"+mon+"/"+dd;
                // parse the date string into Date object
                velidTill.setText(dd+" "+getmonth(month)+" "+yyyy);
            }
        };

        back=findViewById(R.id.req_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestStock.this.finish();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unit = commodityunit.getText().toString().trim();
                Comm = strcomname;
                Quan = quantity.getText().toString().trim();
               // variety = varietyid;
                if (unit.length()>0 && Quan.length()>0 && vlfrom.length()>0 && vlTill.length()>0 && role.length()>0 &&qual.length() > 0   && Organic.length()>0 && ResidueFree.length()>0) { //&& variety.length()>0
                    int quan = Integer.valueOf(Quan);
                    int unitInt = Integer.valueOf(unit);
                    if(unitInt>0 &&  quan>0) {
                        AddRequest req = new AddRequest(strcomname,Quan,unit, SessionManager.getKEY_Entity_id(),vlfrom,vlTill,qual,role,userId.getUserid(),Organic,ResidueFree,UserType,varietyid);
                        new AddStockPOST().execute(Main.getIp()+ "/fStock", new Gson().toJson(req));
                    }
                    else if(unitInt<=0){
                        Toast.makeText(RequestStock.this,"Add Valid Value of Unit",Toast.LENGTH_LONG).show();
                    }
                    else if(quan<=0)
                    {
                        Toast.makeText(RequestStock.this,"Add Valid Value of Quantity",Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(RequestStock.this,"Add Valid Value of Quantity And Unit",Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(RequestStock.this, "Please Fill All Details!!", Toast.LENGTH_LONG).show();
                }
            }
        });
        commodity.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(View view, int position, long id) {
                //   Toast.makeText(AddStock.this, "Item on position " + position + " : " + mSimpleListAdapter.getItem(position) + " Selected", Toast.LENGTH_SHORT).show();
                String pos = commodity.getSelectedItem().toString();
                Log.d("selectedcommo",pos);
                for(int i= 0 ;i < getRequestedCommodityArrayList.size() ; i++){
                    if(pos.equals(getRequestedCommodityArrayList.get(i).getCommodity())){
                        commid = getRequestedCommodityArrayList.get(i).getId();
                        strcomname = getRequestedCommodityArrayList.get(i).getCommodity();
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
    }

    private String getVarietyId(int i) {
        String Vvarirtyid = "";


        Vvarirtyid = getRequestedVarietyArrayList.get(i).getVariety();//getId()

        Log.d("checkid",Vvarirtyid);
        //Returning the id
        return Vvarirtyid;
    }

    private void callvariety(String commid) {
        ReqVarietycategory.clear();
        Map<String, Object> jsonParams = new ArrayMap<>();
//put something inside the map, could be null
        jsonParams.put("id", commid);

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (new JSONObject(jsonParams)).toString());

        AgriInvestor apiService = ApiHandler.getApiService();
        final Call<List<GetRequestedVariety>> loginCall = apiService.wsgetRequestedVariety(
                "123456", body);
        loginCall.enqueue(new Callback<List<GetRequestedVariety>>() {
            @SuppressLint("WrongConstant")
            @Override
            public void onResponse(Call<List<GetRequestedVariety>> call,
                                   Response<List<GetRequestedVariety>> response) {
                Log.d("GetTradeVariety", response.toString());
                if (response.isSuccessful()) {
                    getRequestedVarietyArrayList = response.body();
                    Log.d("getresponse", String.valueOf(getRequestedVarietyArrayList.size()));

                    if (!getRequestedVarietyArrayList.isEmpty()) {
                        Variety.setDropDownHeight(WindowManager.LayoutParams.WRAP_CONTENT);
                        Variety.setClickable(true);
                        Variety.setFocusable(true);
                        for (int i = 0; i < getRequestedVarietyArrayList.size(); i++) {
                            ReqVarietycategory.add(getRequestedVarietyArrayList.get(i).getVariety());
                        }
                        Log.d("Varietycategory", String.valueOf(ReqVarietycategory.size()));

                        Variety.setAdapter(new ArrayAdapter<String>(RequestStock.this, android.R.layout.simple_dropdown_item_1line, ReqVarietycategory));
                    } else {
                        Variety.setDropDownHeight(0);

                        ReqVarietycategory.add("No Data");
                        Variety.setAdapter(new ArrayAdapter<String>(RequestStock.this, android.R.layout.simple_dropdown_item_1line, ReqVarietycategory));
                    }
                }
            }

            @Override
            public void onFailure(Call<List<GetRequestedVariety>> call,
                                  Throwable t) {
                Toast.makeText(RequestStock.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private String getComName(int i) {
        String Ccomname = "";


        Ccomname = getRequestedCommodityArrayList.get(i).getCommodity();//

        Log.d("checkid",Ccomname);
        //Returning the id
        return Ccomname;
    }

    private String getComId(int i) {
        String Ccomid = "";


        Ccomid = getRequestedCommodityArrayList.get(i).getId();//

        Log.d("checkid",Ccomid);
        //Returning the id
        return Ccomid;
    }

    private void callapi() {
        AgriInvestor apiService = ApiHandler.getApiService();
        final Call<List<GetRequestedCommodity>> loginCall = apiService.wsgetRequestedCommodity(
                "123456");
        loginCall.enqueue(new Callback<List<GetRequestedCommodity>>() {
            @SuppressLint("WrongConstant")
            @Override
            public void onResponse(Call<List<GetRequestedCommodity>> call,
                                   Response<List<GetRequestedCommodity>> response) {
                Log.d("GetReqTradeCommodity",response.toString());
                if (response.isSuccessful()) {
                    getRequestedCommodityArrayList = response.body();
                    Log.d("getresponsereq", String.valueOf(getRequestedCommodityArrayList.size()));




                    if(!getRequestedCommodityArrayList.isEmpty()){

                        commodity.setClickable(true);
                        commodity.setFocusable(true);
                        //                    Commoditycategory.add("Select");
                        for(int i=0; i < getRequestedCommodityArrayList.size();i++){
                            ReqCommoditycategory.add(getRequestedCommodityArrayList.get(i).getCommodity());
                        }
                        Log.d("ReqCommoditycategory", String.valueOf(ReqCommoditycategory.size()));

                        commodity.setAdapter(new ArrayAdapter<String>(RequestStock.this, android.R.layout.simple_dropdown_item_1line, ReqCommoditycategory));


                    }else{

                        ReqCommoditycategory.add("No Data");
                        mSimpleListAdapter = new SimpleListAdapter1(RequestStock.this, ReqCommoditycategory);
                        commodity.setAdapter(new ArrayAdapter<String>(RequestStock.this, android.R.layout.simple_dropdown_item_1line, ReqCommoditycategory));
                    }
                }
            }

            @Override
            public void onFailure(Call<List<GetRequestedCommodity>> call,
                                  Throwable t) {
                Toast.makeText(RequestStock.this,"Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getmonth(int month) {

        switch (month) {
            case 1:
                return "Jan";

            case 2:
                return "Feb";

            case 3:
                return "Mar";

            case 4:
                return "Apr";

            case 5:
                return "May";

            case 6:
                return "Jun";

            case 7:
                return "July";

            case 8:
                return "Aug";

            case 9:
                return "Sep";

            case 10:
                return "Oct";

            case 11:
                return "Nov";

            case 12:
                return "Dec";

        }
        return "Jan";

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        qual = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    class AddStockPOST extends AsyncTask<String, Void, String> {

        AlertDialog dialog;
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();
            if(s!=null) {
                if (s.equals("network")) {
                    new Alert().alert("Network !!!", getResources().getString(R.string.network_error_message));
                } else {
                    quantity.setText("");
                     //quality.setText("");
                    //commodity.setSelectedItem(0);
                    // commodity.setText("");

                    commodityunit.setText("");
                    velidFrom.setText("");
                    velidTill.setText("");
                    Variety.setText("");


                    new Alert().alert("Request Stock","Successfully Added");
                }
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new Alert().pleaseWait();
        }

        @Override
        protected String doInBackground(String... strings) {
            String string = null;
            try {
                string = POSTRequest.fetchUserData(strings[0],strings[1],RequestStock.this);
            } catch(Exception e){
                return "network";
            }
            return string;
        }
    }

    class Alert{
        public void alert(String title, String body) {
            final AlertDialog.Builder Alert = new AlertDialog.Builder(RequestStock.this);
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
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(RequestStock.this);
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
