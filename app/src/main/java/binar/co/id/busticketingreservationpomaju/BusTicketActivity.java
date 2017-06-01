package binar.co.id.busticketingreservationpomaju;

        import java.io.Serializable;
        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.List;
        import java.util.Map;

        import org.json.JSONArray;
        import org.json.JSONObject;

        import android.app.ActionBar;
        import android.app.Activity;
        import android.app.AlertDialog;
        import android.app.ProgressDialog;
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
        import android.widget.LinearLayout;
        import android.widget.ListView;
        import android.widget.SimpleAdapter;
        import android.widget.TextView;
        import android.widget.Toast;
        import edu.ucas.bookingticket.util.HttpUtil;

public class TrainTicketActivity extends Activity {

    private ActionBar actionBar;
    private LinearLayout yesterdayLayout;
    private LinearLayout tomorrowLayout;
    private LinearLayout backLayout;
    private TextView dateText;
    private ListView listView;
    private SimpleAdapter adapter;
    private TextView title;

    private Intent intent;
    private String fromStation;
    private String toStation;
    private String fromDate;
    private String isSaled="false";

    private Handler mainHandler;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.train_ticket);

        intent = this.getIntent();
        fromStation = intent.getStringExtra("fromStation");
        toStation = intent.getStringExtra("toStation");
        fromDate = intent.getStringExtra("fromDate");

        initActionBar();
        initView();
        select();

        mainHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                if (msg.what == 1) {
                    Bundle data = msg.getData();
                    @SuppressWarnings("unchecked")
                    List<Map<String, String>> listMap = (List<Map<String, String>>) data
                            .getSerializable("data");

                    adapter = new SimpleAdapter(TrainTicketActivity.this,listMap, R.layout.list_train_ticket,
                            new String[] { "trainName", "isStartStation","fromStation","durationTime","isEndStation", "toStation", "fromTime",
                                    "toTime", "firstLevel", "secondLevel","thirdLevel" },
                            new int[] {R.id.train_name,R.id.is_start, R.id.from_station,R.id.consume_time, R.id.is_end,R.id.to_station,
                                    R.id.from_station_time,R.id.to_station_time, R.id.first_level,R.id.second_level, R.id.third_level });

                    listView.setAdapter(adapter);
                }
                progressDialog.dismiss();
                super.handleMessage(msg);
            }
        };

        listView.setOnItemClickListener(new MyItemListener());

    }

    public void initActionBar() {
        actionBar = getActionBar();
        actionBar.setCustomView(R.layout.common_actionbar);
        actionBar.setDisplayShowCustomEnabled(true);

        actionBar.setDisplayShowTitleEnabled(false);
        // 是否显示Activity的图标，如果此Activity无图标，那么就显示应用程序图标
        actionBar.setDisplayShowHomeEnabled(false);
        // 可点击再加一个返回的小箭头
        actionBar.setDisplayHomeAsUpEnabled(false);

        title = (TextView) findViewById(R.id.action_title);
        title.setText(fromStation + "<>" + toStation);
        backLayout = (LinearLayout) findViewById(R.id.back_layout);
        backLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                backLayout.setBackgroundColor(getResources().getColor(
                        R.color.blue_dark));
                finish();
            }
        });
    }

    public void initView() {

        listView = (ListView) findViewById(R.id.train_ticket_list);

        dateText = (TextView) findViewById(R.id.train_from_time);
        dateText.setText(fromDate);
        yesterdayLayout = (LinearLayout) findViewById(R.id.yesterday_layout);
        tomorrowLayout = (LinearLayout) findViewById(R.id.tomorrow_layout);

    }

    public void select() {
        progressDialog = new ProgressDialog(TrainTicketActivity.this,
                AlertDialog.THEME_TRADITIONAL);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("加载中...");
        progressDialog.setIndeterminate(false);
        progressDialog.show();
        new Thread(new TrainTicketThread()).start();
    }

    class MyItemListener implements OnItemClickListener{

        public void onItemClick(AdapterView<?> parent, View view, int position, long id){
            Intent intent=new Intent(TrainTicketActivity.this,DetailTTActivity.class);
            @SuppressWarnings("unchecked")
            HashMap<String,String>map=(HashMap<String, String>) parent.getItemAtPosition(position);
            Bundle extras=new Bundle ();
            extras.putString("trainId", map.get("trainId"));
            extras.putString("trainName", map.get("trainName"));
            extras.putString("fromStation", map.get("fromStation"));
            extras.putString("fromTime", map.get("fromTime"));
            extras.putString("toStation", map.get("toStation"));
            extras.putString("toTime", map.get("toTime"));
            extras.putString("duration", map.get("durationTime"));

            extras.putString("firstLevel", map.get("firstLevel"));
            extras.putString("secondLevel", map.get("secondLevel"));
            extras.putString("thirdLevel", map.get("thirdLevel"));

            extras.putString("fromDate", fromDate);
            extras.putString("startStation",map.get("startStation"));
            extras.putString("endStation", map.get("endStation"));

            extras.putString("firstLevelPrice","￥"+map.get("firstLevelPrice"));
            extras.putString("secondLevelPrice", "￥"+map.get("secondLevelPrice"));
            extras.putString("thirdLevelPrice", "￥"+map.get("thirdLevelPrice"));

            intent.putExtras(extras);

            startActivity(intent);
        }
    }

    class TrainTicketThread implements Runnable {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            Looper.prepare();
            Message message = new Message();

            Bundle data = new Bundle();

            JSONArray jsonArray = null;
            List<Map<String, String>> list = null;
            try {
                jsonArray = query(fromDate, fromStation, toStation);
                list = convertJSONToList(jsonArray);
                data.putSerializable("data", (Serializable) list);
                message.setData(data);
                message.what = 1;
                mainHandler.sendMessage(message);

            } catch (Exception e) {
                Toast.makeText(TrainTicketActivity.this, "服务器异常",
                        Toast.LENGTH_SHORT).show();
                message.what = 0;
                mainHandler.sendMessage(message);
                e.printStackTrace();
            }

            Looper.loop();

        }

        public JSONArray query(String fromDate, String fromStation,String toStation) throws Exception {
            Map<String, String> map = new HashMap<>();
            map.put("fromDate", fromDate);
            map.put("fromStation", fromStation);
            map.put("toStation", toStation);
            String url = HttpUtil.BASE_URL + "query_train_ticket.jsp";
            return new JSONArray(HttpUtil.postRequest(url, map));
        }

        public List<Map<String, String>> convertJSONToList(JSONArray jsonArray)
                throws Exception {
            List<Map<String, String>> simpleData = new ArrayList<>();
            JSONObject jsonObj = null;
            if (jsonArray.length() > 1) {
                for (int i = 0; i < jsonArray.length() - 1; i++) {
                    Map<String, String> map = new HashMap<>();
                    jsonObj = jsonArray.getJSONObject(i);

                    String trainIdValue=jsonObj.getString("trainId");
                    map.put("trainId", trainIdValue);
                    String trainNameValue = jsonObj.getString("trainName");
                    map.put("trainName", trainNameValue);
                    String fromStationValue = jsonObj.getString("fromStation");
                    map.put("fromStation", fromStationValue);
                    String toStationValue = jsonObj.getString("toStation");
                    map.put("toStation", toStationValue);
                    String fromTimeValue = jsonObj.getString("fromTime");
                    map.put("fromTime", fromTimeValue);
                    String toTimeValue = jsonObj.getString("toTime");
                    map.put("toTime", toTimeValue);
                    String durationTimeValue = jsonObj.getString("durationTime");
                    map.put("durationTime", durationTimeValue);

                    String firstLevelPriceValue=jsonObj.getString("firstLevelPrice");
                    map.put("firstLevelPrice", firstLevelPriceValue);
                    String secondLevelPriceValue=jsonObj.getString("secondLevelPrice");
                    map.put("secondLevelPrice", secondLevelPriceValue);
                    String thirdLevelPriceValue=jsonObj.getString("thirdLevelPrice");
                    map.put("thirdLevelPrice", thirdLevelPriceValue);

                    String startStationValue=jsonObj.getString("startStation");
                    map.put("startStation", startStationValue);
                    String endStationValue=jsonObj.getString("endStation");
                    map.put("endStation", endStationValue);

                    String isStartStationValue = jsonObj.getString("isStartStation");
                    if(isStartStationValue.equals("true")){
                        map.put("isStartStation", "始");
                    }
                    else if(isStartStationValue.equals("false")){
                        map.put("isStartStation", "过");
                    }

                    String isEndStationValue = jsonObj.getString("isEndStation");
                    if(isEndStationValue.equals("true")){
                        map.put("isEndStation", "终");
                    }
                    else if(isEndStationValue.equals("false")){
                        map.put("isEndStation", "过");
                    }
                    if (trainNameValue.startsWith("G")
                            || trainNameValue.startsWith("D")) {
                        String shangWu = jsonObj.getString("商务:");
                        map.put("firstLevel", "商务:"+shangWu);
                        String yiDeng = jsonObj.getString("一等:");
                        map.put("secondLevel", "一等:"+yiDeng);
                        String erDeng = jsonObj.getString("二等:");
                        map.put("thirdLevel", "二等:"+erDeng);
                    } else {
                        String ruanWo = jsonObj.getString("软卧:");
                        map.put("firstLevel", "软卧:"+ruanWo);
                        String yingWo = jsonObj.getString("硬卧:");
                        map.put("secondLevel", "硬卧:"+yingWo);
                        String yingZuo = jsonObj.getString("硬座:");
                        map.put("thirdLevel", "硬座:"+yingZuo);

                        // String wuZuo=jsonObj.getString("无座");
                        // map.put("fourthLevel", wuZuo);
                    }
                    simpleData.add(map);
                }
            }

            return simpleData;
        }

    }

}
    Contact GitHub API Training Shop Blog About
        © 2017 GitHub, Inc. Terms Privacy Sec