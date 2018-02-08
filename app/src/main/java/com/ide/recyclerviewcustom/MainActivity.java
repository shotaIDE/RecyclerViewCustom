package com.ide.recyclerviewcustom;

import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.ChangeImageTransform;
import android.transition.TransitionSet;
import android.view.View;

import static com.ide.recyclerviewcustom.ScreenDetailFragment.SCREEN_CARD_POSITION_KEY;
import static com.ide.recyclerviewcustom.ScreenDetailFragment.SCREEN_IMAGE_TRANSITION_NAME;

public class MainActivity extends AppCompatActivity implements ScreenCardListAdapter.Callback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Fragment fragment = new ScreenCardListFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, fragment)
                .commit();
    }

    @Override
    public void onRecyclerViewCustomListener(View view, int position) {
        Fragment fragment = new ScreenDetailFragment();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            TransitionSet set = new TransitionSet();
            set.addTransition(new ChangeImageTransform());
            fragment.setSharedElementEnterTransition(set);

            Bundle args = new Bundle();
            args.putInt(SCREEN_CARD_POSITION_KEY, position);
            fragment.setArguments(args);

        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View target = view.findViewById(R.id.screen_card_image);
            transaction.addSharedElement(target, SCREEN_IMAGE_TRANSITION_NAME + String.valueOf(position));
        }
        transaction.replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
    }
}
