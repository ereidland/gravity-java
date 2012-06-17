package com.evanreidland.e.client.graphics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import com.evanreidland.e.engine;

public class BlockTypeList {
	private HashMap<Integer, BlockType> blockTypes;
	public TextureGrid grid;
	
	public void addType(BlockType type) {
		blockTypes.put(type.getID(), type);
	}
	
	public BlockType getType(int id) {
		return blockTypes.get(id);
	}
	
	public double cellWidth() {
		return grid.x(1);
	}
	public double cellHeight() {
		return grid.y(1);
	}
	
	public double topX(int id) {
		BlockType t = getType(id);
		if ( t != null ) {
			return grid.x(t.getTX());
		}
		return 0;
	}
	public double topY(int id) {
		BlockType t = getType(id);
		if ( t != null ) {
			return grid.y(t.getTY());
		}
		return 0;
	}
	
	public double bottomX(int id) {
		BlockType t = getType(id);
		if ( t != null ) {
			return grid.x(t.getBX());
		}
		return 0;
	}
	public double bottomY(int id) {
		BlockType t = getType(id);
		if ( t != null ) {
			return grid.y(t.getBY());
		}
		return 0;
	}
	
	public double leftX(int id) {
		BlockType t = getType(id);
		if ( t != null ) {
			return grid.x(t.getLX() );
		}
		return 0;
	}
	public double leftY(int id) {
		BlockType t = getType(id);
		if ( t != null ) {
			return grid.y(t.getLY());
		}
		return 0;
	}
	
	public double rightX(int id) {
		BlockType t = getType(id);
		if ( t != null ) {
			return grid.x(t.getRX() );
		}
		return 0;
	}
	public double rightY(int id) {
		BlockType t = getType(id);
		if ( t != null ) {
			return grid.y(t.getRY());
		}
		return 0;
	}
	
	public double frontX(int id) {
		BlockType t = getType(id);
		if ( t != null ) {
			return grid.x(t.getFX() );
		}
		return 0;
	}
	public double frontY(int id) {
		BlockType t = getType(id);
		if ( t != null ) {
			return grid.y(t.getFY());
		}
		return 0;
	}
	
	public double backX(int id) {
		BlockType t = getType(id);
		if ( t != null ) {
			return grid.x(t.getBkX() );
		}
		return 0;
	}
	public double backY(int id) {
		BlockType t = getType(id);
		if ( t != null ) {
			return grid.y(t.getBkY());
		}
		return 0;
	}
	
	public BlockTypeList(int gridWidth, int gridHeight) {
		grid = new TextureGrid(gridWidth, gridHeight);
		blockTypes = new HashMap<Integer, BlockType>();
	}
	
	public static BlockTypeList fromFile(String path) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File(engine.getPath() + path)));
			
			String line = "";
			
			//System.out.println(engine.getPath() + path);
			
			line = reader.readLine();
			String[] split = line.split(" ");
			if ( split.length < 2 ) {
				engine.Error("Missing size arguments in " + path);
				return null;
			}
			int gridWidth = Integer.valueOf(split[0]),
				gridHeight = Integer.valueOf(split[1]);
			
			BlockTypeList list = new BlockTypeList(gridWidth, gridHeight);
			
			while ( (line = reader.readLine()) != null ) {
				split = line.split(" ");
				int cid = Integer.valueOf(split[0]);
				if ( split[2].equals("tsb") ) {
					list.addType(new BlockType(cid, Integer.valueOf(split[3]), Integer.valueOf(split[4]), Integer.valueOf(split[5]), Integer.valueOf(split[6]), Integer.valueOf(split[7]), Integer.valueOf(split[8])));
				} else if ( split[1].equals("full") ) {
					list.addType(new BlockType(cid, Integer.valueOf(split[3]), Integer.valueOf(split[4]), Integer.valueOf(split[5]), Integer.valueOf(split[6]), Integer.valueOf(split[7]), Integer.valueOf(split[8]), Integer.valueOf(split[9]), Integer.valueOf(split[10]), Integer.valueOf(split[11]), Integer.valueOf(split[12]), Integer.valueOf(split[13]), Integer.valueOf(split[14])));
				} else {
					list.addType(new BlockType(cid, Integer.valueOf(split[3]), Integer.valueOf(split[4])));
				}
			}
			
			return list;
		} catch ( Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
