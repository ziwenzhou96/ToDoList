package comp5216.sydney.edu.au.todolist;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

public class ListItem {
    private String text;
    private Date dateTimeCreation,datetimeLastEdited;
    private static DateFormat df = DateFormat.getDateTimeInstance();

    public ListItem(String text) {
        this.text = text;
        this.dateTimeCreation = new Date();
        this.datetimeLastEdited = new Date();
    }

    public ListItem(String text, Date dateTimeCreation, Date datetimeLastEdited) {
        this.text = text;
        this.dateTimeCreation = dateTimeCreation;
        this.datetimeLastEdited = datetimeLastEdited;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        this.datetimeLastEdited = new Date();
    }

    public Date getDateTimeCreation() {
        return dateTimeCreation;
    }

    public Date getDatetimeLastEdited() {
        return datetimeLastEdited;
    }

    @Override
    public String toString() {
        return text +
                ";" + df.format(dateTimeCreation) +
                ";" + df.format(datetimeLastEdited);
    }

    public static ListItem getObjectFromLine(String line) throws ParseException {
        String[] fields = line.split(";");
        return new ListItem(fields[0],df.parse(fields[1]),df.parse(fields[2]));
    }
}
