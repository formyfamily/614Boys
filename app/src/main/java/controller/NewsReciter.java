package controller;

/**
 * Created by thinkpad on 2017/9/8.
 */

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;

/**
 * 描述：语音播放工具类
 * 作者：卜俊文
 * 创建：2016/8/19 09:07
 * 邮箱：344176791@qq.com
 */
public class NewsReciter {

    private static NewsReciter reciter;

    private SpeechSynthesizer mySynthesizer;

    public boolean hasStarted = false;

    private NewsReciter() {
    }
    
    public static NewsReciter getInstance() {
        if (reciter == null) {
            synchronized (NewsReciter.class) {
                if (reciter == null) {
                    reciter = new NewsReciter();
                }
            }
        }
        return reciter;
    }

    private InitListener myInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            Log.d("mySynthesiezer:", "InitListener init() code = " + code);
        }
    };

    public void init(Context context) {
        //处理语音合成关键类
        mySynthesizer = SpeechSynthesizer.createSynthesizer(context, myInitListener);
        //设置发音人
        mySynthesizer.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");
        //设置音调
        mySynthesizer.setParameter(SpeechConstant.PITCH, "50");
        //设置音量
        mySynthesizer.setParameter(SpeechConstant.VOLUME, "50");

    }

    public void speakText(String content) {
        int code = mySynthesizer.startSpeaking(content, new SynthesizerListener() {
            @Override
            public void onSpeakBegin() {

            }

            @Override
            public void onBufferProgress(int i, int i1, int i2, String s) {

            }

            @Override
            public void onSpeakPaused() {

            }

            @Override
            public void onSpeakResumed() {

            }

            @Override
            public void onSpeakProgress(int i, int i1, int i2) {

            }

            @Override
            public void onCompleted(SpeechError speechError) {

            }

            @Override
            public void onEvent(int i, int i1, int i2, Bundle bundle) {

            }
        });
    }

    public void stopSpeaking() {
        mySynthesizer.pauseSpeaking();
    }
}