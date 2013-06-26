package com.forteknowledge.samples.volley;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.com.forteknowledge.samples.volley.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class FlickerItemsAdapter extends BaseAdapter {

	private final Context mContext;
	private final ImageLoader mImageLoader;
	private final FlickrItem[] mItems;

	public FlickerItemsAdapter(Context context, ImageLoader imageLoader,
			FlickrItem[] items) {
		mContext = context;
		mImageLoader = imageLoader;
		mItems = items;
	}

	@Override
	public int getCount() {
		return mItems.length;
	}

	@Override
	public FlickrItem getItem(int position) {
		return mItems[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		FlickrItem item = mItems[position];

		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.flickr_list_item, parent, false);
		}

		TextView text = (TextView) convertView.findViewById(R.id.text);
		text.setText(mItems[position].title);
		NetworkImageView image = (NetworkImageView) convertView
				.findViewById(R.id.image);
		image.setImageUrl(item.imageUrl, mImageLoader);
		return convertView;
	}
}
