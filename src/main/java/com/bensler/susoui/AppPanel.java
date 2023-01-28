package com.bensler.susoui;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.bensler.suso.Game;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/** Container for this Apps whole UI. */
public class AppPanel extends JPanel {

  public enum GameState {
    SETUP,
    SOLVE
  }

  private final Game game;
  private final GamePanel gamePanel;

  AppPanel() {
    super(new FormLayout(
      "5dlu:g, f:p, 15dlu, f:p, 5dlu:g", // columns
      "5dlu:g, f:p, 5dlu:g"              // row
    ));
    final CellConstraints cc = new CellConstraints();

    game = new Game(SampleGames.SAMPLE_INVALID);
    add(gamePanel = new GamePanel(game),  cc.rc(2, 2));
    add(            createControlPanel(), cc.rc(2, 4));
  }

  private JPanel createControlPanel() {
    final JPanel controlPanel = new JPanel(new FormLayout(
      "c:p",                // column
      "p, 5dlu, p, 5dlu:g"  // rows
    ));
    final CellConstraints cc = new CellConstraints();
    final JButton plusButton = new JButton("+");
    final JButton minusButton = new JButton("-");

    plusButton.addActionListener(event -> gamePanel.incCharSize());
    minusButton.addActionListener(event -> gamePanel.decCharSize());
    controlPanel.add(plusButton, cc.rc(1, 1));
    controlPanel.add(minusButton, cc.rc(3, 1));
    controlPanel.setBackground(new Color(200, 200, 255));
    return controlPanel;
  }

  protected void rightMouseClick(JLabel label) {
    // TODO System.out.println(coordinatesByLabel.get(label));
  }

}