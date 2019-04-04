package com.happylittleapps.osrs.quickerpickerupper.task;
import org.osbot.rs07.api.Bank.BankMode;
import org.osbot.rs07.api.GrandExchange;
import org.osbot.rs07.api.Inventory;
import org.osbot.rs07.api.filter.Filter;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.model.Item;
import org.osbot.rs07.api.model.NPC;

public class Auction extends Task {
	
	
	private static boolean _doAuction = false;
	public static void setDoAuction(boolean value) { _doAuction = value; }
	
	final Area GRAND_EXCHANGE_BANKERS_AREA = new Area(3168,3493,3161,3486);
	private static final int[] GRAND_EXCHANGE_BANKERS = { 6528,5453,5456,5455 };
	private static final int[] GRAND_EXCHANGE_EXCHANGERS = { 2148, 2149, 2150, 2151 };
	
	final static int MAX_WITHDRAW_COUNT = 8;
	
	private int lastBankCount = -1;

	@Override
	public boolean isActive() {	
		if (lastBankCount == 0 && script.inventory.isEmpty()) {
			_doAuction = false;
		}
		
		return _doAuction;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void execute() throws InterruptedException {
		script.setCurrentAction("Attempting auction...");
		boolean doingBanking = false;
		// retrieve items from bank
		if (Inventory.SIZE == script.inventory.getEmptySlotCount() + (script.inventory.getItem(995) != null ? 1 : 0)) {
			script.setCurrentAction("Attempting auction banking...");
			doingBanking = true;
			NPC closestBanker = script.npcs.closest(GRAND_EXCHANGE_BANKERS);
			if (GRAND_EXCHANGE_BANKERS_AREA.contains(script.myPlayer())) {
				if (script.bank.isOpen()) {
					if (script.bank.getItemsInTab(0).length > 0) {
						script.setCurrentAction("Attempting auction banking (trying to withdraw)...");
						script.bank.enableMode(BankMode.WITHDRAW_NOTE);
						int bankItemCount = script.bank.getItemsInTab(0).length;
						int withdrawCount = script.inventory.getEmptySlotCount() > bankItemCount ? bankItemCount : script.inventory.getEmptySlotCount();
						withdrawCount = MAX_WITHDRAW_COUNT < withdrawCount ? MAX_WITHDRAW_COUNT : withdrawCount;
						for (int i = 0; i < withdrawCount; i++) {
							script.bank.withdrawAll(script.bank.getItemsInTab(0)[0].getId());
						}					
					} else {
						lastBankCount = script.bank.getItemsInTab(0).length;
						doingBanking = false;
					}
				} else {
					script.setCurrentAction("Attempting auction banking (trying to open bank)...");
					if (!closestBanker.isVisible()) {
						script.camera.toEntity(closestBanker);
					}
					closestBanker.interact("Bank");
				}
			} else {
				script.walking.walk(GRAND_EXCHANGE_BANKERS_AREA);
			}
		}		
		
		// open grand exchange & sell items
		if (!doingBanking && Inventory.SIZE != script.inventory.getEmptySlotCount() + (script.inventory.getItem(995) != null ? 1 : 0)) {
			script.setCurrentAction("Attempting auction selling...");
			Filter<Item> inverseGoldFilter = item -> item.getId() != 995;
			
			NPC closestExchange = script.npcs.closest(GRAND_EXCHANGE_EXCHANGERS);
			if (GRAND_EXCHANGE_BANKERS_AREA.contains(script.myPlayer())) {
				if (script.grandExchange.isOpen()) {
					script.grandExchange.collect();
					if (script.grandExchange.getStatus(GrandExchange.Box.BOX_1) == GrandExchange.Status.EMPTY
							|| script.grandExchange.getStatus(GrandExchange.Box.BOX_2) == GrandExchange.Status.EMPTY
							|| script.grandExchange.getStatus(GrandExchange.Box.BOX_3) == GrandExchange.Status.EMPTY) {
						if (script.grandExchange.getStatus(GrandExchange.Box.BOX_1) == GrandExchange.Status.EMPTY) { script.grandExchange.sellItems(GrandExchange.Box.BOX_1); }
						if (script.grandExchange.getStatus(GrandExchange.Box.BOX_2) == GrandExchange.Status.EMPTY) { script.grandExchange.sellItems(GrandExchange.Box.BOX_2); }
						if (script.grandExchange.getStatus(GrandExchange.Box.BOX_3) == GrandExchange.Status.EMPTY) { script.grandExchange.sellItems(GrandExchange.Box.BOX_3); }
						Item firstSelectedItem = script.inventory.getItem(inverseGoldFilter);
						script.setCurrentAction("Selling " + firstSelectedItem.getName() + " (" + firstSelectedItem.getAmount() + ")");
						script.grandExchange.sellItem(firstSelectedItem.getId(), script.grandExchange.getOfferPrice(), firstSelectedItem.getAmount());
					} 
				} else {
					if (!closestExchange.isVisible()) {
						script.camera.toEntity(closestExchange);
					}
					closestExchange.interact("Exchange");
				}
			} else {
				script.walking.walk(GRAND_EXCHANGE_BANKERS_AREA);
			}	
		}
	}
}
