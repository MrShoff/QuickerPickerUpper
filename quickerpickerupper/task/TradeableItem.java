package com.happylittleapps.osrs.quickerpickerupper.task;

public class TradeableItem {
	
	private long _value;
	private int _itemId;
	private String _itemName;
	
	public TradeableItem() {
		
	}

	public void setValue(long value) {
		this._value = value;
	}
	
	public void setItemId(int id) {
		this._itemId = id;
	}
	
	public void setItemName(String name) {
		this._itemName = name;
	}
	
	public long getValue() {
		return this._value;
	}
	
	public int getItemId() {
		return this._itemId;
	}
	
	public String getItemName() {
		return this._itemName;
	}
}
