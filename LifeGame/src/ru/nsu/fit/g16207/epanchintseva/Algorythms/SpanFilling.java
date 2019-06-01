package ru.nsu.fit.g16207.epanchintseva.Algorythms;

import javafx.util.Pair;
import ru.nsu.fit.g16207.epanchintseva.FieldView;

import java.awt.*;
import java.util.Stack;

public class SpanFilling {

    private Stack<Pair<Pair<Integer, Integer>, Integer>> spans;
    Color borderColor;
    Color backColor;
    Color aliveColor;

    public SpanFilling(Color borderColor, Color backColor, Color aliveColor) {
        spans = new Stack<>();
        this.borderColor = borderColor;
        this.backColor = backColor;
        this.aliveColor = aliveColor;
    }

    public Color getAliveColor() {
        return aliveColor;
    }

    public Color getBackColor() {
        return backColor;
    }

    public void clickedHexagon(int xClicked, int yClicked, Color newColor) {
        if (!checkVertices(xClicked, yClicked)) {
            return;
        }
        fillHexagon(xClicked, yClicked, newColor);
    }


    public void fillHexagon(int xClicked, int yClicked, Color newColor) {

        Pair<Pair<Integer, Integer>, Integer> current;
        int yTop = yClicked + 1;
        int yLow = yClicked > 0 ? yClicked - 1 : yClicked;
        boolean top = findSpan(xClicked, yTop);
        boolean low = findSpan(xClicked, yLow);
        findSpan(xClicked, yClicked);
        do {
            current = spans.pop();
            int pixX = current.getKey().getKey();
            int pixY = current.getKey().getValue();
            int lastX = current.getValue();

            while (top) {
                top = findSpan(xClicked, yTop);
                yTop++;
            }
            while (low) {
                low = findSpan(xClicked, yLow);
                yLow--;
            }
            for (; pixX <= lastX; pixX++) {
                FieldView.image.setRGB(pixX, pixY, newColor.getRGB());
                if (FieldView.image.getRGB(pixX, pixY + 1) == borderColor.getRGB() && pixX != lastX) {
                    findSpan(pixX + 1, pixY + 1);
                }
                if (FieldView.image.getRGB(pixX, pixY - 1) == borderColor.getRGB() && pixX != lastX) {
                    findSpan(pixX + 1, pixY - 1);
                }
            }
        } while (!spans.empty());
    }

    //проверка на принадлежность полю
    public boolean checkVertices(int x, int y) {
        //граничная точка
        if (FieldView.image.getRGB(x, y) == borderColor.getRGB()) {
            return false;
        }
        //точка вне поля
        if ((FieldView.image.getRGB(x, y) != backColor.getRGB()) && (FieldView.image.getRGB(x, y) != aliveColor.getRGB())) {
            return false;
        }
        return true;
    }

    //находит начало и конец спана и кладет в стек
    private boolean findSpan(int x, int y) {
        int xStart = x;
        int xEnd = x;
        if (FieldView.image.getRGB(x, y) == borderColor.getRGB()) {
            return false;
        }
        while (FieldView.image.getRGB(xStart - 1, y) != borderColor.getRGB())
            xStart--;
        while (FieldView.image.getRGB(xEnd + 1, y) != borderColor.getRGB())
            xEnd++;

        spans.push(new Pair<>(new Pair<>(xStart, y), xEnd));

        return (xEnd != xStart);
    }
}
