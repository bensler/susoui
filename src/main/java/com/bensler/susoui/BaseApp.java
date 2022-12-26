package com.bensler.susoui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Rectangle;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class BaseApp extends JFrame {

	public BaseApp(String title, Component frameContent) {
		super(title);
		getContentPane().add(frameContent, BorderLayout.CENTER);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setVisible(true);
		pack();
		centerOnScreen();
	}

	public void centerOnScreen() {
		final Rectangle screenBounds = getGraphicsConfiguration().getBounds();
		final Rectangle windowBounds = getBounds();

		final int xPos = ((screenBounds.width - windowBounds.width) / 2);
		final int yPos = ((screenBounds.height - windowBounds.height) / 2);

		setLocation(Math.max(0, xPos), Math.max(0, yPos));
		setSize(
			Math.min(screenBounds.width, windowBounds.width),
			Math.min(screenBounds.height, windowBounds.height)
		);
	}

}
