package name.lade;

import java.util.Vector;

public class Controller {

    static GUI gui;
    static DB db;

    public static void main(String[] args) {
        Controller controller = new Controller();
        controller.startApp();
    }

    private void startApp() {
        db = new DB();
        db.createTable();
        Vector<StoredSolution> allSolutions = db.fetchAllRecords();
        gui = new GUI(this);
        gui.setListData(allSolutions);
    }

    Vector<StoredSolution> getAllData(){
        return db.fetchAllRecords();
    }

    void addRecordToDatabase(Solution solution) {
        db.addRecord(solution);
    }

    void updateRecord(StoredSolution solution) {
        db.updateRecord(solution);
    }

    void deleteRecord(StoredSolution solution) {
        db.delete(solution);
    }

    void resetAllData(){
        db.resetTable();
    }
}


