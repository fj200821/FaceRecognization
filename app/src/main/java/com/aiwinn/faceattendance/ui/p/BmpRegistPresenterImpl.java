package com.aiwinn.faceattendance.ui.p;

import android.graphics.Bitmap;

import com.aiwinn.base.log.LogUtils;
import com.aiwinn.base.util.ImageUtils;
import com.aiwinn.faceattendance.common.AttConstants;
import com.aiwinn.faceattendance.ui.v.BmpRegistView;
import com.aiwinn.faceattendance.utils.FaceUtils;
import com.aiwinn.facedetectsdk.FaceDetectManager;
import com.aiwinn.facedetectsdk.Utils.ThreadPoolUtils;
import com.aiwinn.facedetectsdk.bean.DetectBean;
import com.aiwinn.facedetectsdk.bean.RegisterBean;
import com.aiwinn.facedetectsdk.bean.UserBean;
import com.aiwinn.facedetectsdk.common.Status;
import com.aiwinn.facedetectsdk.listener.DetectListener;
import com.aiwinn.facedetectsdk.listener.RegisterListener;

import java.io.File;
import java.util.List;

/**
 * com.aiwinn.faceattendance.ui.p
 * SDK_ATT
 * 2018/08/29
 * Created by LeoLiu on User
 */

public class BmpRegistPresenterImpl implements BmpRegistPresenter {

    public static final String HEAD = "ATT_BMP_REGIST";

    private final BmpRegistView mView;

    public BmpRegistPresenterImpl(BmpRegistView view) {
        mView = view;
    }

    @Override
    public void registUser(File file, final String name) {
        final Bitmap bitmap = ImageUtils.getBitmap(file);
        LogUtils.d(HEAD,"start detect face by bmp");
        ThreadPoolUtils.executeTask(new Runnable() {
            @Override
            public void run() {
                FaceDetectManager.detectFace(bitmap, new DetectListener() {

                    @Override
                    public void onSuccess(List<DetectBean> detectBeanList) {
                        LogUtils.d(HEAD,"detect face size > "+detectBeanList.size());
                        if (detectBeanList.size() > 0) {
                            DetectBean maxFace = FaceUtils.findMaxFace(detectBeanList);
                            //检查人脸是否符合注册条件
                            Status status = FaceDetectManager.checkFace(bitmap,maxFace);
                            LogUtils.d(HEAD,"check face status : "+status);
                            if (status == Status.Ok) {
                                //检查人脸是否注册过
                                float[] feature = FaceDetectManager.extractFeature(bitmap,maxFace);
                                if (feature != null) {
                                    List<UserBean> userBeans = FaceDetectManager.queryByFeatures(feature,AttConstants.REGISTER_DEFAULT?"":AttConstants.EXDB);
                                    LogUtils.d(HEAD,"check is registered : userBeans size > "+userBeans.size());
                                    if (userBeans.size() > 0) {
                                        LogUtils.d(HEAD,"check is registered : userBeans > "+userBeans.get(0).name);
                                        mView.registed(userBeans.get(0));
                                    }else {
                                        regist(detectBeanList,name);
                                    }
                                }else {
                                    mView.registFail(Status.FeatureIsNull);
                                }
                            }else {
                                mView.registFail(status);
                            }
                        }else {
                            mView.noFace();
                        }
                    }

                    @Override
                    public void onError(Status status, String s) {
                        mView.registFail(status);
                    }
                });
            }
        });
    }

    private void regist(List<DetectBean> list, String name) {
        LogUtils.d(HEAD,"start register face");
        DetectBean maxFace = FaceUtils.findMaxFace(list);
        RegisterBean registerBean = new RegisterBean(name);
        FaceDetectManager.registerUser(AttConstants.REGISTER_DEFAULT?"":AttConstants.EXDB,maxFace.faceBitmap, maxFace, registerBean, new RegisterListener() {
            @Override
            public void onSuccess(UserBean userBean) {
                LogUtils.d(HEAD,"register face done . result : "+userBean.name);
                mView.registSucc(userBean);
            }

            @Override
            public void onSimilarity(UserBean userBean) {
                LogUtils.d(HEAD,"register face Error . Similarity : "+userBean.name+" "+userBean.userId);
            }

            @Override
            public void onError(Status status) {
                LogUtils.d(HEAD,"register face Error . status : "+status.toString());
                mView.registFail(status);
            }
        });

    }

}
