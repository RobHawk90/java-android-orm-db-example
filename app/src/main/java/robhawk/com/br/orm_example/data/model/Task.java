package robhawk.com.br.orm_example.data.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import robhawk.com.br.orm_example.orm.annotation.Id;
import robhawk.com.br.orm_example.orm.annotation.Table;
import robhawk.com.br.orm_example.util.DateUtil;

@Table("task")
public class Task implements Serializable {

    @Id
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

    public String getDate() {
        return DateUtil.formatPtBr(date);
    }

    public void setDate(String date) {
        this.date = DateUtil.parsePtBr(date);
    }

    @Override
    public String toString() {
        return description + " - " + completed;
    }
}
