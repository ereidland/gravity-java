package com.evanreidland.e.launcher;

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

import com.evanreidland.e.builder.Project;
import com.evanreidland.e.ftp.download;
import com.evanreidland.e.script.Value;
import com.evanreidland.e.script.Variable;
import com.evanreidland.e.script.basefunctions;
import com.evanreidland.e.script.text.Script;

public class GravityLauncherGUI extends JPanel implements ActionListener
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
			logArea.setCaretPosition(logArea.getDocument().getLength());
		}
		
	}
	
	private static final long serialVersionUID = -727062100602335292L;
	
	public static boolean running = true;
	
	public static void Log(String str)
	{
		logger.log(Level.INFO, str);
	}
	
	private static JFrame frame;
	private JTextArea logArea;
	private JScrollPane logScroll;
	private JTextField consoleLine;
	
	public static void removeFrame()
	{
		// frame.setEnabled(false);
		frame.setVisible(false);
	}
	
	private class ExecuteScript implements Runnable
	{
		private String line;
		
		public void run()
		{
			String res = script.Execute(line).toString();
			if (res.length() > 0)
				Log("<-" + res);
			
			logArea.setCaretPosition(logArea.getDocument().getLength());
		}
		
		public ExecuteScript(String line)
		{
			this.line = line;
		}
	}
	
	public void actionPerformed(ActionEvent evt)
	{
		String text = consoleLine.getText();
		consoleLine.setText("");
		Log("->" + text);
		new Thread(new ExecuteScript(text)).start();
	}
	
	public void startup()
	{
		logger.addHandler(new LogManager());
		System.setOut(new PrintStream(new OutputStreamLogger(), true));
		script = new Script();
		
		basefunctions.printFunction = new launcherfunctions.Print();
		basefunctions.registerAll(script.env);
		script.env.registerFunctions(launcherfunctions.class, true);
		script.env.registerFunctions(Project.class, true);
		script.env.registerFunctions(Launcher.class, true);
		script.env.add(new Variable("path", Project.defaultDirectory()
				+ "/.egravity"));
		script.env.add(new Variable("branch", "master"));
		
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
		
		String OS = System.getProperty("os.name").toUpperCase();
		script.env.add(new Variable.Constant("os", new Value(OS)));
		if (OS.contains("WIN"))
		{
			script.env.add(new Variable.Constant("windows", new Value(1)));
			Log("Set OS to Windows.");
		}
		else if (OS.contains("MAC"))
		{
			script.env.add(new Variable.Constant("mac", new Value(1)));
			Log("Set OS to Mac.");
		}
		else if (OS.contains("NUX"))
		{
			script.env.add(new Variable.Constant("linux", new Value(1)));
			Log("Set OS to Linux.");
		}
		
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
