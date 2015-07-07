/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.svr_pso.prosessvr_pso.pso;

/**
 *
 * @author Giek
 */
public class ProblemSet {

    public static final double LOC_SIGMA_LOW = 0.0009;
    public static final double LOC_SIGMA_HIGH = 9;
    public static final double LOC_EPSILON_LOW = 1e-10;
    public static final double LOC_EPSILON_HIGH = 1e-6;
    public static final double LOC_C_LOW = 1;
    public static final double LOC_C_HIGH = 100;   
    public static final double LOC_LAMDA_LOW = 1.11;
    public static final double LOC_LAMDA_HIGH = 6.66;
    public static final double LOC_LR_LOW = 1e-2;
    public static final double LOC_LR_HIGH = 1e-6;
    public static final double VEL_LOW = 0.6;
    public static final double VEL_HIGH = 60;
    public static final double ERR_TOLERANCE = 1E-20; // the smaller the tolerance, the more accurate the result, 
    // but the number of iteration is increased

    public static double evaluate(Posisi posisi) {
        double result = 0;
        double C = posisi.getLoc()[0]; // the "x" part of the location
        double Lamda = posisi.getLoc()[1]; // the "y" part of the location
        double LR = posisi.getLoc()[2]; // the "y" part of the location
        double Sigma = posisi.getLoc()[3]; // the "y" part of the location
        double Epsilon = posisi.getLoc()[4]; // the "y" part of the location

        result = Math.pow(2.8125 - C + C * Math.pow(LR, 4), 2)//calc fitness
                + Math.pow(2.25 - C + C * Math.pow(LR, 2), 2)
                + Math.pow(1.5 - C + C * LR, 2);
        
        return result;
    }
}
