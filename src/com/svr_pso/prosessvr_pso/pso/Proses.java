/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.svr_pso.prosessvr_pso.pso;

import GUI.Metode_SVR;
import java.util.Random;
import java.util.Vector;
//import PSO_Tes.KonstantaPSO;

/**
 *
 * @author Giek
 */
public class Proses extends KonstantaPSO {

    private Vector<Partikel> swarm = new Vector<Partikel>();
    private double[] pBest = new double[SWARM_SIZE];
    private Vector<Posisi> pBestLocation = new Vector<Posisi>();
    private double gBest;
    private Posisi gBestLocation;
    private double[] fitnessValueList = new double[SWARM_SIZE];
    Random generator = new Random();
    private double C1;
    private double C2;
    int t = 0;
    double w;
    double err = 9999;

    public void execute() {
        initializeSwarm();
        updateFitnessList();

        for (int i = 0; i < SWARM_SIZE; i++) {
            pBest[i] = fitnessValueList[i];
            pBestLocation.add(swarm.get(i).getLocation());
        }

        while (t < MAX_ITERATION && err > ProblemSet.ERR_TOLERANCE) {
            //calc C1
            C1 = (C1f - C1i) * (t / MAX_ITERATION) + C1i;
            C2 = (C2f - C2i) * (t / MAX_ITERATION) + C2i;
            // step 1 - update pBest
            for (int i = 0; i < SWARM_SIZE; i++) {
                if (fitnessValueList[i] < pBest[i]) {
                    pBest[i] = fitnessValueList[i];
                    pBestLocation.set(i, swarm.get(i).getLocation());
                }
            }

            // step 2 - update gBest
            int bestParticleIndex = PelengkapPSO.getMinPos(fitnessValueList);
            if (t == 0 || fitnessValueList[bestParticleIndex] < gBest) {
                gBest = fitnessValueList[bestParticleIndex];
                gBestLocation = swarm.get(bestParticleIndex).getLocation();
            }
//            System.out.println("t : "+t);

            w = W_UPPERBOUND - (((double) t) / MAX_ITERATION) * (W_UPPERBOUND - W_LOWERBOUND);
            System.out.println("w : " + w);

            for (int i = 0; i < SWARM_SIZE; i++) {
                double r1 = generator.nextDouble();
                double r2 = generator.nextDouble();

                Partikel p = swarm.get(i);

                // step 3 - update velocity
                double[] newVel = new double[PROBLEM_DIMENSION];
                newVel[0] = (w * p.getVelocity().getPos()[0])
                        + (r1 * C1) * (pBestLocation.get(i).getLoc()[0] - p.getLocation().getLoc()[0])
                        + (r2 * C2) * (gBestLocation.getLoc()[0] - p.getLocation().getLoc()[0]);//x
                newVel[1] = (w * p.getVelocity().getPos()[1])
                        + (r1 * C1) * (pBestLocation.get(i).getLoc()[1] - p.getLocation().getLoc()[1])
                        + (r2 * C2) * (gBestLocation.getLoc()[1] - p.getLocation().getLoc()[1]);//y
                newVel[2] = (w * p.getVelocity().getPos()[2])
                        + (r1 * C1) * (pBestLocation.get(i).getLoc()[2] - p.getLocation().getLoc()[2])
                        + (r2 * C2) * (gBestLocation.getLoc()[2] - p.getLocation().getLoc()[2]);//z
                newVel[3] = (w * p.getVelocity().getPos()[3])
                        + (r1 * C1) * (pBestLocation.get(i).getLoc()[3] - p.getLocation().getLoc()[3])
                        + (r2 * C2) * (gBestLocation.getLoc()[3] - p.getLocation().getLoc()[3]);//z
                newVel[4] = (w * p.getVelocity().getPos()[4])
                        + (r1 * C1) * (pBestLocation.get(i).getLoc()[4] - p.getLocation().getLoc()[4])
                        + (r2 * C2) * (gBestLocation.getLoc()[4] - p.getLocation().getLoc()[4]);//z
                Velocity vel = new Velocity(newVel);
                p.setVelocity(vel);
                System.out.println("Velocity C: " + newVel[0]);
                System.out.println("Velocity Lamda: " + newVel[1]);
                System.out.println("Velocity LR: " + newVel[2]);
                System.out.println("Velocity Sigma: " + newVel[3]);
                System.out.println("Velocity Epsilon: " + newVel[4]);
                System.out.println("");

                // step 4 - update posisi
                double[] newLoc = new double[PROBLEM_DIMENSION];
                newLoc[0] = p.getLocation().getLoc()[0] + newVel[0];//x
                newLoc[1] = p.getLocation().getLoc()[1] + newVel[1];//y
                newLoc[2] = p.getLocation().getLoc()[2] + newVel[2];//Z
                newLoc[3] = p.getLocation().getLoc()[3] + newVel[3];//y
                newLoc[4] = p.getLocation().getLoc()[4] + newVel[4];//Z
                Posisi loc = new Posisi(newLoc);
                p.setLocation(loc);
                System.out.println("Posisi C: " + newLoc[0]);
                System.out.println("Posisi Lamda: " + newLoc[1]);
                System.out.println("Posisi LR: " + newLoc[2]);
                System.out.println("Posisi Sigma: " + newLoc[3]);
                System.out.println("Posisi Epsilon: " + newLoc[4]);
                System.out.println("");

            }

//            err = ProblemSet.evaluate(gBestLocation) - 0; // minimizing the functions means it's getting closer to 0
            err = Metode_SVR.evaluate(gBestLocation) - 0; // minimizing the functions means it's getting closer to 0

            System.out.println("ITERATION " + t + ": ");
            System.out.println("     Best C: " + gBestLocation.getLoc()[0]);
            System.out.println("     Best Lamda: " + gBestLocation.getLoc()[1]);
            System.out.println("     Best LR: " + gBestLocation.getLoc()[2]);
            System.out.println("     Best Sigma: " + gBestLocation.getLoc()[3]);
            System.out.println("     Best Epsilon: " + gBestLocation.getLoc()[4]);
//            System.out.println("     Value: " + ProblemSet.evaluate(gBestLocation));
            System.out.println("     Value: " + Metode_SVR.evaluate(gBestLocation));

            t++;
            updateFitnessList();
        }

        System.out.println("\nSolution found at iteration " + (t - 1) + ", the solutions is:");
        System.out.println("     Best X: " + gBestLocation.getLoc()[0]);
        System.out.println("     Best Y: " + gBestLocation.getLoc()[1]);
        System.out.println("     Best Z: " + gBestLocation.getLoc()[2]);
        System.out.println("     Best A: " + gBestLocation.getLoc()[3]);
        System.out.println("     Best B: " + gBestLocation.getLoc()[4]);
    }

