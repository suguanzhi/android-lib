package com.android.sgzcommon.cache;

import android.graphics.Bitmap;

import com.android.volley.toolbox.ImageLoader.ImageCache;

import androidx.collection.LruCache;

public class BitmapCache implements ImageCache {

	private int mSize;
	private boolean mSaveLocal;
	private LruCache<String, Bitmap> cache;

	public BitmapCache() {
		long maxMemory = Runtime.getRuntime().maxMemory();
		mSize = (int) maxMemory / 8;
		cache = new LruCache<String, Bitmap>(mSize) {
			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				return bitmap.getRowBytes() * bitmap.getHeight();
			}
		};
	}

	/**
	 * 是否直接保存本地
	 * 
	 * @param saveLocal
	 */
	public void saveLocal(boolean saveLocal) {
		mSaveLocal = saveLocal;
	}

	/**
	 * 获取cache中的图片必须通过该方法获取
	 * 
	 * @param url
	 * @param width
	 * @param height
	 * @return
	 */
	public Bitmap getBitmap(String url, int width, int height) {
		String cacheKey = getCacheKey(url, width, height);
		return getBitmap(cacheKey);
	}

	@Override
	public Bitmap getBitmap(String url) {
		Bitmap bitmap = cache.get(url);
		// if (bitmap == null) {
		// String name = getFileName(url);
		// String path = VisitorUrlConstant.PATH_IMAGE_CACHE + File.separator + name;
		// bitmap = BitmapFactory.decodeFile(path);
		// }
		return bitmap;
	}

	@Override
	public void putBitmap(String url, Bitmap bitmap) {
		// if (mSaveLocal || (cache.size() >= mSize)) {
		// String name = getFileName(url);
		// if (name != null) {
		// File dir = new File(VisitorUrlConstant.PATH_IMAGE_CACHE);
		// if (!dir.exists()) {
		// dir.mkdirs();
		// }
		// String path = dir.getAbsolutePath() + File.separator + name;
		// CommonUtils.saveBimapToLocal(path, bitmap);
		// }
		// } else {
		cache.put(url, bitmap);
		// }
	}

	private String getFileName(String url) {
		if (url != null && !url.isEmpty()) {
			String[] strings = url.split("/");
			if (strings.length < 3) {
				return null;
			}
			return strings[strings.length - 2] + "_" + strings[strings.length - 1];
		}
		return null;
	}

	private static String getCacheKey(String url, int maxWidth, int maxHeight) {
		return new StringBuilder(url.length() + 12).append("#W").append(maxWidth).append("#H").append(maxHeight).append(url).toString();
	}
}
