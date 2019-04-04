package com.happylittleapps.osrs.quickerpickerupper.task;

import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.model.NPC;

public class Bank extends Task {

	final Area GRAND_EXCHANGE_BANKERS_AREA = new Area(3168,3493,3161,3486);
	final int[] GRAND_EXCHANGE_BANKERS = { 6528,5453,5456,5455 };
	
	private static boolean _doBanking = false;
	public static void setDoBanking(boolean value) { _doBanking = value; }
	
	@Override
	public boolean isActive() {		
		if (script.inventory.isEmpty()) {
			return false;
		}
		
		return _doBanking;
	}

	@Override
	public void execute() throws InterruptedException {
		script.setCurrentAction("Attempting banking...");
		NPC closestBanker = script.npcs.closest(GRAND_EXCHANGE_BANKERS);
		if (GRAND_EXCHANGE_BANKERS_AREA.contains(script.myPlayer())) {
			if (script.bank.isOpen()) {
				if (script.bank.depositAll() && script.inventory.isEmpty()) {
					script.bank.close();
					script.lastKnownInventorySize = 0;
					Bank.setDoBanking(false);
				}
			} else {
				if (!closestBanker.isVisible()) {
					script.camera.toEntity(closestBanker);
				}
				closestBanker.interact("Bank");
			}
		} else {
			script.walking.walk(GRAND_EXCHANGE_BANKERS_AREA);
		}
		
	}

}
