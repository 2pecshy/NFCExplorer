package com.lolo.nfcexplorer;


import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcA;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.io.IOException;
import java.util.ArrayList;

public class main_activity extends Activity {

    NfcA reader0;
    Tag tag0;
    TextView uid_info;
    TextView atqa_info;
    TextView sak_info;
    TextView nbRecord_info;
    NfcAdapter Nfc_adapter;
    NdefMessage ndef_file;
    Button scan_tag_button;
    PendingIntent mPendingIntent;
    Parcelable[] rawMsgs;
    NdefMessage[] msgs;
    ListView tag_info_view;
    ArrayAdapter<String> content_tag_info_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m);

        scan_tag_button = (Button) findViewById(R.id.button_scan_tag);
        scan_tag_button.setOnClickListener(scan_tag_buttonListener);
        uid_info = (TextView) findViewById(R.id.uid_info);
        atqa_info = (TextView) findViewById(R.id.atqa_info);
        sak_info = (TextView) findViewById(R.id.sak_info);
        nbRecord_info = (TextView) findViewById(R.id.nbRecord_info);
        tag_info_view = (ListView) findViewById(R.id.list_info_View);

        content_tag_info_view = new ArrayAdapter<String>();

        content_tag_info_view.add("UID: waiting for tag...");
        content_tag_info_view.add("ATQA:");
        content_tag_info_view.add("SAK:");
        content_tag_info_view.add("number of records:");

        resolveIntent(getIntent());
        tag_info_view.setAdapter(content_tag_info_view);

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
    protected void onPause() {
        super.onPause();
        if (Nfc_adapter != null) {
            Nfc_adapter.disableForegroundDispatch(this);
        }
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
            Log.d("NFCExplorer_debug: ","===========getTagInfo===========");
            // The reference to the tag that invoked us is passed as a parameter (intent extra EXTRA_TAG)
            tag0 = data.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            rawMsgs = data.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
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
            uid_info.setText("No Tag...");
        }
        else {
            try {

                reader0 = NfcA.get(tag0);
                reader0.connect();

                tag0 = reader0.getTag();

                reader0.close();
                uid_info.setText("UID: " + printByteArray(tag0.getId()));
                atqa_info.setText("ATQA: " +  printByteArray(reader0.getAtqa()));
                sak_info.setText("SAK :" + String.format("%02x",reader0.getSak()));
                if (rawMsgs != null){
                    msgs = new NdefMessage[rawMsgs.length];
                    for (int i = 0; i < rawMsgs.length; i++) {
                        msgs[i] = (NdefMessage) rawMsgs[i];
                    }
                    nbRecord_info.setText("number of records: " + String.format("%d",msgs[0].getRecords().length));
                }



            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    @Override
    public void onNewIntent(Intent intent) {
        setIntent(intent);
        resolveIntent(intent);
    }

    private String printByteArray(byte[] uid){

        StringBuilder res = new StringBuilder();
        for(int i = 0; i < uid.length; i++)
            res.append( String.format("%02x",uid[i]));
        return res.toString();
    }

}

