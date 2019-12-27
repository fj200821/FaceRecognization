package com.aiwinn.faceattendance.ui.m;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aiwinn.base.activity.BaseActivity;
import com.aiwinn.base.log.LogUtils;
import com.aiwinn.base.util.ImageUtils;
import com.aiwinn.base.util.ScreenUtils;
import com.aiwinn.base.util.ToastUtils;
import com.aiwinn.base.widget.AttPopwindow;
import com.aiwinn.base.widget.CameraInterfaceBak;
import com.aiwinn.base.widget.CameraSurfaceView;
import com.aiwinn.faceattendance.R;
import com.aiwinn.faceattendance.bean.RegisterFaceInfo;
import com.aiwinn.faceattendance.common.AttConstants;
import com.aiwinn.faceattendance.ui.p.YuvRegistPresenter;
import com.aiwinn.faceattendance.ui.p.YuvRegistPresenterImpl;
import com.aiwinn.faceattendance.ui.v.YuvRegistView;
import com.aiwinn.faceattendance.utils.FaceUtils;
import com.aiwinn.faceattendance.utils.TimerUtils;
import com.aiwinn.faceattendance.utils.UVCCameraInterfaceBak1;
import com.aiwinn.faceattendance.widget.PierceMaskView;
import com.aiwinn.facedetectsdk.FaceDetectManager;
import com.aiwinn.facedetectsdk.bean.DetectBean;
import com.aiwinn.facedetectsdk.bean.RegisterBean;
import com.aiwinn.facedetectsdk.bean.UserBean;
import com.aiwinn.facedetectsdk.common.ConfigLib;
import com.aiwinn.facedetectsdk.common.FaceState;
import com.aiwinn.facedetectsdk.common.Status;
import com.bumptech.glide.Glide;
import com.serenegiant.usb.widget.UVCCameraTextureView;

import java.lang.ref.WeakReference;

/**
 * @Description: Created by yong on 2019/5/27 18:40.
 */

public class UVCYuvRegistActivity extends BaseActivity implements CameraInterfaceBak.CameraStateCallBack, YuvRegistView {

    private static final int MSG_DETECT_NO_FACE = 101;
    private static final int MSG_DETECT_NOT_CENTER = 102;
    private static final int MSG_DETECT_ERROR = 100;
    private static final int MSG_DETECT_FACE_INFO = 103;
    private static final int MSG_REGISTER_ERROR = 104;
    private static final int MSG_REGISTER_SUCCESS = 105;

    private CameraSurfaceView mCsvRegister;
    private int mPreviewWidth;
    private int mPreviewHeight;
    private PierceMaskView mPmvRegister;
    private TextView mTvNotify;
    private YuvRegistPresenter mPresenter;
    private Handler mHandler;
    private RelativeLayout mRlRoot;
    private AttPopwindow mPopupWindow;
    private EditText mPopWindowEditText;
    private ImageView mPopWindowImageView;
    private boolean mIsShowPopWindow;
    private TextView mCancelTextView;
    private TextView mSureTextView;
    private ImageView mBack;
    private boolean mLandScape;
    private UVCCameraTextureView mUVCCameraView;
    private TimerUtils mTimer;
    private boolean mFirstTimerTag;

    @Override
    public int getLayoutId() {
        return R.layout.activity_yuvregist;
    }

    @Override
    public void initViews() {
        mCsvRegister = findViewById(R.id.csv_register);
        mCsvRegister.setVisibility(View.GONE);
        mUVCCameraView = findViewById(R.id.uvc_register);
        mUVCCameraView.setVisibility(View.VISIBLE);
        mPmvRegister = findViewById(R.id.pmv_register);
        mPmvRegister.setVisibility(View.GONE);
        mTvNotify = findViewById(R.id.tv_notify);
        mRlRoot = findViewById(R.id.rl_register_root);
        mBack = findViewById(R.id.back);
    }

