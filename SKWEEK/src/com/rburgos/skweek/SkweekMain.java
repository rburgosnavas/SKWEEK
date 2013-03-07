package com.rburgos.skweek;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class SkweekMain extends JFrame implements ActionListener, ChangeListener
{

	/**
	 * 
	 */
    private static final long serialVersionUID = 1891895482395555691L;
	private JPanel mainPanel, textPanel, controlPanel, buttonPanel;
	private JButton playBtn, stopBtn;
	private JTextField expField;
	private JSlider loopSlider, tScaleSlider, xSlider, ySlider, zSlider;
	static Font mono;
	private static String exp;
	private static final String DEFAULT_EXP = "t * (t>>6 | t>>x)";
	static Thread thread;
	private Executor exec = Executors.newSingleThreadExecutor();
	static SkweekAudioEngine bba;
	private JTextPane legend;
	
	public SkweekMain()
	{
		setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		setAlwaysOnTop(true);
		setSize(new Dimension(400, 280));
		setResizable(false);
		setTitle("SKWEEK");
		initGUI();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);

		bba = new SkweekAudioEngine();
	}
	
	public void initGUI()
	{
		expField = new JTextField(DEFAULT_EXP, 56);
		expField.setHorizontalAlignment(JTextField.RIGHT);
		expField.setFont(new Font("Monospaced", Font.PLAIN, 12));
		expField.addActionListener(this);
		
		playBtn = new JButton("play");
		playBtn.setFont(new Font("Monospaced", Font.PLAIN, 12));
		playBtn.addActionListener(this);
		
		stopBtn = new JButton("stop");
		stopBtn.setFont(new Font("Monospaced", Font.PLAIN, 12));
		stopBtn.addActionListener(this);
		
		loopSlider = new JSlider(1, 1000, 100);
		loopSlider.setForeground(Color.BLACK);
		loopSlider.setFont(new Font("Monospaced", Font.PLAIN, 12));
		loopSlider.setBorder(new TitledBorder(new EmptyBorder(0, 0, 0, 0), "len", TitledBorder.LEFT, TitledBorder.TOP, null, null));
		loopSlider.addChangeListener(this);
		
		tScaleSlider = new JSlider(1, 20, 1);
		tScaleSlider.setForeground(Color.BLACK);
		tScaleSlider.setFont(new Font("Monospaced", Font.PLAIN, 12));
		tScaleSlider.setBorder(new TitledBorder(new EmptyBorder(0, 0, 0, 0), "<-...->", TitledBorder.LEFT, TitledBorder.TOP, null, null));
		tScaleSlider.addChangeListener(this);
		
		xSlider = new JSlider(1, 20, 1);
		xSlider.setSnapToTicks(true);
		xSlider.setFont(new Font("Monospaced", Font.PLAIN, 12));
		xSlider.setBorder(new TitledBorder(new EmptyBorder(0, 0, 0, 0), "x", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		xSlider.addChangeListener(this);
		
		ySlider = new JSlider(1, 20, 1);
		ySlider.setSnapToTicks(true);
		ySlider.setFont(new Font("Monospaced", Font.PLAIN, 12));
		ySlider.setBorder(new TitledBorder(new EmptyBorder(0, 0, 0, 0), "y", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		ySlider.addChangeListener(this);
		
		zSlider = new JSlider(1, 20, 1);
		zSlider.setSnapToTicks(true);
		zSlider.setFont(new Font("Monospaced", Font.PLAIN, 12));
		zSlider.setBorder(new TitledBorder(new EmptyBorder(0, 0, 0, 0), "z", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		zSlider.addChangeListener(this);
		
		textPanel = new JPanel();
		textPanel.add(expField);
		
		controlPanel = new JPanel();
		controlPanel.setLayout(new GridLayout(0, 1, 0, 0));
		controlPanel.add(loopSlider);
		controlPanel.add(tScaleSlider);
		controlPanel.add(xSlider);
		controlPanel.add(ySlider);
		controlPanel.add(zSlider);
		
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(2, 0, 0, 0));
		buttonPanel.add(playBtn);
		buttonPanel.add(stopBtn);
		
		legend = new JTextPane();
		legend.setEditable(false);
		legend.setOpaque(false);
		legend.setFont(new Font("Monospaced", Font.BOLD, 12));
		legend.setText("* always use variable t; you can use numbers\n* use x, y, z; use sliders to adjust\n* operators: + - * / % ^ | & >> << ( )");
		
		mainPanel = new JPanel();
		mainPanel.setForeground(Color.WHITE);
		mainPanel.setBackground(Color.LIGHT_GRAY);		
		mainPanel.setLayout(new BorderLayout(0, 0));
		mainPanel.add(textPanel, BorderLayout.NORTH);
		mainPanel.add(controlPanel, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.WEST);
		mainPanel.add(legend, BorderLayout.SOUTH);
		getContentPane().add(mainPanel);
	}

	@Override
    public void actionPerformed(ActionEvent e)
    {
    	if (e.getSource().equals(playBtn))
	    {
	    	if (exp != "" || exp != " " || exp != null)
		    {
		    	exp = expField.getText();
		    	bba.setExp(exp);
		    	bba.setLoop(loopSlider.getValue());
		    	bba.setPlay(true);
		    	thread = new Thread(bba);
		    	playBtn.setEnabled(false);
		    	exec.execute(thread);
		    }
	    }
    	else if (e.getSource().equals(expField))
    	{
			bba.setPlay(false);
    		if (exp != "" || exp != " " || exp != null)
		    {
    			exp = expField.getText();
		    	bba.setExp(exp);
		    	bba.setLoop(loopSlider.getValue());
		    	bba.setPlay(true);
		    	playBtn.setEnabled(false);
		    }
    	}
	    else
	    {
	    	bba.setPlay(false);
	    	playBtn.setEnabled(true);
	    }
    }

	@Override
    public void stateChanged(ChangeEvent e)
    {
	    if (e.getSource().equals(loopSlider))
	    {
	    	bba.setLoop(loopSlider.getValue());
	    }
	    else if (e.getSource().equals(tScaleSlider))
	    {
	    	bba.tScale(tScaleSlider.getValue());
	    }
	    else if (e.getSource().equals(xSlider))
	    {
	    	bba.setX(xSlider.getValue());
	    }
	    else if (e.getSource().equals(ySlider))
	    {
	    	bba.setY(ySlider.getValue());
	    }
	    else if (e.getSource().equals(zSlider))
	    {
	    	bba.setZ(zSlider.getValue());
	    }
    }
	
	public static void main(String[] args)
	{
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} catch (Throwable e) {
			e.printStackTrace();
		}
		
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				new SkweekMain();
			}
		});
	}

}
