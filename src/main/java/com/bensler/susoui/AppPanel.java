package com.bensler.susoui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import com.bensler.suso.Coordinate;
import com.bensler.suso.FieldImpl;
import com.bensler.suso.Game;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/** Container for this Apps whole UI. */
public class AppPanel extends JPanel {

  public enum GameState {
    SETUP,
    SOLVE
  }

  private final Map<JLabel, Coordinate> coordinatesByLabel;

  private final Game game;

  AppPanel() {
    super(new FormLayout(
      "5dlu:g, f:p, 15dlu, f:p, 5dlu:g", // columns
      "5dlu:g, f:p, 5dlu:g"             // row
    ));
    final CellConstraints cc = new CellConstraints();

    coordinatesByLabel = new HashMap<>();
    game = new Game(SampleGames.SAMPLE_1);
    add(createGamePanel(),    cc.rc(2, 2));
    add(createControlPanel(), cc.rc(2, 4));
  }

  private JPanel createGamePanel() {
    final LabelCreator labelCreator = new LabelCreator();
    final JPanel gamePanel = new JPanel(new FormLayout(
      labelCreator.rowColSpecs, // columns
      labelCreator.rowColSpecs  // rows
    ));
    final CellConstraints cc = new CellConstraints();
    final FieldImpl field = game.getField();

    game.getCoordinates().forEach(coordinate -> {
      final JLabel label = labelCreator.createLabel(
        field.getDigit(coordinate).map(digit -> String.valueOf(digit.getNumber())).orElse(""),
        true
      );

      coordinatesByLabel.put(label, coordinate);
      gamePanel.add(label, cc.rc((coordinate.getY() * 2) + 1, (coordinate.getX() * 2) + 1));
    });
    return gamePanel;
  }

  private JPanel createControlPanel() {
    final JPanel controlPanel = new JPanel(new FormLayout(
      "f:p",            // column
      "f:p, 5dlu, f:p"  // rows
    ));
    final CellConstraints cc = new CellConstraints();
    final JButton button = new JButton("Button");

    controlPanel.add(button, cc.rc(1, 1));
    controlPanel.add(new GamePanel(), cc.rc(3, 1));
    controlPanel.setBackground(Color.BLUE);
    return controlPanel;
  }

  protected void rightMouseClick(JLabel label) {
    System.out.println(coordinatesByLabel.get(label));
  }

  class LabelCreator {

    private final Dimension labelPrefSize;
    private final Border labelBorder;
    private final Font boldLabelFont;
    private final String rowColSpecs;

    LabelCreator() {
      final JLabel label = new JLabel("m");
      final Dimension prefSize;
      final int maxDimension;
      final String cellGroup;

      boldLabelFont = label.getFont().deriveFont(Font.BOLD);
      labelBorder = new CompoundBorder(new LineBorder(Color.BLACK), new EmptyBorder(5, 5, 5, 5));
      label.setBorder(labelBorder);
      prefSize = label.getPreferredSize();
      maxDimension = Math.max(prefSize.width, prefSize.height);
      labelPrefSize = new Dimension(maxDimension, maxDimension);
      cellGroup = "f:p, 5dlu, f:p, 5dlu, f:p";
      rowColSpecs = cellGroup + ", 8dlu, " + cellGroup + ", 8dlu, " + cellGroup;
    }

    JLabel createLabel(String text, boolean bold) {
      final JLabel label = new JLabel(text);

      label.setHorizontalAlignment(SwingConstants.CENTER);
      if (bold) {
        label.setFont(boldLabelFont);
      }
      label.setBorder(labelBorder);
      label.setPreferredSize(labelPrefSize);
      label.addMouseListener(new MouseAdapter() {
        @Override
        public void mousePressed(MouseEvent evt) {
          AppPanel.this.rightMouseClick(label);
        }
      });
      return label;
    }

  }

}