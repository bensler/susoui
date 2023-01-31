package com.bensler.susoui.widget;

import static java.lang.Math.round;

import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import java.util.function.BiFunction;
import java.util.stream.IntStream;

class VariableSizes {

  final int fontSize;
  final Font plainFont;
  final Font boldFont;
  final int charSize;
  final float cellSize;
  final float halfCellSize;
  final List<Float> rowColCenters;
  final int widthHeight;

  VariableSizes(int pFontSize, GamePanel parent) {
    final BiFunction<List<Float>, Float, Float> addToLastElement = (
      (list, toAdd) -> list.get(list.size() - 1) + toAdd
    );
    final Dimension text8Size;

    fontSize = pFontSize;
    plainFont = parent.baseFont.deriveFont((float)fontSize);
    boldFont = plainFont.deriveFont(Font.BOLD);
    text8Size = parent.getTextSize("8", boldFont);
    charSize = Math.max(text8Size.width, text8Size.height);
    cellSize = charSize * 2.5f;
    halfCellSize = cellSize / 2.0f;

    final float gap = charSize;
    final float gapBlock = charSize * 2.0f;
    final List<Float> block = new ArrayList<>();

    block.add(halfCellSize);
    block.add(block.get(0) + gap + cellSize);
    block.add(block.get(1) + gap + cellSize);

    final List<Float> result = new ArrayList<>();
    final float start0 = gap;
    block.stream().forEach(value -> result.add(start0 + value));
    final float start1 = addToLastElement.apply(result, halfCellSize + gapBlock);
    block.stream().forEach(value -> result.add(start1 + value));
    final float start2 = addToLastElement.apply(result, halfCellSize + gapBlock);
    block.stream().forEach(value -> result.add(start2 + value));
    widthHeight = round(addToLastElement.apply(result, gap + halfCellSize));
    rowColCenters = List.copyOf(result);
  }

  OptionalInt getRowCol(double position) {
    return IntStream.range(0, rowColCenters.size()).filter(index -> {
      final float cellCenter = rowColCenters.get(index);

      return (position > (cellCenter - halfCellSize)) && (position < (cellCenter + halfCellSize));
    }).findFirst();
  }

}