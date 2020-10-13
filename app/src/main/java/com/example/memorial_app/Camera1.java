package com.example.memorial_app;

//カメラの使用
import android.content.Context;
import android.hardware.Camera;
import android.hardware.camera2.CameraMetadata;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

//サーフェイスビューを使う(旧式のやり方)
public class Camera1 extends SurfaceView implements SurfaceHolder.Callback{

    private SurfaceHolder SH;
    private Camera CM;
    Camera.CameraInfo info;

    public Camera1(Context context) {
        super(context);

        SH = getHolder();
        SH.addCallback(this);
        SH.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        info = new Camera.CameraInfo();
        Camera.getCameraInfo(CameraMetadata.LENS_FACING_BACK, info);
    }

    @Override
    public void surfaceCreated(SurfaceHolder sh) {
        try {
            CM = Camera.open();
            CM.setDisplayOrientation(info.orientation);
            CM.setPreviewDisplay(SH);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder sh, int i, int i1, int i2) {
        CM.startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder sh) {
        CM.setPreviewCallback(null);
        CM.stopPreview();
        CM.release();
        CM = null;
    }
}