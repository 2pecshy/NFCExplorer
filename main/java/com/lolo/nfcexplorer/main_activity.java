package com.lolo.nfcexplorer;


import android.content.Context;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.nfc.Tag;
import android.nfc.tech.NfcA;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.io.IOException;

public class main_activity extends AppCompatActivity {

    NfcA reader0;
    Tag tag0;
    TextView tag_info;
    NfcAdapter Nfc_adapter;
    Button scan_tag_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m);
        scan_tag_button = (Button) findViewById(R.id.button_scan_tag);
        scan_tag_button.setOnClickListener(scan_tag_buttonListener);
        tag_info  = (TextView) findViewById(R.id.tag_info);
        //reader0 = NfcA.get(tag0);
    }

    /**
     * Called when the activity gets focus.
     */
    @Override
    protected void onResume() {
        super.onResume();
        // Retrieve an instance of the NfcAdapter ("connection" to the NFC system service):
        NfcManager nfcManager = (NfcManager) this.getSystemService(Context.NFC_SERVICE);
        if (Nfc_adapter != null) {
            Nfc_adapter = nfcManager.getDefaultAdapter();
        }
        if (Nfc_adapter != null) {
            // The isEnabled() method, if invoked directly after the NFC service
            // crashed, returns false regardless of the real setting (Android 2.3.3+).
            // As a nice side-effect it re-establishes the link to the correct instance
            // of NfcAdapter. Therefore, just execute this method twice whenever we
            // re-request the NfcAdapter, so we can be sure to have a valid handle.
            try {
                Nfc_adapter.isEnabled();
            } catch (NullPointerException e) {
                // Drop NullPointerException that is sometimes thrown
                // when NFC service crashed
            }
            try {
                Nfc_adapter.isEnabled();
            } catch (NullPointerException e) {
                // Drop NullPointerException that is sometimes thrown
                // when NFC service crashed
            }

        }
        nfcManager.
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
    @Override
    private void onNewIntent(Intent data, boolean foregroundDispatch) {
        this.setIntent(data);
        // Resolve the intent that re-invoked us:
        resolveIntent(data, false);
    }

    private void resolveIntent(Intent data, boolean foregroundDispatch) {
        this.setIntent(data);

        String action = data.getAction();

        // We were started from the recent applications history: just show our main activity
        // (otherwise, the last intent that invoked us will be re-processed)
        if ((data.getFlags() & Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY) != 0) { return; }

        // Intent is a tag technology (we are sensitive to Ndef, NdefFormatable) or
        // an NDEF message (we are sensitive to URI records with the URI http://www.mroland.at/)
        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            // The reference to the tag that invoked us is passed as a parameter (intent extra EXTRA_TAG)
            Tag tag = data.getParcelableExtra(NfcAdapter.EXTRA_TAG);
    }

    private View.OnClickListener scan_tag_buttonListener = new View.OnClickListener() {
        public void onClick(View v) {

            try {
                reader0.connect();

                tag0 = reader0.getTag();

                reader0.close();

                tag_info.setText(getTagInfo());

            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    };

    String getTagInfo(){

        byte[] Atqa = reader0.getAtqa();
        StringBuilder result = new StringBuilder();
        result.append("atqa = ");
        result.append(Atqa);

        return result.toString();
    }
}

