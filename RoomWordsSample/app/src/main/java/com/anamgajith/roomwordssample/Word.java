package com.anamgajith.roomwordssample;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "word_table")
public class Word {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name = "word")
    private String mWord;

    public Word(@NonNull String mWord) {

        this.mWord = mWord;
    }

    @Ignore
    public Word(int id,@NonNull String mWord) {

        this.id = id;
        this.mWord = mWord;
    }

    public String getWord() { return mWord; }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
