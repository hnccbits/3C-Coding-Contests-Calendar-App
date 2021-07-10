package com.hnccbits.codingcontestscalendar.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hnccbits.codingcontestscalendar.Models.ContestObject;

import java.util.List;

public class TransferContestsViewModel extends ViewModel {

    private MutableLiveData<List<ContestObject>> queriedContests;

    public TransferContestsViewModel(){
        super();
        queriedContests = new MutableLiveData<>();
    }

    public void setQueriedContests(List<ContestObject> queriedList ){
        queriedContests.postValue(queriedList);
    }

    public LiveData<List<ContestObject>> getQueriedContests() {
        return queriedContests;
    }
}
