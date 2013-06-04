package com.forteknowledge.samples.sifw;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends Activity implements View.OnClickListener {

	private static final String IMAGE_1_MPX = "http://static.giantbomb.com/uploads/original/15/157771/2312719-a6.jpg";
	private static final String IMAGE_4_MPX = "http://wallpapersus.com/wp-content/uploads/2012/11/Android-Logo-01.jpg";
	private static final String IMAGE_9_MPX = "http://cdn5.benzinga.com/files/images/story/2012/shutterstock_132945893.jpg";
	private static final String IMAGE_15_MPX = "http://www.mostlymuppet.com/wp-content/uploads/2011/07/bot.jpg";
	private static final String IMAGE_70_MPX = "http://i1.sndcdn.com/artworks-000046856021-jrn4qk-original.jpg?c831450";

	
	
	private ImageView mImage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findViewById(R.id.load_image_button).setOnClickListener(this);
		mImage = (ImageView) findViewById(R.id.image);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.load_image_button:
			new ImageDownloadTask().execute(IMAGE_1_MPX);
			break;

		default:
			throw new IllegalStateException("Unknown view id " + view.getId());
		}
	}

	private class ImageDownloadTask extends AsyncTask<String, Void, Bitmap> {

		@Override
		protected Bitmap doInBackground(String... params) {
			Bitmap bitmap;
			try {
				InputStream is = new URL(params[0]).openStream();
				BitmapFactory.Options opts = new BitmapFactory.Options();
				opts.inJustDecodeBounds = true;
				try {
					BitmapFactory.decodeStream(is, null, opts);
				} finally {
					is.close();
				}
				int width = opts.outWidth;
				int height = opts.outHeight;
				int availableWidth = mImage.getWidth();
				int availableHeight = mImage.getHeight();

				if (width > availableWidth || height > availableHeight) {
					if (width > height) {
						opts.inSampleSize = width / availableWidth;
					} else {
						opts.inSampleSize = height / availableHeight;
					}
				}
				opts.inJustDecodeBounds = false;
				is = new URL(params[0]).openStream();
				try {
					bitmap = BitmapFactory.decodeStream(is, null, opts);
				} finally {
					is.close();
				}
			} catch (IOException e) {
				bitmap = null;
			}
			return bitmap;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			mImage.setImageBitmap(result);
		}

	}

}
