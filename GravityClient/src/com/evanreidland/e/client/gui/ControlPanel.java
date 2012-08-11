package com.evanreidland.e.client.gui;

import java.util.Vector;

import com.evanreidland.e.script.Value;
import com.evanreidland.e.script.Variable;

public class ControlPanel extends GravityGUIObject
{
	public class PanelItem
	{
		public String label;
		public Value value;
		
		public PanelItem(String label, Value value)
		{
			this.label = label;
			this.value = value;
		}
		
		public PanelItem(Variable var)
		{
			this(var.getName(), var);
		}
	}
	
	private Vector<PanelItem> items;
	
	public double itemSpacing;
	
	public void onRender()
	{
		super.onRender();
		// TODO code.
	}
	
	public void onUpdate()
	{
		super.onUpdate();
		// TODO code.
	}
	
	public void addToPanel(String label, Value value)
	{
		items.add(new PanelItem(label, value));
	}
	
	public ControlPanel(String name, double width, double height)
	{
		super(name, width, height);
		items = new Vector<PanelItem>();
		itemSpacing = 16;
	}
}
