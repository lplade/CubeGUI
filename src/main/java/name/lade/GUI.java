package name.lade;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
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
    private JButton deleteSelectedRecordButton;  //TODO start in disabled state
    private JButton addNewRecordButton;
    private JButton updateSelectedRecordButton; //TODO start in disabled state
    private JButton clearFormButton;

    //private Vector<StoredSolution> allSolutions;

    private SolutionTableModel tableModel;

    private Controller controller;

    private int selectedRecord;

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

        selectedRecord = -1; //this means no record is selected

        addListeners();
        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void addListeners() {

        //watch if user selects a row
        //http://stackoverflow.com/questions/10128064/jtable-selected-row-click-event
        solutionTable.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent event) {

                //first make sure item is not out of bounds
                //listener fires when clearSelecton() fires and causes oobe error if not tested
                //http://stackoverflow.com/questions/13102246/remove-the-selection-from-a-jlist-in-java-swing
                if (!event.getValueIsAdjusting() && solutionTable.getSelectedRow() >= 0) {

                    //grab the contents of the selected record

                    String solver = solutionTable.getValueAt(solutionTable.getSelectedRow(), 1).toString();
                    String time = solutionTable.getValueAt(solutionTable.getSelectedRow(), 2).toString();
                    String memo =  solutionTable.getValueAt(solutionTable.getSelectedRow(), 3).toString();

                    //display these in the form
                    solverTextField.setText(solver);
                    timeTextField.setText(time);
                    memoTextField.setText(memo);

                    //update the index records
                    int id;
                    try {
                        id = (int) solutionTable.getValueAt(solutionTable.getSelectedRow(), 0);
                    } catch (ArrayIndexOutOfBoundsException oobe){
                        //when we clear the selection, the listener fires and returns an invalid value here
                        id = -1;
                    }

                    selectedRecord = id;

                    //just leave these there until the user hits Update

                    //TODO disable the Add New Record button
                    //TODO enable to Update button
                    //TODO enable the Delete button
                }
            }
        });

        //Delete record button
        deleteSelectedRecordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO implement this
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

                if (selectedRecord == -1) {
                    //we should not be able to get here once the button disabling logic is in place
                    return;
                }

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

                //update the Solution in the database
                controller.updateRecord(selectedRecord, newSolution);

                //clear the JTable selection
                solutionTable.clearSelection();

                //clear the input JTextFields
                solverTextField.setText("");
                timeTextField.setText("");
                memoTextField.setText("");


                //refresh allSolutions to reflect the changes

                Vector<StoredSolution> allSolutions = controller.getAllData();
                setListData(allSolutions);


            }
        });


        //Clear the form
        clearFormButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO clear the text fields
                //TODO set selectedRecord back to -1;
                //TODO unselect JTable if needed
                //TODO re-enable Add button if needed
                //TODO disable the Update button
                //TODO disable the Delete button


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
