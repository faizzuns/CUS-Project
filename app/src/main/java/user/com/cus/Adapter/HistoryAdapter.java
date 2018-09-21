package user.com.cus.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import user.com.cus.DataModel.History.HistoryItem;
import user.com.cus.DataModel.History.HistoryResult;
import user.com.cus.Listener.HistoryListener;
import user.com.cus.R;
import user.com.cus.Utils.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.http.Body;

/**
 * Created by User on 20/02/2018.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    HistoryListener listener;
    List<HistoryResult> listHistory;

    public HistoryAdapter(HistoryListener listener, List<HistoryResult> listHistory) {
        this.listener = listener;
        this.listHistory = listHistory;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.txt_place_history) TextView txtPlace;
        @BindView(R.id.txt_time_history) TextView txtTime;
        @BindView(R.id.txt_id_history) TextView txtId;
        @BindView(R.id.txt_total_history) TextView txtTotal;

        public ViewHolder(View v){
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_history,parent,false);

        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final HistoryResult history = listHistory.get(position);

        holder.txtPlace.setText(history.getToko().get(0).getName());
        holder.txtTime.setText(Utils.parseCreatedAt(history.getCreatedAt()));
        int total = 0;
        for (HistoryItem historyItem : history.getList()){
            total += historyItem.getPrice();
        }
        holder.txtTotal.setText(Utils.convertToRupiahFormat(total));
        holder.txtId.setText(history.getTransactionId());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.itemClicked(history);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listHistory.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
    public void refreshData(List<HistoryResult> listHistory){
        this.listHistory = listHistory;
        notifyDataSetChanged();
    }
}
