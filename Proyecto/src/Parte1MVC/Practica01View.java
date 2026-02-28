package Parte1MVC;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Practica01View extends JFrame {
    private JTabbedPane tabbedPane;

    public Practica01View() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        setTitle("Frame Practica01 MVC");
        
        tabbedPane = new JTabbedPane();
        tabbedPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        
        // Pestaña 1: Panel simple
        tabbedPane.add("Pestaña 1", new JPanel());
        
        // Pestaña 2: ScrollPane
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        tabbedPane.add("Pestaña 2", scrollPane);
        
        // Pestaña 3: DesktopPane
        JDesktopPane desktopPane = new JDesktopPane();
        tabbedPane.addTab("Pestaña 3", desktopPane);
        
        // Pestaña 4: InternalFrame
        JInternalFrame internalFrame = new JInternalFrame("Interno");
        internalFrame.setSize(150, 100);
        tabbedPane.addTab("Pestaña 4", internalFrame);
        internalFrame.setVisible(true);

        setContentPane(tabbedPane);
    }
}