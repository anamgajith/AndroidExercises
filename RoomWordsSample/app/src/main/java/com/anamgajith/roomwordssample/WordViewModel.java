package com.anamgajith.roomwordssample;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class WordViewModel extends AndroidViewModel {

    private WordRepository mWordRepository;
    private LiveData<List<Word>> mAllWords;

    public WordViewModel(@NonNull Application application) {
        super(application);
        mWordRepository = new WordRepository(application);
        mAllWords = mWordRepository.getAllWords();
    }

    public LiveData<List<Word>> getmAllWords() { return mAllWords; }

    public void insert(Word word){ mWordRepository.insert(word);}

    public void deleteAll() {mWordRepository.deleteAll();}

    public void deleteWord(Word word){mWordRepository.deleteWord(word);}

    public void update(Word word){mWordRepository.update(word);}

}
