package com.lolo.nfcexplorer;


import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.nfc.Tag;
import android.nfc.tech.NfcA;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.io.IOException;

public class main_activity extends Activity {

    NfcA reader0;
    Tag tag0;
    TextView tag_info;
    NfcAdapter Nfc_adapter;
    Button scan_tag_button;
    PendingIntent mPendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m);
        scan_tag_button = (Button) findViewById(R.id.button_scan_tag);
        scan_tag_button.setOnClickListener(scan_tag_buttonListener);
        tag_info  = (TextView) findViewById(R.id.tag_info);

        resolveIntent(getIntent());

        Nfc_adapter = NfcAdapter.getDefaultAdapter(this);
        if (Nfc_adapter == null) {

            finish();
            return;

        }
        mPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
    }

    /**
     * Called when the activity gets focus.
     */
    @Override
    protected void onResume() {
        super.onResume();

        if (Nfc_adapter != null) {
            Nfc_adapter.enableForegroundDispatch(this, mPendingIntent, null, null);
        }
    }

    /**
     * Called when the activity loses focus.
     */
    @Override
    protected void onPause(){
        super.onPause();
        if (Nfc_adapter != null) {
            try {
                    // Disable foreground dispatch:
                    Nfc_adapter.disableForegroundDispatch(this);
                } catch (NullPointerException e) {
                    // Drop NullPointerException that is sometimes thrown
                    // when NFC service crashed
                }
            }
    }

    /**
     * Called when activity receives a new intent.
     */

    private void onNewIntent(Intent data, boolean foregroundDispatch) {
        this.setIntent(data);
        // Resolve the intent that re-invoked us:
        resolveIntent(data);
    }

    private void resolveIntent(Intent data) {
        this.setIntent(data);

        String action = data.getAction();
        Log.d("NFCExplorer_debug: ","===========resolveIntent===========");

        // Intent is a tag technology (we are sensitive to Ndef, NdefFormatable) or
        // an NDEF message (we are sensitive to URI records with the URI http://www.mroland.at/)

        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Log.d("NFCExplorer_debug: ","===========resolveIntent/getTagInfo===========");
            // The reference to the tag that invoked us is passed as a parameter (intent extra EXTRA_TAG)
            tag0 = data.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            getTagInfo();
        }
    }

    private View.OnClickListener scan_tag_buttonListener = new View.OnClickListener() {
        public void onClick(View v) {
            getTagInfo();

        }
    };

    private void getTagInfo(){

        if(tag0 == null){
            tag_info.setText("No Tag...");
        }
        else {
            try {

                reader0 = NfcA.get(tag0);
                reader0.connect();

                tag0 = reader0.getTag();

                reader0.close();
                tag_info.setText(tag0.getId().toString());
                //tag_info.setText(getTagInfo());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}

