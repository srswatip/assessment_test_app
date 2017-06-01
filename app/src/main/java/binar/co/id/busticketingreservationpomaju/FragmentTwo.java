package binar.co.id.busticketingreservationpomaju;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import binar.co.id.busticketingreservationpomaju.view.ShortLineView;

public class FragmentTwo extends Fragment implements OnClickListener {



    private FinishedOrderFragment finishedOrderFragment;
    private UnfinishedOrderFragment unfinishedOrderFragment;
    private RelativeLayout finishedOrderLayout;
    private RelativeLayout unfinishedOrderLayout;
    private TextView finishedOrderLabel;
    private TextView unfinishedOrderLabel;
    private ShortLineView finishedOrderLine;
    private ShortLineView unfinishedOrderLine;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View globalView = inflater.inflate(R.layout.fragment_two, container,false);
        fragmentManager=this.getFragmentManager();
        initView(globalView);

        return globalView;
    }

    public void initView(View globalView){

        finishedOrderLayout=(RelativeLayout)globalView.findViewById(R.id.finished_order_layout);
        unfinishedOrderLayout=(RelativeLayout)globalView.findViewById(R.id.unfinished_order_layout);
        finishedOrderLabel=(TextView)globalView.findViewById(R.id.finished_order_label);
        unfinishedOrderLabel=(TextView)globalView.findViewById(R.id.unfinished_order_label);
        finishedOrderLine=(ShortLineView)globalView.findViewById(R.id.finished_order_line);
        unfinishedOrderLine=(ShortLineView)globalView.findViewById(R.id.unfinished_order_line);
        finishedOrderLayout.setOnClickListener(this);
        unfinishedOrderLayout.setOnClickListener(this);

        onClick(finishedOrderLayout);
    }

    @Override
    public void onClick(View view){
        switch(view.getId()){
            case R.id.finished_order_layout :
                clickLayout(0);
                break;
            case R.id.unfinished_order_layout:
                clickLayout(1);
                break;
            default : break;
        }
    }

    public void clickLayout(int index){
        fragmentTransaction=fragmentManager.beginTransaction();
        hideFragments(fragmentTransaction);
        switch(index){
            case 0:
                finishedOrderLabel.setTextColor(Color.RED);
                finishedOrderLine.setVisibility(View.VISIBLE);
                unfinishedOrderLabel.setTextColor(Color.BLACK);
                unfinishedOrderLine.setVisibility(View.INVISIBLE);
                if(finishedOrderFragment==null){
                    finishedOrderFragment=new FinishedOrderFragment();
                    fragmentTransaction.add(R.id.select_order_content, finishedOrderFragment);
                }else{
                    fragmentTransaction.show(finishedOrderFragment);
                }

                break;
            case 1:
                unfinishedOrderLabel.setTextColor(Color.RED);
                unfinishedOrderLine.setVisibility(View.VISIBLE);
                finishedOrderLabel.setTextColor(Color.BLACK);
                finishedOrderLine.setVisibility(View.INVISIBLE);
                if(unfinishedOrderFragment==null){
                    unfinishedOrderFragment=new UnfinishedOrderFragment();
                    fragmentTransaction.add(R.id.select_order_content, unfinishedOrderFragment);
                }else{
                    fragmentTransaction.show(unfinishedOrderFragment);
                }
                break;
            default:break;
        }
        fragmentTransaction.commit();
    }

    private void hideFragments(FragmentTransaction transaction) {
        if ( finishedOrderFragment!= null) {
            transaction.hide(finishedOrderFragment);
        }
        if (unfinishedOrderFragment != null) {
            transaction.hide(unfinishedOrderFragment);
        }

    }

}
