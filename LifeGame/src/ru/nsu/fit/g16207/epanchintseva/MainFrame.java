package ru.nsu.fit.g16207.epanchintseva;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.lang.reflect.Method;
import java.security.InvalidParameterException;


public class MainFrame extends JFrame{

    private static final long serialVersionUID = 1L;
    private JMenuBar menuBar;
    private JToolBar toolBar;

    MainFrame(){

        try
        {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        }
        catch(Exception ignored)
        {
        }
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        toolBar = new JToolBar("Main toolbar");
        toolBar.setRollover(true);

        add(toolBar, BorderLayout.PAGE_START);
    }

    private JMenuItem createMenuItem(String title, String tooltip, int mnemonic, String icon, String actionMethod) throws SecurityException, NoSuchMethodException
    {
        JMenuItem item = new JMenuItem(title);
        item.setMnemonic(mnemonic);
        item.setToolTipText(tooltip);
        if(icon != null){
            item.setIcon(new ImageIcon("resources/" +icon, title));
        }
        final Method method = getClass().getMethod(actionMethod);
        item.addActionListener(evt -> {
            try {
                method.invoke(MainFrame.this);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return item;
    }

    public JMenuItem createMenuItem(String title, String tooltip, int mnemonic, String actionMethod) throws SecurityException, NoSuchMethodException
    {
        return createMenuItem(title, tooltip, mnemonic, null, actionMethod);
    }

    private JMenu createSubMenu(String title, int mnemonic)
    {
        JMenu menu = new JMenu(title);
        menu.setMnemonic(mnemonic);
        return menu;
    }

    void addSubMenu(String title, int mnemonic)
    {
        MenuElement element = getParentMenuElement(title);
        if(element == null)
            throw new InvalidParameterException("Menu path not found: "+title);
        JMenu subMenu = createSubMenu(getMenuPathName(title), mnemonic);
        if(element instanceof JMenuBar)
            ((JMenuBar)element).add(subMenu);
        else checkElement(title, element, subMenu);
    }

    void addMenuItem(String title, String tooltip, int mnemonic, String icon, String actionMethod) throws SecurityException, NoSuchMethodException
    {
        MenuElement element = getParentMenuElement(title);
        if(element == null)
            throw new InvalidParameterException("Menu path not found: "+title);
        JMenuItem item = createMenuItem(getMenuPathName(title), tooltip, mnemonic, icon, actionMethod);
        checkElement(title, element, item);
    }

    private void checkElement(String title, MenuElement element, JMenuItem item) {
        if(element instanceof JMenu)
            ((JMenu)element).add(item);
        else if(element instanceof JPopupMenu)
            ((JPopupMenu)element).add(item);
        else
            throw new InvalidParameterException("Invalid menu path: "+title);
    }


    public void addMenuItem(String title, String tooltip, int mnemonic, String actionMethod) throws SecurityException, NoSuchMethodException
    {
        addMenuItem(title, tooltip, mnemonic, null, actionMethod);
    }


    public void addMenuSeparator(String title)
    {
        MenuElement element = getMenuElement(title);
        if(element == null)
            throw new InvalidParameterException("Menu path not found: "+title);
        if(element instanceof JMenu)
            ((JMenu)element).addSeparator();
        else if(element instanceof JPopupMenu)
            ((JPopupMenu)element).addSeparator();
        else
            throw new InvalidParameterException("Invalid menu path: "+title);
    }

    private String getMenuPathName(String menuPath)
    {
        int pos = menuPath.lastIndexOf('/');
        if(pos > 0)
            return menuPath.substring(pos+1);
        else
            return menuPath;
    }


    private MenuElement getParentMenuElement(String menuPath)
    {
        int pos = menuPath.lastIndexOf('/');
        if(pos > 0)
            return getMenuElement(menuPath.substring(0, pos));
        else
            return menuBar;
    }


    private MenuElement getMenuElement(String menuPath)
    {
        MenuElement element = menuBar;
        for(String pathElement: menuPath.split("/"))
        {
            MenuElement newElement = null;
            for(MenuElement subElement: element.getSubElements())
            {
                if((subElement instanceof JMenu && ((JMenu)subElement).getText().equals(pathElement))
                        || (subElement instanceof JMenuItem && ((JMenuItem)subElement).getText().equals(pathElement)))
                {
                    if(subElement.getSubElements().length==1 && subElement.getSubElements()[0] instanceof JPopupMenu)
                        newElement = subElement.getSubElements()[0];
                    else
                        newElement = subElement;
                    break;
                }
            }
            if(newElement == null) return null;
            element = newElement;
        }
        return element;
    }

    protected JButton createToolBarButton(String menuPath)
    {
        JMenuItem item = (JMenuItem)getMenuElement(menuPath);
        if(item == null)
            throw new InvalidParameterException("Menu path not found: "+menuPath);
        JButton button = new JButton(item.getIcon());
        for(ActionListener listener: item.getActionListeners())
            button.addActionListener(listener);
        button.setToolTipText(item.getToolTipText());
        return button;
    }

    protected JToggleButton createToolBarToggleButton(String menuPath)
    {
        JMenuItem item = (JMenuItem)getMenuElement(menuPath);
        if(item == null)
            throw new InvalidParameterException("Menu path not found: "+menuPath);
        JToggleButton button = new JToggleButton(item.getIcon());
        for(ActionListener listener: item.getActionListeners())
            button.addActionListener(listener);
        button.setToolTipText(item.getToolTipText());
        return button;
    }


    void addToolBarButton(String menuPath)
    {
        toolBar.add(createToolBarButton(menuPath));
    }

    void addToolBarToggleButton(JToggleButton button)
    {
        toolBar.add(button);
    }

    void addToolBarButton(JButton button)
    {
        toolBar.add(button);
    }

    void addToolBarToggleButton(String menuPath)
    {
        toolBar.add(createToolBarToggleButton(menuPath));
    }


    void addToolBarSeparator()
    {
        toolBar.addSeparator();
    }

    public File getSaveFileName(String extension, String description)
    {
        return FileUtils.getSaveFileName(this, extension, description);
    }

    public File getOpenFileName(String extension, String description)
    {
        return FileUtils.getOpenFileName(this, extension, description);
    }



}