package com.skeleton.model;

import java.io.Serializable;

/**
 * Created by rishucuber on 7/4/17.
 */

public class BookInfo implements Serializable {
    private String mTitle, mAuthor, mCatagory, mEdition, mPrice;


    public String getTitle() {
        return mTitle;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getCatagory() {
        return mCatagory;
    }

    public String getEdition() {
        return mEdition;
    }

    public String getPrice() {
        return mPrice;
    }

    public BookInfo(String mTitle, String mAuthor, String mCatagory, String mEdition, String mPrice) {
        this.mTitle = mTitle;
        this.mAuthor = mAuthor;
        this.mCatagory = mCatagory;
        this.mEdition = mEdition;
        this.mPrice = mPrice;
    }
}
