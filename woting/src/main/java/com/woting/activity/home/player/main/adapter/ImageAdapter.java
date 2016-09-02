package com.woting.activity.home.player.main.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.woting.R;
import com.woting.activity.home.player.main.model.sharemodel;
import com.woting.helper.ImageLoader;

import java.util.List;

public class ImageAdapter extends BaseAdapter{
	private List<sharemodel> list;
	private Context context;
	private ImageLoader imageLoader;
	private sharemodel lists;

	public ImageAdapter (Context context, List<sharemodel> list) {
		super();
		this.list = list;
		this.context = context;
		imageLoader = new ImageLoader(context);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.adapter_shareitem, null);
			holder.tv_platname = (TextView) convertView.findViewById(R.id.tv_sharetext);// 名
			holder.img_share = (ImageView) convertView.findViewById(R.id.img_shareimg);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		lists = list.get(position);
		holder.img_share.setImageResource(lists.getShareImageUrl());			
		if(lists.getShareText()!=null&&!lists.getShareText().equals("")){
			holder.tv_platname.setText(lists.getShareText());	
		}
		return convertView;
	}

	class ViewHolder {
		public ImageView img_share;
		public TextView tv_platname;
	}
}
