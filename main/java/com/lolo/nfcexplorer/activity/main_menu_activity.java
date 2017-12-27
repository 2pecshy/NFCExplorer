package com.lolo.nfcexplorer.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.lolo.nfcexplorer.R;

/**
 * Created by lolo on 22/12/17.
 */

public class main_menu_activity extends Activity{

    ListView list_view_main_menu;
    ArrayAdapter<String> content_main_menu_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        list_view_main_menu = (ListView) findViewById(R.id.list_info_View);

        content_main_menu_view = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item);

        content_main_menu_view.add("Tag Reader");
        content_main_menu_view.add("Tag Emulation");
        list_view_main_menu.setAdapter(content_main_menu_view);

        list_view_main_menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object o = list_view_main_menu.getItemAtPosition(position);
                String str = (String)o;
                if(str.equals("Tag Reader")){
                    startTagReaderActivity();
                }
                else if(str.equals("Tag Emulation")){
                    startTagEmulationActivity();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void startTagReaderActivity(){
        Intent intent = new Intent(this, tag_reader_activity.class);
        startActivity(intent);
    }

   public void startTagEmulationActivity(){
        Intent intent = new Intent(this, tag_emu_activity.class);
        startActivity(intent);
    }

}
