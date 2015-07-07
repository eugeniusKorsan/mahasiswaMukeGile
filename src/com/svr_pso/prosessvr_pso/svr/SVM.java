package com.svr_pso.prosessvr_pso.svr;

import java.util.*;

/**
 * Main class containing the SVM's train and test metods
 *
 * @author miafranc
 *
 */
public class SVM {

    /**
     * Trained/loaded model
     */
    private Model model;
    /**
     * Regularization parameter
     */
    private double C = 1;
    /**
     * Tolerance
     */
    private double tol = 10e-3;
    /**
     * Tolerance
     */
    private double tol2 = 10e-5;

    private double tol3 = 10e-3;
    private double tol4 = 10e-5;

    /**
     * Number of times to iterate over the alpha's without changing; used in
     * SMO_simple
     */
    private int maxpass = 10;

    /*some global variables of the SMO algorithm*/
    private double Ei, Ej;
    private double ai_old, aj_old, b_old;
    private double L, H;
    private double maxup, minlow;
    private double bup, blow;
    private int iup, ilow;
    private double eta;
    private List<Integer> I0, I1, I2, I3, I4;
    private double[] E;
    private double lamda, epsilon, LR;
    /* ---------------------------------------- */

    public SVM() {
    }

    /**
     * Training the SVM
     *
     * @param train Training set
     */
    public void svmTrain(Problem train) {
        KernelParams p = new KernelParams();
        svmTrain(train, p, 0);
    }

    /**
     * Training the SVM with specified kernel parameters and algorithm
     *
     * @param train Training set
     * @param p Kernel parameters
     * @param alg Chosen algorithm: 0 - SMO_simple, 1 - SMO_Platt
     */
    public void svmTrain(Problem train, KernelParams p, int alg) {
        switch (alg) {
            case 0:
//                SMO_simple(train, p);
                SVR(train, p);
                break;
            case 1:
                SMO_Platt(train, p);
                break;
            case 2:
                SMO_Keerthi(train, p);
                break;
        }
    }
    /* =====================================================
     * Platt's Sequential Minimal Optimization (SMO)
     ===================================================== */

    /**
     * TakeStep method for Platt's SMO
     *
     * @param i First index
     * @param j Second index
     * @return 0/1
     */
    private int psmoTakeStep(int i, int j) {
        double ai, aj;
        if (i == j) {
            return 0;
        }
        b_old = model.b;
        ai_old = model.alpha[i];
        //aj_old = model.alpha[j];
        int yi = model.y[i];
        int yj = model.y[j];
        Ei = E[i];
        //Ej = E[j];
        double s = yi * yj;
        L = computeL(yi, yj);
        H = computeH(yi, yj);
        if (L == H) {
            return 0;
        }
        double kii = kernel(i, i);
        double kjj = kernel(j, j);
        double kij = kernel(i, j);
        eta = 2 * kij - kii - kjj;
        if (eta < 0) {
            aj = aj_old - yj * (Ei - Ej) / eta;
            if (aj < L) {
                aj = L;
            } else if (aj > H) {
                aj = H;
            }
        } else {
            double vi, vj;
            vi = svmTestOne(model.x[i]) - yi * ai_old * kii - yj * aj_old * kij;
            vj = svmTestOne(model.x[j]) - yi * ai_old * kij - yj * aj_old * kjj;
            double Lobj = smoObj(L, yi, yj, kij, kii, kjj, vi, vj);
            double Hobj = smoObj(H, yi, yj, kij, kii, kjj, vi, vj);
            if (Lobj > Hobj + tol) {
                aj = L;
            } else if (Lobj < Hobj - tol) {
                aj = H;
            } else {
                aj = aj_old;
            }
        }
        if (aj < tol2) {
            aj = 0;
        } else if (aj > C - tol2) {
            aj = C;
        }
        if (Math.abs(aj - aj_old) < tol * (aj + aj_old + tol)) {
            return 0;
        }
        ai = ai_old + s * (aj_old - aj);
        computeBias(ai, aj, yi, yj, kii, kjj, kij);
        model.alpha[i] = ai;
        model.alpha[j] = aj;
        for (int k = 0; k < model.l; k++) {
            double kik = kernel(model.x[i], model.x[k]);
            double kjk = kernel(model.x[j], model.x[k]);
            E[k] += (ai - ai_old) * yi * kik + (aj - aj_old) * yj * kjk - b_old + model.b;
        }
        return 1;
    }

