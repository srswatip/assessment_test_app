package binar.co.id.busticketingreservationpomaju;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;

public class SelectDateActivity extends Activity {

    CalendarView calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_date);
        initView();
    }

    public void initView() {
        calendar = (CalendarView) findViewById(R.id.calendar);
        calendar.setOnDateChangeListener(new OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year,
                                            int month, int dayOfMonth) {
                String date;
                String now;
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                now = dateFormat.format(Calendar.getInstance(Locale.getDefault()).getTime());
                int myMonth = month + 1;
                if (myMonth < 10) {
                    if (dayOfMonth < 10) {
                        date = year + "-0" + myMonth + "-0" + dayOfMonth;
                    } else {
                        date = year + "-0" + myMonth + "-" + dayOfMonth;
                    }
                } else {
                    if (dayOfMonth < 10) {
                        date = year + "-" + myMonth + "-0" + dayOfMonth;
                    } else {
                        date = year + "-" + myMonth + "-" + dayOfMonth;
                    }
                }
                if(date.compareTo(now)>=0){
                    Intent intent = new Intent();
                    intent.putExtra("date", date);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }}
        });
    }

}