package com.aiwinn.faceattendance.ui.m;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aiwinn.base.activity.BaseActivity;
import com.aiwinn.base.log.LogUtils;
import com.aiwinn.base.util.FileUtils;
import com.aiwinn.base.util.ImageUtils;
import com.aiwinn.base.util.ScreenUtils;
import com.aiwinn.base.util.StringUtils;
import com.aiwinn.base.util.TimeUtils;
import com.aiwinn.base.util.ToastUtils;
import com.aiwinn.base.widget.CameraInterfaceBak;
import com.aiwinn.base.widget.CameraSurfaceView;
import com.aiwinn.deblocks.utils.FeatureUtils;
import com.aiwinn.faceattendance.AttApp;
import com.aiwinn.faceattendance.R;
import com.aiwinn.faceattendance.adapter.DetectAdapter;
import com.aiwinn.faceattendance.bean.DetectFaceBean;
import com.aiwinn.faceattendance.common.AttConstants;
import com.aiwinn.faceattendance.ui.p.BulkRegistPresenterImpl;
import com.aiwinn.faceattendance.ui.p.DetectPresenter;
import com.aiwinn.faceattendance.ui.p.DetectPresenterImpl;
import com.aiwinn.faceattendance.ui.v.DetectView;
import com.aiwinn.faceattendance.utils.UVCCameraInterfaceBak1;
import com.aiwinn.faceattendance.utils.UVCCameraInterfaceBak2;
import com.aiwinn.faceattendance.widget.MaskView;
import com.aiwinn.facedetectsdk.FaceDetectManager;
import com.aiwinn.facedetectsdk.Utils.ThreadPoolUtils;
import com.aiwinn.facedetectsdk.bean.DetectBean;
import com.aiwinn.facedetectsdk.bean.FaceBean;
import com.aiwinn.facedetectsdk.bean.RegisterBean;
import com.aiwinn.facedetectsdk.bean.UserBean;
import com.aiwinn.facedetectsdk.common.ConfigLib;
import com.aiwinn.facedetectsdk.common.Status;
import com.aiwinn.facedetectsdk.listener.ExtractFeatureListener;
import com.aiwinn.facedetectsdk.listener.RegisterListener;
import com.bumptech.glide.Glide;
import com.serenegiant.usb.widget.UVCCameraTextureView;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @Description: Created by yong on 2019/5/27 18:39.
 */

public class UVCDetectActivity extends BaseActivity implements CameraInterfaceBak.CameraStateCallBack, DetectView, UVCCameraInterfaceBak2.CameraStateCallBack {

    private RelativeLayout mOther;
    private RecyclerView mRecyclerView;
    private MaskView mMaskView;
    private CameraSurfaceView mSurfaceView;
    private UVCCameraTextureView mUVCCameraView;
    private UVCCameraTextureView mUVCCameraView2;
    private TextView mTextView;
    private TextView mDebug;
    private ImageView mBack;
    private ImageView mChangeCamera;
    private DetectPresenter mPresenter;
    private DetectAdapter mDetectAdapter;
    private ArrayList<DetectFaceBean> detectList;
    private DetectHandler mHandler;
    private int mPreviewWidth;
    private int mPreviewHeight;
    private int mPreviewWidth2;
    private int mPreviewHeight2;

    private final static int MSG_DETECT_NO_FACE = 0;
    private final static int MSG_DETECT_FAIL = 1;
    private final static int MSG_DETECT_DATA = 2;
    private final static int MSG_FACE = 3;

    private TextView mTvRegister;
    private TextView mTvCompare;
    private TextView mTvCompareCancel;
    private TextView mTvCompareMsg;
    private ImageView mImCompareWait;
    private ImageView mImCompareSucc;

    boolean isAsyncDoing = false;
    private SurfaceHolder mSurfaceHolder;
    private String mInfraredMsg = "No Face";
    private FrameLayout mFl_uvc;
    private FrameLayout mFl_uvc2;

    void doAsync() {
        isAsyncDoing = true;
    }

    void doAsyncDone() {
        isAsyncDoing = false;
    }

    ArrayList<Float> features = new ArrayList<>();

    @Override
    public void cameraHasOpened2() {

    }

