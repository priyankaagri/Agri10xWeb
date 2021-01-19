package com.mobile.agri10x;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.mobile.agri10x.Connection.POSTRequest;
import com.mobile.agri10x.Model.AddDoc;
import com.mobile.agri10x.Model.Main;
import com.mobile.agri10x.Model.UserId;
import com.mobile.agri10x.SessionManagment.SessionManager;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

public class UploadDocument extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Uri fileUri;
    private String filePath;
    private Button btnChooseFile, upload,back,preview_doc;
    private TextView filePathView;
    private static final int EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 1;
    public static final int PICKFILE_RESULT_CODE = 1;
    private UserId userId;

    private boolean mPermissionGranted = false, fileSelected = false;
    byte[] byteArray;
    private String docType;
    private Spinner docTypeSpinner;
    private String phone;
    private String kyctype = "";
    private AddDoc addDoc;
    byte[] fileBytes = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_document);
        getPermission();
        docTypeSpinner = findViewById(R.id.doc_type_spinner);
        filePathView = findViewById(R.id.document_type);
        upload = findViewById(R.id.Upload1);
        //preview_doc = findViewById(R.id.preview_doc);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userId = new UserId();
            userId.setUserid(extras.getString("Userid"));
        }

        List<String> type = new ArrayList<String>();
        type.add("Aadhar");
        type.add("AddressProof");
        type.add("Passport");
        type.add("PANcard");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, type);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        docTypeSpinner.setAdapter(dataAdapter);
        docTypeSpinner.setOnItemSelectedListener(UploadDocument.this);

        btnChooseFile = findViewById(R.id.choose_file);
        btnChooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filePathView.setText(docTypeSpinner.getSelectedItem().toString());
                Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                //chooseFile.setType("*/*");
                chooseFile.setType("application/pdf");
                chooseFile = Intent.createChooser(chooseFile, "Choose a file");
                startActivityForResult(chooseFile, PICKFILE_RESULT_CODE);
            }
        });
        upload.setEnabled(false);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    new UploadDocument.AddDocPOST().execute();
                    //new UploadDocument.networkPOST().execute(Main.getOldUrl()+"/mVerifyDoc");
                    //new UploadDocument.networkPOST().execute(Main.getOldUrl()+"/mVerifyDoc");
                } catch (Exception e) {
                    Toast.makeText(UploadDocument.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        back=findViewById(R.id.but_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadDocument.this.finish();
            }
        });


    }

    class AddDocPOST extends AsyncTask<Void, Integer, AddDoc> {
        AlertDialog dialog;

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        //@RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected AddDoc doInBackground(Void... voids) {
            addDoc = new AddDoc();
            String encoded;
            encoded = Base64.encodeToString(fileBytes, Base64.DEFAULT);
            encoded.replaceAll("/^[^,]*,/", "");
            addDoc.setFile(encoded);
            addDoc.setKycType(kyctype);
            addDoc.setD1(kyctype);
            addDoc.setUserid(userId.getUserid());
            addDoc.setSession(userId.getUserid());
            addDoc.setEnt(userId.getUserid());
            filePath="";
            return addDoc;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new Alert().pleaseWait();
        }

        @Override
        protected void onPostExecute(AddDoc s) {
            super.onPostExecute(s);
            dialog.dismiss();
            //Toast.makeText(UploadDocument.this,"Data from server: "+addDoc.getFile(),Toast.LENGTH_LONG).show();
            new networkPOST().execute(Main.getOldUrl() + "/VerifyDoc", new Gson().toJson(addDoc));
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
            //Toast.makeText(UploadDocument.this, "Pre Execute", Toast.LENGTH_LONG).show();
            dialog = new UploadDocument.Alert().pleaseWait();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();
            if (s != null) {
                if (s.equals("network")) {
                    new UploadDocument.Alert().alert("Network !!!", getResources().getString(R.string.network_error_message));
                } else if (s.equalsIgnoreCase("Data Saved")) {
                    new UploadDocument.Alert().alert("Upload", "Upload Successful");
                } else if (s.equalsIgnoreCase("Fle Uploaded")) {
                    new UploadDocument.Alert().alert("Upload", "Upload Successful");
                }
            }
            //Toast.makeText(UploadDocument.this, "Post Execute" + s, Toast.LENGTH_LONG).show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String str;
            try {
                str = POSTRequest.fetchUserData(strings[0], strings[1], UploadDocument.this);
            } catch (Exception e) {
                return "network";
            }
            return str;
        }
    }

    public class Alert {

        public void alert(String title, String body) {
            final AlertDialog.Builder Alert = new AlertDialog.Builder(UploadDocument.this);
            Alert.setCancelable(false)
                    .setTitle(title)
                    .setMessage(body);
            Alert.setNegativeButton("Okay", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            Alert.create().show();
        }

        public AlertDialog pleaseWait() {
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(UploadDocument.this);
            View mView = getLayoutInflater().inflate(R.layout.alert_progress_spinning, null);
            ProgressBar pb = mView.findViewById(R.id.progressBar);
            mBuilder.setView(mView);
            mBuilder.setCancelable(false);
            final AlertDialog dialog = mBuilder.create();
            dialog.show();
            return dialog;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PICKFILE_RESULT_CODE:
                if (resultCode == -1) {
                    fileSelected=true;
                    fileUri = data.getData();
                    String uriString1 = fileUri.toString();

                    if(uriString1.startsWith("content://")&& !uriString1.contains(".pdf")){
                        String fp1 = fileUri.getPath();
                        try {
                            InputStream iStream =   getApplicationContext().getContentResolver().openInputStream(fileUri);
                            fileBytes = IOUtils.toByteArray(iStream);
                            iStream.close();
//                            preview_doc.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    Intent intent = new Intent(Intent.ACTION_VIEW);
//                                    intent.setDataAndType(Uri.parse(fp1),"application/pdf");
//                                    startActivity(intent);
//
//                                    Intent pdfOpenintent = new Intent(Intent.ACTION_VIEW);
//                                    pdfOpenintent.setDataAndType(fileUri, "application/pdf");
//                                    pdfOpenintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//                                    try {
//                                        startActivity(pdfOpenintent);
//                                    }
//                                    catch (ActivityNotFoundException e) {
//
//                                    }
//                                }
//                            });
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    else if(uriString1.contains(".pdf")){
                        Toast.makeText(this, filePath, Toast.LENGTH_SHORT).show();
                        filePath = fileUri.getPath();
                        File file = new File(filePath);
                        file.setReadable(true);
                        final String[] split = file.getAbsolutePath().split(":");//split the path.
                        filePath = split[1];//
                        file.setReadable(true);
                        filePath = "/mnt/sdcard/" + filePath;
                        file = new File(filePath);
                        file.setReadable(true);
                        FileInputStream fis = null;
                        try {
                            fis = new FileInputStream(filePath);

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        fileBytes = new byte[(int) file.length()];
                        try {
                            fis.read(fileBytes);
                            Toast.makeText(this, "convert", Toast.LENGTH_SHORT).show();
                            Toast.makeText(this, "convert", Toast.LENGTH_SHORT).show();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    getPermission();
                }
                if(mPermissionGranted&&fileSelected){
                    upload.setEnabled(true);
                }
                while(!mPermissionGranted){
                    getPermission();
                    if(mPermissionGranted&&fileSelected){
                        upload.setEnabled(true);
                        break;
                    }
                }

        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        filePathView.setText(parent.getItemAtPosition(position).toString());
        kyctype = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    private void getPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            mPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                    EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mPermissionGranted = true;
                }
            }
        }
    }

}