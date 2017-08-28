package com.lolo.nfcexplorer;


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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m);
        Button scan_tag_button = (Button) findViewById(R.id.button_scan_tag);
        scan_tag_button.setOnClickListener(scan_tag_buttonListener);
        tag_info  = (TextView) findViewById(R.id.tag_info);
        reader0 = NfcA.get(tag0);
    }

    /**
     * Called when the activity gets focus.
     */
    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * Called when the activity loses focus.
     */
    @Override
    protected void onPause() {
        super.onPause();
        try {
            reader0.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

