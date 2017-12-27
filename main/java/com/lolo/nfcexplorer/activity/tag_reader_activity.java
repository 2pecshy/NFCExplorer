package com.lolo.nfcexplorer.activity;


import android.nfc.cardemulation.HostApduService;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.lolo.nfcexplorer.R;
import com.lolo.nfcexplorer.nfc_service.CardService;

public class tag_reader_activity extends Activity_NFC {

    byte[] uid_info;
    byte[] atqa_info;
    short sak_info;
    int nbRecord_info;

    Button scan_tag_button;
    ListView tag_info_view;
    ArrayAdapter<String> content_tag_info_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m);

        scan_tag_button = (Button) findViewById(R.id.button_scan_tag);
        scan_tag_button.setOnClickListener(scan_tag_buttonListener);
        tag_info_view = (ListView) findViewById(R.id.list_info_View);

        content_tag_info_view = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item);

        content_tag_info_view.add("UID: waiting for tag...");
        content_tag_info_view.add("ATQA:");
        content_tag_info_view.add("SAK:");
        content_tag_info_view.add("number of records:");

        tag_info_view.setAdapter(content_tag_info_view);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private View.OnClickListener scan_tag_buttonListener = new View.OnClickListener() {
        public void onClick(View v) {
            content_tag_info_view.clear();
            getTagInfo();

        }
    };

    @Override
    protected void onNewTagSuportedDetected(){
        content_tag_info_view.clear();
        getTagInfo();
    }

    //==================================================================================================================

    private void getTagInfo(){
        content_tag_info_view.clear();
        if(nfc_manager.getLastTag() == null){
            content_tag_info_view.add("UID: waiting for tag...");
            content_tag_info_view.add("ATQA:");
            content_tag_info_view.add("SAK:");
            content_tag_info_view.add("number of records:");
        }
        else {
                uid_info = nfc_manager.getLastTag().getId();
                atqa_info = nfc_manager.getReader().getAtqa();
                sak_info = nfc_manager.getReader().getSak();
                nbRecord_info = nfc_manager.getNdef_file() == null ? 0 : nfc_manager.getNdef_file().length;
                content_tag_info_view.add("UID: " + printByteArray(uid_info));
                content_tag_info_view.add("ATQA: " +  printByteArray(atqa_info));
                content_tag_info_view.add("SAK :" + String.format("%02x",sak_info));
                content_tag_info_view.add("number of records: " + String.format("%d",nbRecord_info));
                int i;
                for(i = 0; i < nbRecord_info; i++){
                    content_tag_info_view.add("record " + i + ":" + nfc_manager.getNdef_file()[0].getRecords()[i].toString());
                }

        }

    }

    private String printByteArray(byte[] uid){

        StringBuilder res = new StringBuilder();
        for(int i = 0; i < uid.length; i++)
            res.append( String.format("%02x",uid[i]));
        return res.toString();
    }

}

