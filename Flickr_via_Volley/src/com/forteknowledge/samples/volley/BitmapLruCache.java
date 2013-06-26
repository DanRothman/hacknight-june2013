package com.forteknowledge.samples.volley;

import com.android.volley.toolbox.ImageLoader;
import android.graphics.Bitmap;
import android.util.LruCache;

public class BitmapLruCache extends LruCache<String, Bitmap> implements ImageLoader.ImageCache {

	/**
	 * Least recently used bitmap cache.
	 * 
	 * @param maxSizeKb
	 *            maximum cache size in kilobytes.
	 */
	public BitmapLruCache(int maxSizeKb) {
		super(maxSizeKb);
	}

	protected int sizeOf(String key, Bitmap value) {
		return value.getByteCount() / 1024;
	}

	@Override
	public Bitmap getBitmap(String url) {
		return get(url);
	}

	@Override
	public void putBitmap(String url, Bitmap bitmap) {
		put(url, bitmap);
	}

}
