package com.example.spreadtrumdavidzhao.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * Created by SPREADTRUM\david.zhao on 16-9-12.
 */
public class TestCameraFragment extends Fragment {
    private static final String TAG = "TestCameraFragment";
    private android.hardware.Camera mCamera;
    private SurfaceView mSurfaceView;
    private View mProgressbar;
    public static final String EXTRA_PHOTO_FILENAME =
            "com.example.spreadtrumdavidzhao.myapplication.photo_filename";
    private android.hardware.Camera.ShutterCallback mShutterCallback =
            new android.hardware.Camera.ShutterCallback() {
        @Override
        public void onShutter() {
            mProgressbar.setVisibility(View.VISIBLE);
        }
    };
    private android.hardware.Camera.PictureCallback mPictureCallback =
            new android.hardware.Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, android.hardware.Camera camera) {
            //save picture
            String filename = UUID.randomUUID().toString() + ".jpg";
            FileOutputStream out = null;
            Boolean success = true;
            try {
                out = getActivity().openFileOutput(filename, Context.MODE_PRIVATE);
                out.write(data);
            }catch (Exception e) {
                Log.e(TAG,e.toString());
                success = false;
            }finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                }catch (IOException e) {
                    Log.e(TAG,e.toString());
                }
            }
            if (success) {
                Log.i(TAG,"JPEG save at" + filename);
                Intent intent = new Intent();
                intent.putExtra(EXTRA_PHOTO_FILENAME,filename);
                getActivity().setResult(getActivity().RESULT_OK,intent);
            } else {
                getActivity().setResult(getActivity().RESULT_CANCELED);
            }
            getActivity().finish();

        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_test_camera,container,false);

        Button mButton = (Button) view.findViewById(R.id.test_camera_takepicture);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getActivity().finish();
                if (mCamera != null) {
                    mCamera.takePicture(mShutterCallback,null,mPictureCallback);
                }
            }
        });

        mProgressbar = (View)view.findViewById(R.id.camera_progressbar);
        mProgressbar.setVisibility(View.INVISIBLE);



        mSurfaceView = (SurfaceView)view.findViewById(R.id.test_camera_surface);
        SurfaceHolder holder = mSurfaceView.getHolder();
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (mCamera != null) {
                        mCamera.setPreviewDisplay(holder);
                    }
                }catch (IOException e) {
                    Log.e(TAG,e.getMessage());
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                if (mCamera == null) {
                    return;
                }
                android.hardware.Camera.Parameters parameters = mCamera.getParameters();
                android.hardware.Camera.Size size =
                        getBestSupportSize(parameters.getSupportedPreviewSizes(),width,height);
                parameters.setPreviewSize(size.width,size.height);

                parameters.setPictureSize(size.width,size.height);

                mCamera.setParameters(parameters);

                try {
                    if (mCamera != null) {
                        mCamera.startPreview();
                    }
                }catch (Exception e) {
                    Log.e(TAG,e.getMessage());
                    mCamera.release();
                    mCamera = null;
                }

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                if (mCamera == null) {
                    return;
                } else {
                    mCamera.release();
                    mCamera = null;
                }

            }
        });
        return view;
    }

    private android.hardware.Camera.Size getBestSupportSize(List<android.hardware.Camera.Size>
                                                                    sizes,int width,int height) {
        android.hardware.Camera.Size bestSize = sizes.get(0);
        int largestArea = bestSize.width * bestSize.height;
        for (android.hardware.Camera.Size s : sizes) {
            int area = s.width * s.height;
            if (area > largestArea) {
                bestSize = s;
                largestArea = area;
            }
        }
        return bestSize;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
            mCamera = android.hardware.Camera.open(0);
        } else {
            mCamera = android.hardware.Camera.open();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }
}
