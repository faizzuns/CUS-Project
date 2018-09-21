package user.com.cus.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import user.com.cus.DataModel.Payment.ItemList;
import user.com.cus.R;
import user.com.cus.Utils.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by User on 01/02/2018.
 */

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.ViewHolder> {

    List<ItemList> listItem;

    public PaymentAdapter(List<ItemList> listItem) {
        this.listItem = listItem;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.txt_price) TextView txtPrice;
        @BindView(R.id.txt_item) TextView txtItem;

        public ViewHolder(View v){
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_payment,parent,false);

        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ItemList itemList = listItem.get(position);

        holder.txtItem.setText(itemList.getItemQuantity() + "x "+ itemList.getName());
        holder.txtPrice.setText(Utils.convertToRupiahFormat(itemList.getTotalPrice()));
    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
    public void refreshData(List<ItemList> listItem){
        this.listItem = listItem;
        notifyDataSetChanged();
    }
}
