package com.lolo.nfcexplorer;

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
    private static NFC_manager instance = null;

    private NfcAdapter Nfc_adapter;
    private Tag lastTag;
    private NfcA reader;
    private NdefMessage ndef_file[];

    public static NFC_manager getInstance() {

        return instance;
    }

    private NFC_manager(Context context) {

        Nfc_adapter = NfcAdapter.getDefaultAdapter(context);
    }

    public static boolean INIT_NFC_manager(Context context){

        if(instance == null){
            Log.d("NFCExplorer_debug: ","INIT: NFC manager");
            instance = new NFC_manager(context);
            return true;
        }
        Log.d("NFCExplorer_debug: ","INIT: NFC manager already INIT");
        return false;
    }

    public static boolean Delete_NFC_manager(){

        if(instance != null) {
            instance = null;
            return true;
        }
        return false;
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
