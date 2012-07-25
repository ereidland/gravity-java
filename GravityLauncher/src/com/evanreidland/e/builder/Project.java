package com.evanreidland.e.builder;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.evanreidland.e.ftp.DownloadInfo;
import com.evanreidland.e.launcher.GravityLauncherGUI;
import com.evanreidland.e.launcher.launcherfunctions;
import com.evanreidland.e.script.Function;
import com.evanreidland.e.script.Stack;
import com.evanreidland.e.script.Value;

public class Project
{
	public static Logger logger = Logger.getLogger("com.evanreidland.e");
	private Vector<String> sourceFiles, classPaths;
	private String name;
	
	public String getName()
	{
		return name;
	}
	
	public void add(String source)
	{
		sourceFiles.add(source);
	}
	
	public void addClassPath(String classPath)
	{
		classPaths.add(classPath);
	}
	
	public void add(Vector<String> sourceFiles)
	{
		this.sourceFiles.addAll(sourceFiles);
	}
	
	public void removeEntriesWith(String contains)
	{
		int i = 0;
		while (i < sourceFiles.size())
		{
			String str = sourceFiles.get(i);
			if (str.contains(contains))
			{
				sourceFiles.remove(i);
				GravityLauncherGUI.Log("Removed: " + str);
			}
			else
			{
				i++;
			}
		}
	}
	
	public static class LogStream extends PrintWriter
	{
		private Logger logger;
		
		public void println(Object msg)
		{
			println(msg.toString());
		}
		
		public void println(String msg)
		{
			logger.log(Level.INFO, msg.toString());
		}
		
		public LogStream(Logger logger, String fout) throws Exception
		{
			super(fout);
			this.logger = logger;
		}
	}
	
	private static String getDefaultDirectory()
	{
		String OS = System.getProperty("os.name").toUpperCase();
		if (OS.contains("WIN"))
			return System.getenv("APPDATA");
		else if (OS.contains("MAC"))
			return System.getProperty("user.home")
					+ "/Library/Application Support";
		else if (OS.contains("NUX"))
			return System.getProperty("user.home");
		return System.getProperty("user.dir");
	}
	
	public static String defaultDirectory()
	{
		return getDefaultDirectory().replace('\\', '/');
	}
	
	public boolean buildClasses(String outputFolder)
	{
		int p = 2;
		String[] args = new String[sourceFiles.size() + p];
		
		for (int i = 0; i < sourceFiles.size(); i++)
		{
			args[i] = sourceFiles.get(i);
		}
		
		p = sourceFiles.size();
		args[p] = "-classpath";
		p++;
		args[p] = "";
		
		// TODO fix. Something about this is broken. If there is more than one
		// classpath, it causes the builder to just not run.
		for (int i = 0; i < classPaths.size(); i++)
		{
			args[p] += classPaths.get(i)
					+ (i < classPaths.size() - 1 ? ";" : "");
		}
		
		GravityLauncherGUI.Log(args[p]);
		try
		{
			GravityLauncherGUI.Log("Building " + args.length + " files.");
			com.sun.tools.javac.Main.compile(args, new PrintWriter(
					new FileWriter(outputFolder + "/buildlog.txt")));
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return false;
	}
	
	public Project(String name)
	{
		this.name = name;
		
		sourceFiles = new Vector<String>();
		classPaths = new Vector<String>();
	}
	
	public static Project selectedProject = null;
	
	public static class ProjectNew extends Function
	{
		public Value Call(Stack args)
		{
			if (args.size() > 0)
			{
				selectedProject = new Project(args.at(0).toString());
				return new Value("Project created and selected, \""
						+ args.at(0).toString() + "\".");
			}
			else
			{
				String name = "project";
				selectedProject = new Project(name);
				return new Value("Empty arguments. Created project \"" + name
						+ "\".");
			}
			
		}
		
		public ProjectNew()
		{
			super("project.new");
		}
	}
	
	public static class ProjectAdd extends Function
	{
		public Value Call(Stack args)
		{
			if (args.size() > 0)
			{
				if (selectedProject != null)
				{
					selectedProject.add(args.at(0).toString());
					return new Value("Added: " + args.at(0).toString());
				}
				else
				{
					return new Value("No project selected. Cannot execute \""
							+ getName() + "\"");
				}
			}
			else
			{
				return new Value("Not enough arguments. Format: " + getName()
						+ " <sourcefile>");
			}
			
		}
		
		public ProjectAdd()
		{
			super("project.+src");
		}
	}
	
	public static class ProjectAddDownload extends Function
	{
		public Value Call(Stack args)
		{
			if (selectedProject != null)
			{
				DownloadInfo lastDownload = launcherfunctions.lastDownload;
				if (lastDownload.success)
				{
					GravityLauncherGUI.Log("Setting up to add last download ("
							+ lastDownload.entryNames.size() + " entries).");
					
					String to = args.context.get("path").toString();
					if (to.isEmpty())
						to = "./build/bin/";
					
					if (!to.endsWith("/"))
						to += "/";
					
					GravityLauncherGUI.Log("Base directory: " + to);
					
					for (int i = 0; i < lastDownload.entryNames.size(); i++)
					{
						String entry = lastDownload.entryNames.get(i);
						if (entry.endsWith(".java"))
						{
							GravityLauncherGUI.Log("Added " + entry);
							selectedProject.add(to + entry);
						}
					}
					
					return new Value();
				}
				else
				{
					return new Value(
							"Last download was unsuccessful. Did not add files.");
				}
			}
			else
			{
				return new Value("No project selected. Cannot execute \""
						+ getName() + "\"");
			}
			
		}
		
		public ProjectAddDownload()
		{
			super("project.+dl");
		}
	}
	
	public static class ProjectAddPath extends Function
	{
		public Value Call(Stack args)
		{
			if (args.size() > 0)
			{
				if (selectedProject != null)
				{
					selectedProject.addClassPath(args.at(0).toString());
					return new Value("Added source path: "
							+ args.at(0).toString());
				}
				else
				{
					return new Value("No project selected. Cannot execute \""
							+ getName() + "\"");
				}
			}
			else
			{
				return new Value("Not enough arguments. Format: " + getName()
						+ " <classpath>");
			}
		}
		
		public ProjectAddPath()
		{
			super("project.+lib");
		}
	}
	
	public static class ProjectRemove extends Function
	{
		public Value Call(Stack args)
		{
			if (args.size() > 0)
			{
				if (selectedProject != null)
				{
					String str = args.at(0).toString();
					selectedProject.removeEntriesWith(str);
					return new Value("Removed entries that contained \"" + str
							+ "\".");
				}
				else
				{
					return new Value("No project selected. Cannot execute \""
							+ getName() + "\"");
				}
			}
			else
			{
				return new Value("Not enough arguments. Format: " + getName()
						+ " <contains>");
			}
		}
		
		public ProjectRemove()
		{
			super("project.-src");
		}
	}
	
	public static class ProjectBuild extends Function
	{
		public Value Call(Stack args)
		{
			if (selectedProject != null)
			{
				
				String to = args.context.get("path").toString();
				if (to.isEmpty())
					to = "./build/bin/";
				
				if (!to.endsWith("/"))
					to += "/";
				
				selectedProject.buildClasses(to);
				return new Value("Built project \"" + selectedProject.getName()
						+ "\" to \"" + to + "\".");
			}
			else
			{
				return new Value("No project selected. Cannot execute \""
						+ getName() + "\"");
			}
		}
		
		public ProjectBuild()
		{
			super("project.build");
		}
	}
	
}
