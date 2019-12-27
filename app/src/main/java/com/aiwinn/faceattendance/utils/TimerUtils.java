package com.aiwinn.faceattendance.utils;

import android.os.CountDownTimer;

/**
 * Created by yong on 2019/6/1 17:09.
 */
public class TimerUtils extends CountDownTimer {

    private OnTickListener mOnTickListener;

    public TimerUtils(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    @Override
    public void onTick(long millisUntilFinished) {
        if (mOnTickListener != null){
            mOnTickListener.onTick(millisUntilFinished / 1000);
        }
    }

    @Override
    public void onFinish() {
        if (mOnTickListener != null){
            mOnTickListener.onFinish();
        }
    }

    public void setOnTickListener(OnTickListener listener){
        mOnTickListener = listener;
    }

    public interface OnTickListener{
        void onTick(long seconds);
        void onFinish();
    }

}