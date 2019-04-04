package com.happylittleapps.osrs.quickerpickerupper;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

import org.osbot.rs07.api.Inventory;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.model.Player;
import org.osbot.rs07.api.ui.Message;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

import com.happylittleapps.osrs.quickerpickerupper.gui.Gui;
import com.happylittleapps.osrs.quickerpickerupper.task.Auction;
import com.happylittleapps.osrs.quickerpickerupper.task.Bank;
import com.happylittleapps.osrs.quickerpickerupper.task.DesirableItem;
import com.happylittleapps.osrs.quickerpickerupper.task.Pickup;
import com.happylittleapps.osrs.quickerpickerupper.task.Task;
import com.happylittleapps.osrs.quickerpickerupper.task.TradeableItem;

@ScriptManifest(author = "Shoff", info = "Boun-tee!", logo = "https://imgur.com/3zkHCsa.png", name = "Quicker Picker Upper", version = 0)
public class QuickerPickerUpper extends Script {
	
	final static boolean RUN_IN_DEV_MODE = false;
	
	private List<Task> taskList = new ArrayList<Task>();
	private Date lastAction = new Date();
	private Date startTime;
	private Player droppingPlayer;
	private Date droppingPlayerUpdated;
	
	public List<TradeableItem> allTradeableItems = new ArrayList<TradeableItem>();	
	public List<DesirableItem> successfulPickups = new ArrayList<DesirableItem>();
	public int lastKnownInventorySize;
	public Date lastDesirableItemFound = new Date();	
	public DesirableItem currentItem;
	private String _currentAction = "";
	
	InputStream backgroundImageFile;
	Image backgroundImage;
	
	public final Area GRAND_EXCHANGE_AREA = new Area(3137,3466,3190,3517);	
	
	private Gui gui = new Gui();
	private int _userMinimumValue = 500;
	private boolean _allowLeavingGeArea = false;
	private boolean _hideStats = false;
	private boolean _alwaysSprint = true;
	private boolean _followDrop = true;
	private boolean _showDebugInfo = false;	
	private boolean _autoSell = false;
	private boolean _doingAuction = false;

	@Override
	public int onLoop() throws InterruptedException {
		setCurrentAction("");
		int sleepTimer = random(30,60);
		
		for (Task t : taskList) {
			if (new Date().getTime() - lastDesirableItemFound.getTime() > 300000) {
				if (_autoSell) {
					Auction.setDoAuction(true);
					Bank.setDoBanking(false);
				} else {
					Auction.setDoAuction(false);
					Bank.setDoBanking(true);					
				}
			} 		
			
			
			if (t.isActive()) {
				if (t.getClass().equals(Auction.class)) {
					_doingAuction = true;
				}
				if (!(t.getClass().equals(Bank.class) && _doingAuction)) {
					t.execute();
				} 
			} else {
				if (t.getClass().equals(Auction.class)) {
					_doingAuction = false;
				}
			}
		}		

		// return to grand exchange area if we leave 
		if (!GRAND_EXCHANGE_AREA.contains(myPlayer()) && !_allowLeavingGeArea) {
			log("Player moved out of GE area, moving back");
			setCurrentAction("Moving back to grand exchange area...");
			walking.walk(GRAND_EXCHANGE_AREA);
		}
		
		// follow dropping player
		if (droppingPlayer != null && _followDrop && currentItem == null) {
			if (droppingPlayer != myPlayer()) {
				if (new Date().getTime() - droppingPlayerUpdated.getTime() < 15000) { // if he said "drop" in the last 15 seconds
					setCurrentAction("Attempting to follow " + droppingPlayer.getName() + "");
					if (!droppingPlayer.isVisible()) {
						camera.toEntity(droppingPlayer);
					}
					if (!myPlayer().getArea(10).contains(droppingPlayer)) {
						walking.walk(droppingPlayer);
						mouse.click(642, 65, false);
					}
					droppingPlayer.interact("Follow");
				}
			}
		}

		// anti-logout
		if (myPlayer().isAnimating() || myPlayer().isMoving()) {
			lastAction = new Date();
		}
		long timeSinceLastAction = (new Date().getTime() - lastAction.getTime());
		if (timeSinceLastAction > 240000) {
			log("engaging anti-afk");
			setCurrentAction("Engaging anti-afk...");
			String[] messages = { "any rich people on today?", "lol yeah", "accepting donations", "money doubling is a scam", "how much", "np"
					, "no thanks", "lol", "haha", "ty", "mmm", "a", "?", "brb", "please help report advertisers" };
			for(char c : messages[random(0,messages.length - 1)].toCharArray()) {
				this.keyboard.typeKey(c);
			}
			// this.keyboard.pressKey(KeyEvent.VK_ENTER);
			lastAction = new Date();
		}
		
		return sleepTimer;
	}
	
