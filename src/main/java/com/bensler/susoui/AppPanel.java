package com.bensler.susoui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import com.bensler.suso.Game;
import com.bensler.susoui.widget.GamePanel;
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

    game = new Game(SampleGames.SAMPLE_VALID_2);
    add(gamePanel = new GamePanel(game),  cc.rc(2, 2));
    add(            createControlPanel(), cc.rc(2, 4));
  }

  private JPanel createControlPanel() {
    final JPanel controlPanel = new JPanel(new FormLayout(
      "f:p",          // column
      "p, 5dlu, p, 5dlu, p, 5dlu:g, p"  // rows
    ));
    final CellConstraints cc = new CellConstraints();
    final JCheckBox cbSuggest = new JCheckBox("Suggestions");

    cbSuggest.addActionListener(event -> gamePanel.setSuggestions(cbSuggest.isSelected()));
    controlPanel.add(cbSuggest, cc.rc(1, 1));
    controlPanel.add(new BooleanCriteriaPanel("determined"), cc.rc(3, 1));
    controlPanel.add(new BooleanCriteriaPanel("solvable"), cc.rc(5, 1));
    controlPanel.add(createViewSizePanel(), cc.rc(7, 1));
    controlPanel.setBackground(new Color(200, 200, 255));
    return controlPanel;
  }

  private JPanel createViewSizePanel() {
    final FormLayout layout = new FormLayout(
      "f:p, 5dlu:g, f:p",
      "p");
    final JPanel viewSizePanel = new JPanel(layout);
    final CellConstraints cc = new CellConstraints();
    final JButton plusButton = new JButton("+");
    final JButton minusButton = new JButton("-");

    viewSizePanel.setBorder(new TitledBorder(
      new CompoundBorder(
        new LineBorder(plusButton.getForeground(), 1, true),
        new EmptyBorder(5, 5, 5, 5)
      ),
      "Size", TitledBorder.TRAILING, TitledBorder.DEFAULT_POSITION, plusButton.getFont(), plusButton.getForeground()
    ));
    layout.setColumnGroup(1, 3);
    plusButton.addActionListener(event -> gamePanel.incCharSize());
    minusButton.addActionListener(event -> gamePanel.decCharSize());
    viewSizePanel.add(plusButton, cc.rc(1, 1));
    viewSizePanel.add(minusButton, cc.rc(1, 3));
    return viewSizePanel;
  }

  static class BooleanCriteriaPanel extends JPanel implements ActionListener {

    private final JCheckBox checkbox;
    private final JLabel indicator;

    BooleanCriteriaPanel(String text) {
      super(new FormLayout("p, 5dlu:g, p", "p"));

      final CellConstraints cc = new CellConstraints();

      add(checkbox = new JCheckBox(text), cc.rc(1, 1));
      add(indicator = new JLabel(), cc.rc(1, 3));
      checkbox.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      // TODO Auto-generated method stub

    }

  }

}