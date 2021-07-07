package com.noobsever.codingcontests.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.noobsever.codingcontests.DrawerActivity;
import com.noobsever.codingcontests.R;
import com.noobsever.codingcontests.databinding.FragmentFeedbackBinding;
import com.noobsever.codingcontests.databinding.FragmentHomeBinding;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class FeedbackFragment extends Fragment {
    private FragmentFeedbackBinding binding;
    public FeedbackFragment() {
        // Required empty public constructor
    }
    Context context;

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        this.context = context;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentFeedbackBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    private int MAX_WORDS=500;
    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.feedbackEdittext.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    binding.feedbackEdittext.setBackground(AppCompatResources.getDrawable(context,R.drawable.edit_text_bg_pressed));
                }
                else binding.feedbackEdittext.setBackground(AppCompatResources.getDrawable(context,R.drawable.edit_text_bg_not_pressed));
            }
        });
        binding.feedbackEdittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                int wordsLength=countWords(s.toString());
                if (wordsLength >=MAX_WORDS-1) {
                    setCharLimit(binding.feedbackEdittext, binding.feedbackEdittext.getText().length());
                } else {
                    removeFilter(binding.feedbackEdittext);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int wordsLength=countWords(s.toString());
                binding.tvShowWord.setText(String.valueOf(wordsLength)+"/"+MAX_WORDS);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.feedbackSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String feedBack=binding.feedbackEdittext.getText().toString();
                if(feedBack.isEmpty()) Toast.makeText(context, "Please type something", Toast.LENGTH_SHORT).show();
                else addItemToSheet(feedBack);
            }
        });
    }
    private int countWords(String s) {
        return s.replace(" ","").length(); // separate string around spaces
    }

    private void addItemToSheet(String feedBack){
        final ProgressDialog dialog=ProgressDialog.show(context,"Adding Item","Please wait...");
        String url="https://script.google.com/macros/s/AKfycbx1Dhfh1g0F41gjwRIdMcBIHbFlWj2yANfAeAxg7lo9RR3yjIWLVn26vxaWP9kMpOs/exec";
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                context.startActivity(new Intent(context, DrawerActivity.class));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @org.jetbrains.annotations.Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("action","addItem");
                params.put("itemMessage",feedBack);
                return params;
            }
        };
        int shocketTimeOut=50000;
        RetryPolicy retryPolicy=new DefaultRetryPolicy(shocketTimeOut,0,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        RequestQueue queue= Volley.newRequestQueue(context);
        queue.add(stringRequest);
    }
    private InputFilter filter;

    private void setCharLimit(EditText et, int max) {
        filter = new InputFilter.LengthFilter(max);
        et.setFilters(new InputFilter[] { filter });
    }

    private void removeFilter(EditText et) {
        if (filter != null) {
            et.setFilters(new InputFilter[0]);
            filter = null;
        }
    }
}