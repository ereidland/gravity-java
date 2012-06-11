package com.evanreidland.e.script;

import java.util.Vector;

import com.evanreidland.e.script.Value.Type;

public class basefunctions {
	public static Function printFunction = new Print();
	
	public static class Add extends Function {
		public Value Call(Stack args) {
			if ( args.size() >= 2 ) {
				Variable first = args.at(0);
				if ( first.getType() == Type.String ) {
					for ( int i = 1; i < args.size(); i++ ) {
						first.setString(first.toString() + args.at(i).toString());
					}
				} else if ( first.getType() == Type.Int ) {
					for ( int i = 1; i < args.size(); i++ ) {
						first.setInt(first.toInt() + args.at(i).toInt());
					}
				} else if ( first.getType() == Type.Float ) {
					for ( int i = 1; i < args.size(); i++ ) {
						first.setFloat(first.toFloat() + args.at(i).toFloat());
					}
				}
				return new Value(first);
			} else if ( args.size() == 1) {
				return new Value(args.at(0));
			}
			return new Value();
		}
		public Add() {
			super("+");
		}
	}
	
	public static class Subtract extends Function {
		public Value Call(Stack args) {
			if ( args.size() >= 2 ) {
				Variable first = args.at(0);
				if ( first.getType() == Type.String ) {
					for ( int i = 1; i < args.size(); i++ ) {
						first.setFloat(first.toFloat() - args.at(i).toFloat());
					}
				} else if ( first.getType() == Type.Int ) {
					for ( int i = 1; i < args.size(); i++ ) {
						first.setInt(first.toInt() - args.at(i).toInt());
					}
				} else if ( first.getType() == Type.Float ) {
					for ( int i = 1; i < args.size(); i++ ) {
						first.setFloat(first.toFloat() - args.at(i).toFloat());
					}
				}
				return new Value(first);
			} else if ( args.size() == 1) {
				return new Value(args.at(0));
			}
			return new Value();
		}
		public Subtract() {
			super("-");
		}
	}
	public static class Divide extends Function {
		public Value Call(Stack args) {
			if ( args.size() >= 2 ) {
				Variable first = args.at(0);
				if ( first.getType() == Type.String ) {
					for ( int i = 1; i < args.size(); i++ ) {
						float f = args.at(i).toFloat();
						if ( args.at(i).toFloat() != 0 ) {
							first.setFloat(f != 0 ? first.toFloat() / args.at(i).toFloat() : 0);
						} else {
							first.setFloat(0);
						}
					}
				} else if ( first.getType() == Type.Int ) {
					for ( int i = 1; i < args.size(); i++ ) {
						float f = args.at(i).toFloat();
						if ( args.at(i).toInt() != 0 ) {
							first.setInt(f != 0 ? first.toInt() / args.at(i).toInt() : 0);
						} else {
							first.setInt(0);
						}
					}
				} else if ( first.getType() == Type.Float ) {
					for ( int i = 1; i < args.size(); i++ ) {
						float f = args.at(i).toFloat();
						if ( args.at(i).toFloat() != 0 ) {
							first.setFloat(f != 0 ? first.toFloat() / args.at(i).toFloat() : 0);
						} else {
							first.setFloat(0);
						}
					}
				}
				return new Value(first);
			} else if ( args.size() == 1) {
				return new Value(args.at(0));
			}
			return new Value();	
		}
		
		public Divide() {
			super("/");
		}
	}
	
	public static class Multiply extends Function {
		public Value Call(Stack args) {
			if ( args.size() >= 2 ) {
				Variable first = args.at(0);
				if ( first.getType() == Type.String ) {
					for ( int i = 1; i < args.size(); i++ ) {
						first.setFloat(first.toFloat() * args.at(i).toFloat());
					}
				} else if ( first.getType() == Type.Int ) {
					for ( int i = 1; i < args.size(); i++ ) {
						first.setInt(first.toInt() * args.at(i).toInt());
					}
				} else if ( first.getType() == Type.Float ) {
					for ( int i = 1; i < args.size(); i++ ) {
						first.setFloat(first.toFloat() * args.at(i).toFloat());
					}
				}
				return new Value(first);
			} else if ( args.size() == 1) {
				return new Value(args.at(0));
			}
			return new Value();
		}
		public Multiply() {
			super("*");
		}
	}
	
