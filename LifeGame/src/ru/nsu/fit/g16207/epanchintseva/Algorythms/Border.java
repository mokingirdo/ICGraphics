package ru.nsu.fit.g16207.epanchintseva.Algorythms;

import javafx.util.Pair;
import ru.nsu.fit.g16207.epanchintseva.FieldView;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Border {

    private int thickness;

    private int borderColor;
    private int backColor;

    private Map<Pair<Integer, Integer>, Pair<Integer, Integer>> cellsCenters;

    public Border(Color borderColor, Color backColor, int thickness) {
        //центры клеток поля ключ - в поле, значение - в пикселях
        cellsCenters = new HashMap<>();
        this.borderColor = borderColor.getRGB();
        this.backColor = backColor.getRGB();
        this.thickness = thickness;
    }

    public int getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(int borderColor) {
        this.borderColor = borderColor;
    }

    public int getBackColor() {
        return backColor;
    }

    public void setBackColor(int backColor) {
        this.backColor = backColor;
    }

    public void setThickness(int thickness) {
        this.thickness = thickness;
    }

    public Map<Pair<Integer, Integer>, Pair<Integer, Integer>> getCellsCenters() {
        return cellsCenters;
    }

    private void drawBrezenhemLine(int xStart, int yStart, int xEnd, int yEnd) {
        int dx = (xEnd - xStart >= 0 ? 1 : -1);
        int dy = (yEnd - yStart >= 0 ? 1 : -1);

        int lengthX = Math.abs(xEnd - xStart);
        int lengthY = Math.abs(yEnd - yStart);

        int length = Math.max(lengthX, lengthY);

        if (length == 0) {
            FieldView.image.setRGB(xStart, yStart, borderColor);
        }

        // Начальные значения
        int x = xStart;
        int y = yStart;
        int d;

        if (lengthY <= lengthX) {

            d = -lengthX;
        } else {
            d = -lengthY;
        }
        // Основной цикл
        length++;

        while ((length--) != 0) {
            FieldView.image.setRGB(x, y, borderColor);

            if (thickness > 1) {
                drawFatBorder(x, y, lengthY, lengthX);
            }
            if (lengthY <= lengthX) {
                x += dx;
                d += 2 * lengthY;
                if (d > 0) {
                    d -= 2 * lengthX;
                    y += dy;
                }
            } else {
                y += dy;
                d += 2 * lengthX;
                if (d > 0) {
                    d -= 2 * lengthY;
                    x += dx;
                }
            }
        }
    }

    private void drawFatBorder(int x, int y, int lengthY, int lengthX) {

        if (lengthY <= lengthX) {
            for (int i = 1; i < thickness / 2; i++) {
                FieldView.image.setRGB(x, y - i, borderColor);
                FieldView.image.setRGB(x, y + i, borderColor);
                //закрашивание треугольников
                for (int j = 0; j < i; j++) {
                    FieldView.image.setRGB(x - i + j, y - j, borderColor);
                    FieldView.image.setRGB(x + i - j, y - j, borderColor);
                }
            }
        } else {
            for (int i = 1; i < thickness / 2; i++) {
                FieldView.image.setRGB(x + i, y, borderColor);
                FieldView.image.setRGB(x - i, y, borderColor);
                //закрашивание треугольников
                for (int j = 0; j < i; j++) {
                    FieldView.image.setRGB(x + j, y + i - j, borderColor);
                    FieldView.image.setRGB(x - i + j, y + j, borderColor);
                }
            }
        }
    }

    private void drawHexagon(int xStart, int yStart, int length) {
        //0-1
        drawBrezenhemLine(xStart, (int) (yStart + 0.5 * length), (int) (xStart + length * Math.sqrt(3) / 2), yStart);
        //0-5
        drawBrezenhemLine(xStart, (int) (yStart + 0.5 * length), xStart, (int) (yStart + 1.5 * length));
        //2-1
        drawBrezenhemLine((int) (xStart + length * Math.sqrt(3)), (int) (yStart + 0.5 * length), (int) (xStart + length * Math.sqrt(3) / 2), yStart);
        //2-3
        drawBrezenhemLine((int) (xStart + length * Math.sqrt(3)), (int) (yStart + 0.5 * length), (int) (xStart + length * Math.sqrt(3)), (int) (yStart + 1.5 * length));
        //4-5
        drawBrezenhemLine((int) (xStart + length * Math.sqrt(3) / 2), yStart + 2 * length, xStart, (int) (yStart + 1.5 * length));
        //4-3
        drawBrezenhemLine((int) (xStart + length * Math.sqrt(3) / 2), yStart + 2 * length, (int) (xStart + length * Math.sqrt(3)), (int) (yStart + 1.5 * length));

    }

    public void drawField(int high, int wight, int length) {

        cellsCenters.clear();

        int x;
        int y = thickness / 2;
        int xField, yField = 0;

        for (int i = 0; i < high; i++) {
            x = thickness / 2;
            xField = 0;
            for (int j = 0; j < wight; j++) {
                drawHexagon(x, y, length);
                cellsCenters.put(new Pair<>(xField, yField), new Pair<>((int) (x + length * Math.sqrt(3) / 2), y + length));
                x += length * Math.sqrt(3);
                xField++;
            }
            i++;
            if (i == high) {
                break;
            }
            y += 1.5 * length;
            x = (int) (length * Math.sqrt(3) / 2) + thickness / 2;
            yField++;
            xField = 0;
            for (int k = 0; k < (wight - 1); k++) {
                //0-5
                drawBrezenhemLine(x, (int) (y + 0.5 * length), x, (int) (y + 1.5 * length));
                //2-3
                drawBrezenhemLine((int) (x + length * Math.sqrt(3)), (int) (y + 0.5 * length), (int) (x + length * Math.sqrt(3)), (int) (y + 1.5 * length));

                cellsCenters.put(new Pair<>(xField, yField), new Pair<>((int) (x + length * Math.sqrt(3) / 2), y + length));
                x += length * Math.sqrt(3);
                xField++;
            }
            y += 1.5 * length;
            yField++;
        }
        if (high % 2 == 0) {
            x = (int) (length * Math.sqrt(3) / 2) + thickness / 2;
            y -= 1.5 * length;
            for (int k = 0; k < (wight - 1); k++) {
                //4-5
                drawBrezenhemLine((int) (x + length * Math.sqrt(3) / 2), y + 2 * length, x, (int) (y + 1.5 * length));
                //4-3
                drawBrezenhemLine((int) (x + length * Math.sqrt(3) / 2), y + 2 * length, (int) (x + length * Math.sqrt(3)), (int) (y + 1.5 * length));

                x += length * Math.sqrt(3);
            }

        }
    }

    public void fillField(SpanFilling filling) {
        for (Pair<Integer, Integer> cell : cellsCenters.values()) {
            filling.fillHexagon(cell.getKey(), cell.getValue(), Color.CYAN);
        }
    }

}