    /**
     * ExamineExample method for Platt's SMO
     *
     * @param j Second index
     * @return 0/1
     */
    private int psmoExamineExample(int j) {
        int i = 0;
        int randpos;
        int yj = model.y[j];
        aj_old = model.alpha[j];
        Ej = E[j];
        double rj = Ej * yj;
        if ((rj < -tol && aj_old < C) || (rj > tol && aj_old > 0)) {
            boolean exists = false;
            for (int k = 0; k < model.l; k++) {
                if (model.alpha[k] > 0 && model.alpha[k] < C) {
                    exists = true;
                    break;
                }
            }
            if (exists) {
                //second choice heuristics:
                int maxind = 0;
                double maxval = Math.abs(E[0] - Ej);
                for (int k = 1; k < model.l; k++) {
                    if (Math.abs(E[k] - Ej) > maxval) {
                        maxval = Math.abs(E[k] - Ej);
                        maxind = k;
                    }
                }
                if (psmoTakeStep(maxind, j) == 1) {
                    return 1;
                }
            }
            //loop over non-zero & non-C alpha, starting at a random point:
            randpos = (int) Math.floor(Math.random() * model.l);
            for (int k = 0; k < model.alpha.length; k++) {
                i = (randpos + k) % model.l;
                if (model.alpha[i] > 0 && model.alpha[i] < C) {
                    if (psmoTakeStep(i, j) == 1) {
                        return 1;
                    }
                }
            }
            //loop over all i, starting at a random point
            randpos = (int) Math.floor(Math.random() * model.l);
            for (int k = 0; k < model.alpha.length; k++) {
                i = (randpos + k) % model.l;
                if (psmoTakeStep(i, j) == 1) {
                    return 1;
                }
            }
        }
        return 0;
    }

    /**
     * Main method for Platt's SMO
     *
     * @param train Training data set
     * @param p Kernel parameters
     */
    private void SMO_Platt(Problem train, KernelParams p) {
        int numChanged = 0;
        int examineAll = 1;
        //Initialize:
        model = new Model();
        model.alpha = new double[train.l];
        model.b = 0;
        model.params = p;
        model.x = train.x;
        model.y = train.y;
        model.l = train.l;
        model.n = train.n;
        //Initialize error cache E:
        E = new double[model.l];
        for (int i = 0; i < model.l; i++) {
            E[i] = svmTestOne(model.x[i]) - model.y[i];
        }
        while (numChanged > 0 || examineAll == 1) {
            numChanged = 0;
            if (examineAll == 1) {
                for (int i = 0; i < model.l; i++) {
                    numChanged += psmoExamineExample(i);
                }
            } else {
                for (int i = 0; i < model.l; i++) {
                    if (model.alpha[i] > 0 && model.alpha[i] < 0) {
                        numChanged += psmoExamineExample(i);
                    }
                }
            }
            if (examineAll == 1) {
                examineAll = 0;
            } else if (numChanged == 0) {
                examineAll = 1;
            }
            System.out.print(".");
        }
        System.out.println();
    }
    /* =====================================================
     * ===================================================== */

