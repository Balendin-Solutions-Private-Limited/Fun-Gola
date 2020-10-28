package com.thambola.fungola;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;

//import com.github.barteksc.pdfviewer.PDFView;

public class PDFViewActivity extends AppCompatActivity {

    private TextView text_view,pdf_heading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p_d_f_view);

        text_view=findViewById(R.id.text_view);
        pdf_heading=findViewById(R.id.pdf_heading);

        String newString;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                newString= null;
            } else {
                newString= extras.getString("value");
            }
        } else {
            newString= (String) savedInstanceState.getSerializable("value");
        }

        pdf_heading.setText(""+newString);

        String text = "";
        try{
            InputStream inputStream = getAssets().open(""+newString+".txt");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            text = new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        text_view.setText(text);

       /* Toast.makeText(getApplicationContext(),"The file read operation is finished successfully.",
                Toast.LENGTH_SHORT).show();*/

      //  PDFView pdfView = (PDFView) findViewById(R.id.pdfView);
     //   pdfView.fromAsset(""+newString+".pdf").load();
    }
}
