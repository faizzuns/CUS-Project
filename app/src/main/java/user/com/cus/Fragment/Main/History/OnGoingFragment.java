package user.com.cus.Fragment.Main.History;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import user.com.cus.Activity.DetailHistoryActivity;
import user.com.cus.Adapter.HistoryAdapter;
import user.com.cus.DataModel.History.HistoryData;
import user.com.cus.DataModel.History.HistoryModel;
import user.com.cus.DataModel.History.HistoryResult;
import user.com.cus.Listener.HistoryListener;
import user.com.cus.R;
import user.com.cus.Services.RetrofitServices;
import user.com.cus.Utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class OnGoingFragment extends Fragment {

    @BindView(R.id.rv_on_going) RecyclerView rvOnGoing;

    List<HistoryResult> listHistory;
    HistoryAdapter adapter;

    Call<HistoryModel> call;

    public OnGoingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_on_going, container, false);
        ButterKnife.bind(this,v);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        listHistory = new ArrayList<>();
        rvOnGoing.setHasFixedSize(true);
        final LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rvOnGoing.setLayoutManager(llm);

        adapter = new HistoryAdapter(new HistoryListener() {
            @Override
            public void itemClicked(HistoryResult tempHistory) {
                //intent
                goToDetailHistory(tempHistory);
            }
        }, listHistory);
        rvOnGoing.setAdapter(adapter);
    }

    private void goToDetailHistory(HistoryResult tempHistory) {
        Log.d("cekOnGoing", "goToDetailHistory: "+tempHistory.toString());
        Intent intent = new Intent(getContext(), DetailHistoryActivity.class);
        intent.putExtra("historyObject",tempHistory.toString());
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("cekOnGoing", "onResume: ");
        callHistoryData();
    }

    private void callHistoryData() {
        listHistory = new ArrayList<>();

        if (adapter !=null){
            call = RetrofitServices.sendHistoryRequest().callHistory(Utils.sharedJsonHeader, Utils.sharedHeader, new HistoryData(Utils.sharedUser.getId()));
            call.enqueue(new Callback<HistoryModel>() {
                @Override
                public void onResponse(Call<HistoryModel> call, Response<HistoryModel> response) {
                    if (!call.isCanceled()){
                        if (response.body() != null){
                            if (response.body().getError() != null){
                                Toast.makeText(getContext(), response.body().getError().getMsg(), Toast.LENGTH_SHORT).show();
                            }else{
                                //gas
                                for (HistoryResult historyResult : response.body().getList()){
                                    if (historyResult.getStatus() == 0){
                                        listHistory.add(historyResult);
                                    }
                                }
                                adapter.refreshData(listHistory);
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<HistoryModel> call, Throwable t) {
                }
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (call != null){
            call.cancel();
        }
        Log.d("cekHistoryFragment", "onDestroyView: CompletedFragment");
    }
}
