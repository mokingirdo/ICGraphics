package ru.nsu.fit.g16207.epanchintseva;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;

public class MapView extends JPanel {

    public static BufferedImage image = new BufferedImage(3000, 3000, BufferedImage.TYPE_4BYTE_ABGR);
    private Function f;
    private Main frame;

    private boolean interpolation = false;
    private boolean grid = false;
    private boolean isoline = false;
    private boolean dots = false;
    private final double eps = 0.00000000001;

    private double max;
    private double min;

    private double a;
    private double b;
    private double c;
    private double d;//могут изменяться пользователем через окно параметров

    private int u0;
    private int v0;
    private int u1;
    private int v1;

    //из файла
    private int k;
    private int m;
    private int n;
    private Color isolineColor;
    private ArrayList<Color> mapColors;
    private Double[] z;
    private ArrayList<Double> userZ = new ArrayList<>();

    public MapView(Main frame) {
        f = new Function();
        a = 0;
        b = 0;
        c = 5;
        d = 5;

        u0 = 0;
        v0 = 0;
        u1 = 600;
        v1 = 600;

        this.frame = frame;
        mapColors = new ArrayList<Color>();
        loadParameters(new File("loadFile.config"));
        z = new Double[n - 1];
        countZ();

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseEntered(e);
                double x = (c - a) * (e.getX() - u0) / (u1 - u0) + a;
                double y = (d - b) * (e.getY() - v0) / (v1 - v0) + b;
                frame.getF().setText("      f=" + (f.f_Waves(x,y) - f.f_Waves(x,y)%0.01));
                frame.getXf().setText("   x=" + (x-x%0.01));
                frame.getYf().setText("   y=" + (y - y%0.01));
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                double x = (c - a) * (e.getX() - u0) / (u1 - u0) + a;
                double y = (d - b) * (e.getY() - v0) / (v1 - v0) + b;
                if(userZ.size() != 0) {
                    userZ.remove(userZ.size() - 1);
                }
                userZ.add(f.f_Waves(x, y));
                repaint();
                image.getGraphics().drawString(String.valueOf("f:" + f.f_Waves(x,y) + " x:" + x + " y:" + y),e.getX(), e.getY());

            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                double x = (c - a) * (e.getX() - u0) / (u1 - u0) + a;
                double y = (d - b) * (e.getY() - v0) / (v1 - v0) + b;
                userZ.add(f.f_Waves(x, y));
                repaint();
                frame.getF().setText("      f=" + (f.f_Waves(x,y) - f.f_Waves(x,y)%0.01));
                frame.getXf().setText("   x=" + (x-x%0.01));
                frame.getYf().setText("   y=" + (y - y%0.01));
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        v1 = (frame.getHeight())*8/10;
        u1 = frame.getWidth()*8/10;
        countZ();
        super.paintComponent(g);
        updateImage();
        if (grid) {
            drawGrid();
        }
            drawIsolines();
            if (!userZ.isEmpty()) {
                for (double user : userZ) {
                    drawIsoline(user);
                }
            }
        drawLegend();
        g.drawImage(image, 10, 10, null);
    }

