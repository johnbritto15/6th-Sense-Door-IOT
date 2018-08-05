package com.quasar.nemesis.a6thsense.RecognitionModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


import java.util.List;

/**
 * Created by vipla on 01-04-2018.
 */

public class Image {
    @SerializedName("transaction")
    @Expose
    private Transaction transaction;
    @SerializedName("candidates")
    @Expose
    private List<Candidate> candidates = null;

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public List<Candidate> getCandidates() {
        return candidates;
    }

    public void setCandidates(List<Candidate> candidates) {
        this.candidates = candidates;
    }
}
