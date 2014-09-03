package com.rburgos.skweek;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class SkweekAudioEngine implements Runnable {
	private static int CAP = 65535;
    private static SkweekAudioEngine audioEngine;
    private AudioFormat audioFormat;
    private DataLine.Info info;
    private SourceDataLine dataLine;
    private String exp;
    private int t, x = 1, y = 1, z = 1, tweakAmount = 1;
    private byte[] buffer;
    private boolean play = false;

    public SkweekAudioEngine() throws LineUnavailableException {
        t = 0;
        audioFormat = new AudioFormat(11025f, 8, 2, true, false);
        info = new DataLine.Info(SourceDataLine.class, audioFormat);
        dataLine = (SourceDataLine) AudioSystem.getLine(info);
        buffer = new byte[8];
    }

    public static SkweekAudioEngine getEngine()
            throws LineUnavailableException {
        if (audioEngine == null) {
            audioEngine = new SkweekAudioEngine();
        }
        return audioEngine;
    }

    @Override
    public void run() {
        try {
            play();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
        write();
        stop();
    }

    private void play() throws LineUnavailableException {
        dataLine.open(audioFormat, dataLine.getBufferSize());
        dataLine.start();
    }

    private void stop() {
        dataLine.stop();
        dataLine.flush();
        dataLine.close();
    }

    private void write() {
        while (play) {
            for (int i = 0; i < buffer.length; i++) {
	            t = (t > CAP) ? 0 : (t + 1);
                SkweekParser.setT(t);
                SkweekParser.setX(x);
                SkweekParser.setY(y);
                SkweekParser.setZ(z);
                double e = SkweekParser.eval(exp);
                byte res = (byte) (e / tweak(tweakAmount));
                buffer[i] = res;
            }

            dataLine.write(buffer, 0, buffer.length);
        }
    }

    public void setExp(String exp) {
        this.exp = exp;
    }

    public void setPlay(boolean setPlay) {
        this.play = setPlay;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public void setTweakAmount(int tweakAmount) {
        this.tweakAmount = tweakAmount;
    }

    public int tweak(int amount) {
        return amount > 0 ? amount : this.tweakAmount;
    }

}
