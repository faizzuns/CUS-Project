package user.com.cus.Fragment.Main.History;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import user.com.cus.DataModel.History.HistoryResult;
import user.com.cus.DataModel.Rate.RateData;
import user.com.cus.DataModel.Rate.RateModel;
import user.com.cus.MainActivity;
import user.com.cus.R;
import user.com.cus.Services.RetrofitServices;
import user.com.cus.Utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class RateFragment extends Fragment {

    private HistoryResult historyResult;

    public RateFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public RateFragment(HistoryResult historyResult){
        this.historyResult = historyResult;
    }

    @BindView(R.id.root_rating_detail) LinearLayout rootRatingDetail;
    @BindView(R.id.rate_1) ImageView rate1;
    @BindView(R.id.rate_2) ImageView rate2;
    @BindView(R.id.rate_3) ImageView rate3;
    @BindView(R.id.rate_4) ImageView rate4;
    @BindView(R.id.rate_5) ImageView rate5;
    @BindView(R.id.btn_submit) Button btnSubmit;
    @BindView(R.id.root_rate) RelativeLayout rootRate;
    @BindView(R.id.edt_comment) EditText edtComment;

    int rating = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_rate, container, false);
        ButterKnife.bind(this,v);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.isHistoryOpen = true;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rating == 0){
                    Toast.makeText(getContext(), "You must rate your order first!", Toast.LENGTH_SHORT).show();
                }else{
                    String s = edtComment.getText().toString();
                    Call<RateModel> call = RetrofitServices.sendRateRequest().callRate(Utils.sharedJsonHeader, Utils.sharedHeader, historyResult.getTransactionId(), new RateData(rating, s));
                    call.enqueue(new Callback<RateModel>() {
                        @Override
                        public void onResponse(Call<RateModel> call, Response<RateModel> response) {
                            if (response.body() != null){
                                if (response.body().getErrorModel() != null){
                                    Toast.makeText(getContext(), response.body().getErrorModel().getMsg(), Toast.LENGTH_SHORT).show();
                                }else{
                                    if (getContext() != null){
                                        Toast.makeText(getContext(), "Thank You!", Toast.LENGTH_SHORT).show();
                                        Utils.closeSoftKeyboard(getContext(), ((MainActivity)getActivity()).getCurrentFocus());
                                    }
                                    ((MainActivity)getActivity()).afterRateFragment();
                                    MainActivity.isHistoryOpen = false;
                                    getFragmentManager().popBackStack();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<RateModel> call, Throwable t) {
                            Toast.makeText(getContext(), "Something was error", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        rootRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        rate1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rating = 1;
                setRating(rating);
            }
        });
        rate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rating = 2;
                setRating(rating);
            }
        });
        rate3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rating = 3;
                setRating(rating);
            }
        });
        rate4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rating = 4;
                setRating(rating);
            }
        });
        rate5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rating = 5;
                setRating(rating);
            }
        });

    }

    private void setRating(int rating) {
        ImageView[] rate = {
                rate1,
                rate2,
                rate3,
                rate4,
                rate5
        };

        for (int i = 0; i < 5; i++){
            if ( i < rating){
                rate[i].setImageResource(R.drawable.rated);
            }else{
                rate[i].setImageResource(R.drawable.not_rated);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
