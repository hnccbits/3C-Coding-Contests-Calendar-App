package com.hnccbits.codingcontestscalendar.ui.slideshow;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.hnccbits.codingcontestscalendar.Adapters.HelpAdapter;
import com.hnccbits.codingcontestscalendar.Models.HelpObject;
import com.hnccbits.codingcontestscalendar.databinding.FragmentFaqBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class FAQsFragment extends Fragment {

    private FragmentFaqBinding binding;
    private Context context;
    private ArrayList<HelpObject> helpObjectArrayList;
    private HelpAdapter helpAdapter;

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        this.context = context;
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFaqBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        helpObjectArrayList = new ArrayList<>();    //This ArrayList contains question,answers and an ArrowId which determines the View Visibility
        helpObjectArrayList.add(new HelpObject("Why this app?","To remind coders about different coding contests "));
        //helpObjectArrayList.add(new HelpObject("What is the tech stack?","Android, Java, Retrofit, Firebase",0));
        helpObjectArrayList.add(new HelpObject("Is it open source project?","Yes, you can visit HnCC website for more details"));
        helpObjectArrayList.add(new HelpObject("Is the app free to use?","Yes it's completely free"));
        helpObjectArrayList.add(new HelpObject("How many coding platforms does the app support","The app supports 8 coding platforms"));
        helpObjectArrayList.add(new HelpObject("Can I make any changes to the app","yes send a request in the github"));

        helpAdapter = new HelpAdapter(context,helpObjectArrayList);
        binding.helpRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        binding.helpRecyclerView.setHasFixedSize(true);
        binding.helpRecyclerView.setAdapter(helpAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}