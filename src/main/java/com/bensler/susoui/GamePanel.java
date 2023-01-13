package com.bensler.susoui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;

public class GamePanel extends JComponent {

  private static final Color DECKWEISS = new Color(255, 255, 255, 255);
  private static final Color AQUARELL_ROT = new Color(255, 0, 0, 85);
  private static final Color DECKROT = new Color(255, 0, 0);

  public GamePanel() {
    setPreferredSize(new Dimension(70, 70));
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    final Graphics2D graphics = (Graphics2D) g;
    final Dimension size = getSize();
    Font font = getFont().deriveFont(Font.BOLD);
    Rectangle2D stringBounds = new TextLayout("8", font, graphics.getFontRenderContext()).getBounds();
    graphics.setColor(DECKWEISS);
    graphics.fillRect(0, 0, size.width, size.height);
    graphics.setColor(AQUARELL_ROT);
    graphics.fillRect(10, 10, 30, 30);
    graphics.fillRect(20, 20, 30, 30);
    graphics.fillRect(30, 30, 30, 30);
    graphics.setColor(DECKROT);
    graphics.fillRect(45, 10, 15, 15);
    graphics.setFont(font);
    graphics.drawString("8", 10, 50);
  }


}
