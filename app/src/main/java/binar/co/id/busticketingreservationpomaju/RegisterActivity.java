package binar.co.id.busticketingreservationpomaju;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import binar.co.id.busticketingreservationpomaju.util.HttpUtil;


public class RegisterActivity extends Activity {

    private EditText userNameText;
    private EditText passwordText;
    private EditText passwordAgainText;
    private EditText phoneText;
    private EditText mailText;
    private EditText idCardText;
    private Button registerButton;

    private static Handler mainHandler;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private ProgressDialog progressDialog;

    private ActionBar actionBar;
    private LinearLayout backLayout;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        initActionBar();
        initView();
        preferences = getSharedPreferences("BookTicket_register", MODE_PRIVATE);
        editor = preferences.edit();

        mainHandler = new Handler() {
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                    startActivity(intent);
                    progressDialog.dismiss();
                    finish();
                }
                else if(msg.what==0){
                    progressDialog.dismiss();
                }
            }
        };
    }

    public void initView() {
        userNameText = (EditText) findViewById(R.id.register_user_name_text);
        passwordText = (EditText) findViewById(R.id.register_password_text);
        passwordAgainText = (EditText) findViewById(R.id.register_password_again_text);
        phoneText = (EditText) findViewById(R.id.register_phone_text);
        mailText = (EditText) findViewById(R.id.register_mail_text);
        idCardText = (EditText) findViewById(R.id.register_id_card_text);
        registerButton = (Button) findViewById(R.id.register_register_button);
        passwordText.setOnFocusChangeListener(new OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!passwordAgainText.getText().toString().isEmpty()) {
                        if (!passwordText.getText().toString()
                                .equals(passwordAgainText.getText().toString())) {
                            passwordAgainText.setText("");
                            Toast.makeText(RegisterActivity.this, "Password salah",
                                    Toast.LENGTH_LONG).show();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    passwordAgainText.requestFocus();
                                }
                            }, 100);
                        }
                    }
                }
            }
        });

        passwordAgainText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (passwordText.getText().toString().isEmpty()) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                passwordText.requestFocus();
                            }
                        }, 100);

                    } else {
                        if (!passwordText.getText().toString()
                                .equals(passwordAgainText.getText().toString())) {
                            passwordAgainText.setText("");
                            Toast.makeText(RegisterActivity.this, "Password salah",
                                    Toast.LENGTH_LONG).show();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    passwordAgainText.requestFocus();
                                }
                            }, 100);
                        }
                    }
                }
            }
        });

        registerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userNameText.getText().toString().isEmpty()
                        || passwordText.getText().toString().isEmpty()
                        || passwordAgainText.getText().toString().isEmpty()
                        || phoneText.getText().toString().isEmpty()
                        || mailText.getText().toString().isEmpty()
                        || idCardText.getText().toString().isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            RegisterActivity.this);
                    builder.setTitle("info").setMessage("Tidak boleh kosong！")
                            .setPositiveButton("oke", null);
                    builder.create().show();
                } else if (!passwordText.getText().toString()
                        .equals(passwordAgainText.getText().toString())) {
                    passwordAgainText.setText("");
                    Toast.makeText(RegisterActivity.this, "Password salah",
                            Toast.LENGTH_LONG).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            passwordAgainText.requestFocus();
                        }
                    }, 100);
                } else {
                    progressDialog=new ProgressDialog(RegisterActivity.this,AlertDialog.THEME_TRADITIONAL);
                    progressDialog.setCancelable(false);
                    progressDialog.setMessage("Sedang memuat...");
                    progressDialog.setIndeterminate(false);
                    progressDialog.show();
                    new Thread(new VisitDB()).start();
                }
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
        title.setText("Pendaftaran");
        backLayout=(LinearLayout)findViewById(R.id.back_layout);
        backLayout.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                backLayout.setBackgroundColor(getResources().getColor(R.color.blue_dark));
                RegisterActivity.this.onBackPressed();
            }
        });
    }

    public void onPause() {
        super.onPause();
    }

    public void onDestroy() {
        super.onDestroy();
    }

    class VisitDB implements Runnable {

        @Override
        public void run() {

            Looper.prepare();
            JSONObject jsonObj;
            try{
                jsonObj=insert(	userNameText.getText().toString(),passwordText.getText().toString(),
                        phoneText.getText().toString(),mailText.getText().toString(),idCardText.getText().toString());
                if(jsonObj.getString("userName").equals(userNameText.getText().toString())){
                    Toast.makeText(RegisterActivity.this, "Username telah terdaftar , silahkan ganti yang lain！", Toast.LENGTH_LONG).show();
                    mainHandler.sendEmptyMessage(0);
                }
                else if(jsonObj.getString("userName").equals("")){

                    if(jsonObj.getString("register").equals("success")){
                        Toast.makeText(RegisterActivity.this, "Pendaftaran berhasil！", Toast.LENGTH_LONG).show();
                        mainHandler.sendEmptyMessage(1);
                    }
                }
            }catch(Exception e){
                Toast.makeText(RegisterActivity.this, "Server Eror！", Toast.LENGTH_LONG).show();
                mainHandler.sendEmptyMessage(0);
                e.printStackTrace();
            }
            Looper.loop();
        }
        private JSONObject insert(String userName, String password,String phone,String mail,String idCard)
                throws Exception {
            Map<String, String> map = new HashMap<String, String>();
            map.put("userName", userName);
            map.put("password", password);
            map.put("phone", phone);
            map.put("mail", mail);
            map.put("idCard", idCard);
            String url = HttpUtil.BASE_URL + "register.jsp";
            return new JSONObject(HttpUtil.postRequest(url, map));
        }
    }
}