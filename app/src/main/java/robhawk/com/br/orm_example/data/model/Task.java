package robhawk.com.br.orm_example.data.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class Task implements Serializable {
    public int id;
    public String description;
    public Date date;
    public boolean completed;
    public int idUser;

    public Task() {
        this("", Calendar.getInstance().getTime(), 0);
    }

    public Task(String description, Date date, int idUser) {
        this.description = description;
        this.date = date;
        this.idUser = idUser;
    }

    @Override
    public String toString() {
        return description + " - " + completed;
    }
}
