package com.evanreidland.e.script.test2;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.evanreidland.e.script.basefunctions;
import com.evanreidland.e.script.text.Script;

public class Test2
{
	public static class TestObject
	{
		public int a = 1, b = 3;
		public double z;
		public Object notToUse;
		public String str;
		public byte lebyte;
		public short leshort;
		public long lelong;
		public float lefloat;
		public boolean lebool = true;
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
