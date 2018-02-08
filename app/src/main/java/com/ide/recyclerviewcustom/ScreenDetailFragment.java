package com.ide.recyclerviewcustom;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ScreenDetailFragment extends Fragment {

    public static final String SCREEN_IMAGE_TRANSITION_NAME = "screenImage";
    public static final String SCREEN_CARD_POSITION_KEY = "screenCardPosition";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.screen_detail, container, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View target = view.findViewById(R.id.screen_detail_image);
            int targetPosition = getArguments().getInt(SCREEN_CARD_POSITION_KEY);
            target.setTransitionName(SCREEN_IMAGE_TRANSITION_NAME + String.valueOf(targetPosition));
        }

        return view;
    }
}