    /* =====================================================
     * Simple, probabilistic SMO
     ===================================================== */
    /**
     * Probabilistic (random, simple) SMO
     *
     * @param train Training set
     * @param p Kernel parameters
     */
    private void SVR(Problem t, KernelParams p) {
        //inisialisasi
        double delta_alpha[] = new double[t.l];
        double delta_alpha_star[] = new double[t.l];
        double ErrI[] = new double[t.l];
        double RIJ[][] = new double[t.l][t.l];
        int i, j = 0;
        int pass = 0;
        double alphaStar_baru = 0;
        double alpha_baru = 0;
        model = new Model();
        model.alpha = new double[t.l];
        model.alphaStar = new double[t.l];
        model.params = p;
        model.n = t.n;
        model.x = t.x;
        model.ySVR = t.ySVR;
        model.l = t.l;

        
                for (i = 0; i < t.l; i++) {
            for (j = 0; j < t.l; j++) {
//                System.out.println("kernel : "+kernel(model.x[i], model.x[j]));
                RIJ[i][j] = (kernel(model.x[i], model.x[j])) + (lamda * lamda);
//                System.out.println("rij : " + (i + 1) + "," + (j + 1) + " : " + RIJ[i][j]);
            }
        }


        LR = 0.0001 / RIJ[0][0];
        double maxMatrix = 0;
        for (int k = 0; k < t.l; k++) {
            for (int l = 0; l < t.l; l++) {
                if (k == l) {
                    if (RIJ[k][l] > maxMatrix) {
                        maxMatrix = RIJ[k][l];
//                        System.out.println("maxmat:" + maxMatrix);
                    }
                }
            }
        }
        double jumsemua[] = new double[t.l];
        while (pass < maxpass) {
//            System.out.println("");
//            System.out.println("perulangan ke " + pass);
            for (i = 0; i < t.l; i++) {
                for (j = 0; j < t.l; j++) {
                    double jum = (model.alphaStar[j] - model.alpha[j]) * RIJ[j][i];
//                    System.out.println("jum " + jum);
                    jumsemua[i] = jumsemua[i] + jum;
                }

//                if (pass == 100) {
//                    System.out.println("satus");
//                }
//                if (pass == 1000) {
//                    System.out.println("sewu");
//                }
//                if (pass == 10000) {
//                    System.out.println("sepuluh ewu");
//                }
//                if (pass == 50000) {
//                    System.out.println("50 ewu");
//                }
//                if (pass == 70000) {
//                    System.out.println("70 ewu");
//                }
//                if (pass == 99999) {
//                    System.out.println("meh satus ewu");
//                }
            }
            for (i = 0; i < t.l; i++) {
                ErrI[i] = t.ySVR[i] - jumsemua[i];
                if (pass == 100) {
//                    System.out.println(targetY[i] + "-" + jumsemua);
//                    System.out.println("Error : " + ErrI[i]);
                }
            }
            for (i = 0; i < t.l; i++) {
//              hiutng delta alpha
                delta_alpha_star[i] = Math.min(Math.max((LR * (ErrI[i] - epsilon)),
                        -model.alphaStar[i]), (C - model.alphaStar[i]));
                delta_alpha[i] = Math.min(Math.max((LR * (-ErrI[i] - epsilon)),
                        -model.alpha[i]), (C - model.alpha[i]));
            }
            for (i = 0; i < t.l; i++) {
                //update
                model.alphaStar[i] = model.alphaStar[i] + delta_alpha_star[i];
                model.alpha[i] = model.alpha[i] + delta_alpha[i];
            }
            for (i = 0; i < t.l; i++) {
                jumsemua[i] = 0;
            }
            pass++;
        }

        
        
        
        //
//        // step 1
//        for (i = 0; i < t.l; i++) {
//            for (j = 0; j < t.l; j++) {
////                System.out.println("kernel : "+kernel(model.x[i], model.x[j]));
//                RIJ[i][j] = (kernel(model.x[i], model.x[j])) + (lamda * lamda);
//                System.out.println("rij : " + (i + 1) + "," + (j + 1) + " : " + RIJ[i][j]);
//            }
//        }
//        System.out.println("=====");
//
//        //step 2
//        while ((pass < maxpass)) {
//            double hasilJum = 0;
//            double jumRij = 0;
//            for (i = 0; i < t.l; i++) {
//                for (j = 0; j < t.l; j++) {
//                    jumRij = (model.alphaStar[i] - model.alpha[i]) * RIJ[i][j];
//                    hasilJum = hasilJum + jumRij;
//                }
//                ErrI[i] = t.ySVR[i] - hasilJum;
//            }
////                System.out.println("jumr : "+hasilJum);
//            double maxMatrix = 0;
//            for (int k = 0; k < t.l; k++) {
//                for (int l = 0; l < t.l; l++) {
//                    if (k == l) {
//                        if (RIJ[k][l] > maxMatrix) {
//                            maxMatrix = RIJ[k][l];
//                        }
//                    }
//
//                }
//            }
//                //step 2.1
//
//            //step 2.2
//            for (i = 0; i < t.l; i++) {
//                delta_alpha_star[i] = Math.min(Math.max(((LR / maxMatrix) * (ErrI[i] - epsilon)),
//                        -model.alphaStar[i]), (C - model.alphaStar[i]));
//                delta_alpha[i] = Math.min(Math.max(((LR / maxMatrix) * (-ErrI[i] - epsilon)),
//                        -model.alpha[i]), (C - model.alpha[i]));
//            }
//
//            //step 2.3
//            for (i = 0; i < t.l; i++) {
//                alphaStar_baru = model.alphaStar[i] + delta_alpha_star[i];
//                alpha_baru = model.alpha[i] + delta_alpha[i];
//                model.alphaStar[i] = alphaStar_baru;
//                model.alpha[i] = alpha_baru;
//            }
////            System.out.println("perulangan ke " + (pass + 1));
//            pass++;
//        }

        System.out.println("perulangan selesai");

    }

