package binar.co.id.busticketingreservationpomaju;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;
import android.widget.Toast;
import binar.co.id.busticketingreservationpomaju.util.HttpUtil;

public class SelectStationActivity extends Activity {

    private SearchView search;
    private ListView listView;

    private String stationName;
    private ArrayAdapter<String> adapter;
    private static Handler mainHandler;
    private ProgressDialog progressDialog;
    private ActionBar actionBar;
    private LinearLayout  backLayout;
    private TextView title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_station);

        initActionBar();
        initView();
        mainHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Bundle data = msg.getData();
                ArrayList<String>list=data.getStringArrayList("data");
                adapter=new ArrayAdapter<String>(SelectStationActivity.this,R.layout.list_station,R.id.result_station,list);
                listView.setAdapter(adapter);
                progressDialog.dismiss();
                super.handleMessage(msg);
            }
        };

    }

    public void initView() {
        listView = (ListView) findViewById(R.id.list_station);
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent();
                String name = (String)  parent.getItemAtPosition(position);
                intent.putExtra("stationName", name);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
        search = (SearchView) findViewById(R.id.search);
        search.setOnQueryTextListener(new OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.equals("")){
                    stationName="";
                }
                else{
                    stationName = newText + "%" ;
                    progressDialog=new ProgressDialog(SelectStationActivity.this,AlertDialog.THEME_TRADITIONAL);
                    progressDialog.setCancelable(false);
                    progressDialog.setMessage("Sedang memuat...");
                    progressDialog.setIndeterminate(false);
                    progressDialog.show();
                    Thread newThread = new Thread(new StationDatabaseThread());
                    newThread.start();
                }
                return true;
            }
        });
    }

    public void initActionBar(){
        actionBar=getActionBar();
        actionBar.setCustomView(R.layout.common_actionbar);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);

        title=(TextView)findViewById(R.id.action_title);
        title.setText("Pilih Tujuan");
        backLayout=(LinearLayout)findViewById(R.id.back_layout);
        backLayout.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                backLayout.setBackgroundColor(getResources().getColor(R.color.blue_dark));
                SelectStationActivity.this.onBackPressed();
            }
        });
    }


    class StationDatabaseThread implements Runnable {

        @Override
        public void run() {
            Looper.prepare();
            Message message = new Message();
            Bundle data = new Bundle();

            JSONArray jsonArray=null;
            ArrayList<String> list=new ArrayList<>();;
            try{
                jsonArray=query(stationName);
                if(jsonArray==null){
                    Log.d("jsonArray", "is null");
                }
                System.out.println(jsonArray.toString());
                if(jsonArray.length()>1){
                    for(int i=0;i<jsonArray.length()-1;i++){
                        list.add(jsonArray.getString(i));
                    }
                }
            }catch(Exception e){
                Toast.makeText(SelectStationActivity.this,"Server eror",Toast.LENGTH_SHORT).show();;
                e.printStackTrace();
            }finally{
                data.putStringArrayList("data", list);
                message.setData(data);
                mainHandler.sendMessage(message);
                Looper.loop();
            }
        }

        public JSONArray query(String stationName)throws Exception{
            Map<String,String>map=new HashMap<>();
            map.put("stationName", stationName);
            String url = HttpUtil.BASE_URL + "query_station_name.jsp";
            return new JSONArray(HttpUtil.postRequest(url, map));
        }
    }

    public void onDestroy() {
        super.onDestroy();
    }
}