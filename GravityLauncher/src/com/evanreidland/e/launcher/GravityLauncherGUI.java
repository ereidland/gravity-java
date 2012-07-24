package com.evanreidland.e.launcher;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

import com.evanreidland.e.builder.Project;
import com.evanreidland.e.ftp.download;
import com.evanreidland.e.script.Variable;
import com.evanreidland.e.script.basefunctions;
import com.evanreidland.e.script.text.Script;

public class GravityLauncherGUI extends JPanel implements ActionListener
{
	public static Logger logger = Logger.getLogger("com.evanreidland.e");
	
	public Script script;
	
	private class LogManager extends Handler
	{
		public void close() throws SecurityException
		{
		}
		
		public void flush()
		{
		}
		
		public void publish(LogRecord record)
		{
			String str = "[" + record.getLevel().toString() + "]: "
					+ record.getMessage();
			System.out.println(str);
			logArea.append((record.getLevel() != Level.INFO ? "["
					+ record.getLevel().toString() + "]: " : "")
					+ (record.getMessage() + "\n"));
			logArea.setCaretPosition(logArea.getDocument().getLength());
		}
		
	}
	
	private static final long serialVersionUID = -727062100602335292L;
	
	public static boolean running = true;
	
	public static void Log(String str)
	{
		logger.log(Level.INFO, str);
	}
	
	private JFrame frame;
	private JTextArea logArea;
	private JScrollPane logScroll;
	private JTextField consoleLine;
	
	public void actionPerformed(ActionEvent evt)
	{
		String text = consoleLine.getText();
		consoleLine.setText("");
		Log("->" + text);
		String res = script.Execute(text).toString();
		if (res.length() > 0)
			Log("<-" + res);
		
		logArea.setCaretPosition(logArea.getDocument().getLength());
	}
	
	public void startup()
	{
		logger.addHandler(new LogManager());
		
		script = new Script();
		
		basefunctions.printFunction = new launcherfunctions.Print();
		basefunctions.registerAll(script.env);
		script.env.registerFunctions(launcherfunctions.class, true);
		script.env.add(new Variable("path", Project.defaultDirectory()
				+ "/.egravity/downloads"));
		
		download.setDownloaderClass(LauncherDownloader.class);
		
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 600);
		frame.add(this);
		
		consoleLine = new JTextField(64);
		consoleLine.addActionListener(this);
		consoleLine.setFont(new Font("Courier New", Font.PLAIN, 12));
		consoleLine.setBorder(BorderFactory
				.createBevelBorder(BevelBorder.LOWERED));
		
		logArea = new JTextArea(20, 64);
		logArea.setEditable(false);
		logArea.setFont(new Font("Courier New", Font.PLAIN, 12));
		logArea.setLineWrap(true);
		logArea.setWrapStyleWord(true);
		logArea.setBackground(new Color(0.9f, 0.9f, 0.9f));
		
		logScroll = new JScrollPane(logArea);
		logScroll.setHorizontalScrollBar(null);
		logScroll.setBorder(BorderFactory.createEmptyBorder());
		
		GridBagConstraints c = new GridBagConstraints();
		
		c.anchor = GridBagConstraints.PAGE_END;
		c.weightx = c.weighty = 0;
		c.gridx = 1;
		c.gridy = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		add(consoleLine, c);
		
		c.anchor = GridBagConstraints.PAGE_START;
		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.weightx = c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		add(logScroll, c);
		
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.PAGE_START;
		c.weightx = c.weighty = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		
		frame.setVisible(true);
		
		consoleLine.requestFocus();
		
		frame.pack();
	}
	
	public GravityLauncherGUI()
	{
		super(new GridBagLayout());
	}
	
	public static void main(String[] args)
	{
		GravityLauncherGUI app = new GravityLauncherGUI();
		
		app.startup();
	}
}
