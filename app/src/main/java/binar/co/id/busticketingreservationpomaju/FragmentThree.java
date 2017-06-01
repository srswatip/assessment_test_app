package binar.co.id.busticketingreservationpomaju;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FragmentThree extends Fragment{



    private String userName;
    private String loginout;
    private LinearLayout loginLayout;
    private LinearLayout registerLayout;
    private LinearLayout lineLayout;
    private LinearLayout firstLayout;

    private TextView loginLabel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View globalView=inflater.inflate(R.layout.fragment_three, container,false);
        initView(globalView);
        userInfo(globalView);



        return globalView;

    }
    public void initView(View view){
        loginLabel=(TextView)view.findViewById(R.id.my_info_login_label);
        loginLayout=(LinearLayout)view.findViewById(R.id.my_info_first_first_layout);
        firstLayout=(LinearLayout)view.findViewById(R.id.my_info_first_layout);
        lineLayout=(LinearLayout)view.findViewById(R.id.my_info_first_second_layout);
        loginLayout.setOnClickListener(new Login());
        registerLayout=(LinearLayout)view.findViewById(R.id.my_info_first_third_layout);
        registerLayout.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent=new Intent(FragmentThree.this.getActivity(),RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    public void userInfo(View view){
        Intent intent=FragmentThree.this.getActivity().getIntent();
        userName=intent.getStringExtra("userName");
        loginout=intent.getStringExtra("loginout");
        if(userName!=null){
            loginLabel.setText(userName);
            registerLayout.setVisibility(View.GONE);
            lineLayout.setVisibility(View.GONE);
        }
        else if(loginout!=null){
            Intent intentLogin=new Intent(FragmentThree.this.getActivity(),LoginActivity.class);
            startActivity(intentLogin);
        }
    }

    class Login implements OnClickListener{

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            if(loginLabel.getText().toString().equals("登录")){
                Intent intent=new Intent(FragmentThree.this.getActivity(),LoginActivity.class);
                startActivity(intent);}
            else{
                Intent intent=new Intent(FragmentThree.this.getActivity(),UserInfoActivity.class);
                intent.putExtra("userName", loginLabel.getText().toString());
                startActivity(intent);
            }
        }
    }
}
