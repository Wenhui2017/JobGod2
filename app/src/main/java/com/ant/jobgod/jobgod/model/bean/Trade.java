package com.ant.jobgod.jobgod.model.bean;

import java.io.Serializable;

/**
 * Created by Mr.Jude on 2015/7/7.
 */
public class Trade implements Serializable{
    private int id;
    private String name;
    private String icon;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Trade))return false;
        return getId() == ((Trade)o).getId();
    }

    @Override
    public int hashCode() {
        return id;
    }
}
