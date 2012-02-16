package com.coredroid.util;

import java.util.HashMap;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;

public class SoundUtil {

	private static HashMap<Integer, MediaPlayer> soundPoolMap = new HashMap<Integer, MediaPlayer>();

	public static void loadSound(Context context, int sound) {
		MediaPlayer player = soundPoolMap.get(sound);
		if (player == null) {
			player = MediaPlayer.create(context, sound);
			player.setOnErrorListener(new OnErrorListener() {
				
				@Override
				public boolean onError(MediaPlayer mp, int what, int extra) {
					return false;
				}
			});
			soundPoolMap.put(sound, player);
		}
	}
	
	public static void playSound(Context context, int sound) {
		MediaPlayer player = soundPoolMap.get(sound);
		if (player != null) {
			if (!player.isPlaying()) {
//				player.setVolume(1, 1);
				player.start();
			} else {
				player.seekTo(0);
				
			}
		} else {
			loadSound(context, sound);
			playSound(context, sound); // infinite recursion possible?
		}
		
	}
}
