package comp5216.sydney.edu.au.todolist;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

@Entity(tableName = "todolist")
public class ToDoItem {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "toDoItemID")
    private int toDoItemID;

    @ColumnInfo(name = "toDoItemName")
    private String toDoItemName;

    @ColumnInfo(name = "toDoItemCreation")
    private String dateTimeCreation;

    @ColumnInfo(name = "toDoItemLastEdited")
    private String datetimeLastEdited;

    private static DateFormat df = DateFormat.getDateTimeInstance();

    @Ignore
    public ToDoItem(String toDoItemName){
        this.toDoItemName = toDoItemName;
        this.dateTimeCreation = df.format(new Date());
        this.datetimeLastEdited =df.format(new Date());
    }

    public ToDoItem(String toDoItemName, String dateTimeCreation, String datetimeLastEdited) {
        this.toDoItemName = toDoItemName;
        this.dateTimeCreation = dateTimeCreation;
        this.datetimeLastEdited = datetimeLastEdited;
    }

    public int getToDoItemID() {
        return toDoItemID;
    }

    public void setToDoItemID(int toDoItemID) {
        this.toDoItemID = toDoItemID;
    }

    public String getToDoItemName() {
        return toDoItemName;
    }

    public void setToDoItemName(String toDoItemName) {
        this.toDoItemName = toDoItemName;
        this.datetimeLastEdited = df.format(new Date());
    }

    public String getDateTimeCreation() {
        return dateTimeCreation;
    }

    public String getDatetimeLastEdited() {
        return datetimeLastEdited;
    }

    @Override
    public String toString() {
        return toDoItemName +
                ";" + dateTimeCreation +
                ";" + datetimeLastEdited;
    }

    public static ToDoItem getObjectFromLine(String line) throws ParseException {
        String[] fields = line.split(";");
        return new ToDoItem(fields[0],fields[1],fields[2]);
    }
}
