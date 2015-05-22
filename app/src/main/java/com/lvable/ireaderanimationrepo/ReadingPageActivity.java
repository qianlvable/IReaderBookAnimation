package com.lvable.ireaderanimationrepo;

import android.animation.TimeInterpolator;
import android.graphics.Bitmap;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;


public class ReadingPageActivity extends ActionBarActivity {
    private static final TimeInterpolator sDecelerator = new DecelerateInterpolator();
    private static final TimeInterpolator sAccelerator = new AccelerateInterpolator();
    private static final String TAG = "com.lvable.ireaderanimationrepo";
    private static final int ANIM_DURATION = 800;
    private Bitmap mBitmap;
    private int mOriginalOrientation;
    private ImageView mImageView;
    private View mBackgroundView;
    int mLeftDelta;
    int mTopDelta;
    float mWidthScale;
    float mHeightScale;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading_page);
        mImageView = (ImageView) findViewById(R.id.cover_img);
        mBackgroundView = findViewById(R.id.content_view);

        Bundle bundle = getIntent().getExtras();
        final int thumbnailTop = bundle.getInt(TAG + ".top");
        final int thumbnailLeft = bundle.getInt(TAG + ".left");
        final int thumbnailWidth = bundle.getInt(TAG + ".width");
        final int thumbnailHeight = bundle.getInt(TAG + ".height");
        mOriginalOrientation = bundle.getInt(TAG + ".orientation");
        Bitmap bitmap = Util.getBitmap(getResources()
                ,bundle.getInt(TAG+".resourceId"));

        mImageView.setImageBitmap(bitmap);

        ViewTreeObserver observer = mImageView.getViewTreeObserver();
        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mImageView.getViewTreeObserver().removeOnPreDrawListener(this);

                int[] screenLocation = new int[2];
                mImageView.getLocationOnScreen(screenLocation);
                mLeftDelta = thumbnailLeft - screenLocation[0];
                mTopDelta = thumbnailTop - screenLocation[1];
                Log.d("wtf","thumbanailLeft: " + thumbnailLeft + " thumbnailTop : "
                        + thumbnailTop + "x: "+screenLocation[0] + "y :"+screenLocation[1]);
                mWidthScale = (float) thumbnailWidth / mImageView.getWidth();
                mHeightScale = (float) thumbnailHeight / mImageView.getHeight();

                runEnterAnimation();
                return true;
            }
        });
    }

    private void runEnterAnimation() {


        mBackgroundView.setPivotX(0);
        mBackgroundView.setPivotY(0);
        mBackgroundView.setScaleX(mWidthScale);
        mBackgroundView.setScaleY(mHeightScale);
        mBackgroundView.setTranslationX(mLeftDelta);
        mBackgroundView.setTranslationY(mTopDelta);
        mBackgroundView.animate().setDuration(ANIM_DURATION).
                scaleX(1).scaleY(1).
                translationX(0).translationY(0).
                setInterpolator(sDecelerator).start();

        mImageView.setPivotX(0);
        mImageView.setPivotY(0);
        mImageView.setScaleX(mWidthScale);
        mImageView.setScaleY(mHeightScale);
        mImageView.setTranslationX(mLeftDelta);
        mImageView.setTranslationY(mTopDelta);

        mImageView.animate().setDuration(ANIM_DURATION).
                scaleX(1).scaleY(1).
                withStartAction(new Runnable() {
                    @Override
                    public void run() {
                        mImageView.animate().rotationY(-180).setDuration(ANIM_DURATION)
                                .setInterpolator(sDecelerator).start();
                    }
                }).
                translationX(0).translationY(0).
                setInterpolator(sDecelerator).start();

    }

    @Override
    public void onBackPressed() {
        runExitAnimation(new Runnable() {
            public void run() {
                finish();
                mImageView.setAlpha(0f);
            }
        });
    }

    private void runExitAnimation(Runnable endAction) {
        mBackgroundView.animate().setDuration(ANIM_DURATION).
                scaleX(mWidthScale).scaleY(mHeightScale).
                translationX(mLeftDelta).translationY(mTopDelta).
                withEndAction(endAction);

        mImageView.animate().setDuration(ANIM_DURATION).
                scaleX(mWidthScale).scaleY(mHeightScale).
                translationX(mLeftDelta).translationY(mTopDelta).
                withEndAction(endAction);
        mImageView.animate().rotationY(0).setDuration(ANIM_DURATION)
                .setInterpolator(sDecelerator).start();

    }

    @Override
    public void finish() {
        super.finish();

        // override transitions to skip the standard window animations
        overridePendingTransition(0, 0);
    }

}
