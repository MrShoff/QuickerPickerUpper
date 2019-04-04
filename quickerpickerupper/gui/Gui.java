package com.happylittleapps.osrs.quickerpickerupper.gui;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import com.happylittleapps.osrs.quickerpickerupper.QuickerPickerUpper;

public class Gui {
	
	private JFrame jFrame;
	
	JCheckBox chkAutoSell;
	JTextField txtMinValue;
	JCheckBox chkStayInGrandExchangeArea;
	JCheckBox chkAlwaysSprint;
	JCheckBox chkShowStats;
	JCheckBox chkFollowDrop;
	JCheckBox chkPickupItems;

	public void run(QuickerPickerUpper main) {
		jFrame = new JFrame("QPR Settings");
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jFrame.setSize(320, 300);
		jFrame.setResizable(false);

		JPanel settingsPanel = new JPanel();
		TitledBorder leftBorder = BorderFactory.createTitledBorder("Settings");
		leftBorder.setTitleJustification(TitledBorder.LEFT);
		settingsPanel.setBorder(leftBorder);
		settingsPanel.setLayout(null);
		settingsPanel.setBounds(30, 30, 240, 240);
		jFrame.add(settingsPanel);
		
		JLabel lblMinValue = new JLabel("Choose your minimum value:");
		lblMinValue.setBounds(25, 40, 195, 20);
		settingsPanel.add(lblMinValue);

		txtMinValue = new JTextField();
		txtMinValue.setText("500");
		txtMinValue.addActionListener(e -> main.setMinimumValue((String) txtMinValue.getText()));
		txtMinValue.setBounds(185, 40, 110, 20);
		settingsPanel.add(txtMinValue);
		
		chkStayInGrandExchangeArea = new JCheckBox();
		chkStayInGrandExchangeArea.setText("Stay in Grand Exchange area");
		chkStayInGrandExchangeArea.setSelected(true);
		chkStayInGrandExchangeArea.addActionListener(e -> main.setAllowLeaveGeArea(!chkStayInGrandExchangeArea.isSelected()));
		chkStayInGrandExchangeArea.setBounds(25, 60, 195, 20);
		settingsPanel.add(chkStayInGrandExchangeArea);
		
		chkAlwaysSprint = new JCheckBox();
		chkAlwaysSprint.setText("Always sprint");
		chkAlwaysSprint.setSelected(true);
		chkAlwaysSprint.addActionListener(e -> main.setAlwaysSprint(chkAlwaysSprint.isSelected()));
		chkAlwaysSprint.setBounds(25, 80, 195, 20);
		settingsPanel.add(chkAlwaysSprint);
		
		chkShowStats = new JCheckBox();
		chkShowStats.setText("Show stats");
		chkShowStats.setSelected(true);
		chkShowStats.addActionListener(e -> main.setHideStats(!chkShowStats.isSelected()));
		chkShowStats.setBounds(25, 100, 195, 20);
		settingsPanel.add(chkShowStats);		
		
		chkFollowDrop = new JCheckBox();
		chkFollowDrop.setText("Follow drop");
		chkFollowDrop.setSelected(true);
		chkFollowDrop.addActionListener(e -> main.setFollowDrop(chkFollowDrop.isSelected()));
		chkFollowDrop.setBounds(25, 120, 195, 20);
		settingsPanel.add(chkFollowDrop);
		
		chkPickupItems = new JCheckBox();
		chkPickupItems.setText("Pickup Items");
		chkPickupItems.setSelected(true);
		chkPickupItems.addActionListener(e -> {
			main.setPickupItems(chkPickupItems.isSelected());
			txtMinValue.setEnabled(chkPickupItems.isSelected());
			chkStayInGrandExchangeArea.setEnabled(chkPickupItems.isSelected());
			chkAlwaysSprint.setEnabled(chkPickupItems.isSelected());
			chkShowStats.setEnabled(chkPickupItems.isSelected());
			chkFollowDrop.setEnabled(chkPickupItems.isSelected());
		});
		chkPickupItems.setBounds(10, 20, 195, 20);
		settingsPanel.add(chkPickupItems);
		
		chkAutoSell = new JCheckBox();
		chkAutoSell.setText("Auto-sell items on Grand Exchange");
		chkAutoSell.setSelected(true);
		chkAutoSell.addActionListener(e -> main.setAutoSell(chkAutoSell.isSelected()));
		chkAutoSell.setBounds(10, 140, 195, 20);
		settingsPanel.add(chkAutoSell);

		jFrame.setVisible(true);
	}
	
	public void hide() {
		jFrame.setVisible(false);
	}
	
	public void setChkAutoSell(boolean value) { chkAutoSell.setSelected(value); }
	public void setChkStayInGrandExchangeArea(boolean value) { chkStayInGrandExchangeArea.setSelected(value); }
	public void setChkAlwaysSprint(boolean value) { chkAlwaysSprint.setSelected(value); }
	public void setChkShowStats(boolean value) { chkShowStats.setSelected(value); }
	public void setChkFollowDrop(boolean value) { chkFollowDrop.setSelected(value); }
	public void setChkPickupItems(boolean value) { chkPickupItems.setSelected(value); }
	public void setTxtMinValue(String value) { txtMinValue.setText(value); }
	
}
