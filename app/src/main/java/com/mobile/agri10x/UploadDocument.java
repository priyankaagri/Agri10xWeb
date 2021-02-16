package com.mobile.agri10x;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.google.gson.Gson;
import com.mobile.agri10x.Connection.POSTRequest;
import com.mobile.agri10x.Model.AddDoc;
import com.mobile.agri10x.Model.Main;
import com.mobile.agri10x.Model.UserId;
import com.mobile.agri10x.SessionManagment.SessionManager;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UploadDocument extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Uri fileUri;
    long fileSizeInMB;
    private String filePath,extension;
    private Button btnChooseFile, upload,preview_doc;
    ImageView back;
    private TextView filePathView,filename;
    private static final int EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 1;
    public static final int PICKFILE_RESULT_CODE = 1;
    private UserId userId;
    Button CameraClick;
    RelativeLayout relative_img_click;
    private boolean mPermissionGranted = false, fileSelected = false;
    byte[] byteArray;
    private String docType,pathofselected="";
    private Spinner docTypeSpinner;
    private String phone;
    private String kyctype = "";
    private AddDoc addDoc;
    byte[] fileBytes = null;
    private String imgPath;
    final private int CAPTURE_IMAGE = 2;
    int TAKE_PHOTO_CODE = 0;
    public static int count = 0;
    String encoded;

    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_activity_upload_document);
        getPermission();
        docTypeSpinner = findViewById(R.id.doc_type_spinner);
        filePathView = findViewById(R.id.document_type);
        upload = (Button) findViewById(R.id.Upload1);
        filename = findViewById(R.id.filename);

        //preview_doc = findViewById(R.id.preview_doc);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userId = new UserId();
            userId.setUserid(extras.getString("Userid"));
        }

        List<String> type = new ArrayList<String>();
        // Pancard","Electricity Bill","Kisan Credit Card","Voter Id","GST Certificate","Shop Act","Cancelled Cheque"
        type.add("Pancard");
//        type.add("Electricity Bill");
//        type.add("Kisan Credit Card");
        type.add("Voter Id");
//        type.add("GST Certificate");
//        type.add("Shop Act");
        type.add("Cancelled Cheque");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, type);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        docTypeSpinner.setAdapter(dataAdapter);
        docTypeSpinner.setOnItemSelectedListener(UploadDocument.this);

      //  btnChooseFile = findViewById(R.id.choose_file);
        //CameraClick = findViewById(R.id.CameraClick);
        relative_img_click = findViewById(R.id.relative_img_click);

        final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/picFolder/";
        File newdir = new File(dir);
        newdir.mkdirs();
//        CameraClick.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(cameraIntent, CAMERA_REQUEST);
//
//               /* count++;
//                String file = dir+"Image"+count+".jpg";
//                File newfile = new File(file);
//                try {
//                    newfile.createNewFile();
//                }
//                catch (IOException e)
//                {
//                }
//
//                Uri outputFileUri = Uri.fromFile(newfile);
//                //Uri outputFileUri = FileProvider.getUriForFile(UploadDocument.this, BuildConfig.APPLICATION_ID, newfile);
//
//                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
//
//                startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);*/
//            }
//        });

