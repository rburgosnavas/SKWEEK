package com.rburgos.skweek;

import javax.sound.sampled.LineUnavailableException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SkweekMain extends JFrame implements ActionListener,
        ChangeListener {

    private static final long serialVersionUID = 1891895482395555691L;
    private JPanel mainPanel, textPanel, controlPanel, buttonPanel;
    private JButton playBtn, stopBtn;
    private JTextField expField;
    private JSlider tweakSlider, xSlider, ySlider, zSlider;
    private JTextPane legend;
    private static String exp;
    private static final String DEFAULT_EXP =
		    "((t * 0.5) * (t * 1.5) & ((t * x)) * (t * y)) & (t) | (t ^ 0.5)";
    boolean isPlaying = false;
    static Thread thread;
    private Executor exec = Executors.newSingleThreadExecutor();
    static SkweekAudioEngine audioEngine;

    public SkweekMain() throws LineUnavailableException {
        setAlwaysOnTop(true);
        setSize(new Dimension(400, 280));
        setResizable(false);
        setTitle("skweek");
        initGUI();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void initGUI() {
        expField = new JTextField(DEFAULT_EXP);
	    expField.setPreferredSize(new Dimension(390, 40));
        expField.setHorizontalAlignment(JTextField.RIGHT);
        expField.setFont(new Font("Monospaced", Font.PLAIN, 12));
        expField.addActionListener(this);

        playBtn = new JButton("play");
        playBtn.setFont(new Font("Monospaced", Font.PLAIN, 12));
        playBtn.addActionListener(this);

        stopBtn = new JButton("stop");
        stopBtn.setFont(new Font("Monospaced", Font.PLAIN, 12));
        stopBtn.addActionListener(this);

        tweakSlider = new JSlider(1, 20, 1);
        tweakSlider.setForeground(Color.BLACK);
        tweakSlider.setFont(new Font("Monospaced", Font.PLAIN, 12));
        tweakSlider.setBorder(new TitledBorder(new EmptyBorder(0, 0, 0, 0),
		        "<>", TitledBorder.LEFT, TitledBorder.TOP, null, null));
        tweakSlider.addChangeListener(this);

        xSlider = new JSlider(1, 20, 1);
        xSlider.setSnapToTicks(true);
        xSlider.setFont(new Font("Monospaced", Font.PLAIN, 12));
        xSlider.setBorder(new TitledBorder(new EmptyBorder(0, 0, 0, 0),
                "x", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        xSlider.addChangeListener(this);

        ySlider = new JSlider(1, 20, 1);
        ySlider.setSnapToTicks(true);
        ySlider.setFont(new Font("Monospaced", Font.PLAIN, 12));
        ySlider.setBorder(new TitledBorder(new EmptyBorder(0, 0, 0, 0),
                "y", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        ySlider.addChangeListener(this);

        zSlider = new JSlider(1, 20, 1);
        zSlider.setSnapToTicks(true);
        zSlider.setFont(new Font("Monospaced", Font.PLAIN, 12));
        zSlider.setBorder(new TitledBorder(new EmptyBorder(0, 0, 0, 0),
                "z", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        zSlider.addChangeListener(this);

        textPanel = new JPanel();
        textPanel.add(expField);

        controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(0, 1, 0, 0));
        controlPanel.add(tweakSlider);
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
        legend.setText(
                "* always use variable t; you can use numbers\n" +
                        "* use x, y, z in expression; use sliders to adjust\n" +
                        "* use <> for additional mangling\n" +
                        "* operators: + - * / % ^ | & >> << ( )"
        );

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
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(playBtn)) {
            if (!"".equals(exp) || !" ".equals(exp) || exp != null) {
                exp = expField.getText();
                audioEngine.setExp(exp);
                audioEngine.setPlay(true);
                thread = new Thread(audioEngine);
                exec.execute(thread);
                playBtn.setEnabled(false);
                isPlaying = true;
            }
        } else if (e.getSource().equals(expField)) {
            if (!"".equals(exp) || !" ".equals(exp) || (exp != null)) {
                if (!isPlaying) {
                    thread = new Thread(audioEngine);
                    exec.execute(thread);
                }

                exp = expField.getText();
                audioEngine.setPlay(false);
                audioEngine.setExp(exp);
                audioEngine.setPlay(true);
                isPlaying = true;
                playBtn.setEnabled(false);
            }
        } else {
            audioEngine.setPlay(false);
            isPlaying = false;
            playBtn.setEnabled(true);
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (e.getSource().equals(tweakSlider)) {
            audioEngine.setTweakAmount(tweakSlider.getValue());
        } else if (e.getSource().equals(xSlider)) {
            audioEngine.setX(xSlider.getValue());
        } else if (e.getSource().equals(ySlider)) {
            audioEngine.setY(ySlider.getValue());
        } else if (e.getSource().equals(zSlider)) {
            audioEngine.setZ(zSlider.getValue());
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
        } catch (Throwable e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new SkweekMain();
                    audioEngine = SkweekAudioEngine.getEngine();
                } catch (LineUnavailableException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
