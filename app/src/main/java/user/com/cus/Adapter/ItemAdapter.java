package user.com.cus.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import user.com.cus.DataModel.Item.ItemSaved;
import user.com.cus.Listener.ItemListener;
import user.com.cus.R;
import user.com.cus.Utils.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by User on 28/01/2018.
 */

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    List<ItemSaved> listItem;
    ItemListener listener;
    Context context;

    public ItemAdapter(List<ItemSaved> listItem, ItemListener listener, Context context) {
        this.listItem = listItem;
        this.listener = listener;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.img_item) ImageView imgItem;
        @BindView(R.id.txt_item_name) TextView txtItemName;
        @BindView(R.id.txt_description_item) TextView txtDescriptionItem;
        @BindView(R.id.txt_price_item) TextView txtPriceItem;
        @BindView(R.id.img_favourite) ImageView imgFavourite;
        @BindView(R.id.txt_count) TextView txtCount;
        @BindView(R.id.btn_decrease) RelativeLayout btnDecrease;
        @BindView(R.id.btn_increase) RelativeLayout btnIncrease;

        public ViewHolder(View v){
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item,parent,false);

        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final ItemSaved item = listItem.get(position);

        if (item.getImgUrl() != null){
            if (item.getImgUrl().length() != 0){
                Picasso.with(context).load(item.getImgUrl()).fit().into(holder.imgItem);
            }
        }
        holder.txtItemName.setText(item.getName());
        holder.txtDescriptionItem.setText(item.getDescription());
        holder.txtPriceItem.setText(Utils.convertToRupiahFormat(item.getPrice()));
        holder.txtCount.setText(String.valueOf(item.getCount()));

        if (item.isWishlist()){
            holder.imgFavourite.setImageResource(R.drawable.love_red);
        }else{
            holder.imgFavourite.setImageResource(R.drawable.love_kosong);
        }

        holder.btnDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (item.getCount() != 0){
                    listener.onDecreaseClicked(item);
                }
            }
        });

        holder.btnIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onIncreaseClicked(item);
            }
        });

        holder.imgFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onFavouriteClicked(item);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
    public void refreshData(List<ItemSaved> listItem){
        this.listItem = listItem;
        notifyDataSetChanged();
    }
}
