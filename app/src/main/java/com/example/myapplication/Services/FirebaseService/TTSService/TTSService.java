package com.example.myapplication.Services.FirebaseService.TTSService;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.Locale;

public class TTSService implements TextToSpeech.OnInitListener {

    private TextToSpeech mTTS;
    private String message;

    public TTSService(Context context, String message) {
        this.mTTS = new TextToSpeech(context, this);
        this.message = message;
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = mTTS.setLanguage(Locale.UK);
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "Language not supported");
            } else {
                speak();
            }
        } else {
            Log.e("TTS", "Initialization failed");
        }
    }

    void speak(){
        mTTS.setPitch(1);
        mTTS.setSpeechRate(1);
        mTTS.speak(message, TextToSpeech.QUEUE_FLUSH, null);

    }
}