    void drawLegend(){
        int offset = (v1-v0)/n;
        int y = 0;
        int r ,g ,b ;
        Color color;
        while(y < v1*0.9){
            for(int x = (int)(u1 + u1*0.02); x < (int)(u1 + u1*0.1); x++){
                r = mapColors.get(y/offset).getRed();
                g = mapColors.get(y/offset).getGreen();
                b = mapColors.get(y/offset).getBlue();
                if(interpolation){
                    double k;
                    k = (double)y%(double)offset/(double)offset;
                    if (y/offset != 0) {
                        r = (int) (r * (k) + ((Color) mapColors.toArray()[y/offset - 1]).getRed() * (1 - k));
                        g = (int) (g * (k) + ((Color) mapColors.toArray()[y/offset - 1]).getGreen() * (1 - k));
                        b = (int) (b * (k) + ((Color) mapColors.toArray()[y/offset - 1]).getBlue() * (1 - k));
                    }
                }
                color = new Color(r, g, b);
                image.setRGB(x,y,color.getRGB());
            }
            y++;
        }
        while(y < v1){
            for(int x = (int)(u1 + u1*0.02); x < (int)(u1 + u1*0.1); x++){
                r = mapColors.get(mapColors.size()-1).getRed();
                g = mapColors.get(mapColors.size()-1).getGreen();
                b = mapColors.get(mapColors.size()-1).getBlue();
                if(interpolation){
                    double k;
                    k = (double)y%(double)offset/(double)offset;
                        r = (int) (r * (k) + ((Color) mapColors.toArray()[mapColors.size()-1 - 1]).getRed() * (1 - k));
                        g = (int) (g * (k) + ((Color) mapColors.toArray()[mapColors.size()-1 - 1]).getGreen() * (1 - k));
                        b = (int) (b * (k) + ((Color) mapColors.toArray()[mapColors.size()-1 - 1]).getBlue() * (1 - k));

                }
                color = new Color(r, g, b);
                image.setRGB(x,y,color.getRGB());
            }
            y++;
        }
        y = offset-5;
        for(int i = 0; i < n-1; i++){
            Graphics gr = image.getGraphics();
            gr.setColor(Color.BLACK);
            gr.drawString(String.valueOf(z[i] - (z[i]%0.01)),(int)(u1 + u1*0.03), y);
            y += offset;
        }
    }

    void drawIsolines() {
        for (int i = 0; i < n - 1; i++) {
            drawIsoline(z[i]);
        }
    }

    void drawIsoline(double zi) {
        int offsetU = (u1 - u0) / k;
        int offsetV = (v1 - v0) / m;
        for (int xShift = 0; xShift < k || offsetU*xShift < (u1); xShift++) {
            for (int yShift = 0; yShift < m || offsetV*yShift < (v1); yShift++) {
                int u = offsetU * xShift;
                int v = offsetV * yShift;
                double x = (c - a) * (u - u0) / (u1 - u0) + a;
                double y = (d - b) * (v - v0) / (v1 - v0) + b;
                u += offsetU;
                v += offsetV;
                double xNext = (c - a) * (u - u0) / (u1 - u0) + a;
                double yNext = (d - b) * (v - v0) / (v1 - v0) + b;
                //double dx = xNext - x;
                //double dy = yNext - y;
                double f1 = f.f_Waves(x, y);
                double f2 = f.f_Waves(xNext, y);
                double f3 = f.f_Waves(x, yNext);
                double f4 = f.f_Waves(xNext, yNext);
                if(f1 == zi){
                    f1 += eps;
                }
                if(f2 == zi){
                    f2 += eps;
                }
                if(f3 == zi){
                    f3 += eps;
                }
                if(f4 == zi){
                    f4 += eps;
                }
                int entryDots = 0;
                Double[] entrysX = new Double[4];
                Double[] entrysY = new Double[4];
                if (((f1 < zi) && (f2 > zi)) || ((f1 > zi) && (f2 < zi))) {
                    entrysX[entryDots] = x + (xNext - x) * (zi - f1) / (f2 - f1);
                    entrysY[entryDots] = y;
                    entryDots++;
                }
                if (((f1 < zi) && (f3 > zi)) || ((f1 > zi) && (f3 < zi))) {
                    entrysY[entryDots] = y + (yNext - y) * (zi - f1) / (f3 - f1);
                    entrysX[entryDots] = x;
                    entryDots++;
                }
                if (((f3 < zi) && (f4 > zi)) || ((f3 > zi) && (f4 < zi))) {
                    entrysX[entryDots] = x + (xNext - x) * (zi - f3) / (f4 - f3);
                    entrysY[entryDots] = yNext;
                    entryDots++;
                }
                if (((f2 < zi) && (f4 > zi)) || ((f2 > zi) && (f4 < zi))) {
                    entrysY[entryDots] = y + (yNext - y) * (zi - f2) / (f4 - f2);
                    entrysX[entryDots] = xNext;
                    entryDots++;
                }
                if (entryDots >= 2) {
                    int uf0 = (int) ((u1 - u0) * (entrysX[0] - a) / (c - a) + u0 + 0.5);
                    int uf1 = (int) ((u1 - u0) * (entrysX[1] - a) / (c - a) + u0 + 0.5);
                    int vf0 = (int) ((v1 - v0) * (entrysY[0] - b) / (d - b) + v0 + 0.5);
                    int vf1 = (int) ((v1 - v0) * (entrysY[1] - b) / (d - b) + v0 + 0.5);
                    Graphics g = image.createGraphics();
                    g.setColor(isolineColor);
                    if(entryDots == 2 && isoline) {
                        g.drawLine(uf0, vf0, uf1, vf1);
                    }
                if (entryDots == 3) {
                    drawIsoline(zi + eps);
                }
                if (entryDots == 4) {
                    int uf2 = (int) ((u1 - u0) * (entrysX[2] - a) / (c - a) + u0 + 0.5);
                    int uf3 = (int) ((u1 - u0) * (entrysX[3] - a) / (c - a) + u0 + 0.5);
                    int vf2 = (int) ((v1 - v0) * (entrysY[2] - b) / (d - b) + v0 + 0.5);
                    int vf3 = (int) ((v1 - v0) * (entrysY[3] - b) / (d - b) + v0 + 0.5);
                    double middle = (f1+f2+f3+f4)/4;
                    if ((((f1 < zi) && (middle > zi)) || ((f1 > zi) && (middle < zi))) && isoline) {
                        g.drawLine(uf0, vf0, uf1, vf1);
                        g.drawLine(uf2, vf2, uf3, vf3);
                    }
                    if ((((f2 < zi) && (middle > zi)) || ((f2 > zi) && (middle < zi))) && isoline){
                        g.drawLine(uf0, vf0, uf3, vf3);
                        g.drawLine(uf1, vf1, uf2, vf2);
                    }
                    drawDots(g, uf2, uf3, vf2, vf3);
                }
                    drawDots(g, uf0, uf1, vf0, vf1);
                }
            }
        }
    }

