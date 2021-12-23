package com.example.jooye.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private String requestURL="https://api.odcloud.kr/api/15077603/v1/uddi:90bfb316-0caf-495b-92c0-c5cbc7bca1d9?page=1&perPage=50&serviceKey=[YOUR KEY]";
    private TextView textView;
    private ListView listView;
    private ArrayAdapter<String> listAdapter;

    ArrayList<String> centerList = new ArrayList<String>();
    ArrayList<String> addressList = new ArrayList<String>();
    ArrayList<Float> fx = new ArrayList<Float>();
    ArrayList<Float> fy = new ArrayList<Float>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Thread1 thread1 = new Thread1();
        thread1.start();
        textView = findViewById(R.id.textView3);
        textView.setTextColor(Color.RED);
        listView = findViewById(R.id.listView);
        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, centerList);
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                String strText = (String) parent.getItemAtPosition(position) ;
                Intent intent = new Intent(MainActivity.this, MapActivity.class);
                intent.putExtra("fx",fx.get(position));
                intent.putExtra("fy",fy.get(position));
                intent.putExtra("address",addressList.get(position));
                startActivity(intent);
            }
        }) ;
    }
    class Thread1 extends Thread{
        @Override
        public void run(){
            try{
                URL url = new URL(requestURL);
                BufferedReader bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
                StringBuffer buffer = new StringBuffer();
                String line = bf.readLine();
                while (line != null) {
                    buffer.append(line + "\n");
                    line = bf.readLine();
                }
                String result = buffer.toString();
                JSONObject jsonObject = new JSONObject(result);
                JSONArray dataArray = (JSONArray)jsonObject.get("data");

                for(int i=0; i<dataArray.length(); i++) {
                    JSONObject item = (JSONObject) dataArray.get(i);
                    String address = item.get("주소").toString();
                    addressList.add(address);
                    try{
                        URL url2 = new URL("https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query="+address+"&X-NCP-APIGW-API-KEY-ID=pci7h12l33&X-NCP-APIGW-API-KEY=5YT3fMgBArzgOBF582LfcNwXQ9Tg06kVD4SRI9HQ");
                        BufferedReader bf2 = new BufferedReader(new InputStreamReader(url2.openStream(), "UTF-8"));
                        StringBuffer buffer2 = new StringBuffer();
                        String line2 = bf2.readLine();
                        while (line2 != null) {
                            buffer2.append(line2 + "\n");
                            line2 = bf2.readLine();
                        }
                        result = buffer2.toString();
                        JSONObject jsonObject2 = new JSONObject(result);
                        JSONArray addressesArray = (JSONArray)jsonObject2.get("addresses");
                        JSONObject point = (JSONObject)addressesArray.get(0);
                        float x = Float.parseFloat(point.get("x").toString());
                        float y = Float.parseFloat(point.get("y").toString());
                        fx.add(x);
                        fy.add(y);
                        centerList.add(item.get("센터명") + "-" + item.get("시설명") + ")");
                        listAdapter.notifyDataSetChanged();
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
