package com.aiwinn.faceattendance.utils;

import android.app.Activity;
import android.hardware.usb.UsbDevice;
import android.view.Surface;

import com.aiwinn.base.module.log.LogUtils;
import com.aiwinn.base.util.ToastUtils;
import com.aiwinn.base.widget.CameraInterfaceBak;
import com.aiwinn.faceattendance.common.AttConstants;
import com.jiangdg.usbcamera.UVCCameraHelper;
import com.serenegiant.usb.UVCCamera;
import com.serenegiant.usb.common.AbstractUVCCameraHandler;
import com.serenegiant.usb.widget.CameraViewInterface;

public class UVCCameraInterfaceBak1 {

    private static final String TAG = UVCCameraInterfaceBak1.class.getSimpleName();
    private static final UVCCameraInterfaceBak1 instance = new UVCCameraInterfaceBak1();
    private CameraInterfaceBak.CameraStateCallBack mCameraStateCallBack;
    public int mPreviewWidth = UVCCamera.DEFAULT_PREVIEW_WIDTH;
    public int mPreviewHeight = UVCCamera.DEFAULT_PREVIEW_HEIGHT;
    public boolean custom;
    private CameraViewInterface mPreviewView;
    private boolean isPreview;
    private boolean isRequest;
    private UVCCameraHelper mCameraHelper;

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
    private UVCCameraHelper.OnMyDevConnectListener listener = new UVCCameraHelper.OnMyDevConnectListener() {

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
                    mCameraHelper.requestPermission(AttConstants.CAMERA_ID);
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
                mCameraStateCallBack.cameraHasOpened();
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

    public static UVCCameraInterfaceBak1 getInstance() {
        return instance;
    }

    public void setCameraStateCallBack(CameraInterfaceBak.CameraStateCallBack var1) {
        this.mCameraStateCallBack = var1;
    }


    public void init(Activity activity, CameraViewInterface previewView) {
        mPreviewView = previewView;
        mPreviewView.setCallback(mCallback);
        mCameraHelper = UVCCameraHelper.getInstance();
        mCameraHelper.setDefaultPreviewSize(mPreviewWidth, mPreviewHeight);
        mCameraHelper.setDisplayOrientation(AttConstants.PREVIEW_DEGREE);
        mCameraHelper.setDefaultBandwidth(0.5f);
        mCameraHelper.setDefaultFrameFormat(UVCCameraHelper.FRAME_FORMAT_MJPEG);
        mCameraHelper.initUSBMonitor(activity, mPreviewView, listener);
        mCameraHelper.setOnPreviewFrameListener(new AbstractUVCCameraHandler.OnPreViewResultListener() {
            @Override
            public void onPreviewResult(byte[] nv21Yuv) {
                mCameraStateCallBack.cameraHasPreview(nv21Yuv, null);
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

    public void setPreViewSize(int var1, int var2) {
        if (var1 != 0 && var2 != 0) {
            this.mPreviewWidth = var1;
            this.mPreviewHeight = var2;
            this.custom = true;
        } else {
            this.custom = false;
        }
        if (mCameraStateCallBack != null) {
            mCameraStateCallBack.cameraHasParameters();
        }
    }

    public int getPreviewWidth() {
        return this.mPreviewWidth;
    }

    public int getPreviewHeight() {
        return this.mPreviewHeight;
    }
}
