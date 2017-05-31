package binar.co.id.busticketingreservationpomaju;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import binar.co.id.busticketingreservationpomaju.util.HttpUtil;

public class DetailTTActivity extends Activity implements OnClickListener{
    private ActionBar actionBar;
    private int white;
    private int black;
    private Drawable shape;

    private LinearLayout yesterdayLayout;
    private LinearLayout tomorrowLayout;
    private LinearLayout backLayout;
    private TextView dateText;

    private TextView title;

    private String trainId;
    private String trainName;
    private String fromStation;
    private String toStation;
    private String fromDate;
    private String fromTime;
    private String toTime;
    private String duration;
    private String startStation;
    private String endStation;
    private String firstLevel;
    private String secondLevel;
    private String thirdLevel;

    private TextView trainNameView;
    private TextView fromStationView;
    private TextView toStationView;
    private TextView fromTimeView;
    private TextView toTimeView;
    private TextView startStationView;
    private TextView endStationView;
    private TextView durationView;
    private RelativeLayout firstLevelLayout;
    private RelativeLayout secondLevelLayout;
    private RelativeLayout thirdLevelLayout;
    private TextView firstLevelNameView;
    private TextView firstLevelCountView;
    private TextView firstLevelPriceView;
    private TextView secondLevelNameView;
    private TextView secondLevelCountView;
    private TextView secondLevelPriceView;
    private TextView thirdLevelNameView;
    private TextView thirdLevelCountView;
    private TextView thirdLevelPriceView;

    private Handler mainHandler;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_ticket);
        initActionBar();
        initViews();
        initBackgroundColor();
