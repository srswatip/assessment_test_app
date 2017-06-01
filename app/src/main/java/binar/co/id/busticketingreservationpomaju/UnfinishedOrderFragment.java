package binar.co.id.busticketingreservationpomaju;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class UnfinishedOrderFragment extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View globalView=inflater.inflate(R.layout.unfinished_order, container,false);
        return globalView;
    }



}
