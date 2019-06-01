package ru.nsu.fit.g16207.epanchintseva;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class Main extends MainFrame{

    private MapView view;
    private Options options;

    private JToggleButton interpolation;
    private JToggleButton grid;
    private JToggleButton isoline;
    private JToggleButton dots;

    public Main(){
     super();
     this.setSize(700, 700);

        addSubMenu("File", KeyEvent.VK_F);
        try {
            addMenuItem("File/Exit", "Exit application", KeyEvent.VK_X, "exit.png", "onExit");
            addMenuItem("File/Open...", "Load picture", KeyEvent.VK_X, "load.png", "onLoad");

            addSubMenu("Properties", KeyEvent.VK_L);
            addMenuItem("Properties/Dots", "Draw dots", KeyEvent.VK_A, "dots.png", "onDots");
            addMenuItem("Properties/Clear", "Clear user isolines", KeyEvent.VK_A, "clear.png", "onClear");
            addMenuItem("Properties/Isolines", "Show isolines", KeyEvent.VK_A, "lines.png", "onIsoline");
            addMenuItem("Properties/Grid", "Field grid", KeyEvent.VK_A, "grid.png", "onGrid");
            addMenuItem("Properties/Interpolation", "Color interpolation", KeyEvent.VK_A, "interpolation.png", "onInterpolation");
            addMenuItem("Properties/Settings", "Game options", KeyEvent.VK_A, "options.png", "onOptions");


            addSubMenu("Help", KeyEvent.VK_H);
            addMenuItem("Help/About...", "Shows program version and copyright information", KeyEvent.VK_A, "about.png", "onAbout");
        } catch (NoSuchMethodException e){
            e.printStackTrace();
        }

        addToolBarButton("File/Exit");
        addToolBarButton("File/Open...");
        addToolBarButton("Properties/Settings");
        addToolBarButton("Properties/Clear");
        interpolation = createToolBarToggleButton("Properties/Interpolation");
        addToolBarToggleButton(interpolation);
        grid = createToolBarToggleButton("Properties/Grid");
        addToolBarToggleButton(grid);
        isoline = createToolBarToggleButton("Properties/Isolines");
        addToolBarToggleButton(isoline);
        dots = createToolBarToggleButton("Properties/Dots");
        addToolBarToggleButton(dots);
        addToolBarSeparator();
        addToolBarButton("Help/About...");
        setLabels();

        view = new MapView(this);
        options = new Options(view, this);
        view.setPreferredSize(new Dimension(300,300));
        add(view);
        ScrollPane scroller = new ScrollPane(ScrollPane.SCROLLBARS_AS_NEEDED);
        scroller.add(view);
        scroller.setSize(500,500);
        add(scroller);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        new Main();
    }

    public void onLoad(){

        view.loadParameters(FileUtils.getOpenFileName(this, "config", "Configuration file"));
        options.update();
        view.repaint();
    }

    public void onInterpolation(){
        view.setInterpolation(!view.isInterpolation());
        options.update();
        view.repaint();
    }

    public void onDots(){
        view.setDots(!view.isDots());
        options.update();
        view.repaint();
    }

    public void onGrid(){
        view.setGrid(!view.isGrid());
        options.update();
        view.repaint();
    }

    public void onIsoline(){
        view.setIsoline(!view.isIsoline());
        options.update();
        view.repaint();
    }

    public void onClear(){
        view.getUserZ().clear();
        view.repaint();
    }

    public void onExit() {
        System.exit(0);
    }


    public void onOptions(){
        options.setVisible(true);
    }

    public void onAbout() {
        JOptionPane.showMessageDialog(this, "Init, version 1.0\nCopyright © 2019 Julia Epanchintseva, FIT, group 16207", "About Init", JOptionPane.INFORMATION_MESSAGE);
    }

    public JToggleButton getInterpolation(){
        return interpolation;
    }

    public JToggleButton getGrid(){
        return grid;
    }

    public JToggleButton getIsoline(){
        return isoline;
    }

    public JToggleButton getDots(){
        return dots;
    }
}