	public static class Pow extends Function {
		public Value Call(Stack args) {
			if ( args.size() >= 2 ) {
				Variable first = args.at(0);
				if ( first.getType() == Type.String ) {
					for ( int i = 1; i < args.size(); i++ ) {
						first.setFloat((float)Math.pow(first.toFloat(), args.at(i).toFloat()));
					}
				} else if ( first.getType() == Type.Int ) {
					for ( int i = 1; i < args.size(); i++ ) {
						first.setInt((int)Math.pow(first.toInt(), args.at(i).toInt()));
					}
				} else if ( first.getType() == Type.Float ) {
					for ( int i = 1; i < args.size(); i++ ) {
						first.setFloat((float)Math.pow(first.toFloat(), args.at(i).toFloat()));
					}
				}
				return new Value(first);
			} else if ( args.size() == 1) {
				return new Value(args.at(0));
			}
			return new Value();
		}
		public Pow() {
			super("^");
		}
	}
	
	public static class Print extends Function {
		public Value Call(Stack args) {
			for ( int i = 0; i < args.size(); i++ ) {
				System.out.print(args.at(i).toString() + " ");
			}
			System.out.println();
			return new Value();
		}
		
		public Print() {
			super("print");
		}
	}
	
	public static class Set extends Function {
		public Value Call(Stack args) {
			if ( args.size() >= 2 ) {
				String name = args.at(0).getName(); 
				if ( name.equals("_l") || name.startsWith("_i") ) {
					if ( !args.context.hasVar(name) ) {
						return args.context.add(new Variable(args.at(0).toString(), args.at(1)));
					} else {
						return new Value(args.context.get(args.at(0).toString()).set(args.at(1)));
					}
				} else {
					args.at(0).set(args.at(1));
				}
				return new Value(args.at(0));
			} if ( args.size() == 1 ) {
				return new Value(args.at(0));
			}
			return new Value();
		}
		
		public Set() {
			super("set");
		}
	}
	
	public static class CallOther extends Function {
		public Function func;
		
		public Value Call(Stack args) {
			return func.Call(args);
		}
		
		public CallOther(String name, Function func) {
			super(name);
			this.func = func;
		}
	}
	
	public static class Help extends Function {
		public Value Call(Stack args) {
			for ( int i = 0; i < args.context.numFunctions(); i++ ) {
				args.context.getFunction("<<").Call(new Stack(new Value(args.context.functionAt(i).getName())));
			}
			return new Value();
		}
		public Help() {
			super("help");
		}
	}
	
	public static class New extends Function {
		public Value Call(Stack args) {
			if ( args.size() >= 2 ) {
				return args.context.add(new Variable(args.at(0).toString(), args.at(1)));
			} else if ( args.size() == 1 ) {
				return args.context.add(new Variable(args.at(0).toString()));
			}
			return new Value();
		}
		
		public New() {
			super("new");
		}
	}
	
	private static Vector<String> functionLines = new Vector<String>();
	private static String functionName = "";
	
	public static class Begin extends Function {
		public Value Call(Stack args) {
			functionName = args.at(0).toString();
			return new Value();
		}
		
		public Begin() {
			super("begin");
		}
	}
	
	public static class Write extends Function {
		public Value Call(Stack args) {
			String str = "";
			for (int i = 0; i < args.size(); i++ ) {
				str += args.at(i).toString() + " ";
			}
			if ( str.length() > 0 ) {
				functionLines.add(str);
			}
			return new Value("Line: " + str);
		}
		
		public Write() {
			super("write");
		}
	}
	
	public static class End extends Function {
		public Value Call(Stack args) {
			String str = "Failed to make function, '" + functionName + "'";
			if ( functionLines.size() > 0 && functionName.length() > 0 ) {
				args.context.addFunction(new Function.LineBased(functionName, functionLines));
				str = "Created function, '" + functionName + "'.";
			}
			functionLines = new Vector<String>();
			functionName = "";
			return new Value(str);
		}
		
		public End() {
			super("end");
		}
	}
	
	
	public static void registerAll(Stack env) {
		env.addFunction(new Multiply());
		env.addFunction(new Divide());
		env.addFunction(new Add());
		env.addFunction(new Subtract());
		env.addFunction(new Pow());
		
		env.addFunction(new CallOther("multiply", new Multiply()));
		env.addFunction(new CallOther("divide", new Divide()));
		env.addFunction(new CallOther("add", new Add()));
		env.addFunction(new CallOther("subtract", new Subtract()));
		env.addFunction(new CallOther("^", new Pow()));
		
		env.addFunction(new Set());
		env.addFunction(new New());
		env.addFunction(printFunction);
		
		env.addFunction(new CallOther("=", new Set()));
		env.addFunction(new CallOther(">>", new Set()));
		env.addFunction(new CallOther("<<", printFunction));
		
		env.addFunction(new Begin());
		env.addFunction(new Write());
		env.addFunction(new End());
		
		env.addFunction(new Help());
		
		env.add(new Variable.Constant("pi", new Value((float)Math.PI)));
		env.add(new Variable("", 0f)); // Referenced by "@".
	}
}
