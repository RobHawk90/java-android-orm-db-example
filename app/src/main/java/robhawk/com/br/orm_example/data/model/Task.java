package robhawk.com.br.orm_example.data.model;

import java.util.Calendar;
import java.util.Date;

public class Task {
    public int id;
    public String description;
    public Date date;
    public boolean completed;
    public int idUser;

    public Task() {
        this("", Calendar.getInstance().getTime(), false);
    }

    public Task(String description, Date date, boolean completed) {
        this.description = description;
        this.date = date;
        this.completed = completed;
    }

    @Override
    public String toString() {
        return description + " - " + completed;
    }
}
