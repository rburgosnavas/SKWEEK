package com.rburgos.skweek;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class SkweekAudioEngine implements Runnable
{

	private static SkweekAudioEngine audioEngine;
	private AudioFormat audioFormat;
	private DataLine.Info info;
	private SourceDataLine dataLine;
	private String exp;
	private int t, x = 1, y = 1, z = 1, scale = 1, res, loop;
    private byte[] buffer = new byte[8];
    private boolean play = false;
    
    public SkweekAudioEngine() throws LineUnavailableException
    {
    	t = 0;
		audioFormat = new AudioFormat(11025f, 8, 2, false, false);
        info = new DataLine.Info(SourceDataLine.class, audioFormat);
        dataLine = (SourceDataLine) AudioSystem.getLine(info);  	
    }
    
    public static SkweekAudioEngine getEngine() throws LineUnavailableException
    {
    	if (audioEngine == null)
    	{
    		audioEngine = new SkweekAudioEngine();
    	}
    	return audioEngine;
    }

	@Override
	public void run()
	{
		try
        {
	        play();
        }
        catch (LineUnavailableException e)
        {
	        e.printStackTrace();
        }
		write();
		stop();
	}
	
	private void play() throws LineUnavailableException
	{
        dataLine.open(audioFormat, dataLine.getBufferSize());  
		dataLine.start();
		
		System.out.println("+ thread started");
		System.out.println("+ buffer size: " + dataLine.getBufferSize());
	}
	
	private void stop()
	{
        dataLine.stop();
        dataLine.flush();
        dataLine.close();

		System.out.println("- thread stopped");
        System.out.println("x thread closed\n");
	}
	
	private void write()
	{
		while (play)
		{
		    for (int i = 0; i < (buffer.length); i++)
		    {
		        t++;
		    	res = SkweekParser.evalToInt(exp, t, x, y, z) / tInc(scale);
		    	buffer[i] = (byte) res;
		        
		        if (t > (buffer.length*loop))
		        {
		        	t = 0;
		        }
		    }
		    dataLine.write(buffer, 0, buffer.length);
		}
	}
	
	public void setExp(String exp)
	{
		this.exp = exp;
	}
	
	public void setPlay(boolean setPlay)
	{
		this.play = setPlay;
	}
	
	public void setLoop(int loop)
	{
		this.loop = loop;
	}
	
	public void setX(int x)
	{
		this.x = x;
	}
	
	public void setY(int y)
	{
		this.y = y;
	}
	
	public void setZ(int z)
	{
		this.z = z;
	}
	
	public void tScale(int scale)
	{
		this.scale = scale;
	}
	
	public int tInc(int scale)
	{
		return scale > 0 ? scale : this.scale;
	}

}
