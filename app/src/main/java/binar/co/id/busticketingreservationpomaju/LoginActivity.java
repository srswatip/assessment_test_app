package binar.co.id.busticketingreservationpomaju;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;


import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import binar.co.id.busticketingreservationpomaju.util.HttpUtil;

public class LoginActivity extends Activity {

    private ActionBar actionBar;
    private LinearLayout backLayout;
    private TextView title;
    private static EditText userNameText;
    private EditText passwordText;
    private Button registerButton;
    private Button loginButton;
    private CheckBox remPasswd;
    private CheckBox autoLogin;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private MyHandler mainHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        preferences = getSharedPreferences("BookTicket_login", MODE_PRIVATE);
        editor = preferences.edit();
        initActionBar();
        initView();
        mainHandler = new MyHandler(LoginActivity.this);
    }

    public void initView() {
        userNameText = (EditText) findViewById(R.id.login_user_name_text);
        userNameText.setText(preferences.getString("userName", null));

        boolean isChecked = preferences.getBoolean("remPasswd", false);

        passwordText = (EditText) findViewById(R.id.login_password_text);
        if (isChecked) {
            passwordText.setText(preferences.getString("password", null));
        }

        remPasswd = (CheckBox) findViewById(R.id.remember_password);
        remPasswd.setChecked(isChecked);

        autoLogin = (CheckBox) findViewById(R.id.auto_login);

        registerButton = (Button) findViewById(R.id.login_register_button);
        loginButton = (Button) findViewById(R.id.login_login_button);

        registerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,
                        RegisterActivity.class);
                startActivity(intent);
            }
        });
        loginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userNameText.getText().toString().isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            LoginActivity.this);
                    builder.setTitle("Info").setMessage("UserName tidak boleh kosong！");
                    builder.setPositiveButton("确定", null);
                    builder.create().show();
                    userNameText.requestFocus();
                } else if (passwordText.getText().toString().isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            LoginActivity.this);
                    builder.setTitle("Info").setMessage("Password tidak boleh kosong！");
                    builder.setPositiveButton("oke", null);
                    builder.create().show();
                    passwordText.requestFocus();
                } else {
                    progressDialog = new ProgressDialog(LoginActivity.this,
                            AlertDialog.THEME_TRADITIONAL);
                    progressDialog.setCancelable(false);
                    progressDialog.setMessage("sedang memuat...");
                    progressDialog.setIndeterminate(false);
                    progressDialog.show();
                    editor.putString("userName", userNameText.getText()
                            .toString());
                    editor.putBoolean("remPasswd", remPasswd.isChecked());
                    if (remPasswd.isChecked()) {
                        editor.putString("password", passwordText.getText()
                                .toString());
                    } else {
                        editor.putString("password", null);
                    }
                    editor.commit();

                    new Thread(new VisitedDB()).start();
                }
            }
        });

    }

    public void initActionBar() {
        actionBar = getActionBar();
        actionBar.setCustomView(R.layout.common_actionbar);
        actionBar.setDisplayShowCustomEnabled(true);

        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);

        title = (TextView) findViewById(R.id.action_title);
        title.setText("Login");
        backLayout = (LinearLayout) findViewById(R.id.back_layout);
        backLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                backLayout.setBackgroundColor(getResources().getColor(
                        R.color.blue_dark));
                LoginActivity.this.onBackPressed();
            }
        });
    }

    public void onDestory() {
        mainHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    static class MyHandler extends Handler {
        private WeakReference<LoginActivity> mLogin;

        MyHandler(LoginActivity login) {
            mLogin = new WeakReference<LoginActivity>(login);
        }

        public void handleMessage(Message msg) {
            LoginActivity loginActivity = mLogin.get();
            if (msg.what == 1) {
                Intent intent = new Intent(loginActivity, MainActivity.class);
                intent.putExtra("userName", userNameText.getText().toString());
                loginActivity.startActivity(intent);
                progressDialog.dismiss();
                loginActivity.finish();
            }
            else if(msg.what==0){
                progressDialog.dismiss();
            }
            super.handleMessage(msg);
        }
    }

    class VisitedDB implements Runnable {

        public void run() {
            Looper.prepare();
            JSONObject jsonObj;
            try{
                jsonObj=query(	userNameText.getText().toString(),passwordText.getText().toString());
                if(jsonObj.getString("userName").equals(userNameText.getText().toString())){
                    if(jsonObj.getString("login").equals("success")){
                        Toast.makeText(LoginActivity.this, "Berhasil login！", Toast.LENGTH_LONG).show();
                        mainHandler.sendEmptyMessage(1);
                    }
                    else if(jsonObj.getString("login").equals("failure")){
                        Toast.makeText(LoginActivity.this, "Password salah！", Toast.LENGTH_LONG).show();
                        mainHandler.sendEmptyMessage(0);
                    }
                }
                else if(jsonObj.getString("userName").equals("")){
                    Toast.makeText(LoginActivity.this, "Username belum terdaftar！", Toast.LENGTH_LONG).show();
                    mainHandler.sendEmptyMessage(0);
                }

            }catch(Exception e){
                Toast.makeText(LoginActivity.this, "Server Eror！", Toast.LENGTH_LONG).show();
                mainHandler.sendEmptyMessage(0);
                e.printStackTrace();
            }
            Looper.loop();
        }

        private JSONObject query(String userName, String password)
                throws Exception {
            Map<String, String> map = new HashMap<String, String>();
            map.put("userName", userName);
            map.put("password", password);
            String url = HttpUtil.BASE_URL + "login.jsp";
            return new JSONObject(HttpUtil.postRequest(url, map));
        }
    }

}