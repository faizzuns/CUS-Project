package user.com.cus.Fragment.Main;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import user.com.cus.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class IklanFragment extends Fragment {

    @BindView(R.id.img_iklan)
    ImageView imgIklan;

    private int iklan;

    public IklanFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public IklanFragment(int iklan){
        this.iklan = iklan;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_iklan, container, false);
        ButterKnife.bind(this,v);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        imgIklan.setImageResource(iklan);
    }
}
