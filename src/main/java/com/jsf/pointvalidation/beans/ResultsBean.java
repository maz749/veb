package com.jsf.pointvalidation.beans;

import com.jsf.pointvalidation.entity.PointResult;
import com.jsf.pointvalidation.util.AreaChecker;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ManagedBean(name = "resultsBean")
@SessionScoped
public class ResultsBean implements Serializable {

    private double x;
    private double y;
    private double r = 2.0;

    private List<PointResult> results;
    private EntityManagerFactory emf;

    @PostConstruct
    public void init() {
        results = new ArrayList<>();
        emf = Persistence.createEntityManagerFactory("pointValidationPU");
        loadResults();
    }

    public void checkPoint() {
        long startTime = System.nanoTime();

        boolean hit = AreaChecker.checkHit(x, y, r);
        Date checkTime = new Date();

        long executionTime = System.nanoTime() - startTime;

        PointResult result = new PointResult(x, y, r, hit, checkTime, executionTime);

        saveResult(result);
        results.add(0, result);
    }

    public void checkPointFromCanvas(double canvasX, double canvasY) {
        this.x = canvasX;
        this.y = canvasY;
        checkPoint();
    }

    private void saveResult(PointResult result) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(result);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    private void loadResults() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<PointResult> query = em.createQuery(
                    "SELECT p FROM PointResult p ORDER BY p.checkTime DESC", PointResult.class);
            results = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            results = new ArrayList<>();
        } finally {
            em.close();
        }
    }

    public void clearResults() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM PointResult").executeUpdate();
            em.getTransaction().commit();
            results.clear();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    // Getters and Setters
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

    public List<PointResult> getResults() {
        return results;
    }

    public void setResults(List<PointResult> results) {
        this.results = results;
    }
}
