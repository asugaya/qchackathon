package com.company;

import android.util.Log;

import com.apx.speakersknowledge.R;
import com.qualcommlabs.usercontext.Callback;

public abstract class AbstractFailureLoggingCallback<T> implements Callback<T> {

    private static final String TAG = AbstractFailureLoggingCallback.class.getSimpleName();

    private final MallMartPresenter mallMartPresenter;

    public AbstractFailureLoggingCallback(MallMartPresenter mallMartPresenter) {
        this.mallMartPresenter = mallMartPresenter;
    }

    @Override
    public void failure(int statusCode, String errorMessage) {
        Log.e(TAG, errorMessage);
        //        mallMartPresenter.updatePlaceEventText(errorMessage);
        mallMartPresenter.toastAndLogError(errorMessage);
    }

}
