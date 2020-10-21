package controller;

import bag.XmlHandler;
import data.*;
import bag.Backpack;
import data.Shape;
import exceptions.BackpackOverfullingException;
import exceptions.FileCancellingException;
import org.xml.sax.SAXException;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.ParserConfigurationException;

public class Controller extends JFrame {

    private JButton buttonAddCube;
    private JButton buttonAddBall;
    private JButton buttonAddCylinder;
    private JButton buttonDeleteFigure;
    private JButton buttonDeleteAll;
    private JLabel listLabel;
    private JList<Shape> listElements;
    private DefaultListModel<Shape> defaultListModel;
    private Backpack bag;

    public Controller() {
        super("Backpack of shapes");
        setBounds(400, 200, 600, 350);
        setMinimumSize(new Dimension(700, 350));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());

        initialization();

        placeElements();

        createMenu();

        buttonAddCube.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                String sLength = showInputDialog("Input length of the Cube");
                double length;
                if (!sLength.isEmpty()) {
                    try {
                        length = Double.parseDouble(sLength);
                    } catch (NumberFormatException e) {
                        showWrongNumberMessage();
                        return;
                    }
                } else {
                    length = 0;
                }
                if (length < 0) {
                    showWrongNumberMessage();
                    return;
                }

                try {
                    Cube cube = new Cube(length);
                    int index = bag.put(cube);
                    defaultListModel.add(index, cube);
                } catch (BackpackOverfullingException exception) {
                    showBackpackOverfullingMessage();
                }
            }
        });

        buttonAddBall.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                String sRadius = showInputDialog("Input radius of the Ball");
                double radius;
                if (!sRadius.isEmpty()) {
                    try {
                        radius = Double.parseDouble(sRadius);
                    } catch (NumberFormatException e) {
                        showWrongNumberMessage();
                        return;
                    }
                } else {
                    radius = 0;
                }
                if (radius < 0) {
                    showWrongNumberMessage();
                    return;
                }

                try {
                    Ball ball = new Ball(radius);
                    int index = bag.put(ball);
                    defaultListModel.add(index, ball);
                } catch (BackpackOverfullingException exception) {
                    showBackpackOverfullingMessage();
                }
            }
        });

        buttonAddCylinder.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                String sRadius = showInputDialog("Input radius of the Cylinder");
                double radius;
                if (!sRadius.isEmpty()) {
                    try {
                        radius = Double.parseDouble(sRadius);
                    } catch (NumberFormatException e) {
                        showWrongNumberMessage();
                        return;
                    }
                } else {
                    radius = 0;
                }
                if (radius < 0) {
                    showWrongNumberMessage();
                    return;
                }
                String sHeight = showInputDialog("Input height of the Cylinder");
                double height;
                if (!sHeight.isEmpty()) {
                    try {
                        height = Double.parseDouble(sHeight);
                    } catch (NumberFormatException e) {
                        showWrongNumberMessage();
                        return;
                    }
                } else {
                    height = 0;
                }
                if (height < 0) {
                    showWrongNumberMessage();
                    return;
                }

                try {
                    Cylinder cylinder = new Cylinder(radius, height);
                    int index = bag.put(cylinder);
                    defaultListModel.add(index, cylinder);
                } catch (BackpackOverfullingException exception) {
                    showBackpackOverfullingMessage();
                }
            }
        });

        buttonDeleteFigure.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                int index = listElements.getSelectedIndex();
                if (index >= 0) {
                    bag.delete(index);
                    defaultListModel.remove(index);
                } else {
                    JOptionPane.showMessageDialog(
                            Controller.this,
                            "Nothing have been chosen",
                            "Error",
                            JOptionPane.WARNING_MESSAGE
                    );
                }
            }
        });

        buttonDeleteAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bag.clear();
                defaultListModel.clear();
            }
        });

    }

    private void initialization() {
        buttonAddCube = new JButton("Add cube");
        buttonAddBall = new JButton("Add ball");
        buttonAddCylinder = new JButton("Add cylinder");
        buttonDeleteFigure = new JButton("Delete figure");
        buttonDeleteAll = new JButton("Delete all");
        listLabel = new JLabel("Backpack:");

        bag = new Backpack(10000);
        defaultListModel = new DefaultListModel<>();
        listElements = new JList<>(defaultListModel);
    }

    private void placeElements() {
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.WEST;
        listLabel.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        add(listLabel, constraints);

        JPanel listPanel = new JPanel();
        listPanel.add(listElements);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        constraints.gridheight = 5;
        constraints.anchor = GridBagConstraints.CENTER;
        listElements.setFixedCellWidth(350);
        add(listPanel, constraints);

        constraints.gridx = 1;
        constraints.gridy = GridBagConstraints.RELATIVE;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(4, 20, 4, 0);
        add(buttonAddCube, constraints);
        add(buttonAddBall, constraints);
        add(buttonAddCylinder, constraints);
        add(buttonDeleteFigure, constraints);
        add(buttonDeleteAll, constraints);

        JScrollPane scrollMessage = new JScrollPane();
        scrollMessage.setViewportView(listElements);
        scrollMessage.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        listPanel.add(scrollMessage);
    }

    private void createMenu() {
        JMenuBar menuBar = new JMenuBar();

        menuBar.add(createFileMenu());
        menuBar.add(createOptionsMenu());

        setJMenuBar(menuBar);
    }

    private JMenu createFileMenu() {
        JMenu file = new JMenu("File");

        JMenuItem open = new JMenuItem("Open");
        JMenuItem save = new JMenuItem("Save");

        file.add(open);
        file.add(save);

        open.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    File file = fileDialoge(FileChoosingType.OPENING);

                    try {
                        if (XmlHandler.xmlValidator(file)) {
                            bag = XmlHandler.xmlReader(file);
                        } else {
                            showNonValidFileMessage();
                            return;
                        }
                    } catch (ParserConfigurationException parserE) {
                        System.out.println("parser error");
                    } catch (SAXException sExc) {
                        System.out.println("sax error");
                    } catch (IOException ioExc) {
                        System.out.println("io error");
                    }

                    bag.renewViewShapeList(defaultListModel);

                } catch (FileCancellingException exc) {
                    System.out.println("cancelled");
                }
            }
        });

        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if (bag.isEmpty()) {
                    showEmptyBackpackMessage();
                    return;
                }
                try {
                    File file = fileDialoge(FileChoosingType.SAVING);
                    if (!file.getPath().substring(file.getPath().length() - 4).equals(".xml")) {
                        file = new File(file.getPath() + ".xml");
                    }
                    try {
                        file.createNewFile();
                    } catch (IOException ioExc) {
                        showFileErrorMessage();
                    }
                    XmlHandler.xmlWriter(bag, file);
                } catch (FileCancellingException exc) {
                    System.out.println("cancelled");
                }
            }
        });

        return file;
    }

    private JMenu createOptionsMenu() {
        JMenu options = new JMenu("Options");

        JMenuItem aboutAuthor = new JMenuItem("About");

        aboutAuthor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAboutMessage();
            }
        });

        options.add(aboutAuthor);

        return options;
    }

    private void showAboutMessage() {
        String msg = "Author: Artyom Rogozhnikov\n" +
                "The author wishes you good day :)";
        JOptionPane.showMessageDialog(
                Controller.this,
                msg,
                "About",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private File fileDialoge(FileChoosingType type) {
        JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));

        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "XML Files", "xml");
        fileChooser.setFileFilter(filter);

        int result = JFileChooser.CANCEL_OPTION;

        if (type == FileChoosingType.OPENING) {
            result = fileChooser.showOpenDialog(Controller.this);
        } else if (type == FileChoosingType.SAVING) {
            result = fileChooser.showSaveDialog(Controller.this);
        }

        if (result == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        } else {
            throw new FileCancellingException();
        }
    }

    private void showWrongNumberMessage() {
        JOptionPane.showMessageDialog(
                Controller.this,
                "Wrong number",
                "Error",
                JOptionPane.WARNING_MESSAGE
        );
    }

    private void showBackpackOverfullingMessage() {
        JOptionPane.showMessageDialog(
                Controller.this,
                "Backpack is full",
                "Error",
                JOptionPane.WARNING_MESSAGE
        );
    }

    private void showFileErrorMessage() {
        JOptionPane.showMessageDialog(
                Controller.this,
                "File error",
                "Cannot create file",
                JOptionPane.WARNING_MESSAGE
        );
    }

    private void showNonValidFileMessage() {
        JOptionPane.showMessageDialog(
                Controller.this,
                "File is not valid",
                "File error",
                JOptionPane.WARNING_MESSAGE
        );
    }

    private void showEmptyBackpackMessage() {
        JOptionPane.showMessageDialog(
                Controller.this,
                "The bag is empty",
                "Error",
                JOptionPane.WARNING_MESSAGE
        );
    }

    private String showInputDialog(String text) {
        return JOptionPane.showInputDialog(
                Controller.this,
                text,
                "Input the value",
                JOptionPane.QUESTION_MESSAGE
        );
    }

}
