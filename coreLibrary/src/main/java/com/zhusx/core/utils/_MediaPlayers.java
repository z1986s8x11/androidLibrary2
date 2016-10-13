package com.zhusx.core.utils;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

import com.zhusx.core.debug.LogUtil;

import java.io.File;
import java.io.IOException;

public class _MediaPlayers {
	private MediaPlayer mMediaPlayer;
	private String lastPath;

	public _MediaPlayers() {
		mMediaPlayer = new MediaPlayer();
	}

	public void start(String path) {
		if (path == null) {
			if (LogUtil.DEBUG) {
				LogUtil.e(this, "音频文件路径:null");
			}
			return;
		}
		File f = new File(path);
		if (!f.exists()) {
			if (LogUtil.DEBUG) {
				LogUtil.e(this, "音频文件不存在:" + path);
			}
			return;
		}
		if (path.equals(lastPath)) {
			if (mMediaPlayer.isPlaying()) {
				mMediaPlayer.pause();
				return;
			}
			mMediaPlayer.start();
			return;
		}
		lastPath = path;
		mMediaPlayer.reset();
		try {
			mMediaPlayer.setDataSource(path);
			mMediaPlayer.prepare();
			mMediaPlayer.start();
		} catch (IllegalArgumentException e) {
			if (LogUtil.DEBUG) {
				LogUtil.w(e);
			}
		} catch (SecurityException e) {
			if (LogUtil.DEBUG) {
				LogUtil.w(e);
			}
		} catch (IllegalStateException e) {
			if (LogUtil.DEBUG) {
				LogUtil.w(e);
			}
		} catch (IOException e) {
			if (LogUtil.DEBUG) {
				LogUtil.w(e);
			}
		}
	}

	public void onDestry() {
		if (mMediaPlayer != null) {
			mMediaPlayer.release();
		}
	}

	public int getTime() {
		return mMediaPlayer.getDuration();
	}

	public void setOnCompletionListener(OnCompletionListener listener) {
		mMediaPlayer.setOnCompletionListener(listener);
	}

}
