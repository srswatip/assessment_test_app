package binar.co.id.busticketingreservationpomaju;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

    private FragmentOne fragmentOne;
    private FragmentTwo fragmentTwo;
    private FragmentThree fragmentThree;

    private FrameLayout flayout;

    private RelativeLayout bookTicketLayout;
    private RelativeLayout selectOrderLayout;
    private RelativeLayout myInfoLayout;

    private TextView bookTicketLabel;
    private TextView selectOrderLabel;
    private TextView myInfoLabel;

    private int white = 0xFFFFFFFF;
    private int gray = 0xFF7597B3;
    private int blue = 0xFF0AB2FB;

    FragmentManager fManager;
    FragmentTransaction transaction;

    private long exitTime = 0;

    private ActionBar actionBar;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);
        setContentView(R.layout.main);
        fManager = getFragmentManager();

        initActionBar();
        initViews();

        Intent intent = getIntent();
        if (intent.getStringExtra("loginout")!=null) {
            onClick(myInfoLayout);
        } else {
            onClick(bookTicketLayout);
        }
    }

    public void initViews() {
        title=(TextView)findViewById(R.id.main_title);


        bookTicketLabel = (TextView) findViewById(R.id.book_ticket_label);
        selectOrderLabel = (TextView) findViewById(R.id.select_order_label);
        myInfoLabel = (TextView) findViewById(R.id.my_info_label);
        bookTicketLayout = (RelativeLayout) findViewById(R.id.book_ticket_layout);
        selectOrderLayout = (RelativeLayout) findViewById(R.id.select_order_layout);
        myInfoLayout = (RelativeLayout) findViewById(R.id.my_info_layout);
        bookTicketLayout.setOnClickListener(this);
        selectOrderLayout.setOnClickListener(this);
        myInfoLayout.setOnClickListener(this);

    }
    public void initActionBar(){
        actionBar=getActionBar();
        actionBar.setCustomView(R.layout.main_actionbar);
        actionBar.setDisplayShowCustomEnabled(true);

        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.book_ticket_layout:
                setChoiceItem(0);
                break;
            case R.id.select_order_layout:
                setChoiceItem(1);
                break;
            case R.id.my_info_layout:
                setChoiceItem(2);
                break;
            default:
                break;
        }
    }

    public void setChoiceItem(int index) {
        transaction = fManager.beginTransaction();
        clearChoice();
        hideFragments(transaction);
        switch (index) {
            case 0:

                bookTicketLabel.setTextColor(blue);
                title.setText("Pesan Tiket");

                if (fragmentOne == null) {

                    fragmentOne = new FragmentOne();
                    transaction.add(R.id.content, fragmentOne);
                } else {
                    transaction.show(fragmentOne);
                }
                break;

            case 1:

                selectOrderLabel.setTextColor(blue);
                title.setText("Lacak Pesanan");
                if (fragmentTwo == null) {

                    fragmentTwo = new FragmentTwo();
                    transaction.add(R.id.content, fragmentTwo);
                } else {
                    transaction.show(fragmentTwo);
                }
                break;

            case 2:

                myInfoLabel.setTextColor(blue);
                title.setText("tentang kami");
                if (fragmentThree == null) {

                    fragmentThree = new FragmentThree();
                    transaction.add(R.id.content, fragmentThree);
                } else {
                    transaction.show(fragmentThree);
                }
                break;
        }
        transaction.commit();
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (fragmentOne != null) {
            transaction.hide(fragmentOne);
        }
        if (fragmentTwo != null) {
            transaction.hide(fragmentTwo);
        }
        if (fragmentThree != null) {
            transaction.hide(fragmentThree);
        }
    }

    public void clearChoice() {

        bookTicketLayout.setBackgroundColor(white);
        bookTicketLabel.setTextColor(gray);
        selectOrderLayout.setBackgroundColor(white);
        selectOrderLabel.setTextColor(gray);
        myInfoLayout.setBackgroundColor(white);
        myInfoLabel.setTextColor(gray);
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - exitTime > 2000) {
            Toast.makeText(MainActivity.this, "Tekan sekali lagi untuk keluar dari aplikasi", Toast.LENGTH_LONG)
                    .show();

            exitTime = System.currentTimeMillis();
        } else {
            System.exit(0);
        }
    }

}