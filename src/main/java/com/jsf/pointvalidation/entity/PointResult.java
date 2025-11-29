package com.jsf.pointvalidation.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "point_results")
public class PointResult implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "point_seq")
    @SequenceGenerator(name = "point_seq", sequenceName = "point_sequence", allocationSize = 1)
    private Long id;

    @Column(name = "x_coordinate", nullable = false)
    private double x;

    @Column(name = "y_coordinate", nullable = false)
    private double y;

    @Column(name = "radius", nullable = false)
    private double r;

    @Column(name = "hit_result", nullable = false)
    private boolean hit;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "check_time", nullable = false)
    private Date checkTime;

    @Column(name = "execution_time")
    private long executionTime;

    public PointResult() {
    }

    public PointResult(double x, double y, double r, boolean hit, Date checkTime, long executionTime) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.hit = hit;
        this.checkTime = checkTime;
        this.executionTime = executionTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getR() {
        return r;
    }

    public void setR(double r) {
        this.r = r;
    }

    public boolean isHit() {
        return hit;
    }

    public void setHit(boolean hit) {
        this.hit = hit;
    }

    public Date getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Date checkTime) {
        this.checkTime = checkTime;
    }

    public long getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(long executionTime) {
        this.executionTime = executionTime;
    }
}