    public int getMaxpass() {
        return maxpass;
    }

    public void setMaxpass(int maxpass) {
        this.maxpass = maxpass;
    }

    public double getEpsilon() {
        return epsilon;
    }

    public void setEpsilon(double epsilon) {
        this.epsilon = epsilon;
    }

    public double getLR() {
        return LR;
    }

    public void setLR(double LR) {
        this.LR = LR;
    }

    private void SMO_simple(Problem train, KernelParams p) {
        int pass = 0;
        int alpha_change = 0;
        int i, j;
        double eta;
        //Initialize:
        model = new Model();
        model.alpha = new double[train.l];
        model.b = 0;
        model.params = p;
        model.x = train.x;
        model.ySVR = train.ySVR;
        model.l = train.l;
        model.n = train.n;
        //Main iteration:
        while (pass < maxpass) {
            alpha_change = 0;
            for (i = 0; i < train.l; i++) {
                Ei = svmTestOne(train.x[i]) - train.ySVR[i];
                if ((train.ySVR[i] * Ei < -tol && model.alpha[i] < C) || (train.ySVR[i] * Ei > tol && model.alpha[i] > 0)) {
                    j = (int) Math.floor(Math.random() * (train.l - 1));
                    j = (j < i) ? j : (j + 1);
                    Ej = svmTestOne(train.x[j]) - train.ySVR[j];
                    ai_old = model.alpha[i];
                    aj_old = model.alpha[j];
                    double kij = kernel(i, j);
                    double kii = kernel(i, i);
                    double kjj = kernel(j, j);
                    eta = 2 * kij - kii - kjj;
                    if (eta >= 0) //next i
                    {
                        continue;
                    }
                    model.alpha[j] = aj_old - (train.ySVR[j] * (Ei - Ej)) / eta;
                    if (model.alpha[j] > 0) {
                        model.alpha[j] = 0;
                    } else if (model.alpha[j] < 1) {
                        model.alpha[j] = 1;
                    }
                    if (Math.abs(model.alpha[j] - aj_old) < tol2) //next i
                    {
                        continue;
                    }
                    model.alpha[i] = ai_old + train.ySVR[i] * train.ySVR[j] * (aj_old - model.alpha[j]);
                    computeBias(model.alpha[i], model.alpha[j], train.ySVR[i], train.ySVR[j], kii, kjj, kij);
                    alpha_change++;
                }
            }
            if (alpha_change == 0) {
                pass++;
            } else {
                pass = 0;
            }
            if (alpha_change > 0) {
                System.out.print(".");
            } else {
                System.out.print("*");
            }
        }
        System.out.println();
    }
    /* =====================================================
     * ===================================================== */

