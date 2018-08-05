package com.quasar.nemesis.a6thsense.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Images {

    @SerializedName("attributes")
    @Expose
    private Attributes attributes;
    @SerializedName("transaction")
    @Expose
    private Transaction transaction;

    public Attributes getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }
}