    private void drawDots(Graphics g, int uf2, int uf3, int vf2, int vf3) {
        if(dots){
            g.drawOval(uf2-2,vf2-2,5,5);
            g.drawOval(uf3-2,vf3-2,5,5);
            g.setColor(Color.RED);
            g.fillOval(uf2-2,vf2-2,5,5);
            g.fillOval(uf3-2,vf3-2,5,5);
            g.setColor(isolineColor);
        }
    }

    void drawGrid() {
        int offsetU = (u1-u0) / k;
        int offsetV = (v1-v0) / m;
        for (int x = 1; x < k || offsetU*x < u1; x++) {
            image.getGraphics().drawLine(offsetU * x%u1, 0, offsetU * x%u1, v1 - v0);
        }
        for (int y = 1; y < m|| offsetV*y < v1; y++) {
            image.getGraphics().drawLine(0, offsetV * y%v1, u1 - u0, offsetV * y%v1);
        }
    }

    void updateImage() {
        image = new BufferedImage(3000, 3000, BufferedImage.TYPE_4BYTE_ABGR);
        double offsetX = (c - a) / (u1 - u0);
        double offsetY = (d - b) / (v1 - v0);
        for (double x = a; x < c; x += offsetX) {
            for (double y = b; y < d; y += offsetY) {
                int i = 0;
                double temp = f.f_Waves(x, y);
                int u = (int) ((u1 - u0) * (x - a) / (c - a) + u0 + 0.5) ;
                int v = (int) ((v1 - v0) * (y - b) / (d - b) + v0 + 0.5) ;
                while ((i < n - 1) && (temp > z[i])) {
                    i++;
                }
                Color color = (Color) mapColors.toArray()[i];
                if (interpolation) {
                    double k;
                    int r = color.getRed();
                    int g = color.getGreen();
                    int b = color.getBlue();
                    double left;
                    double right;
                    if (i == 0) {
                        left = min;
                        right = z[i];
                    } else if (i == n - 1) {
                        left = z[i - 1];
                        right = max;
                    } else {
                        left = z[i - 1];
                        right = z[i];
                    }
                    k = (temp - left) / (right - left);
                    if (i != 0) {
                        r = (int) (r * (k) + ((Color) mapColors.toArray()[i - 1]).getRed() * (1 - k));
                        g = (int) (g * (k) + ((Color) mapColors.toArray()[i - 1]).getGreen() * (1 - k));
                        b = (int) (b * (k) + ((Color) mapColors.toArray()[i - 1]).getBlue() * (1 - k));
                    }
                    color = new Color(r, g, b);
                }
                image.setRGB(u , v , color.getRGB());

            }
        }
    }

