package com.woting.video;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.woting.activity.home.player.main.fragment.PlayerFragment;

import org.videolan.libvlc.EventHandler;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.LibVlcException;
import org.videolan.vlc.util.VLCInstance;

public class VlcPlayer implements WtAudioPlay {
	public  LibVLC audioPlay;
	private String Url;
//	private Thread a;
	private static VlcPlayer vlcplayer ;
	private static Context context;

	private VlcPlayer() {
		try {
			audioPlay = VLCInstance.getLibVlcInstance();
		} catch (LibVlcException e) {
			e.printStackTrace();
		}
		EventHandler em = EventHandler.getInstance();
		/*em.removeHandler(mVlcHandler);*/
		em.addHandler(mVlcHandler);
//		mtask=new MyTask();
	}

	public  static VlcPlayer getInstance(Context contexts) {
		if(vlcplayer==null){
			vlcplayer=new VlcPlayer();
		}
		context=contexts;
		return vlcplayer;
	}

	@Override
	public void play(String url) {
		this.Url = url;
		if(url != null){
			audioPlay.playMRL(Url);	
		}
	}

	@Override
	public void pause() {
		audioPlay.pause();

	}

	@Override
	public void stop() {
		audioPlay.stop();

	}

	@Override
	public void continuePlay() {
		audioPlay.play();
	}

	@Override
	public boolean isPlaying() {	
		return audioPlay.isPlaying();
	}

	@Override
	public int getVolume() {

		return 0;
	}

	@Override
	public int setVolume() {

		return 0;
	}

	@Override
	public void setTime(long times) {
		if(times>0){
			audioPlay.setTime(times);
		}
	}

	@Override
	public long getTime() {
		return audioPlay.getTime();
	}

	@Override
	public long getTotalTime() {
		return audioPlay.getLength();
	}

	@SuppressLint("HandlerLeak")
	private Handler mVlcHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg == null || msg.getData() == null)
				return;
			switch (msg.getData().getInt("event")) {
			case EventHandler.MediaPlayerEncounteredError:
				Log.e("url", "playerror+Url");
				PlayerFragment.playNext();
				break;
			case EventHandler.MediaPlayerOpening:
				Log.e("url", "MediaPlayerOpenning()"+Url);
				break;
			case EventHandler.MediaParsedChanged:
				break;
			case EventHandler.MediaPlayerTimeChanged:
				break;
			case EventHandler.MediaPlayerPositionChanged:
				break;
			case EventHandler.MediaPlayerPlaying:
				Log.e("url", "MediaPlayerPlaying()"+Url);
				break;
			case EventHandler.MediaPlayerEndReached:
				Log.e("url", "MediaPlayerEndReached()");
				PlayerFragment.playNext();
				break;
			}
		}
	};

	@Override
	public void destory() {
		if(audioPlay!=null){
			audioPlay.destroy();
		}
		if(vlcplayer!=null){
			vlcplayer=null;
		}
		Url=null;
	}
	@Override
	public String mark() {

		return "VLC";
	}
}
