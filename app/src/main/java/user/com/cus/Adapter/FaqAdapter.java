package user.com.cus.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import user.com.cus.DataModel.Faq.FaqResult;
import user.com.cus.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by User on 01/02/2018.
 */

public class FaqAdapter extends RecyclerView.Adapter<FaqAdapter.ViewHolder> {

    List<FaqResult> listFaq;
    Context context;

    public FaqAdapter(List<FaqResult> listFaq, Context context) {
        this.listFaq = listFaq;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.txt_question) TextView txtQuestion;
        @BindView(R.id.txt_answer) TextView txtAnswer;
        @BindView(R.id.arrow_answer_faq) ImageView arrowFaq;
        @BindView(R.id.root_answer_faq) LinearLayout rootAnswerFaq;

        public ViewHolder(View v){
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_faq,parent,false);

        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final FaqResult faqResult = listFaq.get(position);

        holder.txtQuestion.setText(faqResult.getQuestion());
        holder.txtAnswer.setText(faqResult.getAnswer());

        if (faqResult.isShow()){
            holder.rootAnswerFaq.setVisibility(View.VISIBLE);
        }else{
            holder.rootAnswerFaq.setVisibility(View.GONE);
        }

        holder.arrowFaq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (faqResult.isShow()){
                    listFaq.get(position).setShow(false);
                }else{
                    listFaq.get(position).setShow(true);
                }
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return listFaq.size();
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
    public void refreshData(List<FaqResult> listFaq){
        this.listFaq = listFaq;
        notifyDataSetChanged();
    }
}
