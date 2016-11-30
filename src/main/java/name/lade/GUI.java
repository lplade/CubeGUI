package name.lade;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Vector;

public class GUI extends JFrame {

    private JPanel rootPanel;
    private JTable solutionTable;
    private JTextField solverTextField;
    private JTextField timeTextField;
    private JTextField memoTextField;
    private JButton deleteSelectedRecordButton;
    private JButton addNewRecordButton;
    private JButton updateSelectedRecordButton;
    private JButton clearFormButton;

    private Vector<StoredSolution> allSolutions;

    private SolutionTableModel tableModel;

    private Controller controller;

    GUI(Controller controller) {



        super("Rubik's Cube records");
        setContentPane(rootPanel);
        setPreferredSize(new Dimension(800,500));

        //map the controller
        this.controller = controller;

        //set up models here
        tableModel = new SolutionTableModel(allSolutions);
        //hide the first column?
        //http://stackoverflow.com/questions/1492217/how-to-make-a-columns-in-jtable-invisible-for-swing-java

        addListeners();
        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void addListeners() {
        deleteSelectedRecordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        addNewRecordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        updateSelectedRecordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        clearFormButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(
                        GUI.this,
                        "Are you sure you want to exit?",
                        "Exit?",
                        JOptionPane.OK_CANCEL_OPTION)) {
                    //reset the database
                    controller.resetAllData();
                    //And quit.
                    System.exit(0);
                }
                //super.windowClosing(e);
            }
        });
    }



    public void setListData(Vector<StoredSolution> allSolutions) {

    }
}
