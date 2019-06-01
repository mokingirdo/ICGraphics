package ru.nsu.fit.g16207.epanchintseva.Logic;

import javafx.util.Pair;
import ru.nsu.fit.g16207.epanchintseva.Algorythms.Border;
import ru.nsu.fit.g16207.epanchintseva.FieldView;
import ru.nsu.fit.g16207.epanchintseva.Algorythms.SpanFilling;

public class GameLogic {

    private Cell[] fieldCells;

    private int width;
    private int height;

    public GameLogic(int width, int height) {
        this.height = height;
        this.width = width;

        fieldCells = new Cell[height * width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                fieldCells[i * height + j] = new Cell();
            }
        }
    }

    public Cell[] getFieldCells() {
        return fieldCells;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getHeight() {
        return height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public boolean step(Border border, SpanFilling spanFilling, FieldView fieldView) {
        boolean isConfigurationChanged = false;

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width - i % 2; j++) {
                if (isChanged(fieldCells[i * height + j], fieldView)) {
                    int pixX = border.getCellsCenters().get(new Pair<>(j, i)).getKey();
                    int pixY = border.getCellsCenters().get(new Pair<>(j, i)).getValue();
                    if (fieldCells[i * height + j].isAlive()) {
                        spanFilling.fillHexagon(pixX, pixY, spanFilling.getAliveColor());
                    } else {
                        spanFilling.fillHexagon(pixX, pixY, spanFilling.getBackColor());
                    }
                    isConfigurationChanged = true;
                }
            }
        }
        fieldView.repaint();
        updateImpacts();
        return isConfigurationChanged;
    }

    public void updateImpacts() {
        //тороидальное поле
        int firstNeighbours = 0;
        int secondNeighbours = 0;

        for (int i = 0; i < height; i++) {//y
            for (int j = 0; j < width - i % 2; j++) {//x
                //first
                //3
                if (fieldCells[i * height + ((j + 1) % width)].isAlive()) {
                    firstNeighbours++;
                }
                //6
                if (fieldCells[i * height + ((j + width - 1) % width)].isAlive()) {
                    firstNeighbours++;
                }
                //2
                if (fieldCells[((i + height - 2) % height) * height + j].isAlive()) {
                    secondNeighbours++;
                }
                //5
                if (fieldCells[((i + 2) % height) * height + j].isAlive()) {
                    secondNeighbours++;
                }
                int temp = j;
                if (i % 2 == 0) {
                    j = (j - 1 + width) % width;
                }
                //1
                if (fieldCells[((i + height - 1) % height) * height + j].isAlive()) {
                    firstNeighbours++;
                }
                //2
                if (fieldCells[((i + height - 1) % height) * height + ((j + 1) % width)].isAlive()) {
                    firstNeighbours++;
                }
                //4
                if (fieldCells[((i + 1) % height) * height + ((j + 1) % width)].isAlive()) {
                    firstNeighbours++;
                }
                //5
                if (fieldCells[((i + 1) % height) * height + j].isAlive()) {
                    firstNeighbours++;
                }
                //second
                //1
                if (fieldCells[((i + height - 1) % height) * height + ((j + width - 1) % width)].isAlive()) {
                    secondNeighbours++;
                }
                //3
                if (fieldCells[((i + height - 1) % height) * height + ((j + 2) % width)].isAlive()) {
                    secondNeighbours++;
                }
                //4
                if (fieldCells[((i + 1) % height) * height + ((j + 2) % width)].isAlive()) {
                    secondNeighbours++;
                }
                //6
                if (fieldCells[((i + 1) % height) * height + ((j + width - 1) % width)].isAlive()) {
                    secondNeighbours++;
                }
                j = temp;
                fieldCells[i * height + j].setImpact(CellParameters.FST_IMPACT * firstNeighbours + CellParameters.SND_IMPACT * secondNeighbours);
                firstNeighbours = 0;
                secondNeighbours = 0;
            }
        }
    }

    private boolean isChanged(Cell cell, FieldView view) {
        if (cell.isAlive() && CellParameters.timeToDie(cell)) {
            view.decrementAlive();
            cell.setDeath();
            return true;
        }
        if (!cell.isAlive() && CellParameters.newBorn(cell)) {
            view.incrementAlive();
            cell.setAlive();
            return true;
        }
        return false;
    }

    public void clear() {
        for (Cell cell : fieldCells) {
            cell.setDeath();
            cell.setImpact(0);
        }
    }
}
