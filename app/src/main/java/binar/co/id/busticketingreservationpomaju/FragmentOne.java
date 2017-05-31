package binar.co.id.busticketingreservationpomaju;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

public class FragmentOne extends Fragment {


    private TextView sourceStation;
    private TextView destinationStation;
    private ImageView exchange;
    private TextView startDate;
    private TextView startTime;
    private TextView seatLevel;
    private CheckBox isStudent;
    private CheckBox all;
    private CheckBox GDC;
    private CheckBox Z;
    private CheckBox T;
    private CheckBox K;
    private CheckBox QT;
    private Button select;

    private final int sourceCode = 0;
    private final int destinationCode = 1;
    private final int dateCode = 3;
    private final int timeCode = 4;
    private final int seatLevelCode = 5;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View globalView = inflater.inflate(R.layout.fragment_one, null,false);
        initView(globalView);
        return globalView;
    }

    public void initView(View globalView) {


        sourceStation = (TextView) globalView.findViewById(R.id.source_station);
        sourceStation.setOnClickListener(new SelectStation(sourceCode));

        destinationStation = (TextView) globalView.findViewById(R.id.destination_station);
        destinationStation.setOnClickListener(new SelectStation(destinationCode));

        exchange = (ImageView) globalView.findViewById(R.id.exchange);
        exchange.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                String ex = destinationStation.getText().toString();
                destinationStation.setText(sourceStation.getText().toString());
                sourceStation.setText(ex);
            }
        });

        startDate = (TextView) globalView.findViewById(R.id.start_date);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
        startDate.setText(dateFormat.format(Calendar.getInstance(Locale.getDefault()).getTime()));
        startDate.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(FragmentOne.this.getActivity(),SelectDateActivity.class);
                startActivityForResult(intent, dateCode);
            }
        });

        startTime = (TextView) globalView.findViewById(R.id.start_time);
        startTime.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                final String[] items = new String[] { "00:00--24:00","00:00--06:00",
                        "06:00--12:00", "12:00--18:00","18:00--24:00" };
                buildAlertDialog(items, timeCode);
            }
        });
        seatLevel = (TextView) globalView.findViewById(R.id.seat_level);
        seatLevel.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                final String[] items = new String[] { "不限", "商务座", "特等座",
                        "一等座", "二等座", "高级软卧", "软卧", "硬卧", "软座", "硬座" };
                buildAlertDialog(items, seatLevelCode);
            }
        });
        isStudent = (CheckBox) globalView.findViewById(R.id.is_student);

        all = (CheckBox) globalView.findViewById(R.id.all);
        all.setOnCheckedChangeListener(new CheckedChange());
        GDC = (CheckBox) globalView.findViewById(R.id.GDC);
        GDC.setOnCheckedChangeListener(new CheckedChange());
        Z = (CheckBox) globalView.findViewById(R.id.Z);
        Z.setOnCheckedChangeListener(new CheckedChange());
        T = (CheckBox) globalView.findViewById(R.id.T);
        T.setOnCheckedChangeListener(new CheckedChange());
        K = (CheckBox) globalView.findViewById(R.id.K);
        K.setOnCheckedChangeListener(new CheckedChange());
        QT = (CheckBox) globalView.findViewById(R.id.QT);
        QT.setOnCheckedChangeListener(new CheckedChange());
        select = (Button) globalView.findViewById(R.id.select_button);
        select.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent=new Intent(FragmentOne.this.getActivity(),TrainTicketActivity.class);

                intent.putExtra("fromDate", startDate.getText().toString());
                intent.putExtra("fromStation",sourceStation.getText().toString() );
                intent.putExtra("toStation", destinationStation.getText().toString());
                startActivity(intent);
            }
        });

    }


    class CheckedChange implements OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(CompoundButton buttonView,
                                     boolean isChecked) {
            // TODO Auto-generated method stub
            if(!isChecked){
                if(!GDC.isChecked()&&!Z.isChecked()&&!T.isChecked()&&!K.isChecked()&&!QT.isChecked()){
                    all.setChecked(true);
                    all.setTextColor(Color.WHITE);
                }
                buttonView.setTextColor(Color.BLACK);
            }else if(isChecked){
                if(buttonView.getId()==all.getId()){
                    GDC.setChecked(false);
                    Z.setChecked(false);
                    T.setChecked(false);
                    K.setChecked(false);
                    QT.setChecked(false);
                }else{
                    all.setChecked(false);
                    all.setTextColor(Color.BLACK);
                }
                buttonView.setTextColor(Color.WHITE);
            }
        }
    }

    public void buildAlertDialog(final String[] items, final int who) {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                FragmentOne.this.getActivity());

        int checkedItem = 0;
        for (int i = 0; i < items.length; i++) {
            if (who == timeCode) {
                if (items[i].equals(startTime.getText().toString())) {
                    checkedItem = i;
                }
            } else if (who == seatLevelCode) {
                if (items[i].equals(seatLevel.getText().toString())) {
                    checkedItem = i;
                }
            }
        }
        builder.setSingleChoiceItems(items, checkedItem,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        if (who == timeCode) {
                            startTime.setText(items[which]);
                        } else if (who == seatLevelCode) {
                            seatLevel.setText(items[which]);
                        }
                        dialog.dismiss();
                    }
                });
        builder.setNegativeButton("取消", null);
        builder.create().show();
    }

    class SelectStation implements OnClickListener {
        int requestCode;
        SelectStation() {
        }
        SelectStation(int requestCode) {
            this.requestCode = requestCode;
        }

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            Intent intent = new Intent(FragmentOne.this.getActivity(),
                    SelectStationActivity.class);
            startActivityForResult(intent, requestCode);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case sourceCode:
                if (resultCode == Activity.RESULT_OK) {
                    sourceStation.setText(data.getStringExtra("stationName"));
                }
                break;
            case destinationCode:
                if (resultCode == Activity.RESULT_OK) {
                    destinationStation.setText(data.getStringExtra("stationName"));
                }
                break;
            case dateCode:
                if(resultCode == Activity.RESULT_OK){
                    startDate.setText(data.getStringExtra("date"));
                }
            default:
                break;
        }
    }
}