	@Override
	public void onStart() {
		log("Started!");
		openGui();
		
		// load images
		backgroundImageFile = getClass().getResourceAsStream("grey-textured-background.png");
		try {
			backgroundImage = ImageIO.read(backgroundImageFile);
		} catch (IOException e) {
			log(e.getMessage());
			e.printStackTrace();
		}
		
		// set starting variables
		startTime = new Date();
		lastAction = new Date();
		lastKnownInventorySize = Inventory.SIZE - getInventory().getEmptySlotCount();
		log("inventory starting size: " + lastKnownInventorySize);
		this.settings.setRunning(_alwaysSprint);
		
		// add tasks to task list
		taskList.add(new Pickup().init(this));
		taskList.add(new Bank().init(this));
		taskList.add(new Auction().init(this));
		
		// set player to running
		this.configs.settings.setRunning(true);
		
		// populate allTradeableItems
		populateTradeableItems();
		
		// set up test seed
		if (RUN_IN_DEV_MODE) {
			setPickupItems(false);
			setMinimumValue("debug");
			setFollowDrop(false);
			setAutoSell(false);
			
		}
	}

	@Override
	public void onExit() {
		gui.hide();
		log("Exited!");
	}
	
	@Override
	public void onPaint(Graphics2D g) {
		if (_showDebugInfo) {
			g.setColor(new Color(255,0,0));
			g.setFont(new Font("Arial", Font.PLAIN, 16));
			
			long lastActionMs = new Date().getTime() - lastAction.getTime();		
			long la_millis = lastActionMs % 1000;
			long la_second = (lastActionMs / 1000) % 60;
			long la_minute = (lastActionMs / (1000 * 60)) % 60;
			long la_hour = (lastActionMs / (1000 * 60 * 60)) % 24;
			String la_runtimeFormatted = String.format("%02d:%02d:%02d.%d", la_hour, la_minute, la_second, la_millis);
			
			long lastDesirableFoundMs = new Date().getTime() - lastDesirableItemFound.getTime();		
			long ldi_millis = lastDesirableFoundMs % 1000;
			long ldi_second = (lastDesirableFoundMs / 1000) % 60;
			long ldi_minute = (lastDesirableFoundMs / (1000 * 60)) % 60;
			long ldi_hour = (lastDesirableFoundMs / (1000 * 60 * 60)) % 24;
			String ldi_runtimeFormatted = String.format("%02d:%02d:%02d.%d", ldi_hour, ldi_minute, ldi_second, ldi_millis);		
			
			g.drawString("Last action: " + la_runtimeFormatted, 20, 20);
			g.drawString("lastDesirableItemFound: " + ldi_runtimeFormatted, 20, 40);
			g.drawString("Distance to item: " + (currentItem == null ? 0.0 : currentItem.getDistance()), 20, 60);
			g.drawString("Minimum Value: " + _userMinimumValue , 20, 80);
		}		
		
		if (_hideStats) {
			return;
		}
		
		
		try {
			g.drawImage(backgroundImage, 7, 345, 506, 129, null);
		} catch (Exception e) {
			log(e.getMessage());
			setCurrentAction(e.getMessage());
		}	

		
		
		g.setColor(new Color(255, 255, 255));
		g.setFont(new Font("Arial", Font.PLAIN, 18));
		
		long runtimeMs = new Date().getTime() - startTime.getTime();		
		long millis = runtimeMs % 1000;
		long second = (runtimeMs / 1000) % 60;
		long minute = (runtimeMs / (1000 * 60)) % 60;
		long hour = (runtimeMs / (1000 * 60 * 60)) % 24;
		String runtimeFormatted = String.format("%02d:%02d:%02d.%d", hour, minute, second, millis);
		
		long totalValue = 0;
		for(DesirableItem i : successfulPickups) {
			totalValue += i.getValue();
		}
		
		
		g.drawString("Runtime: " + runtimeFormatted, 50, 385);
		g.drawString("Pickup count: " + successfulPickups.size() + " items worth " + NumberFormat.getInstance().format(totalValue), 50, 405);
		g.drawString("Current item: " + (currentItem == null ? "" : currentItem.getGroundItem().getName() + " (Value: " + NumberFormat.getInstance().format(currentItem.getValue()) + ")"), 50, 425);
		g.drawString("Current action: " + _currentAction, 50, 445);
	}
	
