package com.forteknowledge.samples.volley;

import java.io.UnsupportedEncodingException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.example.com.forteknowledge.samples.volley.R;

public class MainActivity extends Activity implements
		AbsListView.OnItemClickListener {

	/**
	 * 20% of the heap goes to image cache. Stored in kiloibytes.
	 */
	private static final int IMAGE_CACHE_SIZE_KB = (int) (Runtime.getRuntime()
			.maxMemory() / 1024 / 5);

	private static final String FLICK_FEED = "http://api.flickr.com/services/feeds/photos_public.gne?format=json&nojsoncallback=1";

	private GridView mGrid;
	// TODO make request queue and image loader application-global.
	private ImageLoader mImageLoader;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mGrid = (GridView) findViewById(R.id.grid);
		mGrid.setOnItemClickListener(this);
		RequestQueue requestQueue = Volley
				.newRequestQueue(getApplicationContext());
		mImageLoader = new ImageLoader(requestQueue, new BitmapLruCache(
				IMAGE_CACHE_SIZE_KB));
		requestQueue.add(new FlickStreamRequest());
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		FlickrItem clickedItem = (FlickrItem) parent.getAdapter().getItem(
				position);
		Uri uri = Uri.parse(clickedItem.link);
		startActivity(new Intent(Intent.ACTION_VIEW, uri));
	}

	private void onFlickItemsReady(FlickrItem[] items) {
		mGrid.setAdapter(new FlickerItemsAdapter(this, mImageLoader, items));
	}

	private final class FlickStreamRequest extends Request<FlickrItem[]> {
		public FlickStreamRequest() {
			super(Method.GET, FLICK_FEED, null);
		}

		@Override
		protected Response<FlickrItem[]> parseNetworkResponse(
				NetworkResponse response) {

			Response<FlickrItem[]> result;

			try {
				String jsonString = new String(response.data,
						HttpHeaderParser.parseCharset(response.headers));

				JSONObject responseObject = new JSONObject(jsonString);
				JSONArray array = responseObject.getJSONArray("items");

				FlickrItem[] items = new FlickrItem[array.length()];
				for (int i = 0; i < array.length(); i++) {
					JSONObject jsonItem = array.getJSONObject(i);
					items[i] = new FlickrItem(jsonItem.getString("title"),
							jsonItem.getJSONObject("media").getString("m"),
							jsonItem.getString("link"));
				}
				return Response.success(items,
						HttpHeaderParser.parseCacheHeaders(response));
			} catch (UnsupportedEncodingException e) {
				result = Response.error(new ParseError(e));
			} catch (JSONException je) {
				result = Response.error(new ParseError(je));
			}
			return result;
		}

		@Override
		protected void deliverResponse(FlickrItem[] items) {
			onFlickItemsReady(items);
		}

	}

}