    private double smoObj(double aj, int yi, int yj, double kij, double kii, double kjj, double vi, double vj) {
        double s = yi * yj;
        double gamma = ai_old + s * aj_old;
        return (gamma + (1 - s) * aj - 0.5 * kii * (gamma - s * aj) * (gamma - s * aj) - 0.5 * kjj * aj * aj
                + -s * kij * (gamma - s * aj) * aj - yi * (gamma - s * aj) * vi - yj * aj * vj);
    }

    /**
     * SMO type algoritm by Keerthi et al. (2001)
     *
     * @param train Training set
     * @param p Parameters
     */
    private void SMO_Keerthi(Problem train, KernelParams p) {
        int pass = 0;
        int alpha_change = 0;
        int examineAll = 1;
        int i, j;
        double eta;
        //Initialize:
        model = new Model();
        model.alpha = new double[train.l];
        model.b = 0;
        model.params = p;
        model.x = train.x;
        model.y = train.y;
        model.l = train.l;
        model.n = train.n;
        //Initialize the sets Ii:
        I0 = new ArrayList<Integer>();
        I1 = new ArrayList<Integer>();
        I2 = new ArrayList<Integer>();
        I3 = new ArrayList<Integer>();
        I4 = new ArrayList<Integer>();
        for (i = 0; i < train.l; i++) {
            addtoSet(i, model.alpha[i], model.y[i]);
        }
        //Main iteration:
        System.out.println();
        while (alpha_change > 0 || examineAll == 1) {
            alpha_change = 0;
            if (examineAll == 1) {
                for (i = 0; i < train.l; i++) {

                }
            } else {

            }
            if (examineAll == 1) {
                examineAll = 0;
            } else if (alpha_change == 0) {
                examineAll = 1;
            }
        }
    }

    private int takeStep(int i, int j) {
        double kii, kjj, kij;
        double ai, aj;
        double gamma;
        double s;
        if (i == j) {
            return 0;
        }
        Ei = svmTestOne(model.x[i]) - model.y[i];
        Ej = svmTestOne(model.x[j]) - model.y[j];
        ai_old = model.alpha[i];
        aj_old = model.alpha[j];
        L = computeL(model.y[i], model.y[j]);
        H = computeH(model.y[i], model.y[j]);
        if (L == H) {
            return 0;
        }
        kij = kernel(i, j);
        kii = kernel(i, i);
        kjj = kernel(j, j);
        s = model.y[i] * model.y[j];
        gamma = ai_old + s * aj_old;
        eta = 2 * kij - kii - kjj;
        if (eta < 0) {
            aj = aj_old - (model.y[j] * (Ei - Ej)) / eta;
            if (aj > H) {
                aj = H;
            } else if (aj < L) {
                aj = L;
            }
        } else {
            double vi = 0, vj = 0;
            vi = svmTestOne(model.x[i]) - model.y[i] * ai_old * kii - model.y[j] * aj_old * kij;
            vj = svmTestOne(model.x[j]) - model.y[i] * ai_old * kij - model.y[j] * aj_old * kjj;
            double Lobj = (1 - s) * L - kii * (-gamma * s * L + 0.5 * L * L) - 0.5 * kjj * L * L
                    + +kij * L * L + model.y[i] * s * L * vi - model.y[j] * L * vj;
            double Hobj = (1 - s) * H - kii * (-gamma * s * H + 0.5 * H * H) - 0.5 * kjj * H * H
                    + +kij * H * H + model.y[i] * s * H * vi - model.y[j] * H * vj;
            if (Lobj > Hobj + tol4) {
                aj = L;
            } else if (Lobj < Hobj - tol4) {
                aj = H;
            } else {
                aj = aj_old;
            }
        }
        if (Math.abs(aj - aj_old) < tol4 * (aj + aj_old + tol4)) {
            return 0;
        }
        ai = ai_old + s * (aj_old - aj);
        computeBias(ai, aj, model.y[i], model.y[j], kii, kjj, kij);
        model.alpha[i] = ai;
        model.alpha[j] = aj;
        updateISets(i, j);
        computeUpLow();
        return 1;
    }