	@Override
	public void onMessage(Message message) {
		if (message.getMessage().contains("drop")) {
			// follow user
			Player messageSender = this.camera.getPlayers().closest(message.getUsername());
			log(messageSender.getName() + " said the word DROP");
			droppingPlayer = messageSender;
			droppingPlayerUpdated = new Date();
		}
	}
	
	
	private void openGui() {
		log("starting GUI");
		gui.run(this);
	}

	private void populateTradeableItems() {		
		List<String[]> allData = readAllDataAtOnce("prices.csv");
		if (allData == null) {
			log("allData came back NULL");
			return;
		}
		
		allData.remove(0); // remove header row
		int curRow = 2;
		for (String[] row : allData) {
			// add parse into new TradeableItem
			TradeableItem item = new TradeableItem();
			try {
				item.setItemId(Integer.valueOf(row[0]));
				item.setItemName(row[1]);
				item.setValue(Long.valueOf(row[2]));
			} catch (Exception e) {
				log("error reading file in row " + curRow);
				log(e.getMessage());
			}
			
			allTradeableItems.add(item);
			curRow++;
		}
	}
	
	private List<String[]> readAllDataAtOnce(String file) 
	{		
		try {
			List<String[]> results = new ArrayList<String[]>();
			InputStreamReader isr = new InputStreamReader(getClass().getResourceAsStream(file));
			BufferedReader br = new BufferedReader(isr);
			while (br.ready()) {
				String curRow = br.readLine();
				results.add(curRow.split(","));
			}
			return results;
		} catch (Exception e) {
			log(e.getMessage());
		}
		
	    return null;
	} 

	
	public void setMinimumValue(String value) {
		try {
			// easter egg values
			if (value.toLowerCase().equals("debug")) {
				_showDebugInfo = true;
				return;
			}
			if (value.toLowerCase().equals("nodebug")) {
				_showDebugInfo = false;
				return;
			}
			
			// set value
			int newMinValue = Integer.valueOf(value);
			if (newMinValue == this._userMinimumValue) {
				return;
			}
			this._userMinimumValue = newMinValue;
			for(Task t : taskList) {
				if (t.getClass() == Pickup.class) {
					Pickup pickupTask = (Pickup) t;
					pickupTask.desirableItems.clear();
					pickupTask.undesirableItems.clear();
				}
			}
		} catch (Exception e) {	}
	}


	public long getMinimumValue() {		
		return this._userMinimumValue;
	}

	public boolean getAllowLeaveGeArea() {
		return this._allowLeavingGeArea;
	}	
	
	public void setAllowLeaveGeArea(boolean value) {
		gui.setChkStayInGrandExchangeArea(!value);
		if (value == this._allowLeavingGeArea) {
			return;
		}
		this._allowLeavingGeArea = value;
		for(Task t : taskList) {
			if (t.getClass() == Pickup.class) {
				Pickup pickupTask = (Pickup) t;
				pickupTask.desirableItems.clear();
				pickupTask.undesirableItems.clear();
			}
		}
	}

	public void setHideStats(boolean selected) {
		this._hideStats = selected;
		gui.setChkShowStats(!selected);
	}

	public void setAlwaysSprint(boolean selected) {
		this._alwaysSprint = selected;
		this.settings.setRunning(_alwaysSprint);
	}

	public void setFollowDrop(boolean selected) {
		this._followDrop = selected;
		gui.setChkFollowDrop(selected);
	}

	public void setLastDesirableItemFound(Date date) {
		this.lastDesirableItemFound = date;
		Bank.setDoBanking(false);
		Auction.setDoAuction(false);
	}

	public void setAutoSell(boolean selected) {
		this._autoSell = selected;
		gui.setChkAutoSell(selected);
	}

	public void setPickupItems(boolean selected) {
		Pickup.setDoPickup(selected);
		gui.setChkPickupItems(selected);
	}
	
	public void setCurrentAction(String action) {
		//this._currentAction = "[" + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME) + "] " +  action;
		this._currentAction = action;
	}
	
	// TODO:
	// auto-navigate to GE area from anywhere
	// auto-auction

}
