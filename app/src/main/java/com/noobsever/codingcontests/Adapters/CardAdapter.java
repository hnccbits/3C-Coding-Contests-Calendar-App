package com.noobsever.codingcontests.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.noobsever.codingcontests.Models.ContestObject;
import com.noobsever.codingcontests.R;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardAdapterViewHolder> implements Filterable {

    public final String TAG = "MyTag";
    private List<ContestObject> ContestObjectArrayList;
    private List<ContestObject> DummyContestObjectArrayList;
    private Context context;
    private List<Boolean> CheckMoreFlag;
    private Filter FilteredData = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<ContestObject> filtered = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filtered.addAll(DummyContestObjectArrayList);
            } else {
                String check = constraint.toString().toLowerCase().trim();

                for (ContestObject items : DummyContestObjectArrayList) {
                    if (items.getTitle().toLowerCase().contains(check) || items.getPlatform().toLowerCase().contains(check)
                            || items.getStatus().toLowerCase().contains(check)) {
                        filtered.add(items);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filtered;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ContestObjectArrayList.clear();
            ContestObjectArrayList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public CardAdapter(Context context) {
        this.context = context;
    }

    public CardAdapter(Context context, List<ContestObject> ContestObjectArrayList) {
        this.context = context;
        this.ContestObjectArrayList = ContestObjectArrayList;
        InitializeBool();
    }

    public void setData(List<ContestObject> data) {
        this.ContestObjectArrayList = data;
        notifyDataSetChanged();
        InitializeBool();
    }

    public void InitializeBool() {
        DummyContestObjectArrayList = new ArrayList<>(ContestObjectArrayList);
        CheckMoreFlag = new ArrayList<Boolean>(getItemCount());
        CheckMoreFlag.addAll(Collections.nCopies(getItemCount(), Boolean.FALSE));
    }

    @NonNull
    @Override
    public CardAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.layout_contest_card, parent, false);
        return new CardAdapterViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final CardAdapterViewHolder holder, final int position) {

        holder.mCard.setAnimation(AnimationUtils.loadAnimation(context, R.anim.pop_in));

        holder.mRoundName.setText(ContestObjectArrayList.get(position).getTitle());
        holder.mDateEnd.setText(ContestObjectArrayList.get(position).getEnd());
        holder.mDateStart.setText(ContestObjectArrayList.get(position).getStart());
        holder.mDuration.setText(ContestObjectArrayList.get(position).getDuration());
        Log.e("TAG : DATA DISPLAY:", "onBindViewHolder: " + ContestObjectArrayList.get(position).getTitle());

        if (CheckMoreFlag.get(position)) {
            holder.mShare.show();
            holder.mNotification.show();
            holder.mReminder.show();
        } else {
            holder.mShare.hide();
            holder.mNotification.hide();
            holder.mReminder.hide();
        }

        holder.mCard.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                if (CheckMoreFlag.get(position)) {
                    holder.mShare.hide();
                    holder.mNotification.hide();
                    holder.mReminder.hide();
                } else {
                    holder.mShare.show();
                    holder.mNotification.show();
                    holder.mReminder.show();
                }
                CheckMoreFlag.set(position, !CheckMoreFlag.get(position));

                return true;
            }
        });

        holder.mMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CheckMoreFlag.get(position)) {
                    holder.mShare.hide();
                    holder.mNotification.hide();
                    holder.mReminder.hide();
                } else {
                    holder.mShare.show();
                    holder.mNotification.show();
                    holder.mReminder.show();
                }
                CheckMoreFlag.set(position, !CheckMoreFlag.get(position));
            }
        });

        holder.mShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(context, "Implement Share " + position, Toast.LENGTH_SHORT).show();

                LocalDateTime start = LocalDateTime.parse(ContestObjectArrayList.get(position).getStart().substring(0, ContestObjectArrayList.get(position).getStart().length() - 2));
                LocalDateTime stop = LocalDateTime.parse(ContestObjectArrayList.get(position).getEnd().substring(0, ContestObjectArrayList.get(position).getEnd().length() - 2));
                DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                String formatStart = start.format(format);
                String formatEnd = stop.format(format);

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                String message = "Checkout this contest on " +
                        ContestObjectArrayList.get(position).getPlatform() + " \n" +
                        ContestObjectArrayList.get(position).getTitle()
                        + "\nDuration " + ContestObjectArrayList.get(position).getDuration() +
                        "\nStatus " + ContestObjectArrayList.get(position).getStatus() +
                        "\nStart Time " + formatStart +
                        "\nEnd Time " + formatEnd + "\n" +
                        ContestObjectArrayList.get(position).getLink();
                sendIntent.putExtra(Intent.EXTRA_TEXT, message);
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, null);
                context.startActivity(shareIntent);

            }
        });
        holder.mNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(context, "Implement Notification " + position, Toast.LENGTH_SHORT).show();
                String url = ContestObjectArrayList.get(position).getLink();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                context.startActivity(i);
            }
        });
        holder.mReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(context,ContestObjectArrayList.get(position).toString(),Toast.LENGTH_SHORT).show();
//                Log.d(TAG, "onClick: Duration " + ContestObjectArrayList.get(position).getDuration());
//                Log.d(TAG, "onClick: Start " + ContestObjectArrayList.get(position).getStart());
//                Log.d(TAG, "onClick: End " + ContestObjectArrayList.get(position).getEnd());
//                Log.d(TAG, "onClick: Status " + ContestObjectArrayList.get(position).getStatus());

                LocalDateTime start = LocalDateTime.parse(ContestObjectArrayList.get(position).getStart().substring(0, ContestObjectArrayList.get(position).getStart().length() - 2));
                LocalDateTime stop = LocalDateTime.parse(ContestObjectArrayList.get(position).getEnd().substring(0, ContestObjectArrayList.get(position).getEnd().length() - 2));

                Intent intent = new Intent(Intent.ACTION_INSERT)
                        .setData(CalendarContract.Events.CONTENT_URI)
                        .putExtra(CalendarContract.Events.TITLE, ContestObjectArrayList.get(position).getTitle())
                        .putExtra(CalendarContract.Events.EVENT_LOCATION, ContestObjectArrayList.get(position).getPlatform())
                        .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, start.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
                        .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, stop.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
                if (intent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(intent);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return ContestObjectArrayList.size();
    }

    @Override
    public Filter getFilter() {
        return FilteredData;
    }

    public static class CardAdapterViewHolder extends RecyclerView.ViewHolder {
        private final TextView mRoundName;
        private final TextView mDateStart;
        private final TextView mDateEnd, mDuration;
        private final MaterialCardView mCard;
        private final FloatingActionButton mShare, mNotification, mReminder;
        private final ImageView mMore;

        public CardAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            mRoundName = itemView.findViewById(R.id.tv_round_name);
            mDateStart = itemView.findViewById(R.id.date_start);
            mDateEnd = itemView.findViewById(R.id.date_end);
            mCard = itemView.findViewById(R.id.card);
            mShare = itemView.findViewById(R.id.fab_share);
            mNotification = itemView.findViewById(R.id.fab_notification);
            mReminder = itemView.findViewById(R.id.fab_reminder);
            mMore = itemView.findViewById(R.id.more_option);
            mDuration = itemView.findViewById(R.id.duration);
        }

    }

}
