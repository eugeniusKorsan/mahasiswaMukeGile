/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.svr_pso.prosessvr_pso.pso;

/**
 *
 * @author Giek
 */
public class Posisi {

    private double[] loc;

    public Posisi(double[] loc) {
        super();
        this.loc = loc;
    }

    public double[] getLoc() {
        return loc;
    }

    public void setLoc(double[] loc) {
        this.loc = loc;
    }
}