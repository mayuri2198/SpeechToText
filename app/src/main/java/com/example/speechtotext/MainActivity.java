package com.example.speechtotext;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Environment;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {
    private static final int REQ_CODE_SPEECH_INPUT = 22;
    private static final int STORAGE_PERMISSION_CODE = 133;
    TextView t;
    Button bt;
    final int REQ_COD_SPEECH_INPUT = 100;
    Calendar calendar;
    String spich;

    SimpleDateFormat simpledataformat;
    //it should be declared seperately and should never be 0 or 1
    // if we dont write final, overridden methods wont recognise that variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connecttoxml();
        operationon();
    }

    private void operationon() {
        bt.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                //perform an action for speech recognition
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                //1.LANGUAGE_MODEL_FREE_FORM  Supports multiple languages and maintains a language free form
                //2.
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                //Locale.getDefault() Using default system language
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, " HI SPEAK SOMETHING !!");
                //EXTRA_PROMPT passing an additional value to the prompt or recognizer
                try {
                    startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
                    //intent,REQ_CODE_SPEECH_INPUT we pass object of intent and req code in startActivityForResult
                    //**********VVIMP***************  startActivityForResult()<start tag jaisa> method is followed by onActivityResult() <end tag jaise> method
                } catch (ActivityNotFoundException a) {

                }
            }
        });
    }


    private void connecttoxml() {
        t = findViewById(R.id.textView);
        bt = findViewById(R.id.button);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    t.setText(result.get(0));
                    //save speech to text file
                    writedatainfile(t.getText().toString());

                }
                break;
            }
        }
    }

    private void writedatainfile(String text) {
        calendar = Calendar.getInstance();
        simpledataformat = new SimpleDateFormat("dd/mm/yyyy HH:MM:SS");
        spich = simpledataformat.format(calendar.getTime());
        spich = "ExternalData" + spich + ".txt";
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File file1 = new File(file, spich);
        writedatainfile(file1, text);
    }

    private void writedatainfile(File file1, String result) {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file1);
            fileOutputStream.write(result.getBytes());
            Toast.makeText(this, "Done" + file1, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