    void countZ() {
        max = Double.MIN_VALUE;
        min = Double.MAX_VALUE;
        double offsetX = (c - a) / (u1 - u0);
        double offsetY = (d - a) / (v1 - v0);
        for (double x = a; x < c; x += offsetX) {
            for (double y = b; y < d; y += offsetY) {
                double temp = f.f_Waves(x, y);
                if (temp > max) {
                    max = temp;
                }
                if (temp < min) {
                    min = temp;
                }
            }
        }
        double offset1 = (Math.signum(max) * max - Math.signum(min) * min) / (n);
        double temp1 = min + offset1;
        for (int i = 0; i < n - 1; i++) {
            z[i] = temp1;
            temp1 += offset1;
        }
    }

    void loadParameters(File file) {

        if(file == null){
            return;
        }
        ArrayList<Color> tempMapColors = new ArrayList<>();
        BufferedReader br = null;
        String current = null;
        int tempm, tempk, tempn;
        try {
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis);
            br = new BufferedReader(isr);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            current = br.readLine();
            String[] pair = current.split(" ");
            if (pair.length != 2) {
                return;
            }
            for(int i = 0; i < pair[0].length(); i++){
                if(!Character.isDigit(pair[0].toCharArray()[i])){
                    System.out.println("Error in input file");
                    return;
                }
            }
            tempk = Integer.parseInt(pair[0]);
            for(int i = 0; i < pair[1].length(); i++){
                if(!Character.isDigit(pair[1].toCharArray()[i])){
                    System.out.println("Error in input file");
                    return;
                }
            }
            tempm = Integer.parseInt(pair[1]);

            current = br.readLine();
            for(int i = 0; i < current.length(); i++){
                if(!Character.isDigit(current.toCharArray()[i])){
                    System.out.println("Error in input file");
                    return;
                }
            }
            tempn = Integer.parseInt(current);
            String[] three;
            while (br.ready()) {

                current = br.readLine();
                for(int i = 0; i < current.length(); i++){
                    if(!Character.isDigit(current.toCharArray()[i]) && !Character.isSpaceChar(current.toCharArray()[i])){
                        System.out.println("Error in input file");
                        return;
                    }
                }
                three = current.split(" ");
                if (three.length != 3) {
                    return;
                }
                if (br.ready()) {
                    tempMapColors.add(new Color(Integer.parseInt(three[0]), Integer.parseInt(three[1]), Integer.parseInt(three[2])));
                } else {
                    isolineColor = new Color(Integer.parseInt(three[0]), Integer.parseInt(three[1]), Integer.parseInt(three[2]));
                }
            }
            if (tempn != tempMapColors.size()) {
                System.out.println("Wrong number of colors");
                return;
            }
            mapColors.clear();
            n = tempn;
            m = tempm;
            k = tempk;
            mapColors = tempMapColors;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setA(double a) {
        this.a = a;
    }

    public void setB(double b) {
        this.b = b;
    }

    public void setC(double c) {
        this.c = c;
    }

    public void setD(double d) {
        this.d = d;
    }

    public void setK(int k) {
        this.k = k;
    }

    public void setM(int m) {
        this.m = m;
    }

    public double getA() {
        return a;
    }

    public double getB() {
        return b;
    }

    public double getC() {
        return c;
    }

    public int getK() {
        return k;
    }

    public double getD() {
        return d;
    }

    public int getM() {
        return m;
    }

    public void setInterpolation(boolean interpolation) {
        this.interpolation = interpolation;
    }

    public boolean isInterpolation() {
        return interpolation;
    }

    public void setGrid(boolean network) {
        this.grid = network;
    }

    public boolean isGrid() {
        return grid;
    }

    public boolean isIsoline() {
        return isoline;
    }

    public void setIsoline(boolean isoline) {
        this.isoline = isoline;
    }

    public boolean isDots() {
        return dots;
    }

    public void setDots(boolean dots) {
        this.dots = dots;
    }

    public ArrayList<Double> getUserZ() {
        return userZ;
    }
}
