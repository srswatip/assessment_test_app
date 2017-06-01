package binar.co.id.busticketingreservationpomaju;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class UserInfoActivity extends Activity{

    private LinearLayout contacts;
    private LinearLayout personal;
    private LinearLayout checkPhone;
    private LinearLayout modifyPassword;
    private LinearLayout modifyMail;
    private Button loginout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_info);
        Intent intent=getIntent();
        this.setTitle(intent.getStringExtra("userName"));
        initView();
    }
    public void initView(){
        contacts=(LinearLayout)findViewById(R.id.user_info_first_layout);
        personal=(LinearLayout)findViewById(R.id.user_info_third_layout);
        checkPhone=(LinearLayout)findViewById(R.id.user_info_fifth_layout);
        modifyPassword=(LinearLayout)findViewById(R.id.user_info_seventh_layout);
        modifyMail=(LinearLayout)findViewById(R.id.user_info_ninth_layout);
        loginout=(Button)findViewById(R.id.user_info_loginout_button);
        loginout.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(UserInfoActivity.this,MainActivity.class);
                intent.putExtra("loginout", "yes");
                startActivity(intent);

            }

        });
    }

}