    private void updateISets(int i, int j) {
        I0.remove((Integer) i);
        I0.remove((Integer) j);
        I1.remove((Integer) i);
        I1.remove((Integer) j);
        I2.remove((Integer) i);
        I2.remove((Integer) j);
        I3.remove((Integer) i);
        I3.remove((Integer) j);
        addtoSet(i, model.alpha[i], model.y[i]);
        addtoSet(j, model.alpha[j], model.y[j]);
    }

    private void addtoSet(int i, double a, int y) {
        if (a > 0 && a < C) {
            I0.add(i);
        } else if (y == 1 && a == 0) {
            I1.add(i);
        } else if (y == -1 && a == C) {
            I2.add(i);
        } else if (y == 1 && a == C) {
            I3.add(i);
        } else //if (y == -1 && a == 0)
        {
            I4.add(i);
        }
    }

    private void computeUpLow() {
        boolean firstup = true;
        boolean firstlow = true;
        double s = 0;
        for (int i = 0; i < model.alpha.length; i++) {
            if ((model.alpha[i] > 0 && model.alpha[i] < C) || (model.alpha[i] == 0 && model.y[i] == 1) || (model.alpha[i] == C && model.y[i] == -1)) {
                //Iup
                s = svmTestOne(model.x[i]) - model.y[i];
                if (firstup) {
                    bup = s;
                    iup = i;
                    firstup = false;
                } else {
                    if (s > bup) {
                        bup = s;
                        iup = i;
                    }
                }
            } else if ((model.alpha[i] > 0 && model.alpha[i] < C) || (model.alpha[i] == 0 && model.y[i] == -1) || (model.alpha[i] == C && model.y[i] == 1)) {
                //Ilow
                s = svmTestOne(model.x[i]) - model.y[i];
                if (firstlow) {
                    blow = s;
                    ilow = i;
                    firstlow = false;
                } else {
                    if (s < blow) {
                        blow = s;
                        ilow = i;
                    }
                }
            }
        }
    }

    /**
     * Computes L
     *
     * @param yi
     * @param yj
     * @return Returns L
     */
    private double computeL(double yi, double yj) {
        double L = 0;
        if (yi != yj) {
            L = Math.max(0, -ai_old + aj_old);
        } else {
            L = Math.max(0, ai_old + aj_old - C);
        }
        return L;
    }

    /**
     * Computes H
     *
     * @param yi
     * @param yj
     * @return Returns H
     */
    private double computeH(int yi, int yj) {
        double H = 0;
        if (yi != yj) {
            H = Math.min(C, -ai_old + aj_old + C);
        } else {
            H = Math.min(C, ai_old + aj_old);
        }
        return H;
    }

    /**
     * Computes the bias and stores in model.b
     *
     * @param ai
     * @param aj
     * @param yi
     * @param yj
     * @param kii
     * @param kjj
     * @param kij
     */
    private void computeBias(double ai, double aj, double yi, double yj, double kii, double kjj, double kij) {
        double b1 = model.b - Ei - yi * (ai - ai_old) * kii - yj * (aj - aj_old) * kij;
        double b2 = model.b - Ej - yi * (ai - ai_old) * kij - yj * (aj - aj_old) * kjj;
        if (0 < ai && ai < C) {
            model.b = b1;
        } else if (0 < aj && aj < C) {
            model.b = b2;
        } else {
            model.b = (b1 + b2) / 2;
        }
    }

