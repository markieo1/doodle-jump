package com.anthony.marco.doodlelibrary.graphics;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.LruCache;

/**
 * Created by marco on 25-2-2017.
 */

public class AssetManager {
	private static AssetManager _instance;

	private LruCache<Integer, Bitmap> mMemoryCache;

	private AssetManager() {
		final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
		final int cacheSize = maxMemory / 8;

		mMemoryCache = new LruCache<Integer, Bitmap>(cacheSize) {
			@Override
			protected int sizeOf(Integer key, Bitmap value) {
				return value.getByteCount() / 1024;
			}
		};
	}

	public static AssetManager getInstance() {
		if (_instance == null)
			_instance = new AssetManager();

		return _instance;
	}

	public void addBitmapToMemoryCache(int key, Bitmap bitmap) {
		if (getBitmapFromMemCache(key) == null) {
			mMemoryCache.put(key, bitmap);
		}
	}

	public Bitmap getBitmapFromMemCache(int key) {
		return mMemoryCache.get(key);
	}

	/**
	 * Loads an bitmap from the cache
	 * If the bitmap does not exists in the cache it will be loaded.
	 *
	 * @param res       The resources to use
	 * @param key       The key of the item
	 * @param reqWidth  The required width of the image
	 * @param reqHeight The required width of the image
	 * @return Bitmap from the cache
	 */
	public Bitmap getBitmapFromMemCache(Resources res, int key, int reqWidth, int reqHeight) {
		if (getBitmapFromMemCache(key) == null) {
			addBitmapToMemoryCache(key, decodeSampledBitmapFromResource(res, key, reqWidth, reqHeight));
		}

		return getBitmapFromMemCache(key);
	}

	private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) >= reqHeight
					&& (halfWidth / inSampleSize) >= reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}

	public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(res, resId, options);
	}
}
