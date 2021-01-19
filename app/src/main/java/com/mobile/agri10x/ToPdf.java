package com.mobile.agri10x;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickCancel;
import com.vansuita.pickimage.listeners.IPickResult;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ToPdf extends AppCompatActivity implements View.OnClickListener {

    private Button button,convert;
    private Bitmap bitmap;
    private boolean select = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_pdf);
        button = findViewById(R.id.button);
        convert = findViewById(R.id.convert);
        button.setOnClickListener(ToPdf.this);
        convert.setOnClickListener(ToPdf.this);
    }

    private void convertToPdf(Bitmap b) {
        PdfDocument document;
        PdfDocument.PageInfo pageInfo;
        PdfDocument.Page page;
        Canvas canvas;
        File file;
        FileOutputStream fos = null;
        document = new PdfDocument();
        String directoryPath = android.os.Environment.getExternalStorageDirectory().toString();
        file = new File(directoryPath,"/example.pdf");
        file.setReadable(true);
        file.setWritable(true);
        Toast.makeText(this, file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        pageInfo = new PdfDocument.PageInfo.Builder(b.getWidth(),b.getHeight(),1).create();
        page = document.startPage(pageInfo);
        canvas = page.getCanvas();
        canvas.drawBitmap(b,0f,0f,null);
        document.finishPage(page);
        pageInfo = new PdfDocument.PageInfo.Builder(b.getWidth(),b.getHeight(),2).create();
        page = document.startPage(pageInfo);
        canvas = page.getCanvas();
        canvas.drawBitmap(b,0f,0f,null);
        document.finishPage(page);
        try {
            fos = new FileOutputStream(file.getAbsolutePath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            document.writeTo(fos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            document.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button:
                PickImageDialog.build(new PickSetup())
                        .setOnPickResult(new IPickResult() {
                            @Override
                            public void onPickResult(PickResult r) {
                                select = true;
                                bitmap = r.getBitmap();
                            }
                        })
                        .setOnPickCancel(new IPickCancel() {
                            @Override
                            public void onCancelClick() {
                            }
                        }).show(getSupportFragmentManager());
                break;
            case R.id.convert:
                if(select){
                    Toast.makeText(this, "Converting", Toast.LENGTH_SHORT).show();
                    convertToPdf(bitmap);
                }
                else {
                    Toast.makeText(this, "Recapture", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
