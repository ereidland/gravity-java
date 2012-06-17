package com.evanreidland.e.commands;

import com.evanreidland.e.Vector3;
import com.evanreidland.e.graphics.TriList;
import com.evanreidland.e.script.Function;
import com.evanreidland.e.script.Stack;
import com.evanreidland.e.script.Value;

public class modelscript {
	public static TriList current = new TriList();
	
	public static Vector3 pos = Vector3.Zero(), angle = Vector3.Zero();
	
	
	
	public static class SetAngle extends Function {
		public Value Call(Stack args) {
			angle.setAs(args.at(0).toDouble(), args.at(1).toDouble(), args.at(2).toDouble());
			return new Value();
		}
		public SetAngle() {
			super("angle");
		}
	}
	
	public static class SetPos extends Function {
		public Value Call(Stack args) {
			pos.setAs(args.at(0).toDouble(), args.at(1).toDouble(), args.at(2).toDouble());
			return new Value();
		}
		public SetPos() {
			super("pos");
		}
	}
	
	public static class SetScale extends Function {
		public Value Call(Stack args) {
			angle.setAs(args.at(0).toDouble(), args.at(1).toDouble(), args.at(2).toDouble());
			return new Value();
		}
		public SetScale() {
			super("scale");
		}
	}
	
	public static class BeginModel extends Function {
		public Value Call(Stack args) {
			return new Value();
		}
		public BeginModel() {
			super("mbegin");
		}
	}
	
	public static class EndModel extends Function {
		public Value Call(Stack args) {
			return new Value();
		}
		public EndModel() {
			super("mend");
		}
	}
	
	public static void registerAll(Stack env) {
		
	}
}
