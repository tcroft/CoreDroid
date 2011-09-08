package com.coredroid.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

public class MusicManager {
	private static final String TAG = "MusicManager";
	public static final int MUSIC_PREVIOUS = -1;

	private static final int PAUSE_TIMER_DELAY = 1500;
	
	private static HashMap<Integer, MediaPlayer> players = new HashMap<Integer, MediaPlayer>();
	private static int currentMusic = -1;
	private static int previousMusic = -1;
	private static Timer pauseTimer;

	public static void start(Context context, int music) {
		start(context, music, false);
	}

	public static void start(Context context, int music, boolean force) {
		
		if (pauseTimer != null) {
			pauseTimer.cancel();
			pauseTimer = null;
		}
		
		if (!force && currentMusic > -1) {
			// already playing some music and not forced to change
			return;
		}
		if (music == MUSIC_PREVIOUS) {
			Log.d(TAG, "Using previous music [" + previousMusic + "]");
			music = previousMusic;
		}
		if (currentMusic == music) {
			// already playing this music
			return;
		}
		if (currentMusic != -1) {
			previousMusic = currentMusic;
			Log.d(TAG, "Previous music was [" + previousMusic + "]");
			// playing some other music, pause it and change
			pause();
		}
		currentMusic = music;
		Log.d(TAG, "Current music is now [" + currentMusic + "]");
		MediaPlayer mp = players.get(music);
		if (mp != null) {
			if (!mp.isPlaying()) {
				mp.start();
			}
		} else {
			mp = MediaPlayer.create(context, music);

			players.put(music, mp);
			float volume = 1f;
			Log.d(TAG, "Setting music volume to " + volume);
			mp.setVolume(volume, volume);
			try {
				mp.setLooping(true);
				mp.start();
			} catch (Exception e) {
				Log.e(TAG, e.getMessage(), e);
			}
		}
	}

	public static void pause() {
		Collection<MediaPlayer> mps = players.values();
		for (MediaPlayer p : mps) {
			if (p.isPlaying()) {
				p.pause();
			}
		}
		// previousMusic should always be something valid
		if (currentMusic != -1) {
			previousMusic = currentMusic;
			Log.d(TAG, "Previous music was [" + previousMusic + "]");
		}
		currentMusic = -1;
		Log.d(TAG, "Current music is now [" + currentMusic + "]");
	}
	
	public static void maybePause() {
		if (pauseTimer != null) {
			return;
		}
		
		pauseTimer = new Timer("MusicManagerPauseTimer");
		pauseTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				pause();
				pauseTimer = null;
			}
		}, PAUSE_TIMER_DELAY);
	}

	public static void release() {
		Log.d(TAG, "Releasing media players");
		Collection<MediaPlayer> mps = players.values();
		for (MediaPlayer mp : mps) {
			try {
				if (mp != null) {
					if (mp.isPlaying()) {
						mp.stop();
					}
					mp.release();
				}
			} catch (Exception e) {
				Log.e(TAG, e.getMessage(), e);
			}
		}
		mps.clear();
		if (currentMusic != -1) {
			previousMusic = currentMusic;
			Log.d(TAG, "Previous music was [" + previousMusic + "]");
		}
		currentMusic = -1;
		Log.d(TAG, "Current music is now [" + currentMusic + "]");
	}
}