package com.evanreidland.e.script.test2;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.evanreidland.e.script.basefunctions;
import com.evanreidland.e.script.text.Script;

public class Test2
{
	public static class TestObject
	{
		public int V1 = 1, v2;
		public double v3;
		public Object notToUse;
		public String v4;
	}
	
	public static void main(String[] args)
	{
		try
		{
			Script script = new Script();
			script.env.registerFunctions(basefunctions.class);
			script.env.addFields(new TestObject(), "test.");
			
			BufferedReader in = new BufferedReader(new InputStreamReader(
					System.in));
			
			String str;
			while ((str = in.readLine()).length() > 0 && !str.equals("exit"))
			{
				str = script.Execute(str).toString();
				if (str.length() > 0)
				{
					System.out.println(str);
				}
			}
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		System.out.println("Fin.");
	}
}
