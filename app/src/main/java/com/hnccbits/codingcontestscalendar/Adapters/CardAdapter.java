package com.hnccbits.codingcontestscalendar.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.hnccbits.codingcontestscalendar.Models.ContestObject;
import com.hnccbits.codingcontestscalendar.R;
import com.hnccbits.codingcontestscalendar.Utils.Methods;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardAdapterViewHolder> implements Filterable {

    private String[] dark ={"#ff2bc8d9","#ffff9b2b","#ff948bfe","#fffe6d6e"};
    private String[] light={"#ffd9f5f8","#ffffedd9","#ffeceaff","#ffffe5e6"};

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

        holder.viewHolder.setBackgroundColor(Color.parseColor(light[position%4]));
        holder.mRoundName.setTextColor(Color.parseColor(dark[position%4]));
        holder.mCard.setAnimation(AnimationUtils.loadAnimation(context,R.anim.pop_in));
        Log.e("Platform>>>", " "+ContestObjectArrayList.get(position).getPlatform() );
        Log.e("Total Objs>>>", "size: "+ ContestObjectArrayList.size() );

        holder.mCard.setAnimation(AnimationUtils.loadAnimation(context, R.anim.pop_in));

        holder.mRoundName.setText(ContestObjectArrayList.get(position).getTitle());
        holder.mDateEnd.setText(Methods.getStringFormat(ContestObjectArrayList.get(position).getEnd()));
        holder.mDateStart.setText(Methods.getStringFormat(ContestObjectArrayList.get(position).getStart()));
        holder.mDuration.setText(durationFormater(ContestObjectArrayList.get(position).getDuration()));
//        holder.mDuration.setText(ContestObjectArrayList.get(position).getDuration());

//        Log.e("TAG : DATA DISPLAY:", "onBindViewHolder: " + ContestObjectArrayList.get(position).getPlatform());


//        if (CheckMoreFlag.get(position)) {
//            holder.mShare.show();
//            holder.mNotification.show();
//            holder.mReminder.show();
//        } else {
//            holder.mShare.hide();
//            holder.mNotification.hide();
//            holder.mReminder.hide();
//        }

//        holder.mCard.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//
//                if (CheckMoreFlag.get(position)) {
//                    holder.mShare.hide();
//                    holder.mNotification.hide();
//                    holder.mReminder.hide();
//                } else {
//                    holder.mShare.show();
//                    holder.mNotification.show();
//                    holder.mReminder.show();
//                }
//                CheckMoreFlag.set(position, !CheckMoreFlag.get(position));
//
//                return true;
//            }
//        });
//
//        holder.mMore.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (CheckMoreFlag.get(position)) {
//                    holder.mShare.hide();
//                    holder.mNotification.hide();
//                    holder.mReminder.hide();
//                } else {
//                    holder.mShare.show();
//                    holder.mNotification.show();
//                    holder.mReminder.show();
//                }
//                CheckMoreFlag.set(position, !CheckMoreFlag.get(position));
//            }
//        });

        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//              // implemented share of contest
                // getStart() provides ie. 2014-07-07T18:30:00.000Z
                String startDate = ContestObjectArrayList.get(position).getStart().substring(0, 16);
                String endDate = ContestObjectArrayList.get(position).getEnd().substring(0, 16);

                String formatStart = startDate.substring(8, 10)+"-"+startDate.substring(5,7)+"-"+startDate.substring(0,4)+" ("+startDate.substring(11,16)+")";
                String formatEnd = endDate.substring(8, 10)+"-"+endDate.substring(5,7)+"-"+endDate.substring(0,4)+" ("+endDate.substring(11,16)+")";

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                String message = "Checkout this contest on " +
                        ContestObjectArrayList.get(position).getPlatform() + " \n" +
                        ContestObjectArrayList.get(position).getTitle() +
                        "\nDuration " + Double.parseDouble(ContestObjectArrayList.get(position).getDuration())/3600 +" hrs" +
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
        holder.openLinkInBrowser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(context, "Implement Notification " + position, Toast.LENGTH_SHORT).show();
                String url = ContestObjectArrayList.get(position).getLink();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                context.startActivity(i);
            }
        });
        holder.calendarReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(context,ContestObjectArrayList.get(position).toString(),Toast.LENGTH_SHORT).show();

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    LocalDateTime start = null;
                    start = LocalDateTime.parse(ContestObjectArrayList.get(position).getStart().substring(0, ContestObjectArrayList.get(position).getStart().length() - 2));
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
                } else {
                    // handle this for lesser api

                    // setting the start
                    Calendar calendarStart =Calendar.getInstance();
                    calendarStart.setTimeInMillis(System.currentTimeMillis());

                    String strStart = ContestObjectArrayList.get(position).getStart().substring(0, 16);
                    calendarStart.set(Calendar.YEAR, Integer.parseInt(strStart.substring(0, 4)));
                    calendarStart.set(Calendar.MONTH, Integer.parseInt(strStart.substring(5, 7)));
                    calendarStart.set(Calendar.DAY_OF_MONTH, Integer.parseInt(strStart.substring(8, 10)));
                    calendarStart.set(Calendar.HOUR_OF_DAY, Integer.parseInt(strStart.substring(11, 13)));
                    calendarStart.set(Calendar.MINUTE, Integer.parseInt(strStart.substring(14, 16)));
                    calendarStart.set(Calendar.SECOND,0);
                    calendarStart.set(Calendar.MILLISECOND, 0);

                    // setting the end
                    Calendar calendarEnd =Calendar.getInstance();
                    calendarEnd.setTimeInMillis(System.currentTimeMillis());

                    String strEnd = ContestObjectArrayList.get(position).getEnd().substring(0, 16);
                    calendarEnd.set(Calendar.YEAR, Integer.parseInt(strEnd.substring(0, 4)));
                    calendarEnd.set(Calendar.MONTH, Integer.parseInt(strEnd.substring(5, 7)));
                    calendarEnd.set(Calendar.DAY_OF_MONTH, Integer.parseInt(strEnd.substring(8, 10)));
                    calendarEnd.set(Calendar.HOUR_OF_DAY, Integer.parseInt(strEnd.substring(11, 13)));
                    calendarEnd.set(Calendar.MINUTE, Integer.parseInt(strEnd.substring(14, 16)));
                    calendarEnd.set(Calendar.SECOND,0);
                    calendarEnd.set(Calendar.MILLISECOND, 0);

                    Intent intent = new Intent(Intent.ACTION_INSERT)
                            .setData(CalendarContract.Events.CONTENT_URI)
                            .putExtra(CalendarContract.Events.TITLE, ContestObjectArrayList.get(position).getTitle())
                            .putExtra(CalendarContract.Events.EVENT_LOCATION, ContestObjectArrayList.get(position).getPlatform())
                            .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, calendarStart.getTimeInMillis())
                            .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, calendarEnd.getTimeInMillis());

                    if(intent.resolveActivity(context.getPackageManager()) != null) {
                        context.startActivity(intent);
                    }
                }

            }
        });
    }

    private String durationFormater(String duration){
        String formatDuration = "";

        double doubleDuration = Double.parseDouble(duration);
        long durMin = (long)doubleDuration / 60;

        // contests of more than a year
        if(durMin > 518400){
            formatDuration += "For practice";
            return formatDuration;
        }

        // contests of more than a month
        if(durMin > 43200){
            formatDuration += durMin/43200 +" month";
            return formatDuration;
        }

        // There ane 24 * 60 minutes in a day
        if(durMin > 1440){
            formatDuration += durMin/1440 +" days";
            durMin %= 1440;

            if(durMin >0){
                formatDuration +=", ";
            }
        }

        if(durMin > 0){
            formatDuration += durMin/60 +" hr";
            durMin %= 60;
        }

        if(durMin > 0){
            formatDuration += ", ";
            formatDuration += durMin + " min";
        }

        // formatDuration is in format ie "3 days, 4 hr, 30 min"
        return formatDuration;
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
        private final CardView mCard;
        private final Button openLinkInBrowser, calendarReminder, share;
        LinearLayout viewHolder;
//        private final ImageView mMore;

        public CardAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            mRoundName = itemView.findViewById(R.id.tv_round_name);
            mDateStart = itemView.findViewById(R.id.date_start);
            mDateEnd = itemView.findViewById(R.id.date_end);
            mCard = itemView.findViewById(R.id.card);
            openLinkInBrowser = itemView.findViewById(R.id.open_link_in_browser);
            calendarReminder = itemView.findViewById(R.id.set_reminder);
            share = itemView.findViewById(R.id.share_in_whatsapp);
//            mMore = itemView.findViewById(R.id.more_option);
            mDuration = itemView.findViewById(R.id.duration);
            viewHolder=itemView.findViewById(R.id.contest_detail_view_holder);
        }

    }

}
