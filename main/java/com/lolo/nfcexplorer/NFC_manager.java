package com.lolo.nfcexplorer;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcA;
import android.os.Parcelable;
import android.util.Log;

/**
 * Created by lolo on 06/10/17.
 */

public class NFC_manager {
    private static NFC_manager instance = new NFC_manager();

    private NfcAdapter Nfc_adapter;
    private Tag lastTag;
    private NfcA reader;
    private NdefMessage ndef_file[];
    private PendingIntent mPendingIntent;

    public static NFC_manager getInstance() {
        return instance;
    }

    private NFC_manager() {
        Nfc_adapter = null;
        lastTag = null;
        reader = null;
        ndef_file = null;
        mPendingIntent = null;
        Log.d("NFCExplorer_debug: ","INIT: NFC manager");
    }

    /*==============================================================================================
    ================================================================================================
    ==============================================================================================*/

    public void onCreate(Context context){
        Nfc_adapter = NfcAdapter.getDefaultAdapter(context);

    }

    public void onPause(Activity activity){
        if (Nfc_adapter != null) {
            Nfc_adapter.disableForegroundDispatch(activity);
        }
    }

    public void onResume(Activity activity, PendingIntent mPendingIntent){
        if (Nfc_adapter != null) {
            Nfc_adapter.enableForegroundDispatch(activity, mPendingIntent, null, null);
        }
    }

    public boolean resolveIntent(Intent data){

        Parcelable[] rawMsgs;
        String action = data.getAction();
        Log.d("NFCExplorer_debug: ","===========resolveIntent===========");

        // Intent is a tag technology (we are sensitive to Ndef, NdefFormatable) or
        // an NDEF message (we are sensitive to URI records with the URI http://www.mroland.at/)

        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {

            Log.d("NFCExplorer_debug: ","===========getTagInfo===========");
            // The reference to the tag that invoked us is passed as a parameter (intent extra EXTRA_TAG)
            lastTag = data.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            rawMsgs = data.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if(lastTag != null)
                reader = NfcA.get(lastTag);
            else
                reader = null;
            if(rawMsgs != null){
                ndef_file = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    ndef_file[i] = (NdefMessage) rawMsgs[i];
                }
            }

            return true;
        }
        return false;
    }

    /*==============================================================================================
    ================================================================================================
    ==============================================================================================*/

    public Tag getLastTag() {
        return lastTag;
    }

    public NfcA getReader() {
        return reader;
    }

    public NdefMessage[] getNdef_file() {
        return ndef_file;
    }
}
