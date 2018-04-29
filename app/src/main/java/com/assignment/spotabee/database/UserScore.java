package com.assignment.spotabee.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;



@Entity
public class UserScore {

    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "account_name")
    private String accountName;

    @ColumnInfo(name = "score")
    private int score;



    @Ignore
    public UserScore(String userName, int score) {
        this.accountName = userName;
        this.score = score;
    }

    public UserScore(){}

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
