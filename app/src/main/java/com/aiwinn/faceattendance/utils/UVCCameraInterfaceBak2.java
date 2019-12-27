package com.aiwinn.faceattendance.utils;

import android.app.Activity;
import android.hardware.Camera;
import android.hardware.usb.UsbDevice;
import android.view.Surface;

import com.aiwinn.base.module.log.LogUtils;
import com.aiwinn.base.util.ToastUtils;
import com.aiwinn.faceattendance.common.AttConstants;
import com.jiangdg.usbcamera.UVCCameraHelper;
import com.serenegiant.usb.UVCCamera;
import com.serenegiant.usb.common.AbstractUVCCameraHandler;
import com.serenegiant.usb.widget.CameraViewInterface;

public class UVCCameraInterfaceBak2 {

    private static final String TAG = UVCCameraInterfaceBak2.class.getSimpleName();
    private static final UVCCameraInterfaceBak2 instance = new UVCCameraInterfaceBak2();
    private CameraStateCallBack mCameraStateCallBack;
    public int mPreviewWidth = UVCCamera.DEFAULT_PREVIEW_WIDTH;
    public int mPreviewHeight = UVCCamera.DEFAULT_PREVIEW_HEIGHT;
    public boolean custom;
    private CameraViewInterface mPreviewView;
    private boolean isPreview;
    private boolean isRequest;
    private UVCCameraHelper2 mCameraHelper;

    private CameraViewInterface.Callback mCallback = new CameraViewInterface.Callback() {
        @Override
        public void onSurfaceCreated(CameraViewInterface cameraViewInterface, Surface surface) {
            if (!isPreview && mCameraHelper.isCameraOpened()) {
                LogUtils.d(TAG, "startPreview");
                mCameraHelper.startPreview(mPreviewView);
                isPreview = true;
            }
        }

        @Override
        public void onSurfaceChanged(CameraViewInterface cameraViewInterface, Surface surface, int i, int i1) {

        }

        @Override
        public void onSurfaceDestroy(CameraViewInterface cameraViewInterface, Surface surface) {
            if (isPreview && mCameraHelper.isCameraOpened()) {
                LogUtils.d(TAG, "stopPreview");
                mCameraHelper.stopPreview();
                isPreview = false;
            }
        }
    };
    private UVCCameraHelper2.OnMyDevConnectListener listener = new UVCCameraHelper2.OnMyDevConnectListener() {

        @Override
        public void onAttachDev(UsbDevice device) {
            LogUtils.d(TAG, "onAttachDev");
            if (mCameraHelper == null || mCameraHelper.getUsbDeviceCount() == 0) {
                ToastUtils.showShort("check no usb camera");
                LogUtils.d(TAG, "check no usb camera");
                return;
            }
            // request open permission
            if (!isRequest) {
                isRequest = true;
                if (mCameraHelper != null) {
                    LogUtils.d(TAG, "requestPermission");
                    mCameraHelper.requestPermission(AttConstants.CAMERA_ID_INFRARED);
                }
            }
        }

        @Override
        public void onDettachDev(UsbDevice device) {
            LogUtils.d(TAG, "onDettachDev");
            // close camera
            if (isRequest) {
                isRequest = false;
                mCameraHelper.closeCamera();
                ToastUtils.showShort(device.getDeviceName() + " is out");
            }
        }

        @Override
        public void onConnectDev(UsbDevice device, boolean isConnected) {
            LogUtils.d(TAG, "onConnectDev");
            if (!isConnected) {
                ToastUtils.showShort("fail to connect,please check resolution params");
                isPreview = false;
            } else {
                isPreview = true;
                mCameraStateCallBack.cameraHasOpened2();
                ToastUtils.showShort("connecting");
                // need to wait UVCCamera initialize over
            }
        }

        @Override
        public void onDisConnectDev(UsbDevice device) {
            LogUtils.d(TAG, "onDisConnectDev");
            ToastUtils.showShort("disconnecting");
            mCameraHelper.closeCamera();
            isRequest = false;
        }
    };

    public static UVCCameraInterfaceBak2 getInstance() {
        return instance;
    }

    public void setCameraStateCallBack(CameraStateCallBack var1) {
        this.mCameraStateCallBack = var1;
    }


    public void init(Activity activity, CameraViewInterface previewView) {
        mPreviewView = previewView;
        mPreviewView.setCallback(mCallback);
        mCameraHelper = UVCCameraHelper2.getInstance();
        mCameraHelper.setDefaultPreviewSize(mPreviewWidth, mPreviewHeight);
        mCameraHelper.setDefaultBandwidth(0.5f);
        mCameraHelper.setDefaultFrameFormat(UVCCameraHelper.FRAME_FORMAT_MJPEG);
        mCameraHelper.setDisplayOrientation(AttConstants.PREVIEW_DEGREE_INFRARED);
        mCameraHelper.initUSBMonitor(activity, mPreviewView, listener);
        mCameraHelper.setOnPreviewFrameListener(new AbstractUVCCameraHandler.OnPreViewResultListener() {
            @Override
            public void onPreviewResult(byte[] nv21Yuv) {
                mCameraStateCallBack.cameraHasPreview2(nv21Yuv, null);
            }
        });
    }

    public void registerUSB() {
        if (mCameraHelper != null) {
            mCameraHelper.registerUSB();
        }
    }

    public void unregisterUSB() {
        if (mCameraHelper != null) {
            mCameraHelper.unregisterUSB();
        }
    }

    public void releaseCamera() {
        if (mCameraHelper != null) {
            mCameraHelper.release();
        }
    }

    public void setPreViewSize(int width, int height) {
        if (width != 0 && height != 0) {
            this.mPreviewWidth = width;
            this.mPreviewHeight = height;
            this.custom = true;
        } else {
            this.custom = false;
        }
        if (mCameraStateCallBack != null) {
            mCameraStateCallBack.cameraHasParameters2();
        }
    }

    public int getPreviewWidth() {
        return this.mPreviewWidth;
    }

    public int getPreviewHeight() {
        return this.mPreviewHeight;
    }

    public interface CameraStateCallBack {
        void cameraHasOpened2();

        void cameraHasParameters2();

        void cameraHasPreview2(byte[] bytes, Camera camera);
    }
}
