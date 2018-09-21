package user.com.cus.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import user.com.cus.DataModel.Place.SearchLocation;
import user.com.cus.Listener.SearchLocationListener;
import user.com.cus.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by User on 17/02/2018.
 */

public class SearchLocationAdapter extends RecyclerView.Adapter<SearchLocationAdapter.ViewHolder> {

    List<SearchLocation> listSearchLocation;
    SearchLocationListener listener;

    public static class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.txt_place_search_location) TextView txtPlaceSearchLocation;
        @BindView(R.id.txt_address_search_location) TextView txtAddressSearchLocation;

        public ViewHolder(View v){
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    public SearchLocationAdapter(List<SearchLocation> listSearchLocation, SearchLocationListener listener) {
        this.listSearchLocation = listSearchLocation;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_search_location,parent,false);

        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final SearchLocation searchLocation = listSearchLocation.get(position);

        holder.txtPlaceSearchLocation.setText(searchLocation.getName());
        holder.txtAddressSearchLocation.setText(searchLocation.getAlamat());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemSearchClicked(searchLocation);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listSearchLocation.size();
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
    public void refreshData(List<SearchLocation> listSearchLocation){
        this.listSearchLocation = listSearchLocation;
        notifyDataSetChanged();
    }
}
