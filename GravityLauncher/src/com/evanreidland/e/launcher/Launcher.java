package com.evanreidland.e.launcher;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Vector;

import com.evanreidland.e.script.Function;
import com.evanreidland.e.script.Stack;
import com.evanreidland.e.script.Value;

public class Launcher
{
	private Vector<String> items;
	private String mainClass;
	
	private String baseDir;
	
	public boolean hasItem(String name)
	{
		return items.contains(name);
	}
	
	public String getBaseDirectory()
	{
		return baseDir;
	}
	
	public void setBaseDirectory(String newBase)
	{
		baseDir = newBase.replace('\\', '/');
		if (!baseDir.endsWith("/"))
			baseDir += "/";
	}
	
	public void setMainClass(String className)
	{
		mainClass = className;
	}
	
	public void add(String name)
	{
		if (!items.contains(name))
		{
			items.add(name);
			System.out.println("Added to launcher: " + name);
		}
	}
	
	public void addDirectory(String dir)
	{
		try
		{
			addDirectory(new File(dir));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void addDirectory(File file)
	{
		try
		{
			if (!file.isDirectory())
				return;
			
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++)
			{
				if (files[i].isFile()
						&& (files[i].getName().toLowerCase().endsWith(".class") || files[i]
								.getName().toLowerCase().endsWith(".jar")))
				{
					add(files[i].getCanonicalPath());
				}
				else if (files[i].isDirectory())
				{
					addDirectory(files[i]);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void addLocalDirectory(String dir)
	{
		addDirectory(baseDir + dir);
	}
	
	public void Launch(String[] args)
	{
		try
		{
			
			URL[] url = new URL[items.size()];
			for (int i = 0; i < items.size(); i++)
			{
				String item = items.get(i);
				url[i] = new URL("file:" + (item.startsWith("/") ? "/" : "//")
						+ item);
			}
			ClassLoader loader = new URLClassLoader(url);
			
			System.out.println("Loading main class: " + mainClass);
			Class<?> main = loader.loadClass(mainClass);
			System.out.println("Loaded main class.");
			// Assuming this is okay because loadClass throws a
			// ClassNotFoundException.
			
			Method mainMethod = main.getMethod("main", String[].class);
			System.out.println("Loaded main method. Invoking now.");
			mainMethod.invoke(null, (Object) (args != null ? args
					: new String[0]));
			System.out.println("Finished main Method of " + mainClass);
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("Failed launch! Exception: " + e.toString());
		}
	}
	
	public Launcher(String baseDir)
	{
		this.baseDir = baseDir;
		mainClass = "";
		items = new Vector<String>();
	}
	
	public static Launcher selected = null;
	
	public static class LauncherNew extends Function
	{
		public Value Call(Stack args)
		{
			if (args.size() > 0)
			{
				selected = new Launcher(args.at(0).toString());
				return new Value("Created launcher.");
			}
			try
			{
				selected = new Launcher(new File(".").getCanonicalPath());
				return new Value(
						"Created launcher with current directory as base.");
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			return new Value(
					"Could not create launcher default directory. Please use: "
							+ getName() + " <directory>");
		}
		
		public LauncherNew()
		{
			super("launcher.new");
		}
	}
	
	public static class LauncherAdd extends Function
	{
		public Value Call(Stack args)
		{
			if (selected != null)
			{
				if (args.size() > 0)
				{
					selected.addDirectory(args.at(0).toString());
					return new Value("Added directory: "
							+ args.at(0).toString());
				}
				else
				{
					return new Value("Not enough arguments. Format: "
							+ getName() + " <absolute dir>");
				}
			}
			else
			{
				return new Value(
						"Launcher not initialized. Use launcher.new <base directory>");
			}
		}
		
		public LauncherAdd()
		{
			super("launcher.+dir");
		}
	}
	
	public static class LauncherSetMain extends Function
	{
		public Value Call(Stack args)
		{
			if (selected != null)
			{
				if (args.size() > 0)
				{
					selected.setMainClass(args.at(0).toString());
					return new Value("Set main class: " + args.at(0).toString());
				}
				else
				{
					return new Value("Not enough arguments. Format: "
							+ getName() + " <main class>");
				}
			}
			else
			{
				return new Value(
						"Launcher not initialized. Use launcher.new <base directory>");
			}
		}
		
		public LauncherSetMain()
		{
			super("launcher.main");
		}
	}
	
	public static class LauncherLaunch extends Function
	{
		public Value Call(Stack args)
		{
			if (selected != null)
			{
				String[] sargs = new String[args.size()];
				if (args.size() > 0)
					System.out.println("Arguments:");
				for (int i = 0; i < args.size(); i++)
				{
					sargs[i] = args.at(i).toString();
					System.out.println("\\" + sargs[i]);
				}
				selected.Launch(sargs);
				return new Value("Launched.");
			}
			else
			{
				return new Value(
						"Launcher not initialized. Use launcher.new <base directory>");
			}
		}
		
		public LauncherLaunch()
		{
			super("launcher.launch");
		}
	}
}
