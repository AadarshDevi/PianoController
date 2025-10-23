package com.alphagen.studio.pianocontroller.ui.swing;

import com.alphagen.studio.pianocontroller.ui.managers.ControllerManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class KeyVisualizer extends JFrame {


    private JLabel pkn;
    private JLabel pkna;
    private JLabel kkn;

    public KeyVisualizer() {

//        jFrame = new JFrame("Piano Visualizer");
        setTitle("Piano Visualizer");
        setSize(new Dimension(334, 106));
        pkn = new JLabel("PK#");
        pkna = new JLabel("KN");
        kkn = new JLabel("KK#");

        Dimension labelSize = new Dimension(100, 60);

        pkn.setPreferredSize(labelSize);
        pkna.setPreferredSize(labelSize);
        kkn.setPreferredSize(labelSize);

        pkn.setMinimumSize(labelSize);
        pkna.setMinimumSize(labelSize);
        kkn.setMinimumSize(labelSize);

        pkn.setMaximumSize(labelSize);
        pkna.setMaximumSize(labelSize);
        kkn.setMaximumSize(labelSize);

        pkn.setOpaque(true);
        pkn.setBackground(Color.decode("#1e1f22"));
        pkn.setForeground(Color.WHITE);
        pkn.setHorizontalAlignment(JLabel.CENTER);
        pkn.setVerticalAlignment(JLabel.CENTER);


        pkna.setOpaque(true);
        pkna.setBackground(Color.decode("#1e1f22"));
        pkna.setForeground(Color.WHITE);
        pkna.setHorizontalAlignment(JLabel.CENTER);
        pkna.setVerticalAlignment(JLabel.CENTER);


        kkn.setOpaque(true);
        kkn.setBackground(Color.decode("#1e1f22"));
        kkn.setForeground(Color.WHITE);
        kkn.setVerticalAlignment(JLabel.CENTER);
        kkn.setHorizontalAlignment(JLabel.CENTER);

        JPanel titlebar = new JPanel();
        BoxLayout titleLayout = new BoxLayout(titlebar, BoxLayout.X_AXIS);
        titlebar.setLayout(titleLayout);
        JLabel title = new JLabel("Piano Visualizer");
        title.setPreferredSize(new Dimension(290, 20));
//        URL url = MidiDeviceReceiver.class.getResource("/src/main/resources/com/alphagen/studio/pianocontroller/images/app_logo_2.png");
//        ImageIcon logoIcon = new ImageIcon(url);
//        JLabel logo = new JLabel(logoIcon);

        Dimension logoDim = new Dimension(18, 18);

        JButton closeButton = new JButton("x");
        Dimension titleButtonDim = new Dimension(40, 20);
        closeButton.setPreferredSize(titleButtonDim);
        closeButton.setMaximumSize(titleButtonDim);
        closeButton.setMinimumSize(titleButtonDim);
        closeButton.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                ControllerManager.getMainFrameController().stopApplication();
                dispose();
            }
        });
        closeButton.setFont(closeButton.getFont().deriveFont(10f));
        closeButton.setFocusPainted(false);
        closeButton.setBackground(Color.decode("#2c2c3c"));
        closeButton.setForeground(Color.WHITE);
        closeButton.setOpaque(true);
        closeButton.setContentAreaFilled(true);
        closeButton.setBorderPainted(false);
        closeButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                closeButton.setBackground(Color.decode("#414157"));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                closeButton.setBackground(Color.decode("#2c2c3c"));
            }
        });

        title.setForeground(Color.WHITE);

        titlebar.add(Box.createRigidArea(new Dimension(3, 0)));
//        title.add(logo);
        titlebar.add(Box.createRigidArea(new Dimension(3, 0)));
        titlebar.add(title);
        titlebar.add(Box.createRigidArea(new Dimension(3, 0)));
        titlebar.add(closeButton);
        titlebar.setBackground(Color.decode("#2c2c3c"));

        JPanel centerPanel = new JPanel();
        centerPanel.setPreferredSize(new Dimension(330, 104));
        centerPanel.setBackground(Color.decode("#1e1f22"));
        BoxLayout boxLayout = new BoxLayout(centerPanel, BoxLayout.X_AXIS);
        centerPanel.setLayout(boxLayout);

        centerPanel.add(Box.createRigidArea(new Dimension(4, 0)));
        centerPanel.add(pkn);
        centerPanel.add(Box.createRigidArea(new Dimension(4, 0)));
        centerPanel.add(pkna);
        centerPanel.add(Box.createRigidArea(new Dimension(4, 0)));
        centerPanel.add(kkn);

        BorderLayout borderLayout = new BorderLayout();
        getContentPane().setLayout(borderLayout);
        setUndecorated(true);
        getContentPane().add(centerPanel, BorderLayout.CENTER);
        getContentPane().add(titlebar, BorderLayout.NORTH);

        // Variables to hold initial click coordinates
        final Point clickPoint = new Point();

        // Mouse pressed: record start point
        titlebar.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                clickPoint.x = e.getX();
                clickPoint.y = e.getY();
            }
        });

        titlebar.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                // Calculate new location for JFrame
                Point currPoint = e.getLocationOnScreen();
                setLocation(currPoint.x - clickPoint.x, currPoint.y - clickPoint.y);
            }
        });


        setVisible(true);
        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) {
                ControllerManager.getMainFrameController().stopApplication();
                dispose();
            }
        });

    }

    public JFrame getRoot() {
        return this;
    }

    public JLabel getPianoKeyNumLabel() {
        return pkn;
    }

    public JLabel getPianoKeyNameLabel() {
        return pkna;
    }

    public JLabel getKeyboardKeyNumLabel() {
        return kkn;
    }
}
