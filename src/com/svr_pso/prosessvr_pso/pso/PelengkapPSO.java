/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.svr_pso.prosessvr_pso.pso;

/**
 *
 * @author Giek
 */
public class PelengkapPSO {

    public static int getMinPos(double[] list) {
        int pos = 0;
        double minValue = list[0];

        for (int i = 0; i < list.length; i++) {
            if (list[i] < minValue) {
                pos = i;
                minValue = list[i];
            }
        }

        return pos;
    }
}