//        btnChooseFile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                filePathView.setText(docTypeSpinner.getSelectedItem().toString());
////                Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
////                //chooseFile.setType("*/*");
////                chooseFile.setType("application/pdf");
////                chooseFile = Intent.createChooser(chooseFile, "Choose a file");
//
//                final String IMAGE = "image/*";
//                final String PDF = "application/pdf";
//                Intent intent = getCustomFileChooserIntent(PDF, IMAGE);
//                startActivityForResult(intent, PICKFILE_RESULT_CODE);
//            }
//        });
        upload.setEnabled(false);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pathofselected.equals("File size should not exceed more than 1MB")){
                    pathofselected = "";
                    filename.setText(pathofselected);
                    Toast.makeText(UploadDocument.this, "File size should not exceed more than 1MB", Toast.LENGTH_LONG).show();
                }else if(pathofselected.equals("") && kyctype.equals("")){
                    filename.setText("");
                    Toast.makeText(UploadDocument.this, "Please select Type and File", Toast.LENGTH_LONG).show();
                }
                else if(pathofselected.equals("")){
                    filename.setText("");
                    Toast.makeText(UploadDocument.this, "Please select file", Toast.LENGTH_LONG).show();
                }else if(kyctype.equals("")){
                    Toast.makeText(UploadDocument.this, "Please select type", Toast.LENGTH_LONG).show();
                }
                else{
                    try {
                        filename.setText("");
                        docTypeSpinner.setSelection(0);
                        new UploadDocument.AddDocPOST().execute();
                        //new UploadDocument.networkPOST().execute(Main.getOldUrl()+"/mVerifyDoc");
                        //new UploadDocument.networkPOST().execute(Main.getOldUrl()+"/mVerifyDoc");
                    } catch (Exception e) {
                        Toast.makeText(UploadDocument.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
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

        relative_img_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder=new AlertDialog.Builder(UploadDocument.this);
                LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View layout=inflater.inflate(R.layout.custom_alert_box,null);
                builder.setView(layout);
                TextView btn_camera=layout.findViewById(R.id.btn_camera);
                TextView btn_gal=layout.findViewById(R.id.btn_gal);

                final AlertDialog alertDialog = builder.create();

                btn_camera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v){

                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST);

/* count++;
String file = dir+"Image"+count+".jpg";
File newfile = new File(file);
try {
newfile.createNewFile();
}
catch (IOException e)
{
}

Uri outputFileUri = Uri.fromFile(newfile);
//Uri outputFileUri = FileProvider.getUriForFile(UploadDocument.this, BuildConfig.APPLICATION_ID, newfile);

Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);*/
                        alertDialog.dismiss();
                    }

                });


                btn_gal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        filePathView.setText(docTypeSpinner.getSelectedItem().toString());
// Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
// //chooseFile.setType("*/*");
// chooseFile.setType("application/pdf");
// chooseFile = Intent.createChooser(chooseFile, "Choose a file");

                        final String IMAGE = "image/*";
                        final String PDF = "application/pdf";
                        Intent intent = getCustomFileChooserIntent(PDF, IMAGE);
                        startActivityForResult(intent, PICKFILE_RESULT_CODE);
                        alertDialog.dismiss();
                    }

                });
                alertDialog.show();
