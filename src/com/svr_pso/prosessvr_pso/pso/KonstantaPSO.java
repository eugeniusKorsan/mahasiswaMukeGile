/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.svr_pso.prosessvr_pso.pso;

/**
 *
 * @author Giek
 */
public class KonstantaPSO {

    int SWARM_SIZE = 5;//jumlah partikel (array sing mudun jarenen posisi)
    int MAX_ITERATION = 25;
    int PROBLEM_DIMENSION = 5;//jumlah parameter yg digunakan
    double C1f = 0.5;
    double C1i = 2.5;
    double C2f = 2.5;
    double C2i = 0.5;
    double W_UPPERBOUND = 0.9;
    double W_LOWERBOUND = 0.4;
}