    public void initializeSwarm() {
        Partikel p;
        for (int i = 0; i < SWARM_SIZE; i++) {
            p = new Partikel();

            // randomize location inside a space defined in Problem Set
            double[] loc = new double[PROBLEM_DIMENSION];
            loc[0] = ProblemSet.LOC_C_LOW + generator.nextDouble()* (ProblemSet.LOC_C_HIGH - ProblemSet.LOC_C_LOW);//C
            loc[1] = ProblemSet.LOC_LAMDA_LOW + generator.nextDouble() * (ProblemSet.LOC_LAMDA_HIGH - ProblemSet.LOC_LAMDA_LOW);//LAMDA
            loc[2] = ProblemSet.LOC_LR_LOW + generator.nextDouble() * (ProblemSet.LOC_LR_HIGH - ProblemSet.LOC_LR_LOW);//LR
            loc[3] = ProblemSet.LOC_SIGMA_LOW + generator.nextDouble() * (ProblemSet.LOC_SIGMA_HIGH - ProblemSet.LOC_SIGMA_LOW);//LR
            loc[4] = ProblemSet.LOC_EPSILON_LOW + generator.nextDouble() * (ProblemSet.LOC_EPSILON_HIGH - ProblemSet.LOC_EPSILON_LOW);//LR
            
            
            Posisi posisi = new Posisi(loc);

            // randomize velocity in the range defined in Problem Set
            double[] vel = new double[PROBLEM_DIMENSION];
            vel[0] = ProblemSet.VEL_LOW + generator.nextDouble() * (ProblemSet.VEL_HIGH - ProblemSet.VEL_LOW);//x
            vel[1] = ProblemSet.VEL_LOW + generator.nextDouble() * (ProblemSet.VEL_HIGH - ProblemSet.VEL_LOW);//y
            vel[2] = ProblemSet.VEL_LOW + generator.nextDouble() * (ProblemSet.VEL_HIGH - ProblemSet.VEL_LOW);//Z
            vel[3] = ProblemSet.VEL_LOW + generator.nextDouble() * (ProblemSet.VEL_HIGH - ProblemSet.VEL_LOW);//Z
            vel[4] = ProblemSet.VEL_LOW + generator.nextDouble() * (ProblemSet.VEL_HIGH - ProblemSet.VEL_LOW);//Z
            Velocity velocity = new Velocity(vel);

            p.setLocation(posisi);
            p.setVelocity(velocity);
            swarm.add(p);
        }
    }

    public void updateFitnessList() {
        for (int i = 0; i < SWARM_SIZE; i++) {
//            if (t == 0) {
//                fitnessValueList[i] = 0;
//            } else {
                fitnessValueList[i] = swarm.get(i).getFitnessValue();
//            }
        }
    }
}
