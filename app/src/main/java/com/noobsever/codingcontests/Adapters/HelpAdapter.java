package com.noobsever.codingcontests.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.noobsever.codingcontests.Models.HelpObject;
import com.noobsever.codingcontests.R;

import java.util.ArrayList;

public class HelpAdapter extends RecyclerView.Adapter<HelpAdapter.HelpAdapterViewHolder>{

    private ArrayList<HelpObject> helpObjectArrayList;
    private Context context;
    private ArrayList<Integer> viewHolderPosition  = new ArrayList<>();
    private int dropDownIndex = -1; //This variable controls the one at a time implementation of drop down view
    public HelpAdapter(Context context, ArrayList<HelpObject> helpObjectArrayList)
    {
        this.context = context;
        this.helpObjectArrayList = helpObjectArrayList;
    }

    @NonNull
    @Override
    public HelpAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.layout_faq_design,parent,false);
        return new HelpAdapterViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final HelpAdapterViewHolder holder, final int position) {
        HelpObject object=helpObjectArrayList.get(position);
        holder.questionTextView.setText(object.getQuestion());
        holder.answerTextView.setText(object.getAnswer());
        holder.answerTextView.setVisibility(object.isExpanded()?View.VISIBLE:View.GONE);
        holder.arrowImageView.setImageResource(object.isExpanded()?R.drawable.ic_drop_up:R.drawable.ic_drop_down);
        holder.helpContainer.setOnClickListener(v -> {
            object.setExpansion(!object.isExpanded());
            notifyItemChanged(position);
        });
    }

    @Override
    public int getItemCount(){
        return helpObjectArrayList.size();
    }

    public static class HelpAdapterViewHolder extends RecyclerView.ViewHolder{
        private final TextView questionTextView;
        private final TextView answerTextView;
        private final ImageView arrowImageView;
        private LinearLayout helpContainer;
        public HelpAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            questionTextView = itemView.findViewById(R.id.help_question);
            answerTextView = itemView.findViewById(R.id.help_answer);
            arrowImageView = itemView.findViewById(R.id.help_arrow_id);
            helpContainer=itemView.findViewById(R.id.help_container);
        }
    }

}