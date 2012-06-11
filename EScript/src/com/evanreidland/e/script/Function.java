package com.evanreidland.e.script;

import java.util.Vector;

import com.evanreidland.e.script.text.Script;


public abstract class Function {
	private String name;
	
	public String getName() {
		return name;
	}
	
	public static class Null extends Function {
		public Value Call(Stack args) {
			return new Value();
		}
		
		public Null() {
			super("_null");
		}
	}
	
	public static class LineBased extends Function {
		public Vector<String> lines;
		public Value Call(Stack args) {
			for ( int i = 0; i < args.size(); i++ ) {
				args.context.add(new Variable("f." + i, args.at(i)));
			}
			Value v = new Value();
			Script scr = new Script();
			scr.env = args.context;
			for ( int i = 0; i < lines.size(); i++ ) {
				v = scr.Execute(lines.get(i));
			}
			return v;
		}
		
		public LineBased(String name, Vector<String> lines) {
			super(name);
			this.lines = lines;
		}
	}
	
	
	//TODO finish coding.
	/*public static class MethodBased extends Function {
		public static enum ObjectType {
			Variable,
			Value,
			Int,
			Float,
			String,
			Stack,
			Null,
		}
		
		private ObjectType rtype;
		private Object object;
		
		ObjectType[] argTypes;
		
		public static ObjectType toType(Type t) {
			System.out.println(t.toString());
			return ObjectType.Null;
		}
		
		public Value Call(Stack args) {
			return new Value();
		}
		public MethodBased(String name, Method m, Object object) {
			super(name);
			
			Type[] types = m.getGenericParameterTypes();
			argTypes = new ObjectType[types.length];
		}
	}
	
	public static Function fromMethod(String name, Method m, Object object) { 
		return new MethodBased(name, m, object);
	}*/
	public abstract Value Call(Stack args);
	
	public Function(String name) {
		this.name = name.toLowerCase();
	}
}
