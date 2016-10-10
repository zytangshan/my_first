package com.example.spreadtrumdavidzhao.myapplication;

import android.graphics.drawable.BitmapDrawable;
import android.hardware.camera2.CameraDevice;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;



/**
 * Created by SPREADTRUM\david.zhao on 16-9-13.
 */
public class ImageFragment extends DialogFragment {

    public static final String EXTRA_IMAGE_PATH =
            "com.example.spreadtrumdavidzhao.myapplication.image_path";

    private ImageView imageView;
    private static ImageFragment imageFragment;

    public static ImageFragment newInstance(String imagePath) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_IMAGE_PATH,imagePath);

        imageFragment = new ImageFragment();
        imageFragment.setArguments(args);
        imageFragment.setStyle(DialogFragment.STYLE_NO_TITLE,0);

        return imageFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        imageView = new ImageView(getActivity());
        final String path = (String) getArguments().getSerializable(EXTRA_IMAGE_PATH);
        BitmapDrawable image = PictureUtil.getScaledDrawable(getActivity(),path);
        imageView.setImageDrawable(image);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getActivity().finish();
                //ImageFragment imageFragment = ImageFragment();
                Log.e("david","image ");

                //imageFragment.dismiss(); there is error

                //this is no problem
                dismiss();
            }
        });

        return imageView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        PictureUtil.cleanImageView(imageView);
    }
}