//                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
//                layoutParams.copyFrom(alertDialog.getWindow().getAttributes());
//                layoutParams.width = 800;
//                layoutParams.height =500;
//                alertDialog.getWindow().setAttributes(layoutParams);
            }
        });
    }



    private Intent getCustomFileChooserIntent(String ...types) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        // Filter to only show results that can be "opened"
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_MIME_TYPES, types);
        return intent;
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
            encoded = Base64.encodeToString(fileBytes, Base64.DEFAULT);
            encoded.replaceAll("/^[^,]*,/", "");
            addDoc.setFile(encoded);
            addDoc.setKycType(kyctype);
            addDoc.setD1(kyctype);
            addDoc.setExt(extension);
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
                    new UploadDocument.Alert().alert("Upload", "Upload Successful!");
                } else if (s.equalsIgnoreCase("Fle Uploaded")) {
                    new UploadDocument.Alert().alert("Upload", "Upload Successful!");
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
                    pathofselected = getPDFPath(fileUri);
                    Log.d("pathofselected",pathofselected);
                    if(pathofselected.equals("File size should not exceed more than 1MB")){
                        pathofselected = "File size should not exceed more than 1MB";
                    }else{
                        filename.setText(pathofselected.substring(pathofselected.lastIndexOf("/") + 1));
                        String someFilepath = pathofselected.substring(pathofselected.lastIndexOf("/") + 1);
                        Log.d("strpath",someFilepath);
                        extension  = someFilepath.substring(someFilepath.lastIndexOf("."));
                        extension = extension.replaceAll("[-+.^:,]","");
                        Log.d("extention",extension);
                    }

                    if(uriString1.startsWith("content://")&& !uriString1.contains(".pdf")){
                        String fp1 = fileUri.getPath();

                        try {
                            InputStream iStream =   getApplicationContext().getContentResolver().openInputStream(fileUri);
                            Bitmap bitmap = BitmapFactory.decodeStream(iStream);
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            if(fileSizeInMB > 1){
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                                Log.d("going jpeg gal","going jpeg gal");
                            }else{
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                                Log.d("going png gal","going png gal");
                            }

                            fileBytes = stream.toByteArray();
                            Log.d("going gLLARY","going gLLARY");
                            bitmap.recycle();
//                            fileBytes = IOUtils.toByteArray(iStream);
//                            iStream.close();
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
                break;
            case CAMERA_REQUEST:

                if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK)
                {
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                   // Bitmap bmp = intent.getExtras().get("data");
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    if(fileSizeInMB > 1){
                        photo.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                        Log.d("going jpeg cam","going jpeg cam");
                    }else{
                        photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        Log.d("going png cam","going png cam");
                    }
                 //  photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    fileBytes = stream.toByteArray();
                    photo.recycle();
                    upload.setEnabled(true);
                    Log.d("going camera","going camera");
                    count++;
                    //String file = dir+"Image"+count+".jpg";
                    filename.setText("IMG_0000"+count+"jpg");
                    extension = "jpg";
                    String aaa="IMG_0000"+count+"jpg";
                    pathofselected="/data/user/0/com.mobile.agri10x/files/"+aaa;
                    // imageView.setImageBitmap(photo);
                }
                /*if (requestCode == TAKE_PHOTO_CODE && resultCode == RESULT_OK) {
                    final Uri imageUri = data.getData();
                    InputStream imageStream = null;
                    try {
                        imageStream = getContentResolver().openInputStream(imageUri);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    String encodedImage = encodeImage(selectedImage);

                    encoded=encodedImage;
                    Log.d("hwaitfor",encoded);
                }*/
        }
    }

    private String encodeImage(Bitmap bm)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }

    private String getPDFPath(Uri fileUri) {
        Uri returnUri = fileUri;
        Cursor returnCursor = UploadDocument.this.getContentResolver().query(returnUri, null, null, null, null);
        /*
         * Get the column indexes of the data in the Cursor,
         *     * move to the first row in the Cursor, get the data,
         *     * and display it.
         * */
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
        returnCursor.moveToFirst();
        String name = (returnCursor.getString(nameIndex));
        String size = (Long.toString(returnCursor.getLong(sizeIndex)));
        File file = new File(UploadDocument.this.getFilesDir(), name);
        // Get length of file in bytes
        long fileSizeInBytes = file.length();
// Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
        long fileSizeInKB = fileSizeInBytes / 1024;
// Convert the KB to MegaBytes (1 MB = 1024 KBytes)
        fileSizeInMB = fileSizeInKB / 1024;

//        if(fileSizeInMB <= 1){
            try {
                InputStream inputStream = UploadDocument.this.getContentResolver().openInputStream(fileUri);
                FileOutputStream outputStream = new FileOutputStream(file);
                int read = 0;
                int maxBufferSize = 1 * 1024 * 1024;
                int bytesAvailable = inputStream.available();

                //int bufferSize = 1024;
                int bufferSize = Math.min(bytesAvailable, maxBufferSize);

                final byte[] buffers = new byte[bufferSize];
                while ((read = inputStream.read(buffers)) != -1) {
                    outputStream.write(buffers, 0, read);
                }
                Log.e("File Size", "Size " + file.length());
                inputStream.close();
                outputStream.close();
                Log.e("File Path", "Path " + file.getPath());
                Log.e("File Size", "Size " + file.length());
            } catch (Exception e) {
                Log.e("Exception", e.getMessage());
            }
            return file.getPath();
     //   }
//        else{
//
//            Toast.makeText(UploadDocument.this, "File size should not exceed more than 1MB", Toast.LENGTH_SHORT).show();
//            return "File size should not exceed more than 1MB";
//        }

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

    @Override
    public void onBackPressed() {

    }
}
