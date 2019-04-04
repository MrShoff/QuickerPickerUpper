package com.happylittleapps.osrs.quickerpickerupper.task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.osbot.rs07.api.Inventory;
import org.osbot.rs07.api.model.GroundItem;

public class Pickup extends Task {
	
	public ArrayList<DesirableItem> desirableItems = new ArrayList<DesirableItem>();
	public ArrayList<DesirableItem> undesirableItems = new ArrayList<DesirableItem>();
	
	private static boolean _doPickup = true;

	@Override
	public boolean isActive() {
		if (script.inventory.isFull()) {
			return false;
		}
		
		return _doPickup;
	}

	@Override
	public void execute() throws InterruptedException {
		
		List<GroundItem> itemsOnGround = script.getGroundItems().getAll();
		
		for (GroundItem item : itemsOnGround) {
			boolean alreadyTested = false;
			for (DesirableItem i : desirableItems) {
				if (i.getGroundItem() == item) {
					alreadyTested = true;
				}
			}
			for (DesirableItem i : undesirableItems) {
				if (i.getGroundItem() == item) {
					alreadyTested = true;
				}
			}
			if (alreadyTested) {
				continue;
			}
			script.log("new item found: " + item.getName());
			DesirableItem testItem = new DesirableItem(script, item);
			if (!script.getAllowLeaveGeArea() && !script.GRAND_EXCHANGE_AREA.contains(item)) {
				undesirableItems.add(testItem);
				script.log("failed (outside GE): " + testItem.getGroundItem().getName());
				continue;
			}
			if (testItem.isDesirable()) {
				desirableItems.add(testItem);
				
				if (Collections.max(desirableItems, new SortByValue()).getValue() < 1000) {
					Collections.sort(desirableItems, new SortByDistance());
				} else {
					Collections.sort(desirableItems, new SortByValue());
				}
				script.log("passed: " + testItem.getGroundItem().getName());
				script.setLastDesirableItemFound(new Date());
			} else {
				undesirableItems.add(testItem);
				script.log("failed: " + testItem.getGroundItem().getName());
			}
		}	
		
		if (script.currentItem == null) {
			script.setCurrentAction("Scanning for items...");
			for (DesirableItem item : desirableItems) {
				script.log("setting currentItem = " + item.getGroundItem().getName());
				if (item != null) {
					script.currentItem = item;
					desirableItems.remove(script.currentItem);
					break;
				}
			}
		} else {
			script.log("attempting to pickup " + script.currentItem.getGroundItem().getName());
			script.setCurrentAction("Attempting to pick up item");
			// if its still on the ground
			if (itemsOnGround.contains(script.currentItem.getGroundItem())) {
				script.currentItem.getGroundItem().interact("Take");
				
				// if its not close, walk over to it				
				if (script.currentItem.getDistance() > 10) {
					if (script.camera.toEntity(script.currentItem.getGroundItem())) {
						script.mouse.click(642, 65, false);
					}
				}
				// if its not visible, turn camera to it
				if (!script.currentItem.getGroundItem().isVisible()) {
					script.log("not isVisible: " + script.currentItem.getGroundItem().getName());
					script.camera.toEntity(script.currentItem.getGroundItem());
				} 
			} else {
				// item is no longer on the ground, is it in our backpack?
				int inventorySizeDiff = Inventory.SIZE - script.getInventory().getEmptySlotCount() - script.lastKnownInventorySize; 
				if (inventorySizeDiff != 0) {
					if (script.inventory.contains(script.currentItem.getGroundItem().getId())) {
						script.successfulPickups.add(script.currentItem);						
					}
					script.lastKnownInventorySize = Inventory.SIZE - script.getInventory().getEmptySlotCount();
				}
				script.currentItem = null;
			}
		}		
	}

	public static void setDoPickup(boolean selected) {
		_doPickup = selected;		
	}
}
