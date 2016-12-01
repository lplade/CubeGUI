package name.lade;

import javax.swing.table.AbstractTableModel;
import java.util.Vector;

public class SolutionTableModel extends AbstractTableModel{

    private Vector<StoredSolution> allSolutions;

    // Column names, displayed as table headers in the JTable.

    private String[] columnNames = {
            "ID", //hide this?
            "Solver",
            "Time",
            "Comment"
    };

    SolutionTableModel(Vector<StoredSolution> solutions) {
        allSolutions = solutions;
    }

    void updateData(Vector<StoredSolution> updatedSolutions) {
        //completely replace data in table with new data
        allSolutions = updatedSolutions;
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return allSolutions.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex){
            case 0: //ID
                return allSolutions.get(rowIndex).id;
            case 1: // Solver
                return allSolutions.get(rowIndex).name;
            case 2: // Time
                return allSolutions.get(rowIndex).time;
            case 3: // Comment
                return allSolutions.get(rowIndex).notes;
            default: //should never get here
                return null;
        }
    }

    StoredSolution getSolutionAtRow(int rowIndex){
        return allSolutions.get(rowIndex);
    }

    @Override
    public String getColumnName(int col){
        return columnNames[col];
    }


}
