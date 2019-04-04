package com.happylittleapps.osrs.quickerpickerupper.task;

import com.happylittleapps.osrs.quickerpickerupper.QuickerPickerUpper;

public abstract class Task {
	
	protected QuickerPickerUpper script;
	
	public Task init(QuickerPickerUpper script) {
		this.script = script;
		
		return this;
	}
	
	public abstract boolean isActive();
	public abstract void execute() throws InterruptedException;

}
