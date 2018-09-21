package user.com.cus.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import user.com.cus.DataModel.Place.PlaceResult;
import user.com.cus.Listener.ListPlaceListener;
import user.com.cus.R;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by User on 26/01/2018.
 */

public class ListPlaceAdapter extends RecyclerView.Adapter<ListPlaceAdapter.ViewHolder> {

    List<PlaceResult> listPlace;
    ListPlaceListener listPlaceListener;
    Context context;

    public ListPlaceAdapter(List<PlaceResult> listPlace, ListPlaceListener listPlaceListener, Context context) {
        this.listPlace = listPlace;
        this.listPlaceListener = listPlaceListener;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.img_place) ImageView imgPlace;
        @BindView(R.id.txt_place_name) TextView txtPlaceName;
        @BindView(R.id.txt_address) TextView txtAddress;
        @BindView(R.id.txt_open_hour) TextView txtOpenHour;
        @BindView(R.id.txt_closed) TextView txtClosed;
        @BindView(R.id.view_close) View viewClose;

        public ViewHolder(View v){
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_place,parent,false);

        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final PlaceResult placeResult = listPlace.get(position);

        Picasso.with(context).load(placeResult.getImgUrl()).fit().into(holder.imgPlace);
        holder.txtPlaceName.setText(placeResult.getName());
        holder.txtAddress.setText(placeResult.getAddress());

        if (placeResult.isClose()){
            holder.txtClosed.setVisibility(View.VISIBLE);
            holder.txtOpenHour.setVisibility(View.GONE);
            holder.viewClose.setVisibility(View.VISIBLE);
        }else{
            holder.txtClosed.setVisibility(View.GONE);
            holder.txtOpenHour.setVisibility(View.VISIBLE);
            holder.txtOpenHour.setText("Jam Buka : "+placeResult.getOpenAt()+" - "+placeResult.getClosedAt());
            holder.viewClose.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listPlaceListener.onPlaceClicked(placeResult);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listPlace.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
    public void refreshData(List<PlaceResult> list){
        this.listPlace = list;
        notifyDataSetChanged();
    }
}
