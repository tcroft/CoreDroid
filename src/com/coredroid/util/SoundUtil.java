package com.coredroid.util;

import java.util.HashMap;

import android.content.Context;
import android.media.MediaPlayer;

public class SoundUtil {

	private static HashMap<Integer, MediaPlayer> soundPoolMap = new HashMap<Integer, MediaPlayer>();

	public static void loadSound(Context context, int sound) {
		MediaPlayer player = soundPoolMap.get(sound);
		if (player == null) {
			player = MediaPlayer.create(context, sound);
			soundPoolMap.put(sound, player);
		}
	}
	
	public static void playSound(Context context, int sound) {
		MediaPlayer player = soundPoolMap.get(sound);
		if (player != null) {
			if (!player.isPlaying()) {
				player.setVolume(1, 1);
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
