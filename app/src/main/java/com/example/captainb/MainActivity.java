package com.example.captainb;

import static android.Manifest.permission.RECORD_AUDIO;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.os.Bundle;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

//private void checkPermission() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO},RecordAudioRequestCode);}
//}

public class MainActivity extends AppCompatActivity {

    private SpeechRecognizer speechRecognizer;
    private Intent intentRecognizer;
    private TextView textView;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "checkSelfPermission: ");
        }

        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);

        ActivityCompat.requestPermissions(this, new String[]{RECORD_AUDIO}, PackageManager.PERMISSION_GRANTED);

        intentRecognizer = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//
        intentRecognizer.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
//        intentRecognizer.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,"ru-RU");
        intentRecognizer.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);

        speechRecognizer.setRecognitionListener(new RecognitionListener() {

            @Override
            public void onReadyForSpeech(Bundle bundle) {
                Log.d(TAG, "onReadyForSpeech: ");
            }
            @Override
            public void onBeginningOfSpeech() {
                textView.setText("Listening..");
                Log.d(TAG, "onBeginningOfSpeech: ");
            }

            @Override
            public void onRmsChanged(float v) {
//                Log.d(TAG, "onRmsChanged: ");
            }

            @Override
            public void onBufferReceived(byte[] bytes) {
                Log.d(TAG, "onBufferReceived: ");
            }

            @Override
            public void onEndOfSpeech() {
                Log.d(TAG, "onEndOfSpeech: ");
            }

            @Override
            public void onError(int i) {
                Log.d(TAG, "onError: ");
            }

            @Override
            public void onResults(Bundle bundle) {
                Log.d(TAG, "onResults: ");
                ArrayList<String> matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                String spokenText = "";

                new GetData().execute();

                if (matches != null){
                    spokenText = matches.get(0);
                    textView.setText(spokenText);
                }
                else {
                    textView.setText("Not set");
                }
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });
    }

    public void StartButton(View view){
        Log.d(TAG, "StartButton: ");
        try {
            URL url = new URL("https://algame9-vps.roborumba.com/vector_search");
            new GetData().execute(url);

        } catch (MalformedURLException e){
            e.printStackTrace();
        }
        speechRecognizer.startListening(intentRecognizer);
    }

    public void StopButton(View view){
        Log.d(TAG, "StopButton: ");
        speechRecognizer.stopListening();

    }
}