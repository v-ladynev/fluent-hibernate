package com.github.fluent.hibernate.test.persistent;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.ForeignKey;

/**
 *
 * @author V.Ladynev
 */
@Entity
@Table(name = "stationar_departments")
public class StationarDepartment extends StationarCommonData {

    private static final long serialVersionUID = 1569194049402712485L;

    private Stationar stationar;

    private StationarRoom room;

    public StationarDepartment() {

    }

    @ManyToOne(fetch = FetchType.LAZY)
    @ForeignKey(name = "fk_rooms_stdepartments")
    public StationarRoom getRoom() {
        return room;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @ForeignKey(name = "fk_stationaries_stdepartments")
    public Stationar getStationar() {
        return stationar;
    }

    public void setRoom(StationarRoom room) {
        this.room = room;
    }

    public void setStationar(Stationar stationar) {
        this.stationar = stationar;
    }

    @Transient
    public StationarDepartment assureChildren() {
        if (room == null) {
            room = new StationarRoom();
        }
        room.assureChildren();
        return this;
    }

}
