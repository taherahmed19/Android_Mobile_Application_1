package com.example.myapplication.Services.FirebaseService.TTSService;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

public class TTSService implements TextToSpeech.OnInitListener {

    private TextToSpeech mTTS;
    private String[] messages;

    public TTSService(Context context, String[] messages) {
        this.mTTS = new TextToSpeech(context, this);
        this.messages = messages;
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = mTTS.setLanguage(Locale.UK);
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            } else {
                speak();
            }
        }
    }

    void speak(){
        mTTS.setPitch(1);
        mTTS.setSpeechRate(1);

        for (String message : this.messages) {
            mTTS.speak(message, TextToSpeech.QUEUE_ADD, null);
            mTTS.playSilence(500, TextToSpeech.QUEUE_ADD, null);
        }
    }
}
