package name.lade;

//class that holds data for storing Solutions
//we only use the base class when creating new records
class Solution {
    String name = "";
    double time = 0;
    String notes = "";


    Solution(String name, double time){
        this.name = name;
        this.time = time;
    }

    //notes is an optional third parameter
    Solution(String name, double time, String notes){
        this.name = name;
        this.time = time;
        this.notes = notes;
    }

    @Override
    public String toString() {
        return name + ":" + time + "s";
    }

}

//when we retrieve a record from the database, additionally store the primary key
//use this subclass when building our table model
class StoredSolution extends Solution{
    int id = -1; //set this after retrieving a record from DB

    StoredSolution(String name, double time, String notes, int id) {
        super(name, time, notes);
        this.id = id;
    }

}
