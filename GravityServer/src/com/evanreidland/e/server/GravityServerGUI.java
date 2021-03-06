package com.evanreidland.e.server;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
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

import com.evanreidland.e.commands.enginescript;
import com.evanreidland.e.net.network;
import com.evanreidland.e.script.basefunctions;
import com.evanreidland.e.script.text.Script;
import com.evanreidland.e.shared.console.sharedfunctions;

public class GravityServerGUI extends JPanel implements Runnable,
		ActionListener
{
	public static Logger logger = Logger.getLogger("com.evanreidland.e");
	
	public Script script;
	
	private class OutputStreamLogger extends ByteArrayOutputStream
	{
		public String ls = System.getProperty("line.separator");
		
		public void flush() throws IOException
		{
			super.flush();
			String item = this.toString();
			super.reset();
			if (!item.isEmpty() && !item.equals(ls))
				logger.log(Level.INFO, item);
		}
	}
	
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
			logArea.append((record.getLevel() != Level.INFO ? "["
					+ record.getLevel().toString() + "]: " : "")
					+ (record.getMessage() + "\n"));
		}
		
	}
	
	public static GravityServerGUI global = null;
	
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
	
	public void setTitle(String title)
	{
		global.frame.setTitle(title);
	}
	
	public void run()
	{
		long nextTime = 0;
		startup();
		while (running)
		{
			if (System.currentTimeMillis() >= nextTime)
			{
				GravityServer.global.Update();
				nextTime = System.currentTimeMillis() + 20;
			}
		}
		System.out.println("Fin.");
	}
	
	public void actionPerformed(ActionEvent evt)
	{
		String text = consoleLine.getText();
		consoleLine.setText("");
		Log("->" + text);
		String res = script.Execute(text).toString();
		if (!res.isEmpty())
			Log("<-" + res);
		
		logArea.setCaretPosition(logArea.getDocument().getLength());
	}
	
	public void startup()
	{
		logger.addHandler(new LogManager());
		
		script = new Script();
		
		System.setOut(new PrintStream(new OutputStreamLogger(), true));
		
		serverfunctions.registerAll(script.env);
		enginescript.registerAll(script.env);
		basefunctions.registerAll(script.env);
		sharedfunctions.registerAll(script.env);
		
		network.setServer(new GravityServer());
		
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
		
		frame.setTitle("Gravity Server");
		frame.setVisible(true);
		
		consoleLine.requestFocus();
		
		frame.pack();
	}
	
	public GravityServerGUI()
	{
		super(new GridBagLayout());
		global = this;
	}
	
	public static void main(String[] args)
	{
		GravityServerGUI app = new GravityServerGUI();
		new Thread(app).start();
	}
}
