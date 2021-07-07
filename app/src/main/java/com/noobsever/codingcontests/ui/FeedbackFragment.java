package com.noobsever.codingcontests.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

//import com.android.volley.AuthFailureError;
//import com.android.volley.DefaultRetryPolicy;
//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.RetryPolicy;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.StringRequest;
//import com.android.volley.toolbox.Volley;
import com.noobsever.codingcontests.DrawerActivity;
import com.noobsever.codingcontests.R;
import com.noobsever.codingcontests.databinding.FragmentFeedbackBinding;

import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class FeedbackFragment extends Fragment {

    ProgressDialog dialog;
    Context context;

    private FragmentFeedbackBinding binding;
    public FeedbackFragment() {
        // Required empty public constructor
    }

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
                if(feedBack.isEmpty()) {
                    Toast.makeText(context, "Please type something", Toast.LENGTH_SHORT).show();
                } else {
                    addFeedbackToSheet(feedBack);
                }
            }
        });
    }
    private int countWords(String s) {
        return s.replace(" ","").length(); // separate string around spaces
    }

//    private void addItemToSheet(String feedBack){
//        final ProgressDialog dialog=ProgressDialog.show(context,"Adding Item","Please wait...");
//        String url="https://script.google.com/macros/s/AKfycbwB96-jJMlKERp9hbKNySyY7Lj3jqhrr83XO7p7s8RQ9ejBCnSaz8xhDp2bvLxFSDXh/exec";
//        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                dialog.dismiss();
//                Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
//                context.startActivity(new Intent(context, DrawerActivity.class));
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
//            }
//        }){
//            @Nullable
//            @org.jetbrains.annotations.Nullable
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String,String> params=new HashMap<>();
//                params.put("action","addItem");
//                params.put("itemMessage",feedBack);
//                return params;
//            }
//        };
//        int shocketTimeOut=50000;
//        RetryPolicy retryPolicy=new DefaultRetryPolicy(shocketTimeOut,0,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//        stringRequest.setRetryPolicy(retryPolicy);
//        RequestQueue queue= Volley.newRequestQueue(context);
//        queue.add(stringRequest);
//    }

    private  void addFeedbackToSheet(String feedback){
        dialog=ProgressDialog.show(context,"Adding Item","Please wait...");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://docs.google.com/forms/u/0/d/e/")
                .build();

        Webservice webservice = retrofit.create(Webservice.class);
        Call<Void> call = webservice.postValues(feedback);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                dialog.dismiss();
                int responseCode = response.code();
                if (responseCode == 200) {

                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setTitle("Thank you");
                    alert.setMessage("Your response has been recorded. Thank you for your feedback.");
                    alert.setPositiveButton("Home", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            context.startActivity(new Intent(context, DrawerActivity.class));
                        }
                    });
                    alert.show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                dialog.dismiss();

                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("Failed");
                alert.setMessage("We were unable to record your response. Make sure you are connected to the internet.");
                alert.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Open Settings
                    }
                });
                alert.show();
            }
        });
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

    public interface Webservice {

        @FormUrlEncoded
        @POST("1FAIpQLSfppWCNqfSBYPLa7wT-iYwr_8jAVMyly2W00WoxwOwqo45Dqg/formResponse")
        Call<Void> postValues(
                @Field("entry.1968331607") String feedback
        );

    }
}