//		showDialog();
        mainHandler=new Handler(){
            @Override
            public void handleMessage(Message msg){
                progressDialog.dismiss();
            }
        };
    }

    public void initActionBar(){
        actionBar=this.getActionBar();
        actionBar.setCustomView(R.layout.common_actionbar);
        actionBar.setDisplayShowCustomEnabled(true);

        actionBar.setDisplayShowTitleEnabled(false);
        // 是否显示Activity的图标，如果此Activity无图标，那么就显示应用程序图标
        actionBar.setDisplayShowHomeEnabled(false);
        // 可点击再加一个返回的小箭头
        actionBar.setDisplayHomeAsUpEnabled(false);

        title = (TextView) findViewById(R.id.action_title);
        title.setText("确认订单");
        backLayout = (LinearLayout) findViewById(R.id.back_layout);
        backLayout.setOnClickListener(this);

    }

    public void initViews(){
        Intent intent=this.getIntent();

        fromDate=intent.getStringExtra("fromDate");
        dateText=(TextView)findViewById(R.id.detail_from_date);
        dateText.setText(fromDate);
        yesterdayLayout = (LinearLayout) findViewById(R.id.detail_yesterday_layout);
        tomorrowLayout = (LinearLayout) findViewById(R.id.detail_tomorrow_layout);

        trainName=intent.getStringExtra("trainName");

        trainNameView=(TextView)findViewById(R.id.detail_train_name);
        trainNameView.setText(trainName);

        duration=intent.getStringExtra("duration");
        durationView=(TextView)findViewById(R.id.detail_consume_time);
        durationView.setText(duration);

        fromStation=intent.getStringExtra("fromStation");
        fromStationView=(TextView)findViewById(R.id.detail_from_station);
        fromStationView.setText(fromStation);

        toStation=intent.getStringExtra("toStation");
        toStationView=(TextView)findViewById(R.id.detail_to_station);
        toStationView.setText(toStation);

        fromTime=intent.getStringExtra("fromTime");
        fromTimeView=(TextView)findViewById(R.id.detail_from_time);
        fromTimeView.setText(fromTime);

        toTime=intent.getStringExtra("toTime");
        toTimeView=(TextView)findViewById(R.id.detail_to_time);
        toTimeView.setText(toTime);

        startStation=intent.getStringExtra("startStation");
        startStationView=(TextView)findViewById(R.id.start_station_name);
        startStationView.setText(startStation);

        endStation=intent.getStringExtra("endStation");
        endStationView=(TextView)findViewById(R.id.end_station_name);
        endStationView.setText(endStation);

        firstLevel=intent.getStringExtra("firstLevel");
        secondLevel=intent.getStringExtra("secondLevel");
        thirdLevel=intent.getStringExtra("thirdLevel");

        String[] firstLevelArray=firstLevel.split(":");
        String[] secondLevelArray=secondLevel.split(":");
        String[] thirdLevelArray=thirdLevel.split(":");

        firstLevelLayout=(RelativeLayout)findViewById(R.id.detail_first_level);
        firstLevelLayout.setOnClickListener(this);
        secondLevelLayout=(RelativeLayout)findViewById(R.id.detail_second_level);
        secondLevelLayout.setOnClickListener(this);
        thirdLevelLayout=(RelativeLayout)findViewById(R.id.detail_third_level);
        thirdLevelLayout.setOnClickListener(this);
        firstLevelNameView=(TextView)findViewById(R.id.detail_first_level_name);
        firstLevelNameView.setText(firstLevelArray[0]);
        firstLevelCountView=(TextView)findViewById(R.id.detail_first_level_count);
        firstLevelCountView.setText(firstLevelArray[1]);
        firstLevelPriceView=(TextView)findViewById(R.id.detail_first_level_price);
        firstLevelPriceView.setText(intent.getStringExtra("firstLevelPrice"));

        secondLevelNameView=(TextView)findViewById(R.id.detail_second_level_name);
        secondLevelNameView.setText(secondLevelArray[0]);
        secondLevelCountView=(TextView)findViewById(R.id.detail_second_level_count);
        secondLevelCountView.setText(secondLevelArray[1]);
        secondLevelPriceView=(TextView)findViewById(R.id.detail_second_level_price);
        secondLevelPriceView.setText(intent.getStringExtra("secondLevelPrice"));

        thirdLevelNameView=(TextView)findViewById(R.id.detail_third_level_name);
        thirdLevelNameView.setText(thirdLevelArray[0]);
        thirdLevelCountView=(TextView)findViewById(R.id.detail_third_level_count);
        thirdLevelCountView.setText(thirdLevelArray[1]);
        thirdLevelPriceView=(TextView)findViewById(R.id.detail_third_level_price);
        thirdLevelPriceView.setText(intent.getStringExtra("thirdLevelPrice"));

        trainId=intent.getStringExtra("trainId");

    }
    @SuppressWarnings("deprecation")
    public void initBackgroundColor(){
        white=getResources().getColor(R.color.white);
        black=getResources().getColor(R.color.black);
        shape=getResources().getDrawable(R.drawable.layout);
        thirdLevelLayout.setBackgroundDrawable(shape);
        thirdLevelNameView.setTextColor(white);
        thirdLevelCountView.setTextColor(white);
        thirdLevelPriceView.setTextColor(white);
    }

    @Override
    public void onClick(View view){
        switch(view.getId()){
            case R.id.detail_first_level:
                restoreDefault();
                firstLevelLayout.setBackgroundDrawable(shape);
                firstLevelNameView.setTextColor(white);
                firstLevelCountView.setTextColor(white);
                firstLevelPriceView.setTextColor(white);

                break;
            case R.id.detail_second_level:
                restoreDefault();
                secondLevelLayout.setBackgroundDrawable(shape);
                secondLevelNameView.setTextColor(white);
                secondLevelCountView.setTextColor(white);
                secondLevelPriceView.setTextColor(white);
                break;
            case R.id.detail_third_level:
                restoreDefault();
                thirdLevelLayout.setBackgroundDrawable(shape);
                thirdLevelNameView.setTextColor(white);
                thirdLevelCountView.setTextColor(white);
                thirdLevelPriceView.setTextColor(white);
                break;
            case R.id.back_layout:
                backLayout.setBackgroundColor(getResources().getColor(
                        R.color.blue_dark));
                finish();
                break;
            default:break;
        }
    }


    public void restoreDefault(){
        firstLevelLayout.setBackgroundColor(white);
        firstLevelNameView.setTextColor(black);
        firstLevelCountView.setTextColor(black);
        firstLevelPriceView.setTextColor(black);
        secondLevelLayout.setBackgroundColor(white);
        secondLevelNameView.setTextColor(black);
        secondLevelCountView.setTextColor(black);
        secondLevelPriceView.setTextColor(black);
        thirdLevelLayout.setBackgroundColor(white);
        thirdLevelNameView.setTextColor(black);
        thirdLevelCountView.setTextColor(black);
        thirdLevelPriceView.setTextColor(black);
    }
    public void showDialog(){
        progressDialog = new ProgressDialog(DetailTTActivity.this,AlertDialog.THEME_TRADITIONAL);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("加载中...");
        progressDialog.setIndeterminate(false);
        progressDialog.show();
        new Thread(new DetailTTThread()).start();
    }
    class DetailTTThread implements Runnable {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            Looper.prepare();
            Message message = new Message();

            Bundle data = new Bundle();

            JSONObject jsonObj = null;

            try {
                jsonObj = query(trainId);
                data.putString("startStation", jsonObj.getString("startStation"));
                data.putString("endStation", jsonObj.getString("endStation"));
                data.putString("firstLevelPrice", jsonObj.getString("firstLevelPrice"));
                data.putString("secondLevelPrice", jsonObj.getString("secondLevelPrice"));
                data.putString("thirdLevelPrice", jsonObj.getString("thirdLevelPrice"));


                message.setData(data);
                message.what = 1;
                mainHandler.sendMessage(message);

            } catch (Exception e) {
                Toast.makeText(DetailTTActivity.this, "服务器异常",Toast.LENGTH_SHORT).show();
                message.what = 0;
                mainHandler.sendMessage(message);
                e.printStackTrace();
            }

            Looper.loop();

        }

        public JSONObject query(String trainId) throws Exception {
            Map<String, String> map = new HashMap<>();
            map.put("trainId", trainId);

            String url = HttpUtil.BASE_URL + "detail_ticket.jsp";
            return new JSONObject(HttpUtil.postRequest(url, map));
        }

    }

}