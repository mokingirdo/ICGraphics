package ru.nsu.fit.g16207.epanchintseva;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Options extends JFrame {

    private MapView view;
    private Main frame;

    private JRadioButton interpolationButton;
    private JRadioButton isolinesButton;
    private JRadioButton gridButton;
    private JRadioButton dotsButton;

    private double aField;
    private double bField;
    private double cField;
    private double dField;
    private int mField;
    private int kField;

    private JTextField aText;
    private JTextField bText;
    private JTextField cText;
    private JTextField dText;
    private JTextField mText;
    private JTextField kText;

    private JPanel bigPanel;
    private JPanel leftPanel;
    private JPanel topPanel;
    private JPanel lowPanel;
    private JPanel buttonsPanel;

    public Options(MapView view, Main frame) {
        init(view.getA(), view.getB(), view.getC(), view.getD(), view.getM(), view.getK());
        this.view = view;
        this.frame = frame;
        createPanels();

        setVisible(false);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();
        setBounds((dimension.width) / 5, (dimension.height) / 5, 450, 400);


        JPanel okPanel = new JPanel();
        JButton okButton = new JButton("OK");
        okPanel.add(okButton);
        lowPanel.add(okPanel);
        okButton.addActionListener(e -> onOk());

    }

    public void update() {
        init(view.getA(), view.getB(), view.getC(), view.getD(), view.getM(), view.getK());
        aText.setText(String.valueOf(aField));
        bText.setText(String.valueOf(bField));
        cText.setText(String.valueOf(cField));
        dText.setText(String.valueOf(dField));
        mText.setText(String.valueOf(mField));
        kText.setText(String.valueOf(kField));
        if (view.isInterpolation()) {
            interpolationButton.setSelected(true);
        } else {
            interpolationButton.setSelected(false);
        }
        if (view.isGrid()) {
            gridButton.setSelected(true);
        } else {
            gridButton.setSelected(false);
        }
        if (view.isIsoline()) {
            isolinesButton.setSelected(true);
        } else {
            isolinesButton.setSelected(false);
        }
        if (view.isDots()) {
            dotsButton.setSelected(true);
        } else {
            dotsButton.setSelected(false);
        }
    }

    private void init(double a, double b, double c, double d, int m, int k) {
        this.aField = a;
        this.bField = b;
        this.cField = c;
        this.dField = d;
        this.mField = m;
        this.kField = k;
    }

    private void createPanels() {
        bigPanel = new JPanel();
        bigPanel.setLayout(new GridLayout(1, 2));
        add(bigPanel);

        leftPanel = new JPanel();
        leftPanel.setLayout(new GridLayout(2, 1));

        topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(2, 2));
        Border topPanelBorder = BorderFactory.createTitledBorder("Field size");
        topPanel.setBorder(topPanelBorder);

        lowPanel = new JPanel();
        lowPanel.setLayout(new GridLayout(3, 1));
        Border lowPanelBorder = BorderFactory.createTitledBorder("grid size");
        lowPanel.setBorder(lowPanelBorder);

        buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(6, 1));

        bigPanel.add(leftPanel);
        bigPanel.add(buttonsPanel);
        leftPanel.add(topPanel);
        leftPanel.add(lowPanel);

        addFieldSizes();
        addButtons();
    }

    private void addButtons() {
        interpolationButton = new JRadioButton("Interpollation");
        gridButton = new JRadioButton("grid");
        dotsButton = new JRadioButton("Dots");
        isolinesButton = new JRadioButton("Isolines");
        buttonsPanel.add(isolinesButton);
        buttonsPanel.add(interpolationButton);
        buttonsPanel.add(gridButton);
        buttonsPanel.add(dotsButton);
    }

    private JTextField addFieldWithText(JPanel parrentPanel, int valueParam, String string) {
        return getTextField(parrentPanel, string, String.valueOf(valueParam), valueParam);
    }

    private JTextField addFieldWithTextDouble(JPanel parrentPanel, double valueParam, String string) {
        return getTextField(parrentPanel, string, String.valueOf(valueParam), valueParam);
    }

    private JTextField getTextField(JPanel parrentPanel, String string, String s, double valueParam) {
        JPanel panel = new JPanel();
        Border panelBorder = BorderFactory.createTitledBorder(string);
        panel.setBorder(panelBorder);
        parrentPanel.add(panel);
        JTextField textField = new JTextField(s, 4);
        panel.add(textField);

        return textField;
    }

    private void addFieldSizes() {
        try {
            aText = addFieldWithText(topPanel, (int) aField, "a");
            aText.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    if(!aText.getText().isEmpty()) {
                        for(int i = 0; i < aText.getText().length(); i++){
                            if(!Character.isDigit(aText.getText().toCharArray()[i])){
                                System.out.println("Wrong parameter");
                                return;
                            }
                        }
                        aField = Double.parseDouble(aText.getText());
                    }
                }
            });

            bText = addFieldWithText(topPanel, (int) bField, "b");
            bText.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    if(!bText.getText().isEmpty()) {
                        for(int i = 0; i < bText.getText().length(); i++){
                            if(!Character.isDigit(bText.getText().toCharArray()[i])){
                                System.out.println("Wrong parameter");
                                return;
                            }
                        }
                        bField = Double.parseDouble(bText.getText());
                    }
                }
            });

            cText = addFieldWithText(topPanel, (int) cField, "c");
            cText.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    if(!cText.getText().isEmpty()) {
                        for(int i = 0; i < cText.getText().length(); i++){
                            if(!Character.isDigit(cText.getText().toCharArray()[i])){
                                System.out.println("Wrong parameter");
                                return;
                            }
                        }
                        cField = Double.parseDouble(cText.getText());
                    }
                }
            });

            dText = addFieldWithText(topPanel, (int) dField, "d");
            dText.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    if(!dText.getText().isEmpty()) {
                        for(int i = 0; i < dText.getText().length(); i++){
                        if(!Character.isDigit(dText.getText().toCharArray()[i])){
                            System.out.println("Wrong parameter");
                            return;
                        }
                    }
                        dField = Double.parseDouble(dText.getText());
                    }
                }
            });

            mText = addFieldWithText(lowPanel, mField, "m");
            mText.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    if(!mText.getText().isEmpty()) {
                        for(int i = 0; i < mText.getText().length(); i++){
                            if(!Character.isDigit(mText.getText().toCharArray()[i])){
                                System.out.println("Wrong parameter");
                                return;
                            }
                        }
                        mField = Integer.parseInt(mText.getText());
                    }
                }
            });

            kText = addFieldWithText(lowPanel, kField, "k");
            kText.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    if(!kText.getText().isEmpty()) {
                        for(int i = 0; i < kText.getText().length(); i++){
                            if(!Character.isDigit(kText.getText().toCharArray()[i])){
                                System.out.println("Wrong parameter");
                                return;
                            }
                        }
                        kField = Integer.parseInt(kText.getText());
                    }
                }
            });
        } catch (NumberFormatException ignored) {
        }
    }

    private void onOk() {
        //проставить иконки в соответствии с кнопками

        if ((aField >= cField) || (bField >= dField) || (kField == 0) || (mField == 0)) {

            JFrame error = new JFrame();
            error.setSize(300, 100);
            error.setLocation(500, 500);
            JTextField field = new JTextField("Error: wrong field parameters");
            field.setDisabledTextColor(Color.red);
            field.setEnabled(false);
            error.add(field);
            error.setVisible(true);
            error.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        } else {
            view.setA(aField);
            view.setB(bField);
            view.setC(cField);
            view.setD(dField);
            view.setK(kField);
            view.setM(mField);
            view.setInterpolation(interpolationButton.isSelected());
            view.setGrid(gridButton.isSelected());
            view.setIsoline(isolinesButton.isSelected());
            view.setDots(dotsButton.isSelected());
            frame.getInterpolation().setSelected(interpolationButton.isSelected());
            frame.getGrid().setSelected(gridButton.isSelected());
            frame.getIsoline().setSelected(isolinesButton.isSelected());
            frame.getDots().setSelected(dotsButton.isSelected());
            update();
            view.repaint();
            setVisible(false);
        }
    }
}
