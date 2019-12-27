package com.aiwinn.faceattendance.utils;

import android.hardware.Camera;
import android.view.SurfaceHolder;

import com.aiwinn.base.log.LogUtils;
import com.aiwinn.base.util.ScreenUtils;
import com.aiwinn.base.utils.CameraParametersUtils;

import java.io.IOException;

/**
 * @Description: Created by yong on 2019/4/11 15:36.
 */
public class CameraInterfaceBak2 {
    public static final String HEAD = "CameraInterfaceBak2";
    private static CameraInterfaceBak2 mCameraInterface;
    private CameraStateCallBack mCameraStateCallBack;
    public Camera mCamera;
    public Camera.Parameters mParams;
    public int mPreviewWidth;
    public int mPreviewHeight;
    public int mDegree = 0;
    public int mCameraId;
    public boolean custom;
    boolean exception = false;
    Camera.PreviewCallback mPreviewCallback = new Camera.PreviewCallback() {
        public void onPreviewFrame(byte[] data, Camera camera) {
            CameraInterfaceBak2.this.mCameraStateCallBack.cameraHasPreview2(data, camera);
        }
    };

    public void setCameraStateCallBack(CameraStateCallBack callBack) {
        this.mCameraStateCallBack = callBack;
    }

    private CameraInterfaceBak2() {
    }

    public static synchronized CameraInterfaceBak2 getInstance() {
        if (mCameraInterface == null) {
            mCameraInterface = new CameraInterfaceBak2();
        }

        return mCameraInterface;
    }

    public void doOpenCamera(int cameraId, int degree) {
        try {
            this.mCameraId = cameraId;
            this.mDegree = degree;
            this.mCamera = Camera.open(this.mCameraId);
            this.mCamera.setDisplayOrientation(this.mDegree);
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        this.mCameraStateCallBack.cameraHasOpened2();
    }

    public void doStartPreview(SurfaceHolder surfaceHolder) {
        if (this.mCamera != null) {
            if (surfaceHolder != null) {
                try {
                    this.mCamera.setPreviewDisplay(surfaceHolder);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            this.initCameraParameters();
        }

    }

    public void doStopCamera() {
        if (null != this.mCamera) {
            this.mCamera.setPreviewCallback((Camera.PreviewCallback) null);
            this.mCamera.stopPreview();
            this.mCamera.release();
            this.mCamera = null;

            try {
                Thread.sleep(400L);
            } catch (InterruptedException var2) {
                var2.printStackTrace();
            }
        }

    }

    private void initCameraParameters() {
        try {
            this.mParams = this.mCamera.getParameters();
            if (!this.custom) {
                boolean var1 = ScreenUtils.isLandscape();
                if (var1) {
                    this.mPreviewWidth = ScreenUtils.getScreenHeight();
                    this.mPreviewHeight = ScreenUtils.getScreenWidth();
                } else {
                    this.mPreviewWidth = ScreenUtils.getScreenWidth();
                    this.mPreviewHeight = ScreenUtils.getScreenHeight();
                }

                if (CameraParametersUtils.getBestPreviewSize(this.mParams, var1, this.mPreviewWidth, this.mPreviewHeight)) {
                    this.mPreviewWidth = CameraParametersUtils.bestPreviewWidth;
                    this.mPreviewHeight = CameraParametersUtils.bestPreviewHeight;
                    LogUtils.d(HEAD, "Best Preview mPreviewWidth = " + this.mPreviewWidth + " ; mPreviewHeight = " + this.mPreviewHeight);
                } else {
                    LogUtils.d(HEAD, "Best Preview Fail");
                    if (var1) {
                        this.mPreviewWidth = ScreenUtils.getScreenWidth();
                        this.mPreviewHeight = ScreenUtils.getScreenHeight();
                    } else {
                        this.mPreviewWidth = ScreenUtils.getScreenHeight();
                        this.mPreviewHeight = ScreenUtils.getScreenWidth();
                    }
                }
            }

            LogUtils.i(HEAD, "final set camera preview size w = " + this.mPreviewWidth + " ; h = " + this.mPreviewHeight);
            this.mParams.setPreviewSize(this.mPreviewWidth, this.mPreviewHeight);
            if (!this.exception) {
                this.mParams.setFocusMode("continuous-picture");
            }

            this.mCameraStateCallBack.cameraHasParameters2();
            this.mCamera.setParameters(this.mParams);
            this.mCamera.setPreviewCallback(this.mPreviewCallback);
            this.mCamera.startPreview();
        } catch (Exception var2) {
            var2.printStackTrace();
            LogUtils.i(HEAD, "initCameraParameters exception : " + var2.toString());
            this.exception = true;
            this.initCameraParameters();
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
