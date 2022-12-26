package com.bensler.susoui;

import java.awt.AWTException;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.jgoodies.looks.plastic.Plastic3DLookAndFeel;
import com.jgoodies.looks.plastic.theme.DesertYellow;

/** App entry point. */
public class Main {

  public static void main(String[] args) throws UnsupportedLookAndFeelException, AWTException {
    Plastic3DLookAndFeel.setCurrentTheme(new DesertYellow());
    UIManager.setLookAndFeel(new Plastic3DLookAndFeel());

    new BaseApp("Sudoku Solver", new AppPanel());
  }

}
