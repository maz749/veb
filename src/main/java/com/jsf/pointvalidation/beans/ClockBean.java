package com.jsf.pointvalidation.beans;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

@ManagedBean(name = "clockBean")
@ApplicationScoped
public class ClockBean implements Serializable {

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    public String getCurrentDateTime() {
        return dateFormat.format(new Date());
    }

    public Date getCurrentDate() {
        return new Date();
    }
}