    @Override
    public void initData() {
        mIsShowPopWindow = false;
        mHandler = new RegisterHandler(this);
        mPresenter = new YuvRegistPresenterImpl(this);
        mLandScape = ScreenUtils.isLandscape();
        int mPreviewWidth = ScreenUtils.getScreenWidth();
        int mPreviewHeight = ScreenUtils.getScreenHeight();
        int mCenterX;
        int mCenterY;
        int mRadius;
        if (mLandScape) {
            mCenterX = mPreviewWidth / 2;
            mCenterY = mPreviewHeight / 2;
            mRadius = mPreviewHeight / 2 - 60;
        } else {
            mCenterX = mPreviewWidth / 2;
            mCenterY = mPreviewHeight / 2;
            mRadius = mPreviewWidth / 2 - 60;
        }
        mPmvRegister.setPiercePosition(mCenterX, mCenterY, mRadius);
        UVCCameraInterfaceBak1.getInstance().setCameraStateCallBack(this);
        setPreViewSize();
        UVCCameraInterfaceBak1.getInstance().init(UVCYuvRegistActivity.this, mUVCCameraView);
        mTimer = new TimerUtils(6000, 1000);
        mTimer.setOnTickListener(new TimerUtils.OnTickListener() {
            @Override
            public void onTick(long seconds) {
                updateFaceInfo(String.format(getString(R.string.ready_to_register), seconds));
                if (seconds == 0) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            updateFaceInfo(getString(R.string.start_register));
                        }
                    }, 300);
                }
            }

            @Override
            public void onFinish() {
                mPresenter.dealFaceInfoFinish();
            }
        });
    }

    @Override
    public void initListeners() {
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        UVCCameraInterfaceBak1.getInstance().registerUSB();
    }

    @Override
    protected void onStop() {
        super.onStop();
        UVCCameraInterfaceBak1.getInstance().unregisterUSB();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ConfigLib.picScaleRate = 0;
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
        UVCCameraInterfaceBak1.getInstance().releaseCamera();
        if (mTimer != null){
            mTimer.cancel();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        ToastUtils.showShort(getResources().getString(R.string.reopen_activity));
        UVCYuvRegistActivity.this.finish();
    }

    @Override
    public void cameraHasOpened() {
    }

    public void setPreViewSize() {
        switch (AttConstants.CAMERA_PREVIEW_HEIGHT) {

            case 0:
                UVCCameraInterfaceBak1.getInstance().setPreViewSize(0, 0);
                break;

            case 480:
                UVCCameraInterfaceBak1.getInstance().setPreViewSize(640, 480);
                break;
            case 640:
                UVCCameraInterfaceBak1.getInstance().setPreViewSize(480, 640);
                break;

            case 720:
                UVCCameraInterfaceBak1.getInstance().setPreViewSize(1280, 720);
                break;
            case 1280:
                UVCCameraInterfaceBak1.getInstance().setPreViewSize(720, 1080);
                break;

            case 1080:
                UVCCameraInterfaceBak1.getInstance().setPreViewSize(1920, 1080);
                break;
            case 1920:
                UVCCameraInterfaceBak1.getInstance().setPreViewSize(1080, 1920);
                break;

            case 960:
                UVCCameraInterfaceBak1.getInstance().setPreViewSize(1280, 960);
                break;
            case 1280*2:
                UVCCameraInterfaceBak1.getInstance().setPreViewSize(960, 1280);
                break;
        }
    }

    @Override
    public void cameraHasParameters() {
        mPreviewWidth = UVCCameraInterfaceBak1.getInstance().getPreviewWidth();
        mPreviewHeight = UVCCameraInterfaceBak1.getInstance().getPreviewHeight();
        FaceDetectManager.setDegree(AttConstants.CAMERA_DEGREE);
        ConfigLib.picScaleRate = mPreviewWidth > mPreviewHeight ? (float) ConfigLib.Nv21ToBitmapScale / (float) mPreviewWidth : (float) ConfigLib.Nv21ToBitmapScale / (float) mPreviewHeight;
    }

    @Override
    public void cameraHasPreview(byte[] data, Camera camera) {
        dissmisDialog();
        if (!mFirstTimerTag){
            mFirstTimerTag = true;
            mTimer.start();
        }
        mPresenter.registerFace(data, mPreviewWidth, mPreviewHeight);
    }

    @Override
    public void onError(Status status) {
        Message message = Message.obtain();
        message.what = MSG_DETECT_ERROR;
        message.obj = status;
        if (mHandler != null) {
            mHandler.sendMessage(message);
        }
    }

    @Override
    public void noFace() {
        if (mHandler != null) {
            mHandler.sendEmptyMessage(MSG_DETECT_NO_FACE);
        }
        mPresenter.dealFaceInfoFinish();
    }

    @Override
    public void notCenter() {
        if (mHandler != null) {
            mHandler.sendEmptyMessage(MSG_DETECT_NOT_CENTER);
        }
        mPresenter.dealFaceInfoFinish();
    }

    @Override
    public void faceInfo(Bitmap src, DetectBean maxFace) {
        Message message = Message.obtain();
        message.what = MSG_DETECT_FACE_INFO;
        message.obj = new RegisterFaceInfo(src, maxFace);
        if (mHandler != null) {
            mHandler.sendMessage(message);
        }
    }

    @Override
    public void onRegisterSuccess(UserBean userBean) {
        if (mHandler != null) {
            mHandler.sendEmptyMessage(MSG_REGISTER_SUCCESS);
        }
    }

    @Override
    public void onRegisterError(Status status) {
        Message message = Message.obtain();
        message.what = MSG_REGISTER_ERROR;
        message.obj = status;
        if (mHandler != null) {
            mHandler.sendMessage(message);
        }
    }

    private static class RegisterHandler extends Handler {

        private WeakReference<UVCYuvRegistActivity> mActivity;

        public RegisterHandler(UVCYuvRegistActivity activity) {
            super();
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_DETECT_NO_FACE:
                    mActivity.get().mTvNotify.setText(mActivity.get().getResources().getString(R.string.no_face));
                    break;
                case MSG_DETECT_NOT_CENTER:
                    mActivity.get().mTvNotify.setText(mActivity.get().getResources().getString(R.string.not_center));
                    break;
                case MSG_DETECT_ERROR:
                    Status status = (Status) msg.obj;
                    mActivity.get().mTvNotify.setText(status.toString());
                    break;
                case MSG_DETECT_FACE_INFO:
                    RegisterFaceInfo face = (RegisterFaceInfo) msg.obj;
                    dealFaceInfo(face);
                    break;
                case MSG_REGISTER_ERROR:
                    Status result = (Status) msg.obj;
                    mActivity.get().registerResult(result);
                    break;
                case MSG_REGISTER_SUCCESS:
                    mActivity.get().registerSuccess();
                    break;
                default:
                    break;
            }
        }

        private void dealFaceInfo(RegisterFaceInfo face) {
            UVCYuvRegistActivity activity = mActivity.get();
            FaceState positionState = face.getDetectBean().faceState;
            switch (positionState) {
                case OK:
                    if (!mActivity.get().mIsShowPopWindow) {
                        mActivity.get().mTvNotify.setText(mActivity.get().getResources().getString(R.string.register));
                        mActivity.get().showPopWindow(face);
                    }
                    break;
                case BOW:
                    activity.updateFaceInfo(mActivity.get().getResources().getString(R.string.rise));
                    activity.mPresenter.dealFaceInfoFinish();
                    break;
                case RISE:
                    activity.updateFaceInfo(mActivity.get().getResources().getString(R.string.down));
                    activity.mPresenter.dealFaceInfoFinish();
                    break;
                case LEFT_DEVIATION:
                    activity.updateFaceInfo(mActivity.get().getResources().getString(R.string.keep_left));
                    activity.mPresenter.dealFaceInfoFinish();
                    break;
                case RIGHT_DEVIATION:
                    activity.updateFaceInfo(mActivity.get().getResources().getString(R.string.keep_right));
                    activity.mPresenter.dealFaceInfoFinish();
                    break;
            }
        }

    }

    private void updateFaceInfo(String msg) {
        mTvNotify.setText(msg);
//        mPresenter.dealFaceInfoFinish();
    }

    private void registerSuccess() {
        ToastUtils.showShort(getResources().getString(R.string.register_success));
        mPopupWindow.dissmiss();
        UVCYuvRegistActivity.this.finish();
    }

    private void registerResult(Status status) {

        switch (status) {
            case Ok:
                ToastUtils.showShort(getResources().getString(R.string.register_success));
                mPopupWindow.dissmiss();
                UVCYuvRegistActivity.this.finish();
                break;
            default:
                notifyResult(status);
                break;
        }
    }

    private void notifyResult(Status status) {
        ToastUtils.showShort(status.toString());
        mPopupWindow.dissmiss();
    }

    private void showPopWindow(final RegisterFaceInfo faceInfo) {
        mIsShowPopWindow = true;
        View contentView = LayoutInflater.from(this).inflate(R.layout.register_popwindow, null);
        mPopupWindow = new AttPopwindow.PopupWindowBuilder(UVCYuvRegistActivity.this)
                .setView(contentView)
                .enableOutsideTouchableDissmiss(false)
                .create()
                .showAtLocation(UVCYuvRegistActivity.this.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
        mPopWindowEditText = contentView.findViewById(R.id.et_register_name);
        mPopWindowImageView = contentView.findViewById(R.id.iv_register_pop);
        mCancelTextView = contentView.findViewById(R.id.btn_register_cancel);
        mSureTextView = contentView.findViewById(R.id.btn_register_sure);
        mCancelTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dissmiss();
            }
        });
        mSureTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mPopWindowEditText.getText().toString().trim();
                if (TextUtils.isEmpty(name)) {
                    ToastUtils.showShort(getResources().getString(R.string.name_empty));
                } else {
                    RegisterBean registerBean = new RegisterBean(name);
                    mPresenter.saveRegisterInfo(faceInfo.getSrc(), faceInfo.getDetectBean(), registerBean);
                }
            }
        });
        Bitmap clipBitmap = FaceUtils.createBitmapfromDetectBean(faceInfo.getDetectBean(), faceInfo.getSrc());
        if (AttConstants.CAMERA_ID == 1 && AttConstants.CAMERA_DEGREE == 90) {
            Bitmap rotBitmap = ImageUtils.rotate(clipBitmap, 180, 0, 0);
            clipBitmap = Bitmap.createBitmap(rotBitmap);
        }
        Glide.with(this).load(clipBitmap).into(mPopWindowImageView);
        mPopupWindow.getPopupWindow().setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                LogUtils.d(YuvRegistPresenterImpl.HEAD, "popwindow dismiss");
                mIsShowPopWindow = false;
                mTimer.start();
//                mPresenter.dealFaceInfoFinish();
            }
        });
    }
}
