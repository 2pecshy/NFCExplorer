package com.lolo.nfcexplorer.activity;


import android.nfc.cardemulation.HostApduService;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.lolo.nfcexplorer.R;
import com.lolo.nfcexplorer.nfc_service.CardService;

public class tag_reader_activity extends Activity_NFC {

    byte[] uid_info;
    byte[] atqa_info;
    short sak_info;
    int nbRecord_info;

    TextView textView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m);
        textView1 = (TextView) findViewById(R.id.text_tag_info1);
        textView1.setText("");
        nfc_manager.setTv(textView1);
        textView1.setMovementMethod(new ScrollingMovementMethod());

        nfc_manager.showMessage(this,"UID: waiting for tag...",'n');
        nfc_manager.showMessage(this,"ATQA:",'n');
        nfc_manager.showMessage(this,"SAK:",'n');
        nfc_manager.showMessage(this,"number of records:",'n');

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onNewTagSuportedDetected(){
        getTagInfo();
    }

    //==================================================================================================================

    private void getTagInfo(){
        nfc_manager.showMessage(this,"======ISO/IEC 14443-4 Type4======",'n');
        if(nfc_manager.getLastTag() == null){
            nfc_manager.showMessage(this,"UID: waiting for tag...",'n');
            nfc_manager.showMessage(this,"ATQA:",'n');
            nfc_manager.showMessage(this,"SAK:",'n');
            nfc_manager.showMessage(this,"======NFC Forum Standard Info======",'n');
            nfc_manager.showMessage(this,"number of records:",'n');
        }
        else {
                uid_info = nfc_manager.getLastTag().getId();
                atqa_info = nfc_manager.getReader().getAtqa();
                sak_info = nfc_manager.getReader().getSak();
                nbRecord_info = nfc_manager.getNdef_file() == null ? 0 : nfc_manager.getNdef_file()[0].getRecords().length;
                nfc_manager.showMessage(this,"UID: " + printByteArray(uid_info),'n');
                nfc_manager.showMessage(this,"ATQA: " + printByteArray(atqa_info),'n');
                nfc_manager.showMessage(this,"SAK: " + String.format("%02x",sak_info),'n');
                nfc_manager.showMessage(this,"======NFC Forum Standard Info======",'n');
                nfc_manager.showMessage(this,"number of records: " + String.format("%d",nbRecord_info),'n');
                int i;
                for(i = 0; i < nbRecord_info; i++){
                    nfc_manager.showMessage(this,"record " + i + ":" +
                            nfc_manager.getNdef_file()[0].getRecords()[i].toString(),'n');
                }

        }

    }

    private String printByteArray(byte[] uid){

        if(uid == null) return "";
        StringBuilder res = new StringBuilder();
        for(int i = 0; i < uid.length; i++)
            res.append( String.format("%02x",uid[i]));
        return res.toString();
    }

}

