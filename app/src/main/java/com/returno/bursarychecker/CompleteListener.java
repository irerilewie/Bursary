package com.returno.bursarychecker;

import java.util.List;

public interface CompleteListener {
    default void onComplete(String downLoadUrl){

    }

    default void onUploadFetched(List<Upload> uploads){

    }

    default void onGenderGroup(int maleCount, int femaleCount,int others){

    }
}
