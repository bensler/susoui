package com.bensler.susoui.widget;

import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.bensler.suso.Digit;

class KeyboardActions extends KeyAdapter {

  private final GamePanel parent;
  private final Map<Integer, Point> moveSelectionKeys;
  private final Map<Character, Digit> digits;

  KeyboardActions(GamePanel pParent) {
    parent = pParent;
    moveSelectionKeys = Map.of(
      KeyEvent.VK_LEFT,  new Point(-1,  0),
      KeyEvent.VK_RIGHT, new Point( 1,  0),
      KeyEvent.VK_UP,    new Point( 0, -1),
      KeyEvent.VK_DOWN,  new Point( 0,  1)
    );
    digits = Arrays.stream(Digit.values()).collect(Collectors.toMap(
      Digit::getNumberChar,
      Function.identity()
    ));
  }

  @Override
  public void keyPressed(KeyEvent evt) {
    Optional.ofNullable(moveSelectionKeys.get(evt.getKeyCode())).ifPresent(
      moveSelectionDelta -> parent.moveSelection(moveSelectionDelta)
    );
    Optional.ofNullable(digits.get(evt.getKeyChar())).ifPresent(
      digit -> parent.setSelectedDigit(Optional.of(digit))
    );
    if ((evt.getKeyCode() == KeyEvent.VK_BACK_SPACE) || (evt.getKeyCode() == KeyEvent.VK_DELETE)) {
      parent.setSelectedDigit(Optional.empty());
    }
  }

  @Override
  public void keyTyped(KeyEvent evt) {
    digits.get(evt.getKeyChar());
    System.out.println("p " + evt.getKeyChar() + " # " + (int)evt.getKeyChar() + " # " + evt.getKeyCode());
  }

}