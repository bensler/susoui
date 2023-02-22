package com.bensler.susoui.widget;

import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.bensler.suso.Digit;

class KeyboardActions extends KeyAdapter {

  private final GamePanel parent;
  private final Map<Integer, Point> moveSelectionKeys;
  private final Set<Integer> clearCellKeys;
  private final Map<Character, Digit> digits;

  KeyboardActions(GamePanel pParent) {
    parent = pParent;
    moveSelectionKeys = Map.of(
      KeyEvent.VK_LEFT,  new Point(-1,  0),
      KeyEvent.VK_RIGHT, new Point( 1,  0),
      KeyEvent.VK_UP,    new Point( 0, -1),
      KeyEvent.VK_DOWN,  new Point( 0,  1)
    );
    clearCellKeys = Set.of(
      KeyEvent.VK_BACK_SPACE,
      KeyEvent.VK_DELETE,
      KeyEvent.VK_SPACE
    );
    digits = Arrays.stream(Digit.values()).collect(Collectors.toMap(
      Digit::getNumberChar,
      Function.identity()
    ));
  }

  @Override
  public void keyPressed(KeyEvent evt) {
    final int keyCode = evt.getKeyCode();

    Optional.ofNullable(moveSelectionKeys.get(keyCode)).ifPresent(
      parent::moveSelection
    );
    Optional.ofNullable(digits.get(evt.getKeyChar())).ifPresent(
      digit -> parent.setSelectedDigit(Optional.of(digit))
    );
    if (clearCellKeys.contains(keyCode)) {
      parent.setSelectedDigit(Optional.empty());
    }
  }

}