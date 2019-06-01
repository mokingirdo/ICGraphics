package ru.nsu.fit.g16207.epanchintseva;

import ru.nsu.fit.g16207.epanchintseva.Logic.CellParameters;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Options extends JFrame {
    private FieldView view;
    private Main frame;

    private JRadioButton xorButton;
    private JRadioButton replaceButton;

    private int widthField;
    private int heightField;

    private int cellThickness;
    private int cellLength;

    private JPanel bigPanel;
    private JPanel leftPanel;
    private JPanel topPanel;
    private JPanel lowPanel;
    private JPanel modePanel;
    private JPanel parametersPanel;
    private JPanel fieldPanel;
    private JPanel thicknessPanel;
    private JPanel lengthPanel;

    private JTextField widthText;
    private JTextField heightText;
    private JTextField thicknessField;
    private JTextField lengthField;

    private JSlider lengthSlider;
    private JSlider thicknessSlider;

    private double LIVE_BEGIN = CellParameters.LIVE_BEGIN;
    private double LIVE_END = CellParameters.LIVE_END;
    private double BIRTH_BEGIN = CellParameters.BIRTH_BEGIN;
    private double BIRTH_END = CellParameters.BIRTH_END;
    private double FST_IMPACT = CellParameters.FST_IMPACT;
    private double SND_IMPACT = CellParameters.SND_IMPACT;

    //к CellParameters обращение напрямую

    public Options(FieldView view, Main frame) {
        init(view.getWidthField(), view.getHeightField(), view.getThickness(), view.getLength());
        this.view = view;
        this.frame = frame;
        createPanels();

        setVisible(false);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();
        setBounds((dimension.width) / 5, (dimension.height) / 5, 600, 600);


        JPanel okPanel = new JPanel();
        JButton okButton = new JButton("OK");
        okPanel.add(okButton);
        lowPanel.add(okPanel);
        okButton.addActionListener(e -> onOk());

    }

    public void updateData(FieldView view){
        this.view = view;
        init(view.getWidthField(), view.getHeightField(), view.getThickness(), view.getLength());
        //view.updateImage();
        widthText.setText(String.valueOf(widthField));
        heightText.setText(String.valueOf(heightField));
        thicknessField.setText(String.valueOf(cellThickness));
        lengthField.setText(String.valueOf(cellLength));

        lengthSlider.setValue(cellLength);
        thicknessSlider.setValue(cellThickness);
    }

    private void init(int widthField, int heightField, int cellThickness, int cellLength) {
        this.cellLength = cellLength;
        this.cellThickness = cellThickness;
        this.heightField = heightField;
        this.widthField = widthField;
    }

    private void createPanels() {
        bigPanel = new JPanel();
        bigPanel.setLayout(new GridLayout(1, 2));
        add(bigPanel);

        leftPanel = new JPanel();
        leftPanel.setLayout(new GridLayout(2, 1));

        topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(1, 2));

        lowPanel = new JPanel();
        lowPanel.setLayout(new GridLayout(3, 1));
        Border lowPanelBorder = BorderFactory.createTitledBorder("Cell properties");
        lowPanel.setBorder(lowPanelBorder);

        parametersPanel = new JPanel();
        parametersPanel.setLayout(new GridLayout(6, 1));
        Border parametersPanelBorder = BorderFactory.createTitledBorder("Cell parameters");
        parametersPanel.setBorder(parametersPanelBorder);

        fieldPanel = new JPanel();
        fieldPanel.setLayout(new GridLayout(2, 1));
        Border fieldPanelBorder = BorderFactory.createTitledBorder("Field Size");
        fieldPanel.setBorder(fieldPanelBorder);

        bigPanel.add(leftPanel);
        bigPanel.add(parametersPanel);
        leftPanel.add(topPanel);
        leftPanel.add(lowPanel);

        modePanel = new JPanel();
        addModePanel();
        topPanel.add(modePanel);
        topPanel.add(fieldPanel);

        thicknessPanel = new JPanel();
        addThicknessPanel();
        lowPanel.add(thicknessPanel);

        lengthPanel = new JPanel();
        addLengthPanel();
        lowPanel.add(lengthPanel);

        addFieldSizes();
        addCellParameters();
    }

    private void addModePanel() {
        xorButton = new JRadioButton("Xor");
        replaceButton = new JRadioButton("Replace");
        modePanel.add(xorButton);
        modePanel.add(replaceButton);
        Border modePanelBorder = BorderFactory.createTitledBorder("Mode");
        modePanel.setBorder(modePanelBorder);
        ButtonGroup group = new ButtonGroup();
        group.add(xorButton);
        group.add(replaceButton);
        replaceButton.setSelected(true);
    }

    private void addThicknessPanel() {
        Border thicknessPanelBorder = BorderFactory.createTitledBorder("Thickness");
        thicknessPanel.setBorder(thicknessPanelBorder);
        thicknessSlider = new JSlider(1, 40, cellThickness);
        thicknessField = new JTextField(String.valueOf(cellThickness), 5);
        thicknessField.setEnabled(false);
        thicknessPanel.add(thicknessSlider);
        thicknessPanel.add(thicknessField);
        thicknessSlider.setPaintTicks(true);

        thicknessSlider.addChangeListener(e -> {
            thicknessField.setText(String.valueOf(thicknessSlider.getValue()));
            cellThickness = thicknessSlider.getValue();
        });
    }

    private void addLengthPanel() {
        Border lengthPanelBorder = BorderFactory.createTitledBorder("Length");
        lengthPanel.setBorder(lengthPanelBorder);
        lengthSlider = new JSlider(4, 120, cellLength);
        lengthField = new JTextField(String.valueOf(cellLength), 5);
        lengthField.setEnabled(false);
        lengthPanel.add(lengthSlider);
        lengthPanel.add(lengthField);
        lengthSlider.setPaintTicks(true);

        lengthSlider.addChangeListener(e -> {
            lengthField.setText(String.valueOf(lengthSlider.getValue()));
            cellLength = lengthSlider.getValue();
        });
    }

    private JTextField addFieldWithText(JPanel parrentPanel, int valueParam, String string){
        return getTextField(parrentPanel, string, String.valueOf(valueParam), valueParam);
    }

    private JTextField addFieldWithTextDouble(JPanel parrentPanel, double valueParam, String string){
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

    private void addFieldSizes(){

        widthText = addFieldWithText(fieldPanel, widthField, "width");
        widthText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                int value = Integer.parseInt(widthText.getText());
                if(value < 1) value = 1; else if(value > 100) value = 100;
                widthField = value;
            }
        });

        heightText = addFieldWithText(fieldPanel, heightField, "height");
        heightText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                int value = Integer.parseInt(heightText.getText());
                if(value < 1) value = 1; else if(value > 100) value = 100;
                heightField = value;
            }
        });
    }

    public void addCellParameters(){
        JTextField lbText = addFieldWithTextDouble(parametersPanel, LIVE_BEGIN, "LIVE_BEGIN");
        lbText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                double value = Double.parseDouble(lbText.getText());
                LIVE_BEGIN = value;
            }
        });


        JTextField leText = addFieldWithTextDouble(parametersPanel, LIVE_END, "LIVE_END");
        leText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                double value = Double.parseDouble(leText.getText());
                LIVE_END = value;
            }
        });


        JTextField bbText = addFieldWithTextDouble(parametersPanel, BIRTH_BEGIN, "BIRTH_BEGIN");
        bbText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                double value = Double.parseDouble(bbText.getText());
                BIRTH_BEGIN = value;
            }
        });

        JTextField beText = addFieldWithTextDouble(parametersPanel, BIRTH_END, "BIRTH_END");
        beText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                double value = Double.parseDouble(beText.getText());
                BIRTH_END = value;
            }
        });

        JTextField fstText = addFieldWithTextDouble(parametersPanel, FST_IMPACT, "FST_IMPACT");
        fstText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                double value = Double.parseDouble(fstText.getText());
                FST_IMPACT = value;
            }
        });

        JTextField sndText = addFieldWithTextDouble(parametersPanel, SND_IMPACT, "SND_IMPACT");
        sndText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                double value = Double.parseDouble(sndText.getText());
                SND_IMPACT = value;
            }
        });
    }

    private void setParameters(){
        CellParameters.FST_IMPACT = FST_IMPACT;
        CellParameters.LIVE_END = LIVE_END;
        CellParameters.LIVE_BEGIN = LIVE_BEGIN;
        CellParameters.SND_IMPACT = SND_IMPACT;
        CellParameters.BIRTH_END = BIRTH_END;
        CellParameters.BIRTH_BEGIN = BIRTH_BEGIN;
    }

    private void onOk() {
        setParameters();
        if((replaceButton.isSelected() && view.isXorMode()) || (xorButton.isSelected() && !view.isXorMode())){
            frame.onXor();
        }
        if((cellThickness < cellLength) && (cellLength*widthField < 2300) && (cellLength*heightField < 2300)){
            view.setThickness(cellThickness);
            view.setLength(cellLength);
            view.setHeightField(heightField);
            view.setWidthField(widthField);
            view.getLogic().setWidth(widthField);
            view.getLogic().setHeight(heightField);
            view.updateField();
            frame.changeViewSize();
            setVisible(false);
        } else if(cellThickness >= cellLength){
            JFrame error = new JFrame();
            error.setSize(300,100);
            error.setLocation(500,500);
            JTextField field = new JTextField("Error: thickness more than length");
            field.setDisabledTextColor(Color.red);
            field.setEnabled(false);
            error.add(field);
            error.setVisible(true);
            error.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        }else{

            JFrame error = new JFrame();
            error.setSize(300,100);
            error.setLocation(500,500);
            JTextField field = new JTextField("Error: field is too big");
            field.setDisabledTextColor(Color.red);
            field.setEnabled(false);
            error.add(field);
            error.setVisible(true);
            error.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        }
    }
}
