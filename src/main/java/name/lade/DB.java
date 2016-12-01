package name.lade;

import java.sql.*;
import java.util.ArrayList;
import java.util.Vector;

public class DB {

    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_CONNECTION_URL = "jdbc:mysql://localhost:3306/cubes";
    private static final String USER = "lade";
    private static final String PASSWORD = "agram";
    //TODO don't hard code table name or column names?


    DB() {
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException cnfe) {
            System.out.println("Can't instantiate driver class; check drives and classpath");
            cnfe.printStackTrace();
            System.exit(-1); //exit if driver doesn't work
        }
    }

    void createTable() {
        try (
            Connection conn = DriverManager.getConnection(DB_CONNECTION_URL, USER, PASSWORD);
            Statement statement = conn.createStatement()) {
            //Database should already have been created

            //Create the table in the database if it doesn't already exist
            String createTableSQL =
                    "CREATE TABLE IF NOT EXISTS Solutions(" +
                            "SolutionID int NOT NULL AUTO_INCREMENT, " +
                            "Solver varchar(60), " +
                            "SolvedTime double, " +
                            "Notes varchar(60)," +
                            "PRIMARY KEY(SolutionID)" +
                            ")";
            statement.execute(createTableSQL);
            System.out.println("Created cube solution table");

            //Add starting data
            //TODO move this somewhere else?

            ArrayList<Solution> seedData = new ArrayList<>();
            seedData.add(new Solution("Cubestormer II robot", 5.270));
            seedData.add(new Solution("Fakhri Raihaan", 27.93, "using his feet"));
            seedData.add(new Solution("Ruxin Liu", 99.33, "age 3"));
            seedData.add(new Solution("Mats Valk", 6.27, "human record holder"));

            String prepStatInsert = "INSERT INTO Solutions VALUES (?, ?, ?, ? )";
            PreparedStatement psInsert = conn.prepareStatement(prepStatInsert);
            for (Solution solution : seedData){
                addRecord(solution);
            }

            System.out.println("Added initial data to database");


            statement.close();
            conn.close();

        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    void addRecord(Solution solution) {
        try (Connection conn = DriverManager.getConnection(DB_CONNECTION_URL, USER, PASSWORD)) {
            String prepStatInsert = "INSERT INTO Solutions VALUES (?, ?, ?, ? )";
            PreparedStatement psInsert = conn.prepareStatement(prepStatInsert);
            psInsert.setInt(1,0); //auto-increment key
            psInsert.setString(2, solution.name);
            psInsert.setDouble(3, solution.time);
            psInsert.setString(4, solution.notes);

            psInsert.execute();

            System.out.println("Added solution record for " + solution);

            psInsert.close();
            conn.close();

        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    void updateRecord(int currentID, Solution newSolution) {
        try (Connection conn = DriverManager.getConnection(DB_CONNECTION_URL, USER, PASSWORD)) {

            //Don't anticipate updating anything but time, but we update all three fields anyway
            String updateTime = "UPDATE Solutions SET Solver = ?, SolvedTime = ?, Notes = ? WHERE SolutionID = ?";
            PreparedStatement updatePS = conn.prepareStatement(updateTime);

            updatePS.setString(1, newSolution.name);
            updatePS.setDouble(2, newSolution.time);
            updatePS.setString(3, newSolution.notes);
            updatePS.setInt(4, currentID);

            updatePS.executeUpdate();

            System.out.println("Updated record " + currentID + " to " + newSolution);

            updatePS.close();
            conn.close();

        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    void delete(StoredSolution solution) {
        //TODO implement record deletion
    }

    void resetTable() {
        //Delete the table so we don't keep adding duplicate data at launch
        //TODO when adding data, check if any data exists and don't add duplicate data
        //then we can stop deleting this every time
        try (
                Connection conn = DriverManager.getConnection(DB_CONNECTION_URL, USER, PASSWORD);
                Statement statement = conn.createStatement()){
            String dropTable = "DROP TABLE Solutions";
            statement.executeUpdate(dropTable);
            System.out.println("Deleted table");
        } catch (SQLException se) {
            se.printStackTrace();
        }

    }

    Vector<StoredSolution> fetchAllRecords() {
        Vector<StoredSolution> allRecords = new Vector<>();

        try (
                Connection conn = DriverManager.getConnection(DB_CONNECTION_URL, USER, PASSWORD);
                Statement statement = conn.createStatement()){
            String selectAllSQL = "SELECT * FROM Solutions";
            ResultSet rs = statement.executeQuery(selectAllSQL);

            while (rs.next()){
                int id = rs.getInt("SolutionID");
                String name = rs.getString("Solver");
                String notes = rs.getString("Notes");
                double time = rs.getDouble("SolvedTime");
                StoredSolution solutionRecord = new StoredSolution(name, time, notes, id);
                allRecords.add(solutionRecord);
            }

            rs.close();
            statement.close();
            conn.close();


            return allRecords; //if there is no data, this will be empty

        } catch (SQLException se) {
                    se.printStackTrace();
                    return null; //we have to return something
        }

    }
}
