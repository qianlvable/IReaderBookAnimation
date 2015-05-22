package com.lvable.ireaderanimationrepo;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Jiaqi Ning on 21/5/2015.
 */
public class Util {
    static HashMap<Integer, Bitmap> sBitmapResourceMap = new HashMap<Integer, Bitmap>();
    static int[] mPhotos = {
            R.drawable.cover1,
            R.drawable.cover2,
            R.drawable.cover3,
            R.drawable.cover4,
            R.drawable.cover5,
            R.drawable.cover6,
            R.drawable.cover7,
            R.drawable.cover8,
            R.drawable.cover9
    };
    public static ArrayList<PictureData> loadThunmbnails(Resources resources) {
        ArrayList<PictureData> thumbnails = new ArrayList<PictureData>();
        for (int i = 0; i < mPhotos.length; ++i) {
            int resourceId = mPhotos[i];
            Bitmap bitmap = getBitmap(resources, resourceId);
            Bitmap thumbnail = getThumbnail(bitmap, 230);
            thumbnails.add(new PictureData(resourceId,thumbnail));
        }
        return thumbnails;
    }
    /**
     * Utility method to get bitmap from cache or, if not there, load it
     * from its resource.
     */
    static Bitmap getBitmap(Resources resources, int resourceId) {
        Bitmap bitmap = sBitmapResourceMap.get(resourceId);
        if (bitmap == null) {
            bitmap = BitmapFactory.decodeResource(resources, resourceId);
            sBitmapResourceMap.put(resourceId, bitmap);
        }
        return bitmap;
    }

    /**
     * Create and return a thumbnail image given the original source bitmap and a max
     * dimension (width or height).
     */
    static Bitmap getThumbnail(Bitmap original, int maxDimension) {
        int width = original.getWidth();
        int height = original.getHeight();
        int scaledWidth, scaledHeight;
        if (width >= height) {
            float scaleFactor = (float) maxDimension / width;
            scaledWidth = 200;
            scaledHeight = (int) (scaleFactor * height);
        } else {
            float scaleFactor = (float) maxDimension / height;
            scaledWidth = (int) (scaleFactor * width);
            scaledHeight = 200;
        }
        Bitmap thumbnail = Bitmap.createScaledBitmap(original, scaledWidth, scaledHeight, true);

        return thumbnail;
    }
}
