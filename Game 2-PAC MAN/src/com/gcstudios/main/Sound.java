package com.gcstudios.main;

import java.applet.Applet;
import java.applet.AudioClip;

@SuppressWarnings("removal")
public class Sound {

	private AudioClip clip;
	
	public static final Sound eat = new Sound("/eat.wav");
	public static final Sound waka = new Sound("/waka.wav");
	
	@SuppressWarnings("deprecation")
	private Sound(String name) {
		try {
			clip = Applet.newAudioClip(Sound.class.getResource(name));
		}catch(Throwable e) {}
	}
	
	public void play() {
		try {
			new Thread() {
				@SuppressWarnings("deprecation")
				public void run() {
					clip.play();
				}
			}.start();
		}catch(Throwable e) {}
	}
	
	public void loop() {
		try {
			new Thread() {
				@SuppressWarnings("deprecation")
				public void run() {
					clip.loop();
				}
			}.start();
		}catch(Throwable e) {}
	}
}