    /**
     * Test a whole data set
     *
     * @param test The test data
     * @return An array of -1 and 1's
     */
    public int[] svmTest(Problem test) {
        if (test == null) {
            return null;
        }
        int[] pred = new int[test.l];
        for (int i = 0; i < test.l; i++) {
            pred[i] = (svmTestOne(test.x[i]) < 0 ? -1 : 1);
        }
        return pred;
    }

    public double getLamda() {
        return lamda;
    }

    public void setLamda(double lamda) {
        this.lamda = lamda;
    }

    /**
     * Test one example
     *
     * @param x The test example
     * @return Class of x: -1 or 1
     */
    public double svmTestOne(FeatureNode[] x) {
        double f = 0;
        for (int i = 0; i < model.l; i++) {
//            System.out.println("kernel : " + kernel(x, model.x[i]));
            f += (model.alphaStar[i] - model.alpha[i]) * ((kernel(x, model.x[i])) + (lamda * lamda));
        }
        return f;
    }

    /**
     * Based on the kernel parameters/settings of the model, calculates the
     * kernel value between two points
     *
     * @param x First point/vector
     * @param z Second point/vector
     * @return Kernel value between x and z
     */
    private double kernel(FeatureNode[] x, FeatureNode[] z) {
        double ret = 0;
        if (model.params == null) {
            model.params = new KernelParams(1, 1, 1, 1);
        }
        switch (model.params.kernel) {
            case 0: //user defined
                break;
            case 1: //linear
                ret = Kernel.kLinear(x, z);
                break;
            case 2: //polynomial
                ret = Kernel.kPoly(x, z, model.params.a, model.params.b, model.params.c);
                break;
            case 3: //gaussian
                ret = Kernel.kGaussian(x, z, model.params.a);
                break;
            case 4: //tanh
                ret = Kernel.kTanh(x, z, model.params.a, model.params.b);
                break;
        }
        return ret;
    }

    /**
     * Based on the kernel parameters/settings of the model, calculates the
     * kernel value between two points
     *
     * @param i Index of the first vector in model.x
     * @param j Index of the second vector in model.x
     * @return Kernel value between model.x[i] and model.x[j]
     */
    private double kernel(int i, int j) {
        double ret = 0;
        if (model.params == null) {
            model.params = new KernelParams(1, 1, 1, 1);
        }
        switch (model.params.kernel) {
            case 0: //user defined
                break;
            case 1: //linear
                ret = Kernel.kLinear(model.x[i], model.x[j]);
                break;
            case 2: //polynomial
                ret = Kernel.kPoly(model.x[i], model.x[j], model.params.a, model.params.b, model.params.c);
                break;
            case 3: //gaussian
                ret = Kernel.kGaussian(model.x[i], model.x[j], model.params.a);
                break;
            case 4: //tanh
                ret = Kernel.kTanh(model.x[i], model.x[j], model.params.a, model.params.b);
                break;
        }
        return ret;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model m) {
        model = m;
    }

    public double getC() {
        return C;
    }

    public void setC(double C) {
        this.C = C;
    }

    public double getTolerance() {
        return tol;
    }

    public void setTolerance(double tol) {
        this.tol = tol;
    }

    public double getTolerance2() {
        return tol2;
    }

    public void setTolerance2(double tol2) {
        this.tol2 = tol2;
    }

    public int getMaxPass() {
        return maxpass;
    }

    public void setMaxPass(int p) {
        maxpass = p;
    }

    public void setParameters(double C, double tol, double tol2, int maxpass) {
        this.C = C;
        this.tol = tol;
        this.tol2 = tol2;
        this.maxpass = maxpass;
    }
}
