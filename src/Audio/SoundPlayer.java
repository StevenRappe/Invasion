package Audio;

import java.util.HashMap;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class SoundPlayer {
	
	private static HashMap<String, Clip> clips;
	private static int pause;
	private static boolean mute = false;
	
	public static void init() {
		clips = new HashMap<String, Clip>();
		pause = 0;
	}
	
	public static void load(String file, String name) {
		if(clips.get(name) != null) return; //exit if it exists
		Clip clip;
		try {
			AudioInputStream audioin = 
					AudioSystem.getAudioInputStream(SoundPlayer.class.getResourceAsStream(file));
			AudioFormat base = audioin.getFormat();
			AudioFormat decoded = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
					base.getSampleRate(),
					16,
					base.getChannels(),
					base.getChannels() * 2,
					base.getSampleRate(),
					false);
			
			AudioInputStream decodedinput = AudioSystem.getAudioInputStream(decoded, audioin);
			clip = AudioSystem.getClip();
			clip.open(decodedinput);
			clips.put(name, clip);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void play(String s) {
		play(s, pause);
	}
	
	public static void play(String s, int i) {
		if(mute) return;
		Clip c = clips.get(s);
		if(c.isRunning()) return;
		if(c == null) return;
		if(c.isRunning()) c.stop();
		c.setFramePosition(i);
		while(!c.isRunning()) c.start();
	}
	
	public static void stop(String s) {
		if(clips.get(s) == null) return;
		if(clips.get(s).isRunning()) clips.get(s).stop();
	}
	
	public static void resume(String s) {
		if(mute) return;
		if(clips.get(s).isRunning()) return;
		clips.get(s).start();
	}
	
	public static void loop(String s) {
		loop(s, pause, pause, clips.get(s).getFrameLength() - 1);
	}
	
	public static void loop(String s, int frame) {
		loop(s, frame, pause, clips.get(s).getFrameLength() - 1);
	}
	
	public static void loop(String s, int start, int end) {
		loop(s, pause, start, end);
	}
	
	public static void loop(String s, int frame, int start, int end) {
		stop(s);
		if(mute) return;
		clips.get(s).setLoopPoints(start, end);
		clips.get(s).setFramePosition(frame);
		clips.get(s).loop(Clip.LOOP_CONTINUOUSLY);
	}
	
	public static void setPosition(String s, int frame) {
		clips.get(s).setFramePosition(frame);
	}
	
	public static int getFrames(String s) { return clips.get(s).getFrameLength(); }
	public static int getPosition(String s) { return clips.get(s).getFramePosition(); }
	
	public static void close(String s) {
		stop(s);
		clips.get(s).close();
	}
}
