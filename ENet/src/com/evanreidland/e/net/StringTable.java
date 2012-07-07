package com.evanreidland.e.net;

//Added this to be useful for strings that are used many times.
public class StringTable
{
	public static class MappedString
	{
		private int id;
		private String str;

		public int getID()
		{
			return id;
		}

		public String getString()
		{
			return str;
		}
	}

}
