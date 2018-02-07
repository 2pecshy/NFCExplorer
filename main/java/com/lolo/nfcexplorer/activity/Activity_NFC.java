package com.lolo.nfcexplorer.activity;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.lolo.nfcexplorer.nfc_service.NFC_manager;

/**
 * Created by lolo on 21/12/17.
 */

public class Activity_NFC extends Activity {

    NFC_manager nfc_manager;
    PendingIntent mPendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        nfc_manager = NFC_manager.getInstance(this);
        nfc_manager.onCreate(this);
        mPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        nfc_manager.onResume(this,mPendingIntent);

    }

    @Override
    protected void onPause() {
        super.onPause();
        nfc_manager.onPause(this);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        this.setIntent(intent);
        nfc_manager.resolveIntent(intent,this);
        if(nfc_manager.getLastTag() != null)
            onNewTagSuportedDetected();
    }

    protected void onNewTagSuportedDetected(){
        //to override
        Log.d("Activity_NFC","New Supported tag detected");
    }
}
