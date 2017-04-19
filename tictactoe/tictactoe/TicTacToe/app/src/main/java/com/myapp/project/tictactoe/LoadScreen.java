package com.myapp.project.tictactoe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class LoadScreen extends Activity {
    private ArrayList<File> fileArrayList;

    /**
     * Load screen activity when saving a game
     * @param savedInstanceState Instance of activity to load
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_load_screen);
        final ListView lv = (ListView) findViewById(R.id.saveList);
        File[] files = getFilesDir().listFiles();
        fileArrayList = new ArrayList<>(Arrays.asList(files));
        saveArrayAdapter adapter = new saveArrayAdapter(this,0,fileArrayList);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String ret = readFile(fileArrayList.get(i));
                Intent intent = new Intent(getApplicationContext(),GameActivity.class);
                intent.putExtra("data",ret);
                startActivity(intent);
            }
        });
    }

    /**
     * File reader method
     * @param file File to read
     * @return Content of file read
     */
    private String readFile(File file){
        String content = null;
        try {
            FileInputStream inputStream = openFileInput(file.getName());

            byte[] readByte = new byte[inputStream.available()];

            while(inputStream.read(readByte) != -1){
                content = new String(readByte);
            }
            inputStream.close();
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }
        return content;
    }

}
