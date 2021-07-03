package com.noobsever.codingcontests.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.noobsever.codingcontests.R;
import com.noobsever.codingcontests.Screens.ShowContestCardsActivity;
import com.noobsever.codingcontests.Utils.Constants;

import java.util.List;


public class PlatformsListAdapter extends RecyclerView.Adapter<PlatformsListAdapter.PlatformsListAdapterViewHolder>{

    /** This is a Traditional RecyclerView Adapter*/

    private List<String> ContestObjectArrayList;
    private Context context;

    /** These Colour are picked one by one and are set for View Holder*/
    private String[] dark ={"#ff2bc8d9","#ffff9b2b","#ff948bfe","#fffe6d6e"};
    private String[] light={"#ffd9f5f8","#ffffedd9","#ffeceaff","#ffffe5e6"};

    public PlatformsListAdapter(Context context)
    {
        this.context = context;
    }

    public PlatformsListAdapter(Context context, List<String> ContestObjectArrayList)
    {
        this.context = context;
        this.ContestObjectArrayList = ContestObjectArrayList;
    }

    public void setData(List<String> data) {
        this.ContestObjectArrayList = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PlatformsListAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.layout_contest_title,parent,false);
        return new PlatformsListAdapterViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final PlatformsListAdapterViewHolder holder, final int position) {
        /** Set ViewHolder Title, Colour, Body*/
        holder.contestTitle.setText(ContestObjectArrayList.get(position));
        holder.contestTitle.setTextColor(Color.parseColor(dark[position%4]));
        holder.viewHolder.setBackgroundColor(Color.parseColor(light[position%4]));
        holder.viewHolderLeft.setBackgroundColor(Color.parseColor(dark[position%4]));


        holder.contestTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: ShowContestCardsActivity is important class. Show all images using Glide in ShowContestCardsActivity
                context.startActivity(new Intent(context, ShowContestCardsActivity.class).putExtra(Constants.WEBSITE,ContestObjectArrayList.get(position)));
            }
        });
    }

    @Override
    public int getItemCount(){
        return ContestObjectArrayList.size();
    }

    public static class PlatformsListAdapterViewHolder extends RecyclerView.ViewHolder{
        private final TextView contestTitle;
        RelativeLayout viewHolder;
        LinearLayout viewHolderLeft;

        public PlatformsListAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
             /** Access View Holder Items*/
             contestTitle = itemView.findViewById(R.id.contest_title);
             viewHolder=itemView.findViewById(R.id.view_holder);
             viewHolderLeft=itemView.findViewById(R.id.view_holder_left);
        }
    }
}
