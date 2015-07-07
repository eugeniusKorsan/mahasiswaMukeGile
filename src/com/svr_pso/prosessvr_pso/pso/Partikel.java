/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.svr_pso.prosessvr_pso.pso;

import com.svr_pso.guisvr_pso.FRAME_UTAMA;
import java.util.Random;

/**
 *
 * @author Giek
 */
public class Partikel {

    private double fitnessValue;
    private Velocity velocity;
    private Posisi posisi;

    public Partikel() {
        super();
    }

    public Partikel(double fitnessValue, Velocity velocity, Posisi posisi) {
        super();
        this.fitnessValue = fitnessValue;
        this.velocity = velocity;
        this.posisi = posisi;
    }

    public Velocity getVelocity() {
        return velocity;
    }

    public void setVelocity(Velocity velocity) {
        this.velocity = velocity;
    }

    public Posisi getLocation() {
        return posisi;
    }

    public void setLocation(Posisi location) {
        this.posisi = location;
    }

    public double getFitnessValue() {
//        fitnessValue = ProblemSet.evaluate(posisi);
        
        fitnessValue = FRAME_UTAMA.evaluate(posisi);
        return fitnessValue;
        ini natnti akan di koneksikan ke frame_utama

    }
}