    @Override
    public void cameraHasParameters2() {
        mPreviewWidth2 = UVCCameraInterfaceBak2.getInstance().getPreviewWidth();
        mPreviewHeight2 = UVCCameraInterfaceBak2.getInstance().getPreviewHeight();
         if(!AttConstants.PORTRAIT_LANDSCAPE){
            //RGB全屏显示时红外在左下角宽高为全屏1/5
            int screenWidth = ScreenUtils.getScreenWidth();
            int screenHeight = ScreenUtils.getScreenHeight();
            final RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mFl_uvc2.getLayoutParams();
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            layoutParams.width = screenWidth / 5;
            layoutParams.height = screenHeight / 5;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mFl_uvc2.setLayoutParams(layoutParams);
                }
            });
        }
    }

    @Override
    public void cameraHasPreview2(byte[] data, Camera camera) {
        LogUtils.d(DetectPresenterImpl.HEAD, "Begin2 -> ( w = " + mPreviewWidth2 + " h = " + mPreviewHeight2 + " size = " + data.length + " )");
        mPresenter.detectInfraredLiveData(data, mPreviewWidth2, mPreviewHeight2, AttConstants.CAMERA_DEGREE_INFRARED);
    }

    private static class DetectHandler extends Handler {

        final WeakReference<UVCDetectActivity> mActivity;

        public DetectHandler(UVCDetectActivity detectActivity) {
            mActivity = new WeakReference<>(detectActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case MSG_DETECT_DATA:
                    mActivity.get().updateMessgae((String) msg.obj);
                    break;

                case MSG_DETECT_FAIL:
                    mActivity.get().updateMessgae(((Status) msg.obj).toString());
                    mActivity.get().mMaskView.clearRect();
                    break;

                case MSG_DETECT_NO_FACE:
                    mActivity.get().updateMessgae(mActivity.get().getResources().getString(R.string.no_face));
                    mActivity.get().mMaskView.clearRect();
                    break;

                case MSG_FACE:
                    Bitmap bitmap = (Bitmap) msg.obj;
                    Glide.with(mActivity.get()).load(bitmap).into(mActivity.get().mImCompareSucc);
                    break;

            }
        }
    }

    void updateMessgae(String s) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("w = " + mPreviewWidth + " h = " + mPreviewHeight + getInfraredMsg());
        stringBuilder.append("\n");
        stringBuilder.append(s);
        mTextView.setText(stringBuilder.toString());
    }

    @NonNull
    private String getInfraredMsg() {
        if (!ConfigLib.doubleCameraWithInfraredLiveness) return "";

        return " || Infrared " + mInfraredMsg;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_detect;
    }

    @Override
    public void initViews() {
        AttApp.hideBottomUIMenu(this);
        mOther = findViewById(R.id.other);
        mChangeCamera = findViewById(R.id.exchangeCamera);
        mTvRegister = findViewById(R.id.tvregister);
        mTvCompare = findViewById(R.id.tvcompare);
        mTvCompareCancel = findViewById(R.id.tvcomparecancel);
        mTvCompareMsg = findViewById(R.id.tvcomparemsg);
        mImCompareWait = findViewById(R.id.imcomparewait);
        mImCompareSucc = findViewById(R.id.imcomparesucc);
        mSurfaceView = findViewById(R.id.sv);
        mSurfaceView.setVisibility(View.GONE);
        mUVCCameraView = findViewById(R.id.uvc);
        mUVCCameraView.setVisibility(View.VISIBLE);
        mUVCCameraView2 = findViewById(R.id.uvc2);
        mRecyclerView = findViewById(R.id.rv);
        mBack = findViewById(R.id.back);
        mTextView = findViewById(R.id.message);
        mDebug = findViewById(R.id.debugmessage);
        mMaskView = findViewById(R.id.kcfmv);
        mTextView.setMovementMethod(ScrollingMovementMethod.getInstance());
        mFl_uvc = (FrameLayout) findViewById(R.id.fl_uvc);
        mFl_uvc2 = (FrameLayout) findViewById(R.id.fl_uvc2);
        if (AttConstants.PORTRAIT_LANDSCAPE) {
            //设备竖屏，预览横的时，红外预览view位置
            final RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mFl_uvc2.getLayoutParams();
            params.addRule(RelativeLayout.BELOW, mFl_uvc.getId());
            mFl_uvc2.setLayoutParams(params);
        }
    }

    @Override
    public void initData() {
        mHandler = new DetectHandler(this);
        mPresenter = new DetectPresenterImpl(this);
        FaceDetectManager.setDegree(AttConstants.CAMERA_DEGREE);
        mMaskView.setLandScape();
        UVCCameraInterfaceBak1.getInstance().setCameraStateCallBack(this);
        setPreViewSize();
        if (AttConstants.PORTRAIT_LANDSCAPE){
            //竖屏预览是横的时可设置preview与相机预览比例一致
            mUVCCameraView.setAspectRatio(mPreviewWidth, mPreviewHeight);
        }
        UVCCameraInterfaceBak1.getInstance().init(UVCDetectActivity.this, mUVCCameraView);
        if (ConfigLib.doubleCameraWithInfraredLiveness) {
            ((View) mUVCCameraView2).setVisibility(View.VISIBLE);
            UVCCameraInterfaceBak2.getInstance().setCameraStateCallBack(this);
            setPreViewSize();
            if (AttConstants.PORTRAIT_LANDSCAPE){
                mUVCCameraView2.setAspectRatio(mPreviewWidth, mPreviewHeight);
            }
            UVCCameraInterfaceBak2.getInstance().init(UVCDetectActivity.this, mUVCCameraView2);
        }
        LinearLayoutManager detectRvManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(detectRvManager);
        detectList = new ArrayList<>();
        detectList.clear();
        mDetectAdapter = new DetectAdapter(detectList);
        mRecyclerView.setAdapter(mDetectAdapter);
        if (!ScreenUtils.isLandscape()) {
            mOther.setVisibility(View.GONE);
        }
        if (AttConstants.cameraCount == 1) {
            mChangeCamera.setVisibility(View.GONE);
        }
    }

    @Override
    public void initListeners() {
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AttConstants.Detect_Exception = false;
                LogUtils.d(TAG, "DetectActivity_mBack_setOnClickListener");
                UVCDetectActivity.this.finish();
            }
        });
        mTvCompare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAsyncDoing) {
                    return;
                }
                deal(true);
            }
        });
        mTvCompareCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetCompare();
            }
        });
        mTvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAsyncDoing) {
                    return;
                }
                deal(false);
            }
        });
        mChangeCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exchangeCamera();
            }
        });
    }

    private void deal(final boolean isCompare) {
        doAsync();
        List<File> files = FileUtils.listFilesInDir(AttConstants.PATH_CARD);
        File file;
        if (files.size() > 0) {
            Random random = new Random();
            int i = random.nextInt(files.size());
            file = files.get(i);
        } else {
            doAsyncDone();
            return;
        }
        resetCompare();
        final String name = BulkRegistPresenterImpl.getName(file.getName());
        showSlotCardState(getResources().getString(R.string.parse_begin) + " : " + name);
        Glide.with(UVCDetectActivity.this).load(file).into(mImCompareWait);
        final Bitmap bitmap = ImageUtils.getBitmap(file);
        LogUtils.d(DetectPresenterImpl.HEAD, "extractFeatureASync Parsing");
        FaceDetectManager.extractFeatureASync(bitmap, new ExtractFeatureListener() {

            @Override
            public void onSuccess(DetectBean detectBean, ArrayList<Float> floats) {
                LogUtils.d(DetectPresenterImpl.HEAD, "extractFeatureASync success");
                showSlotCardState(getResources().getString(R.string.parse_success));
                if (isCompare) {
                    features.clear();
                    features.addAll(floats);
                    doAsyncDone();
                } else {
                    register(bitmap, detectBean, floats, name);
                }
            }

            @Override
            public void onError(final Status code) {
                LogUtils.d(DetectPresenterImpl.HEAD, "extractFeatureASync fail : " + code.toString());
                showSlotCardState(getResources().getString(R.string.parse_fail) + " : " + code.toString());
                doAsyncDone();
            }
        });
    }

    private void register(final Bitmap bitmap, final DetectBean detectBean, final ArrayList<Float> floats, final String name) {
        ThreadPoolUtils.executeTask(new Runnable() {
            @Override
            public void run() {
                FaceDetectManager.registerUser(AttConstants.REGISTER_DEFAULT ? "" : AttConstants.EXDB, bitmap, detectBean, new RegisterBean(name), floats, new RegisterListener() {
                    @Override
                    public void onSuccess(UserBean userBean) {
                        LogUtils.d(DetectPresenterImpl.HEAD, "Register success");
                        showSlotCardState(getResources().getString(R.string.register_success) + " : " + userBean.name);
                        doAsyncDone();
                    }

                    @Override
                    public void onSimilarity(UserBean userBean) {
                        LogUtils.d(DetectPresenterImpl.HEAD, "Register User fail Similarity : " + userBean.name + " " + userBean.userId);
                    }

                    @Override
                    public void onError(Status status) {
                        LogUtils.d(DetectPresenterImpl.HEAD, "Register fail : " + status.toString());
                        showSlotCardState(getResources().getString(R.string.register_fail) + " : " + status.toString());
                        doAsyncDone();
                    }
                });
            }
        });
    }

    private void resetCompare() {
        mImCompareWait.setImageDrawable(null);
        mImCompareSucc.setImageDrawable(null);
        mTvCompareMsg.setText("");
        features.clear();
    }

    @Override
    protected void onStart() {
        super.onStart();
        AttConstants.Detect_Exception = true;
        UVCCameraInterfaceBak1.getInstance().registerUSB();
    }

    @Override
    protected void onStop() {
        UVCCameraInterfaceBak2.getInstance().unregisterUSB();
        UVCCameraInterfaceBak1.getInstance().unregisterUSB();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d("onResume", "DetectActivity -> onDestroy > Detect_Exception " + AttConstants.Detect_Exception + " INIT_STATE " + AttConstants.INIT_STATE);
        mMaskView.unInit();
        detectList.clear();
        mHandler.removeCallbacksAndMessages(null);
        mHandler = null;
        mPresenter = null;
        mSurfaceView = null;
        ConfigLib.detectionFirstInitFlag = true;
        UVCCameraInterfaceBak2.getInstance().releaseCamera();
        UVCCameraInterfaceBak1.getInstance().releaseCamera();
        mUVCCameraView = null;
        mUVCCameraView2 = null;
        LogUtils.d(DetectPresenterImpl.HEAD, "RecognitionFace face destroy " + detectList.size());
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        ToastUtils.showShort(getResources().getString(R.string.reopen_activity));
        UVCDetectActivity.this.finish();
    }

    int initFrame = 1;
    int detectFrame = 4;
    int nowFrame = initFrame;
    boolean checkFrame = false;

    boolean check() {
        if (!checkFrame) {
            nowFrame++;
//            dissmisDialog();
            if (nowFrame >= detectFrame) {
                nowFrame = initFrame;
                checkFrame = true;
            }
        }
        return checkFrame;
    }

    @Override
    public void cameraHasOpened() {
        if (ConfigLib.doubleCameraWithInfraredLiveness) {
            UVCCameraInterfaceBak2.getInstance().registerUSB();
        }
    }

    @Override
    public void cameraHasParameters() {
        mPreviewWidth = UVCCameraInterfaceBak1.getInstance().getPreviewWidth();
        mPreviewHeight = UVCCameraInterfaceBak1.getInstance().getPreviewHeight();
    }

    @Override
    public void cameraHasPreview(byte[] data, Camera camera) {
        LogUtils.d(DetectPresenterImpl.HEAD, "Begin -> ( w = " + mPreviewWidth + " h = " + mPreviewHeight + " size = " + data.length + " )");
        if (check()) {
            mPresenter.detectFaceData(data, mPreviewWidth, mPreviewHeight);
        }
    }

    @Override
    public void recognizeFace(final UserBean userBean) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                synchronized (userBean) {
                    String time = TimeUtils.millis2String(System.currentTimeMillis());
                    DetectFaceBean detectFaceBean = new DetectFaceBean(userBean, time);
                    if (detectList.size() != 0 && StringUtils.equals(detectList.get(0).getUserBean().name, userBean.name)) {
                        LogUtils.d(DetectPresenterImpl.HEAD, "RecognitionFace face set new + " + detectList.size());
                        detectList.set(0, detectFaceBean);
                    } else {
                        LogUtils.d(DetectPresenterImpl.HEAD, "RecognitionFace face add new + " + detectList.size());
                        detectList.add(0, detectFaceBean);
                    }
                    if (detectList.size() > AttConstants.DETECT_LIST_SIZE) {
                        List<DetectFaceBean> list = new ArrayList<>();
                        list.clear();
                        list.addAll(detectList);
                        detectList.clear();
                        detectList.addAll(list.subList(0, AttConstants.DETECT_LIST_SIZE));
                    }
                    mDetectAdapter.replaceData(detectList);
                }
            }
        });
    }

    @Override
    public void recognizeFaceNotMatch(UserBean userBean) {
        if (features.size() > 0) {
            LogUtils.d(DetectPresenterImpl.HEAD, "extractFeatureASync find recognizeFaceNotMatch");
            ArrayList<Float> floatArrayList = new ArrayList<>();
            floatArrayList.clear();
            floatArrayList.addAll(features);
            final float compare = FaceDetectManager.compareFeature(FeatureUtils.arrayListToFloat(floatArrayList), FeatureUtils.arrayListToFloat(userBean.features));
            LogUtils.d(DetectPresenterImpl.HEAD, "extractFeatureASync find recognizeFaceNotMatch : " + compare);
            if (compare > ConfigLib.featureThreshold) {
                Message message = Message.obtain();
                message.obj = userBean.headImage;
                message.what = MSG_FACE;
                mHandler.sendMessage(message);
                features.clear();
                showSlotCardState(getResources().getString(R.string.match_success) + " : " + compare);
            } else {
                showSlotCardState(getResources().getString(R.string.match_fail) + " : " + compare);
            }

        }
    }

    private void showSlotCardState(final String state) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTvCompareMsg.setText(state);
            }
        });
    }

    @Override
    public void detectNoFace() {
        if (mHandler != null) {
            mHandler.sendEmptyMessage(MSG_DETECT_NO_FACE);
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                lastDebugText = System.currentTimeMillis();
                mDebug.setText("");
            }
        });
    }

    @Override
    public void detectFail(Status status) {
        Message message = Message.obtain();
        message.what = MSG_DETECT_FAIL;
        message.obj = status;
        if (mHandler != null) {
            mHandler.sendMessage(message);
        }
    }

    @Override
    public void detectFace(final List<FaceBean> faceBeans) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                synchronized (faceBeans) {
                    List<FaceBean> faceBeanList = new ArrayList<>();
                    faceBeanList.clear();
                    faceBeanList.addAll(faceBeans);
                    StringBuilder stringBuilder = new StringBuilder();
                    try {
                        for (int i = 0; i < faceBeanList.size(); i++) {
                            FaceBean bean = faceBeanList.get(i);
                            if (bean.mUserBean != null && !StringUtils.isEmpty(bean.mUserBean.name)) {
                                String name = bean.mUserBean.name;
                                stringBuilder.append("< Find " + name + " >");
                                stringBuilder.append("\n");
                            } else {
                                String find = "";
                                if (ConfigLib.detectWithLiveness || ConfigLib.detectWithInfraredLiveness) {
                                    if (bean.mLiveBean != null && bean.mLiveBean.livenessTag == bean.mLiveBean.UNKNOWN) {
                                        find = "UNKNOWN";
                                    } else if (bean.mLiveBean != null && bean.mLiveBean.livenessTag == bean.mLiveBean.FAKE) {
                                        find = "FAKE";
                                    } else {
                                        find = bean.mUserBean.compareScore + "";
                                    }
                                } else {
                                    if (bean.mUserBean != null) {
                                        find = bean.mUserBean.compareScore + "";
                                    }
                                }
                                stringBuilder.append("< Find " + find + " >");
                                stringBuilder.append("\n");
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    sendDebugMessage(stringBuilder.toString());
                    mMaskView.drawRect(faceBeanList, mPreviewWidth, mPreviewHeight);
                    if (System.currentTimeMillis() - lastDebugText > 1000) {
                        mDebug.setText("");
                    }
                }
            }
        });
    }

    long lastDebugText = 0;

    @Override
    public void debug(final FaceBean faceBean) {
        if (faceBean != null && faceBean.mDetectBean != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    lastDebugText = System.currentTimeMillis();
                    mDebug.setText("Remove :" + "\n" + "ID = " + faceBean.mDetectBean.id + "\n" + "Reason = " + faceBean.mDetectBean.faceState);
                }
            });
        }
    }

    @Override
    public void detectInfraredInfo(String faceMsg) {
        mInfraredMsg = faceMsg;
    }

    public void sendDebugMessage(String s) {
        Message message = Message.obtain();
        message.what = MSG_DETECT_DATA;
        message.obj = s;
        if (mHandler != null) {
            mHandler.sendMessage(message);
        }
    }

    public void setPreViewSize() {
        switch (AttConstants.CAMERA_PREVIEW_HEIGHT) {

            case 0:
                UVCCameraInterfaceBak1.getInstance().setPreViewSize(0, 0);
                if (ConfigLib.doubleCameraWithInfraredLiveness) {
                    UVCCameraInterfaceBak2.getInstance().setPreViewSize(0, 0);
                }
                break;

            case 480:
                UVCCameraInterfaceBak1.getInstance().setPreViewSize(640, 480);
                if (ConfigLib.doubleCameraWithInfraredLiveness) {
                    UVCCameraInterfaceBak2.getInstance().setPreViewSize(640, 480);
                }
                break;
            case 640:
                UVCCameraInterfaceBak1.getInstance().setPreViewSize(480, 640);
                if (ConfigLib.doubleCameraWithInfraredLiveness) {
                    UVCCameraInterfaceBak2.getInstance().setPreViewSize(480, 640);
                }
                break;
            case 720:
                UVCCameraInterfaceBak1.getInstance().setPreViewSize(1280, 720);
                if (ConfigLib.doubleCameraWithInfraredLiveness) {
                    UVCCameraInterfaceBak2.getInstance().setPreViewSize(1280, 720);
                }
                break;
            case 1280:
                UVCCameraInterfaceBak1.getInstance().setPreViewSize(720, 1080);
                if (ConfigLib.doubleCameraWithInfraredLiveness) {
                    UVCCameraInterfaceBak2.getInstance().setPreViewSize(720, 1080);
                }
                break;
            case 1080:
                UVCCameraInterfaceBak1.getInstance().setPreViewSize(1920, 1080);
                if (ConfigLib.doubleCameraWithInfraredLiveness) {
                    UVCCameraInterfaceBak2.getInstance().setPreViewSize(1920, 1080);
                }
                break;
            case 1920:
                UVCCameraInterfaceBak1.getInstance().setPreViewSize(1080, 1920);
                if (ConfigLib.doubleCameraWithInfraredLiveness) {
                    UVCCameraInterfaceBak2.getInstance().setPreViewSize(1080, 1920);
                }
                break;
            case 960:
                UVCCameraInterfaceBak1.getInstance().setPreViewSize(1280, 960);
                if (ConfigLib.doubleCameraWithInfraredLiveness) {
                    UVCCameraInterfaceBak2.getInstance().setPreViewSize(1280, 960);
                }
                break;
            case 1280*2:
                UVCCameraInterfaceBak1.getInstance().setPreViewSize(960, 1280);
                if (ConfigLib.doubleCameraWithInfraredLiveness) {
                    UVCCameraInterfaceBak2.getInstance().setPreViewSize(960, 1280);
                }
                break;

        }
    }

    private void exchangeCamera() {
        if (AttConstants.CAMERA_ID == 0) {
            AttConstants.CAMERA_ID = 1;
        } else {
            AttConstants.CAMERA_ID = 0;
        }
        AttApp.sp.edit().putInt(AttConstants.PREFS_CAMERA_ID, AttConstants.CAMERA_ID).commit();
//        setPreViewSize();
    }

    // 退出按键
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AttConstants.Detect_Exception = false;
        LogUtils.d(TAG, "DetectActivity_onBackPressed");

    }
}
