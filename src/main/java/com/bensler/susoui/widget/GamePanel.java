package com.bensler.susoui.widget;

import static java.lang.Math.round;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JLabel;

import com.bensler.suso.Constraint;
import com.bensler.suso.Coordinate;
import com.bensler.suso.Digit;
import com.bensler.suso.Game;
import com.bensler.suso.Solver;

public class GamePanel extends JComponent {

  private static final float CONSTRAINT_EXTEND = 1.3f;
  private static final Color BACKGROUND_WHITE = Color.WHITE;
  private static final Color CONSTRAINT_VIOLATED_RED = new Color(255, 0, 0, 85);
  private static final Color CHARACTER_BLACK = Color.BLACK;
  private static final Color CHARACTER_DARK_RED = new Color(255, 0, 0);
  private static final Color FRAME_BLACK = Color.BLACK;
  private static final Color SELECTION_INDICATOR_GREEN = Color.GREEN;

  final Font baseFont;
  private final Game game;
  private final Solver solver;

  private Optional<Coordinate> selectedCell;
  private VariableSizes sizes;

  public GamePanel(Game pGame) {
    baseFont = new JLabel().getFont();
    solver = new Solver(game = pGame);
    setFontSize(15);
    selectedCell = Optional.of(new Coordinate(4, 4));
    setFocusable(true);
    addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent evt) {
        selectCell(evt.getPoint());
      }
    });
    addKeyListener(new KeyboardActions(this));
  }

  void moveSelection(Point delta) {
    selectedCell = selectedCell.map(oldSelection -> new Coordinate(
      (oldSelection.getX() + delta.x + game.getWidth() ) % game.getWidth(),
      (oldSelection.getY() + delta.y + game.getHeight()) % game.getHeight()
    ));
    repaint();
  }

  void setSelectedDigit(Optional<Digit> digit) {
    selectedCell.ifPresent(cell -> game.setDigit(cell, digit));
    repaint();
  }

  private void selectCell(Point position) {
    final OptionalInt row = sizes.getRowCol(position.getY());
    final OptionalInt col = sizes.getRowCol(position.getX());

    selectedCell = Optional.ofNullable(
      (row.isPresent() && col.isPresent())
      ? new Coordinate(col.getAsInt(), row.getAsInt())
      : null
    );
    requestFocusInWindow();
    repaint();
  }

  @Override
  public Dimension getPreferredSize() {
    return new Dimension(sizes.widthHeight, sizes.widthHeight);
  }

  public void incCharSize() {
    setFontSize(sizes.fontSize + 1);
  }

  public void decCharSize() {
    setFontSize(sizes.fontSize - 1);
  }

  private void setFontSize(int pFontSize) {
    sizes = new VariableSizes(pFontSize, this);
    revalidate();
    repaint();
  }

  Dimension getTextSize(String str, Font pFont) {
    final FontMetrics fontMetrics = getFontMetrics(pFont);

    return new Dimension(
      fontMetrics.stringWidth(str),
      fontMetrics.getAscent() - (fontMetrics.getDescent() / 2)
    );
  }

  @Override
  protected void paintComponent(Graphics g) {
    final Graphics2D graphics = (Graphics2D) g;
    final Dimension size = getSize();
    final Map<Constraint, Set<Digit>> failingConstraints = game.getFailingConstraints();

    super.paintComponent(g);
    graphics.setColor(BACKGROUND_WHITE);
    graphics.fillRect(0, 0, size.width, size.height);
    failingConstraints.keySet().forEach(constraint -> {
      final Rectangle bounds = constraint.getBounds();
      final float x = sizes.rowColCenters.get(bounds.x);
      final float y = sizes.rowColCenters.get(bounds.y);
      final float w = sizes.rowColCenters.get(bounds.x + bounds.width) - x;
      final float h = sizes.rowColCenters.get(bounds.y + bounds.height) - y;

      g.setColor(CONSTRAINT_VIOLATED_RED);
      g.fillRect(
        round(x - ((sizes.cellSize / 2) * CONSTRAINT_EXTEND)),
        round(y - ((sizes.cellSize / 2) * CONSTRAINT_EXTEND)),
        round(w + (sizes.cellSize * CONSTRAINT_EXTEND)),
        round(h + (sizes.cellSize * CONSTRAINT_EXTEND))
      );
    });
    game.getCoordinates().forEach(coordinate -> drawCell(graphics, coordinate, failingConstraints));
  }

  private void drawCell(Graphics2D g, Coordinate coordinate, Map<Constraint, Set<Digit>> failingConstraints) {
    final Point2D center = new Point2D.Float(
      sizes.rowColCenters.get(coordinate.getX()),
      sizes.rowColCenters.get(coordinate.getY())
    );
    final float halfCellSize = sizes.cellSize / 2.0f;
    final boolean suggestMove = failingConstraints.isEmpty();
    final int intCellSize = round(sizes.cellSize);
    final Font font = sizes.boldFont;
    final Optional<Digit> optionalDigit = game.getDigit(coordinate);

    g.setColor(FRAME_BLACK);
    g.drawRect(
      (int)round(center.getX() - halfCellSize),
      (int)round(center.getY() - halfCellSize),
      intCellSize,
      intCellSize
    );

    selectedCell.filter(coordinate::equals).ifPresent(selectedCoordinate -> {
      final float thirdCellSize = sizes.cellSize / 3.0f;
      final int twoThirdCellSize = round(thirdCellSize * 2.0f);

      g.setColor(SELECTION_INDICATOR_GREEN);
      g.drawRect(
        (int)round(center.getX() - thirdCellSize),
        (int)round(center.getY() - thirdCellSize),
        twoThirdCellSize,
        twoThirdCellSize
      );
    });
    optionalDigit.map(digit -> String.valueOf(digit.getNumber())).ifPresentOrElse(str -> {
      final Dimension textSize = getTextSize(str, font);
      final boolean troubleMaker = failingConstraints.entrySet().stream().filter(
        entry -> entry.getKey().covers(coordinate)
      ).map(entry -> entry.getValue())
      .flatMap(digits -> digits.stream())
      .anyMatch(duplicateDigit -> duplicateDigit.equals(optionalDigit.get()));

      g.setColor(troubleMaker ? CHARACTER_DARK_RED : CHARACTER_BLACK);
      g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
      g.setFont(font);
      g.drawString(
        str,
        round(center.getX() - (textSize.width  / 2.0f) + 1),
        round(center.getY() - (textSize.height / 2.0f) + 1) + textSize.height
      );
    }, () -> {
      if (suggestMove && (solver.findPossibleDigits(coordinate).size() == 1)) {
        g.setColor(SELECTION_INDICATOR_GREEN);
        g.fillRect(
          (int)round(center.getX() - halfCellSize) + 1,
          (int)round(center.getY() - halfCellSize) + 1,
          intCellSize - 1,
          intCellSize - 1
        );
      }
    });
  }

}
