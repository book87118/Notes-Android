package ching.notes.DB;

/**
 * Created by book871181 on 16/6/4.
 */
public class Notes {

    public int id;
    public String title;
    public String notes;




    public Notes(int id, String title, String notes) {
        this.id = id;
        this.title = title;
        this.notes = notes;
    }

    public void setNotes(String notes){
        this.notes = notes;


    }
}
