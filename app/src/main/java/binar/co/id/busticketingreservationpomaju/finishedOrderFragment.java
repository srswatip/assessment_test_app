package binar.co.id.busticketingreservationpomaju;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FinishedOrderFragment extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View globalView=inflater.inflate(R.layout.finished_order, container,false);
        return globalView;
    }
}