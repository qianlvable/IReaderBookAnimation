package com.lvable.ireaderanimationrepo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends ActionBarActivity{
    private static final String TAG = "com.lvable.ireaderanimationrepo";
    GridLayout mGridLayout;
    HashMap<ImageView,PictureData> mThumbnailData = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGridLayout = (GridLayout)findViewById(R.id.grid);
        mGridLayout.setColumnCount(3);
        mGridLayout.setUseDefaultMargins(true);

        ArrayList<PictureData> thumbnails = Util.loadThunmbnails(getResources());
        for (int i = 0; i < thumbnails.size();i++){
            ImageView imageView = new ImageView(this);
            imageView.setImageBitmap(thumbnails.get(i).thumbnail);
            imageView.setPivotX(0);
            mGridLayout.addView(imageView);
            imageView.setOnClickListener(thumbnailClickListener);
            mThumbnailData.put(imageView,thumbnails.get(i));
        }


    }

    private View.OnClickListener thumbnailClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int[] screenLocation = new int[2];
            view.getLocationOnScreen(screenLocation);
            PictureData info = mThumbnailData.get(view);
            Intent intent = new Intent(MainActivity.this,ReadingPageActivity.class);
            int orientation = getResources().getConfiguration().orientation;
            intent.putExtra(TAG+".orientation",orientation).
                    putExtra(TAG+".resourceId",info.resourceId).
                    putExtra(TAG+".left",screenLocation[0]).
                    putExtra(TAG+".top",screenLocation[1]).
                    putExtra(TAG+".width", view.getWidth()).
                    putExtra(TAG+".height",view.getHeight());
            startActivity(intent);

            // disable normal window animation
            overridePendingTransition(0, 0);
        }
    };


}
