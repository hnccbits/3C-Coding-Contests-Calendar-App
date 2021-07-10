package com.hnccbits.codingcontestscalendar.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.hnccbits.codingcontestscalendar.Models.ContestObject;
import com.hnccbits.codingcontestscalendar.Repository.FetchContestsRepository;

import java.util.List;

public class ApiViewModel extends ViewModel {

    private FetchContestsRepository repository;
    private LiveData<List<ContestObject>> liveContestList;

    public ApiViewModel() {
        super();
    }

    public void init() {
        repository = new FetchContestsRepository();
        liveContestList = repository.getContestsListAsync();
    }

    public void fetchContestFromApi() {
        repository.fetchContestFromApi();
    }

    public LiveData<List<ContestObject>> getAllContests() {
        return liveContestList;
    }

}
