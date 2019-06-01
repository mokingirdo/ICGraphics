package ru.nsu.fit.g16207.epanchintseva;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class Main extends MainFrame {

    private FieldView view;
    private Options options;
    private boolean play = false;

    private JToggleButton runButton;
    private JButton xorButton;
    private JButton clearButton;
    private JButton stepButton;

    final ScrollPane scroller;

    private ImageIcon xor = new ImageIcon("resources/xor.png");
    private ImageIcon replace = new ImageIcon("resources/replace.png");

    Main() {
        //взять и проверить параметры из config
        super();
        setSize(600, 600);
        try {
            addSubMenu("File", KeyEvent.VK_F);
            addMenuItem("File/Exit", "Exit application", KeyEvent.VK_X, "exit.png", "onExit");
            addMenuItem("File/Open...", "Load configuration", KeyEvent.VK_X, "load.png", "onLoad");
            addMenuItem("File/Save", "Save configuration", KeyEvent.VK_X, "save.png", "onSave");
            addSubMenu("Edit", KeyEvent.VK_L);
            addSubMenu("Properties", KeyEvent.VK_L);
            addMenuItem("Properties/Settings", "Game options", KeyEvent.VK_A, "options.png", "onOptions");
            addSubMenu("Edit/Mode", KeyEvent.VK_L);
            addMenuItem("Edit/Clear", "Clear", KeyEvent.VK_A, "erase.png", "onClear");
            addMenuItem("Edit/Step", "Step", KeyEvent.VK_A, "step.png", "onStep");
            addMenuItem("Edit/Run", "Run", KeyEvent.VK_A, "run.png", "onRun");
            addMenuItem("Edit/Mode/XorReplace", "Xor/Replace", KeyEvent.VK_A, "xor.png", "onXor");
            addMenuItem("Edit/Impacts", "Show cells impacts", KeyEvent.VK_A, "impact.png", "onImpact");
            addSubMenu("Help", KeyEvent.VK_H);
            addMenuItem("Help/About...", "Shows program version and copyright information", KeyEvent.VK_A, "vopros.png", "onAbout");

            addToolBarButton("File/Exit");
            addToolBarButton("File/Open...");
            addToolBarButton("File/Save");
            addToolBarSeparator();

            xorButton = createToolBarButton("Edit/Mode/XorReplace");
            clearButton = createToolBarButton("Edit/Clear");
            stepButton = createToolBarButton("Edit/Step");
            runButton = createToolBarToggleButton("Edit/Run");
            addToolBarButton(clearButton);
            addToolBarButton(stepButton);
            addToolBarToggleButton(runButton);
            addToolBarButton(xorButton);
            addToolBarButton("Properties/Settings");
            addToolBarToggleButton("Edit/Impacts");
            addToolBarButton("Help/About...");

            view = new FieldView();
            options = new Options(view, this);
            view.setPreferredSize(new Dimension((int)(view.length*view.getWidthField()*Math.sqrt(3)),(int)(view.length*view.getHeightField()*Math.sqrt(3))));
            add(view);
            scroller = new ScrollPane(ScrollPane.SCROLLBARS_AS_NEEDED);
            scroller.add(view);
            scroller.setSize(view.getHeightField(),view.getWidthField());
            add(scroller);

            setVisible(true);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        setVisible(true);
    }


    public static void main(String[] args) {
        new Main();
    }

    public void onLoad(){
        view.parse(FileUtils.getOpenFileName(this, "config", "Configuration file"));
        options.updateData( view);
        view.setPreferredSize(new Dimension((int)(view.length*view.getWidthField()*Math.sqrt(3)),(int)(view.length*view.getHeightField()*Math.sqrt(3))));
        scroller.add(view);
    }

    public void changeViewSize(){
        view.setPreferredSize(new Dimension((int)(view.length*view.getWidthField()*Math.sqrt(3)),(int)(view.length*view.getHeightField()*Math.sqrt(3))));
        scroller.add(view);
    }

    public void onSave(){
        File file = FileUtils.getSaveFileName(this, "config", "Configuration file");
        try(FileWriter writer = new FileWriter(file)){
            writer.write(String.format("%d %d\n",view.getWidthField() , view.getHeightField()));
            writer.write(String.format("%d\n",view.getThickness()));
            writer.write(String.format("%d\n",view.getLength()));
            writer.write(String.format("%d\n",view.getNumAlive()));
            for (int i = 0; i <view.getHeightField(); i++) {
                for (int j = 0; j < view.getWidthField() - i % 2; j++) {
                    if(view.getLogic().getFieldCells()[i*view.getHeightField() + j].isAlive()){
                        writer.write(String.format("%d %d\n",j , i ));
                    }
                }
            }
            writer.flush();
        } catch (IOException e){
            e.printStackTrace();
        }

    }

    public void onExit() {
        System.exit(0);
    }

    public void onClear() {
        view.drawEmptyField();
        repaint();
    }

    public void onOptions(){
        options.setVisible(true);
    }

    public void onStep() {
        view.step();
        repaint();
    }

    public void onXor(){
        view.setXorMode();
        if(view.isXorMode()){
            xorButton.setIcon(replace);
        } else{
            xorButton.setIcon(xor);
        }
    }

    public void onRun() {
        if (play) {
            play = false;
            view.setClickable(true);
            clearButton.setEnabled(true);
            stepButton.setEnabled(true);
            return;
        }
        play = true;
        clearButton.setEnabled(false);
        stepButton.setEnabled(false);
        view.setClickable(false);
        new Thread(() -> {
            while (play && view.step()) {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException ignored) { }
            }
            play = false;
            clearButton.setEnabled(true);
            stepButton.setEnabled(true);
            view.setClickable(true);
            runButton.setSelected(false);
        }).start();

    }

    public void onImpact() {
        view.setImpact();
        view.repaint();
        repaint();
    }

    public void onAbout() {
        JOptionPane.showMessageDialog(this, "Init, version 1.0\nCopyright © 2019 Julia Epanchintseva, FIT, group 16207", "About Init", JOptionPane.INFORMATION_MESSAGE);
    }


}