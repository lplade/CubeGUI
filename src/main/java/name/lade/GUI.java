package name.lade;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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

    //private Vector<StoredSolution> allSolutions;

    private SolutionTableModel tableModel;

    private Controller controller;

    GUI(Controller controller) {

        super("Rubik's Cube records");
        setContentPane(rootPanel);
        setPreferredSize(new Dimension(800,500));

        //map the controller
        this.controller = controller;

        //set up models here
        tableModel = new SolutionTableModel(controller.allSolutions);
        solutionTable.setModel(tableModel);


        //hide the first column?
        //http://stackoverflow.com/questions/1492217/how-to-make-a-columns-in-jtable-invisible-for-swing-java

        addListeners();
        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void addListeners() {
        //Delete record button
        deleteSelectedRecordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        //Add New Record button
        addNewRecordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //get solver, time, memo from fields
                String solver = solverTextField.getText();
                String timeStr = timeTextField.getText();
                Double time;
                String memo = memoTextField.getText();

                //validate these
                if (! testStringNotNull(solver, "name")) return;
                //first test if time is not null, then test if it is a valid float
                if (! testStringNotNull(timeStr, "time")) {
                    return;
                } else if (! testIsPositiveDouble(timeStr)) {
                    return;
                } else {
                    time = Double.parseDouble(timeStr);
                }
                assert (time >= 0.0);
                //memo is optional field, don't validate

                //construct the new object
                Solution newSolution;
                if (memo.isEmpty()) {
                    newSolution = new Solution(solver, time);
                } else {
                    newSolution = new Solution(solver, time, memo);
                }

                //save the Solution in the database

                controller.addRecordToDatabase(newSolution);

                //clear the input JTextFields
                solverTextField.setText("");
                timeTextField.setText("");
                memoTextField.setText("");


                //refresh allSolutions to reflect the changes

                Vector<StoredSolution> allSolutions = controller.getAllData();
                setListData(allSolutions);


            }
        });
        //Update record button
        updateSelectedRecordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        //Clear the form
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


    void setListData(Vector<StoredSolution> allSolutions) {
        tableModel.updateData(allSolutions);
    }

    //helper methods

    //tests if a string is empty
    //displays an error dialog if it is
    private boolean testStringNotNull(String inString, String fieldName) {
        // inString is the string to test
        // fieldName is the word displayed to the user in the error
        if ( inString == null || inString.length() == 0) {
            String errMsg = "Please enter a " + fieldName + " for this solver";
            JOptionPane.showMessageDialog(
                    GUI.this,
                    errMsg
            );
            return false;
        } else {
            return true;
        }
    }

    //tests if a string can be a valid double
    //displays error dialog if it is not
    private boolean testIsPositiveDouble(String str) {
        try {
            Double dbl = Double.parseDouble(str);
            if (dbl <= 0) {
                String errMsg = "Time entered must be greater than zero";
                JOptionPane.showMessageDialog(
                        GUI.this,
                        errMsg
                );
                return false;
            } else {
                return true;
            }
        } catch (NumberFormatException e) {
            String errMsg = "Time entered is not a valid number";
            JOptionPane.showMessageDialog(
                    GUI.this,
                    errMsg
            );
            return false;
        }
    }


}
