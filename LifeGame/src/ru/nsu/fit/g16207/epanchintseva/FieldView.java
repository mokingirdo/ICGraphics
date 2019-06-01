package ru.nsu.fit.g16207.epanchintseva;

import javafx.util.Pair;
import ru.nsu.fit.g16207.epanchintseva.Logic.Cell;
import ru.nsu.fit.g16207.epanchintseva.Logic.GameLogic;
import ru.nsu.fit.g16207.epanchintseva.Algorythms.Border;
import ru.nsu.fit.g16207.epanchintseva.Algorythms.SpanFilling;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashSet;

public class FieldView extends JPanel {
    private SpanFilling filling;
    private Border border;
    private GameLogic logic;

    private boolean showImpact = false;
    private boolean xorMode = false;
    private boolean isClickable = true;

    //параметры из config
    int widthField = 100;
    int heightField = 100;
    int thickness = 1;
    int length = 20;
    int numAlive = 0;

    HashSet<Cell> markedCells = new HashSet<>();

    public static BufferedImage image;// = new BufferedImage(3000, 3000, BufferedImage.TYPE_4BYTE_ABGR);

    public FieldView() {
        updateImage();
        clearImage();
        this.setSize(image.getWidth(), image.getHeight());
        filling = new SpanFilling(Color.BLACK, Color.CYAN, Color.BLUE);
        border = new Border(Color.BLACK, Color.CYAN, 2);
        logic = new GameLogic(widthField, heightField);
        border.drawField(heightField, widthField, length);
        border.fillField(filling);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                markedCells.clear();
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (isClickable) {
                    Cell cell = findCell(e.getX(), e.getY());
                    if (xorMode && (image.getRGB(e.getX(), e.getY()) == filling.getAliveColor().getRGB())) {
                        filling.clickedHexagon(e.getX(), e.getY(), filling.getBackColor());
                        cell.setDeath();
                    } else {
                        filling.clickedHexagon(e.getX(), e.getY(), filling.getAliveColor());
                        cell.setAlive();
                    }
                    logic.updateImpacts();
                    repaint();
                }
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (isClickable && filling.checkVertices(e.getX(), e.getY())) {
                    Cell cell = findCell(e.getX(), e.getY());
                    if (!markedCells.contains(cell)) {
                        markedCells.add(cell);
                        if (xorMode && (image.getRGB(e.getX(), e.getY()) == filling.getAliveColor().getRGB())) {
                            filling.clickedHexagon(e.getX(), e.getY(), filling.getBackColor());
                            cell.setDeath();
                        } else {
                            filling.clickedHexagon(e.getX(), e.getY(), filling.getAliveColor());
                            cell.setAlive();
                        }
                        logic.updateImpacts();
                        repaint();
                    }
                }
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    public void updateImage() {
        image = new BufferedImage(4000, 4000, BufferedImage.TYPE_4BYTE_ABGR);
    }

    public int getHeightField() {
        return heightField;
    }

    public int getWidthField() {
        return widthField;
    }

    public int getThickness() {
        return thickness;
    }

    public int getLength() {
        return length;
    }

    public GameLogic getLogic() {
        return logic;
    }

    public void setThickness(int thickness) {
        this.thickness = thickness;
    }

    public void setHeightField(int heightField) {
        this.heightField = heightField;
    }

    public void setWidthField(int widthField) {
        this.widthField = widthField;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public boolean step() {
        return logic.step(border, filling, this);
    }

    public void setXorMode() {
        xorMode = !xorMode;
    }

    public boolean isXorMode() {
        return xorMode;
    }

    public void setClickable(boolean isClickable) {
        this.isClickable = isClickable;
    }

    public void incrementAlive() {
        numAlive++;
    }

    public void decrementAlive() {
        numAlive++;
    }

    public int getNumAlive() {
        numAlive = 0;
        for (int i = 0; i <getHeightField(); i++) {
            for (int j = 0; j < getWidthField() - i % 2; j++) {
                if(getLogic().getFieldCells()[i*getHeightField() + j].isAlive()){
                    numAlive++;
                }
            }
        }
        return numAlive;
    }

    public void parse(File file) {

        if (file == null) {
            return;
        }
        drawEmptyField();
        String current;
        try {
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            //field size
            current = br.readLine();
            String[] pair = current.split(" ");
            if (pair.length != 2) {
                return;
            }
            widthField = Integer.parseInt(pair[0]);
            heightField = Integer.parseInt(pair[1]);

            //updateImage();
            //drawEmptyField();

            logic.setHeight(heightField);
            logic.setWidth(widthField);

            //length & thickness
            current = br.readLine();
            thickness = Integer.parseInt(current);
            current = br.readLine();
            length = Integer.parseInt(current);


            current = br.readLine();
            numAlive = Integer.parseInt(current);
            updateField();
            while(br.ready()) {
                current = br.readLine();
                pair = current.split(" ");
                int pixX = border.getCellsCenters().get(new Pair<>(Integer.parseInt(pair[0]), Integer.parseInt(pair[1]))).getKey();
                int pixY = border.getCellsCenters().get(new Pair<>(Integer.parseInt(pair[0]), Integer.parseInt(pair[1]))).getValue();
                filling.clickedHexagon(pixX, pixY, filling.getAliveColor());
                logic.getFieldCells()[Integer.parseInt(pair[1]) * logic.getHeight() + Integer.parseInt(pair[0])].setAlive();
                logic.updateImpacts();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Cell findCell(int xClicked, int yClicked) {
        if (!filling.checkVertices(xClicked, yClicked)) {
            return null;
        }
        double criteria = Double.MAX_VALUE;
        int xTarget = 0, yTarget = 0;

        for (int i = 0; i < heightField; i++) {
            for (int j = 0; j < widthField - i % 2; j++) {
                int pixX = border.getCellsCenters().get(new Pair<>(j, i)).getKey();
                int pixY = border.getCellsCenters().get(new Pair<>(j, i)).getValue();
                double length = Math.sqrt((pixX - xClicked) * (pixX - xClicked) + (pixY - yClicked) * (pixY - yClicked));
                if (length < criteria) {
                    criteria = length;
                    xTarget = j;
                    yTarget = i;
                }
            }
        }
        return logic.getFieldCells()[yTarget * logic.getHeight() + xTarget];
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, null);
        if (showImpact && (length >= 20)) {
            for (int i = 0; i < heightField; i++) {
                for (int j = 0; j < widthField - i % 2; j++) {
                    g.setFont(new Font(null, Font.BOLD,  40 / 3));
                    double d = Math.abs(logic.getFieldCells()[i * heightField + j].getImpact());
                    String number = String.format("%.1f", d);
                    int pixX = border.getCellsCenters().get(new Pair<>(j, i)).getKey();
                    int pixY = border.getCellsCenters().get(new Pair<>(j, i)).getValue();
                    g.drawString(number, pixX - length / 3, pixY + length / 3);

                }
            }
        }
    }

    public static void clearImage() {
        image.getGraphics().clearRect(0, 0, image.getWidth(),
                image.getHeight());
        image.getGraphics().fillRect(0, 0, image.getWidth(),
                image.getHeight());
        image.getGraphics().dispose();
    }

    public void updateField() {
        //updateImage();
        clearImage();
        border.setThickness(thickness);
        border.drawField(heightField, widthField, length);
        border.fillField(filling);
        for (int i = 0; i < heightField; i++) {
            for (int j = 0; j < widthField - i % 2; j++) {
                if (logic.getFieldCells()[i * logic.getHeight() + j].isAlive()) {
                    int pixX = border.getCellsCenters().get(new Pair<>(j, i)).getKey();
                    int pixY = border.getCellsCenters().get(new Pair<>(j, i)).getValue();
                    filling.clickedHexagon(pixX, pixY, filling.getAliveColor());
                }
            }
        }
        repaint();
    }

    public void drawEmptyField() {
        clearImage();
        //updateImage();
        border.drawField(heightField, widthField, length);
        border.fillField(filling);
        logic.clear();
        repaint();
    }

    public void setImpact() {
        showImpact = !showImpact;
    }
}

