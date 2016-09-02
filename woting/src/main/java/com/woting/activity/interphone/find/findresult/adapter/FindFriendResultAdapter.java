package com.woting.activity.interphone.find.findresult.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.woting.R;
import com.woting.activity.interphone.find.findresult.model.UserInviteMeInside;
import com.woting.common.config.GlobalConfig;
import com.woting.helper.ImageLoader;

import java.util.List;

public class FindFriendResultAdapter extends BaseAdapter{
	private List<UserInviteMeInside> list;
	private Context context;
	private ImageLoader imageLoader;
	private String url;
	public FindFriendResultAdapter(Context context,List<UserInviteMeInside> list) {
		super();
		this.list = list;
		this.context = context;
		imageLoader=new ImageLoader(context);
	}
	
	public void ChangeData(List<UserInviteMeInside> list){
		this.list = list;
		this.notifyDataSetChanged();
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
		if(convertView == null){
			convertView = LayoutInflater.from(context).inflate(R.layout.adapter_contactquery,null);
			holder = new ViewHolder();
			holder.textview_invitename=(TextView)convertView.findViewById(R.id.RankTitle);		//人名
			holder.textview_invitemessage=(TextView)convertView.findViewById(R.id.RankContent);	//介绍
			holder.imageview_inviteimage=(ImageView)convertView.findViewById(R.id.RankImageUrl);//该人头像
			convertView.setTag(holder); 
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		UserInviteMeInside Inviter = list.get(position);
	    if(Inviter.getUserName()==null||Inviter.getUserName().equals("")){
	    	holder.textview_invitename.setText("未知");
		}else{
			holder.textview_invitename.setText(Inviter.getUserName());
		}
		if(Inviter.getDescn()==null||Inviter.getDescn().equals("")){
		/*	holder.textview_invitemessage.setVisibility(View.GONE);*/
			holder.textview_invitemessage.setText("驾车体验生活");
		}else{
			holder.textview_invitemessage.setVisibility(View.VISIBLE);
			holder.textview_invitemessage.setText(""+Inviter.getDescn());
		}
		if(Inviter.getPortraitMini() == null || Inviter.getPortraitMini().equals("")
				|| Inviter.getPortraitMini().equals("null") || Inviter.getPortraitMini().trim().equals("")){
			holder.imageview_inviteimage.setImageResource(R.mipmap.wt_image_tx_hy);
		}else{
			if(Inviter.getPortraitMini().startsWith("http:")){
				url = Inviter.getPortraitMini();
			}else{
				url = GlobalConfig.imageurl+Inviter.getPortraitMini();
			}
			imageLoader.DisplayImage(url.replace( "\\/", "/"), holder.imageview_inviteimage, false, false, null, null);
		}
		return convertView;
	}
	class ViewHolder{
		public TextView textview_invitename;
		public TextView textview_invitemessage;
		public ImageView imageview_inviteimage;
	}
}
