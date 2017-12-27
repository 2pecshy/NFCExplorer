package com.lolo.nfcexplorer.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.lolo.nfcexplorer.R;

/**
 * Created by lolo on 27/12/17.
 */

public class tag_emu_activity extends Activity {

    ListView tag_info_view;
    ArrayAdapter<String> content_tag_info_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        tag_info_view = (ListView) findViewById(R.id.list_info_View);

        content_tag_info_view = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item);

        content_tag_info_view.add("Tag Emulation");

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
}
