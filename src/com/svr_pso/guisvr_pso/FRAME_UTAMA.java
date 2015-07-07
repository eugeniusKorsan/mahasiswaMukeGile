/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.svr_pso.guisvr_pso;

import com.svr_pso.koneksisvr_pso.connect;
import com.svr_pso.prosessvr_pso.pso.Posisi;
import com.svr_pso.prosessvr_pso.svr.KernelParams;
import com.svr_pso.tabelsvr_pso.hari.Hari_TMA;
import com.svr_pso.tabelsvr_pso.jam.Jam_TMA;
import com.svr_pso.tabelsvr_pso.minggu.Minggu_TMA;
import com.svr_pso.tabelsvr_pso.hari.TabelHari_TMA;
import com.svr_pso.tabelsvr_pso.jam.*;
import com.svr_pso.tabelsvr_pso.minggu.TabelMinggu_TMA;
import com.svr_pso.prosessvr_pso.svr.Problem;
import com.svr_pso.prosessvr_pso.svr.SVM;
import com.svr_pso.tabelsvr_pso.jam.*;
import com.svr_pso.tabelsvr_pso.hari.*;
import com.svr_pso.tabelsvr_pso.minggu.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.UIManager;

/**
 *
 * @author eugenius
 */
public class FRAME_UTAMA extends javax.swing.JFrame {
    
    connect con = new connect();
    TabelJam_TMA tabJam_TMA;
    TabelHari_TMA tabHari_TMA;
    TabelMinggu_TMA tabMinggu_TMA;
    int fitur;
    String data;
    public final double LOC_SIGMA_LOW = 0.0009;
    public final double LOC_SIGMA_HIGH = 9;
    public final double LOC_EPSILON_LOW = 1e-10;
    public final double LOC_EPSILON_HIGH = 1e-6;
    public final double LOC_C_LOW = 1;
    public final double LOC_C_HIGH = 100;
    public final double LOC_LAMDA_LOW = 1.11;
    public final double LOC_LAMDA_HIGH = 6.66;
    public final double LOC_LR_LOW = 1e-2;
    public final double LOC_LR_HIGH = 1e-6;
    public final double VEL_LOW = -1;
    public final double VEL_HIGH = 1;
    public final double ERR_TOLERANCE = 1E-20;
    //===============================
//    static TabelTabulasi2Fitur tTab2Fitur;
//    static TabelTabulasi3Fitur tTab3Fitur;
//    static TabelTabulasi4Fitur tTab4Fitur;
//
//    static TabelPengujianTabulasi2Fitur tPeng2Fitur;
//    static TabelPengujianTabulasi3Fitur tPeng3Fitur;
//    static TabelPengujianTabulasi4Fitur tPeng4Fitur;
//
//    static TabelTMA TMA;
//    static TabelTabulasi tab;
//
//    static TabelTMASVRPelatihan TMASVRPelatihan;
//    static TabelTMASVRPengujian TMASVRPengujian;
//    static TabelTMASVRHasil TMASVRHasil;
//    static TabelAlphaSVRLatih tabelAlpha;
    double timeValue;
    JSpinner.DateEditor de, de2, de3, de4;
    long totalJumlah;
    
    static Object[][] dataArrTMA;
    static Object[][] dataArrTMABaru;
    double[] TimeValue;
    static double MSE = 0;
    int dtTMA = 0, dtTimVal = 0;
    //=========================//
    int dtTMABaru = 0;
    static Problem probR;
    static KernelParams kernelR;
    static SVM svmR = new SVM();
    static int totDataLatih = 0;
    static int jumHariDlm1Bln = 0;
    static int totDataUji = 0;
    
    TabelJam_TMA1 tabJam_TMA1;
    TabelJam_TMA2 tabJam_TMA2;
    TabelJam_TMA3 tabJam_TMA3;
    TabelJam_TMA4 tabJam_TMA4;
    TabelJam_TMA5 tabJam_TMA5;
    
    TabelHari_TMA1 tabHari_TMA1;
    TabelHari_TMA2 tabHari_TMA2;
    TabelHari_TMA3 tabHari_TMA3;
    TabelHari_TMA4 tabHari_TMA4;
    TabelHari_TMA5 tabHari_TMA5;
    
    TabelMinggu_TMA1 tabMinggu_TMA1;
    TabelMinggu_TMA2 tabMinggu_TMA2;
    TabelMinggu_TMA3 tabMinggu_TMA3;
    TabelMinggu_TMA4 tabMinggu_TMA4;
    TabelMinggu_TMA5 tabMinggu_TMA5;

    /**
     * Creates new form FRAME_UTAMA
     */
    public FRAME_UTAMA() {
        initComponents();
        tampilJam();
        tampilHari();
        tampilMinggu();
        
        jTabbedPane4.setEnabledAt(0, false);
        jTabbedPane4.setEnabledAt(1, false);
        jTabbedPane4.setEnabledAt(2, false);
    }
    
    public void tampilJam() {
        int no = 0;
        tabJam_TMA = new TabelJam_TMA();
        try {
            ResultSet rs = con.getTable("select * from data_jam order by no asc  ");
            while (rs.next()) {
                Jam_TMA jam_tma = new Jam_TMA();
                jam_tma.setNO(no + 1);
                jam_tma.setJAM(rs.getInt(6));
                jam_tma.setHARI(rs.getInt(7));
                jam_tma.setBULAN(rs.getInt(8));
                jam_tma.setTAHUN(rs.getInt(9));
                jam_tma.setTMA(rs.getDouble(4));
                tabJam_TMA.add(jam_tma);
                no++;
            }
        } catch (SQLException ex) {
            System.err.print(ex);
        }
        tbljamsvr_pso.setModel(tabJam_TMA);
    }
    
    public void tampilMinggu() {
        int no = 0;
        tabMinggu_TMA = new TabelMinggu_TMA();
        try {
            ResultSet rs = con.getTable("select * from data_minggu order by no asc  ");
            while (rs.next()) {
                Minggu_TMA minggu_tma = new Minggu_TMA();
                minggu_tma.setNO(no + 1);
                minggu_tma.setMINGGU(rs.getInt(2));
                minggu_tma.setTAHUN(rs.getInt(3));
                minggu_tma.setTMA(rs.getDouble(4));
                tabMinggu_TMA.add(minggu_tma);
                no++;
            }
        } catch (SQLException ex) {
            System.err.print(ex);
        }
        tblminggusvr_pso.setModel(tabMinggu_TMA);
    }
    
    public void tampilHari() {
        int no = 0;
        tabHari_TMA = new TabelHari_TMA();
        try {
            ResultSet rs = con.getTable("select * from data_hari order by no asc  ");
            while (rs.next()) {
                Hari_TMA hari_tma = new Hari_TMA();
                hari_tma.setNO(no + 1);
                hari_tma.setHARI(rs.getInt(2));
                hari_tma.setBULAN(rs.getInt(3));
                hari_tma.setTAHUN(rs.getInt(4));
                hari_tma.setTMA(rs.getDouble(5));
                tabHari_TMA.add(hari_tma);
                no++;
            }
        } catch (SQLException ex) {
            System.err.print(ex);
        }
        tblharisvr_pso.setModel(tabHari_TMA);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbljamsvr_pso = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblharisvr_pso = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblminggusvr_pso = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jTabbedPane3 = new javax.swing.JTabbedPane();
        jPanel6 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        comboData = new javax.swing.JComboBox();
        comboFitur = new javax.swing.JComboBox();
        jTabbedPane4 = new javax.swing.JTabbedPane();
        jPanel12 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        tblPelatihanJam = new javax.swing.JTable();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblPengujianJam = new javax.swing.JTable();
        jPanel13 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        tblPelatihanHari = new javax.swing.JTable();
        jPanel16 = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        tblPengujianHari = new javax.swing.JTable();
        jPanel14 = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        jScrollPane9 = new javax.swing.JScrollPane();
        tblPelatihanMinggu = new javax.swing.JTable();
        jPanel18 = new javax.swing.JPanel();
        jScrollPane10 = new javax.swing.JScrollPane();
        tblPengujianMinggu = new javax.swing.JTable();
        jPanel19 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jPanel20 = new javax.swing.JPanel();
        jProgressBar1 = new javax.swing.JProgressBar();
        jLabel1 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setLayout(new java.awt.BorderLayout());

        jTabbedPane2.setTabPlacement(javax.swing.JTabbedPane.LEFT);

        jPanel2.setLayout(new java.awt.BorderLayout());

        tbljamsvr_pso.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tbljamsvr_pso);

        jPanel2.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jTabbedPane2.addTab("Jam", jPanel2);

        jPanel3.setLayout(new java.awt.BorderLayout());

        tblharisvr_pso.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(tblharisvr_pso);

        jPanel3.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        jTabbedPane2.addTab("Hari", jPanel3);

        jPanel4.setLayout(new java.awt.BorderLayout());

        tblminggusvr_pso.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane3.setViewportView(tblminggusvr_pso);

        jPanel4.add(jScrollPane3, java.awt.BorderLayout.CENTER);

        jTabbedPane2.addTab("Minggu", jPanel4);

        jPanel1.add(jTabbedPane2, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("Data TMA", jPanel1);

        jPanel5.setLayout(new java.awt.BorderLayout());

        jTabbedPane3.setTabPlacement(javax.swing.JTabbedPane.LEFT);

        jButton1.setText("Proses");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder("Grafik"));
        jPanel10.setLayout(new java.awt.BorderLayout());

        jPanel11.setBackground(new java.awt.Color(255, 153, 102));
        jPanel11.setForeground(new java.awt.Color(255, 102, 102));
        jPanel11.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel11MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 599, Short.MAX_VALUE)
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 195, Short.MAX_VALUE)
        );

        jPanel10.add(jPanel11, java.awt.BorderLayout.CENTER);

        jLabel2.setText("Fitur :");

        jLabel3.setText("Data :");

        comboData.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "==Pilih==", "Jam", "Hari", "Minggu" }));

        comboFitur.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "==Pilih==", "1 Fitur", "2 Fitur", "3 Fitur", "4 Fitur", "5 Fitur" }));

        jPanel12.setLayout(new javax.swing.BoxLayout(jPanel12, javax.swing.BoxLayout.LINE_AXIS));

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder("Pelatihan"));
        jPanel9.setLayout(new java.awt.BorderLayout());

        tblPelatihanJam.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tblPelatihanJam.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jScrollPane6.setViewportView(tblPelatihanJam);

        jPanel9.add(jScrollPane6, java.awt.BorderLayout.CENTER);

        jPanel12.add(jPanel9);

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("Pengujian"));
        jPanel8.setLayout(new java.awt.BorderLayout());

        tblPengujianJam.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tblPengujianJam.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jScrollPane4.setViewportView(tblPengujianJam);

        jPanel8.add(jScrollPane4, java.awt.BorderLayout.CENTER);

        jPanel12.add(jPanel8);

        jTabbedPane4.addTab("Jam", jPanel12);

        jPanel13.setLayout(new javax.swing.BoxLayout(jPanel13, javax.swing.BoxLayout.LINE_AXIS));

        jPanel15.setBorder(javax.swing.BorderFactory.createTitledBorder("Pelatihan"));
        jPanel15.setLayout(new java.awt.BorderLayout());

        tblPelatihanHari.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tblPelatihanHari.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jScrollPane7.setViewportView(tblPelatihanHari);

        jPanel15.add(jScrollPane7, java.awt.BorderLayout.CENTER);

        jPanel13.add(jPanel15);

        jPanel16.setBorder(javax.swing.BorderFactory.createTitledBorder("Pengujian"));
        jPanel16.setLayout(new java.awt.BorderLayout());

        tblPengujianHari.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tblPengujianHari.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jScrollPane8.setViewportView(tblPengujianHari);

        jPanel16.add(jScrollPane8, java.awt.BorderLayout.CENTER);

        jPanel13.add(jPanel16);

        jTabbedPane4.addTab("Hari", jPanel13);

        jPanel14.setLayout(new javax.swing.BoxLayout(jPanel14, javax.swing.BoxLayout.LINE_AXIS));

        jPanel17.setBorder(javax.swing.BorderFactory.createTitledBorder("Pelatihan"));
        jPanel17.setLayout(new java.awt.BorderLayout());

        tblPelatihanMinggu.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tblPelatihanMinggu.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jScrollPane9.setViewportView(tblPelatihanMinggu);

        jPanel17.add(jScrollPane9, java.awt.BorderLayout.CENTER);

        jPanel14.add(jPanel17);

        jPanel18.setBorder(javax.swing.BorderFactory.createTitledBorder("Pengujian"));
        jPanel18.setLayout(new java.awt.BorderLayout());

        tblPengujianMinggu.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tblPengujianMinggu.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jScrollPane10.setViewportView(tblPengujianMinggu);

        jPanel18.add(jScrollPane10, java.awt.BorderLayout.CENTER);

        jPanel14.add(jPanel18);

        jTabbedPane4.addTab("Minggu", jPanel14);

        jPanel19.setBorder(javax.swing.BorderFactory.createTitledBorder("Info"));
        jPanel19.setLayout(new java.awt.BorderLayout());

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jTextArea1.setText("nanti pada area grafik dapat di klik \ndan akan manampilkan grafik secara\nFULL SCREEN.\n\ndan pada area TextArea akan menampilkan \ninformasi penting dari kedua tabel di atas\n\ntabel jam, hari, minggu.......???????\n??????////?????///?????/////?????/////???//\n???////??/??///????//???/??/???/");
        jScrollPane5.setViewportView(jTextArea1);

        jPanel19.add(jScrollPane5, java.awt.BorderLayout.CENTER);

        jButton2.setText("Batal");

        jButton3.setText("Simpan");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(comboData, 0, 78, Short.MAX_VALUE)
                            .addComponent(comboFitur, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPane4)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, 316, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton3)
                .addGap(33, 33, 33))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(comboData, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(comboFitur, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(37, 37, 37)
                .addComponent(jButton1)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addComponent(jTabbedPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jButton3))
                .addContainerGap())
        );

        jTabbedPane3.addTab("Pelatihan", jPanel6);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1055, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 498, Short.MAX_VALUE)
        );

        jTabbedPane3.addTab("Uji Coba", jPanel7);

        jPanel5.add(jTabbedPane3, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("Pengujian", jPanel5);

        jLabel1.setText("Info Panel");

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel20Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jMenu1.setText("File");
        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        //        proses di sini akan menampilkan semua (alpha star dan alpha serta MSE) hasil terbaik dari proses PSO-SVR
        tblPelatihanJam.removeAll();
        tblPelatihanHari.removeAll();
        tblPelatihanMinggu.removeAll();
        
        ambilData();
        System.out.println(fitur);
        System.out.println(data);
//pengaturan untuk enable tidaknya tab         
        if (data == "Jam") {
            jTabbedPane4.setSelectedIndex(0);
            jTabbedPane4.setEnabledAt(1, false);
            jTabbedPane4.setEnabledAt(2, false);
        } else if (data == "Hari") {
            jTabbedPane4.setSelectedIndex(1);
            jTabbedPane4.setEnabledAt(0, false);
            jTabbedPane4.setEnabledAt(2, false);
        } else if (data == "Minggu") {
            jTabbedPane4.setSelectedIndex(2);
            jTabbedPane4.setEnabledAt(0, false);
            jTabbedPane4.setEnabledAt(1, false);
        }
////////////////////////tampil jam di tabel
        if (fitur == 1 && data == "Jam") {
            Thread th = new Thread(new Runnable() {
                @Override
                public void run() {
                    tampilJamFitur1();
                    
                }
            });
            th.start();
        } else if (fitur == 2 && data == "Jam") {
            tampilJamFitur2();
        } else if (fitur == 3 && data == "Jam") {
            tampilJamFitur3();
        } else if (fitur == 4 && data == "Jam") {
            tampilJamFitur4();
        } else if (fitur == 5 && data == "Jam") {
            tampilJamFitur5();
        }
//   tampil hari di tabel     
        if (fitur == 1 && data == "Hari") {
            tampilHariFitur1();
        } else if (fitur == 2 && data == "Hari") {
            tampilHariFitur2();
        } else if (fitur == 3 && data == "Hari") {
            tampilHariFitur3();
        } else if (fitur == 4 && data == "Hari") {
            tampilHariFitur4();
        } else if (fitur == 5 && data == "Hari") {
            tampilHariFitur5();
        }
//   tampil minggu di tabel     
        if (fitur == 1 && data == "Minggu") {
            
            tampilMingguFitur1();
        } else if (fitur == 2 && data == "Minggu") {
            tampilMingguFitur2();
        } else if (fitur == 3 && data == "Minggu") {
            tampilMingguFitur3();
        } else if (fitur == 4 && data == "Minggu") {
            tampilMingguFitur4();
        } else if (fitur == 5 && data == "Minggu") {
            tampilMingguFitur5();
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jPanel11MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel11MouseClicked
        // membuat event klik event grafik di perbesar


    }//GEN-LAST:event_jPanel11MouseClicked
////////////////////TAMPIL JAM/////////////////////

    public void tampilJamFitur1() {
        int no = 0;
        tabJam_TMA1 = new TabelJam_TMA1();
        try {
            ResultSet rs = con.getTable("select * from dt_tab_jam order by no asc limit 0,20000  ");
            while (rs.next()) {
                try {
                    Thread.sleep(10);
                    Jam_TMA1 jam_tma1 = new Jam_TMA1();
                    jam_tma1.setNO(no + 1);
                    jam_tma1.setJAM(rs.getInt(2));
                    jam_tma1.setHARI(rs.getInt(3));
                    jam_tma1.setBULAN(rs.getInt(4));
                    jam_tma1.setTAHUN(rs.getInt(5));
                    jam_tma1.setFITUR1(rs.getDouble(6));
                    jam_tma1.setTMA(rs.getDouble(7));
                    tabJam_TMA1.add(jam_tma1);
                    no++;
                } catch (InterruptedException x) {
                    
                }
            }
        } catch (SQLException ex) {
            System.err.print(ex);
        }
        tblPelatihanJam.setModel(tabJam_TMA1);
    }
    
    public void tampilJamFitur2() {
        int no = 0;
        tabJam_TMA2 = new TabelJam_TMA2();
        try {
            ResultSet rs = con.getTable("select * from dt_tab_jam order by no asc limit 0,75 ");
            while (rs.next()) {
                Jam_TMA2 jam_tma2 = new Jam_TMA2();
                jam_tma2.setNO(no + 1);
                jam_tma2.setJAM(rs.getInt(2));
                jam_tma2.setHARI(rs.getInt(3));
                jam_tma2.setBULAN(rs.getInt(4));
                jam_tma2.setTAHUN(rs.getInt(5));
                jam_tma2.setFITUR1(rs.getDouble(6));
                jam_tma2.setFITUR2(rs.getDouble(7));
                jam_tma2.setTMA(rs.getDouble(8));
                tabJam_TMA2.add(jam_tma2);
                no++;
            }
        } catch (SQLException ex) {
            System.err.print(ex);
        }
        tblPelatihanJam.setModel(tabJam_TMA2);
    }
    
    public void tampilJamFitur3() {
        int no = 0;
        tabJam_TMA3 = new TabelJam_TMA3();
        try {
            ResultSet rs = con.getTable("select * from dt_tab_jam order by no asc limit 0,75 ");
            while (rs.next()) {
                Jam_TMA3 jam_tma3 = new Jam_TMA3();
                jam_tma3.setNO(no + 1);
                jam_tma3.setJAM(rs.getInt(2));
                jam_tma3.setHARI(rs.getInt(3));
                jam_tma3.setBULAN(rs.getInt(4));
                jam_tma3.setTAHUN(rs.getInt(5));
                jam_tma3.setFITUR1(rs.getDouble(6));
                jam_tma3.setFITUR2(rs.getDouble(7));
                jam_tma3.setFITUR3(rs.getDouble(8));
                jam_tma3.setTMA(rs.getDouble(9));
                tabJam_TMA3.add(jam_tma3);
                no++;
            }
        } catch (SQLException ex) {
            System.err.print(ex);
        }
        tblPelatihanJam.setModel(tabJam_TMA3);
    }
    
    public void tampilJamFitur4() {
        int no = 0;
        tabJam_TMA4 = new TabelJam_TMA4();
        try {
            ResultSet rs = con.getTable("select * from dt_tab_jam order by no asc limit 0,75 ");
            while (rs.next()) {
                Jam_TMA4 jam_tma4 = new Jam_TMA4();
                jam_tma4.setNO(no + 1);
                jam_tma4.setJAM(rs.getInt(2));
                jam_tma4.setHARI(rs.getInt(3));
                jam_tma4.setBULAN(rs.getInt(4));
                jam_tma4.setTAHUN(rs.getInt(5));
                jam_tma4.setFITUR1(rs.getDouble(6));
                jam_tma4.setFITUR2(rs.getDouble(7));
                jam_tma4.setFITUR3(rs.getDouble(8));
                jam_tma4.setFITUR4(rs.getDouble(9));
                jam_tma4.setTMA(rs.getDouble(10));
                tabJam_TMA4.add(jam_tma4);
                no++;
            }
        } catch (SQLException ex) {
            System.err.print(ex);
        }
        tblPelatihanJam.setModel(tabJam_TMA4);
    }
    
    public void tampilJamFitur5() {
        int no = 0;
        tabJam_TMA5 = new TabelJam_TMA5();
        try {
            ResultSet rs = con.getTable("select * from dt_tab_jam order by no asc limit 0,75 ");
            while (rs.next()) {
                Jam_TMA5 jam_tma5 = new Jam_TMA5();
                jam_tma5.setNO(no + 1);
                jam_tma5.setJAM(rs.getInt(2));
                jam_tma5.setHARI(rs.getInt(3));
                jam_tma5.setBULAN(rs.getInt(4));
                jam_tma5.setTAHUN(rs.getInt(5));
                jam_tma5.setFITUR1(rs.getDouble(6));
                jam_tma5.setFITUR2(rs.getDouble(7));
                jam_tma5.setFITUR3(rs.getDouble(8));
                jam_tma5.setFITUR4(rs.getDouble(9));
                jam_tma5.setFITUR5(rs.getDouble(10));
                jam_tma5.setTMA(rs.getDouble(11));
                tabJam_TMA5.add(jam_tma5);
                no++;
            }
        } catch (SQLException ex) {
            System.err.print(ex);
        }
        tblPelatihanJam.setModel(tabJam_TMA5);
    }
////////////////////TAMPIL HARI///////////////////

    public void tampilHariFitur1() {
        int no = 0;
        tabHari_TMA1 = new TabelHari_TMA1();
        try {
            ResultSet rs = con.getTable("select * from dt_tab_hari order by no asc limit 0,75  ");
            while (rs.next()) {
                Hari_TMA1 hari_tma1 = new Hari_TMA1();
                hari_tma1.setNO(no + 1);
                hari_tma1.setHARI(rs.getInt(2));
                hari_tma1.setBULAN(rs.getInt(3));
                hari_tma1.setTAHUN(rs.getInt(4));
                hari_tma1.setFITUR1(rs.getDouble(5));
                hari_tma1.setTMA(rs.getDouble(6));
                tabHari_TMA1.add(hari_tma1);
                no++;
            }
        } catch (SQLException ex) {
            System.err.print(ex);
        }
        tblPelatihanHari.setModel(tabHari_TMA1);
    }
    
    public void tampilHariFitur2() {
        int no = 0;
        tabHari_TMA2 = new TabelHari_TMA2();
        try {
            ResultSet rs = con.getTable("select * from dt_tab_hari order by no asc limit 0,75 ");
            while (rs.next()) {
                Hari_TMA2 hari_tma2 = new Hari_TMA2();
                hari_tma2.setNO(no + 1);
                hari_tma2.setHARI(rs.getInt(2));
                hari_tma2.setBULAN(rs.getInt(3));
                hari_tma2.setTAHUN(rs.getInt(4));
                hari_tma2.setFITUR1(rs.getDouble(5));
                hari_tma2.setFITUR2(rs.getDouble(6));
                hari_tma2.setTMA(rs.getDouble(7));
                tabHari_TMA2.add(hari_tma2);
                no++;
            }
        } catch (SQLException ex) {
            System.err.print(ex);
        }
        tblPelatihanHari.setModel(tabHari_TMA2);
    }
    
    public void tampilHariFitur3() {
        int no = 0;
        tabHari_TMA3 = new TabelHari_TMA3();
        try {
            ResultSet rs = con.getTable("select * from dt_tab_hari order by no asc limit 0,75 ");
            while (rs.next()) {
                Hari_TMA3 hari_tma3 = new Hari_TMA3();
                hari_tma3.setNO(no + 1);
                hari_tma3.setHARI(rs.getInt(2));
                hari_tma3.setBULAN(rs.getInt(3));
                hari_tma3.setTAHUN(rs.getInt(4));
                hari_tma3.setFITUR1(rs.getDouble(5));
                hari_tma3.setFITUR2(rs.getDouble(6));
                hari_tma3.setFITUR3(rs.getDouble(7));
                hari_tma3.setTMA(rs.getDouble(8));
                tabHari_TMA3.add(hari_tma3);
                no++;
            }
        } catch (SQLException ex) {
            System.err.print(ex);
        }
        tblPelatihanHari.setModel(tabHari_TMA3);
    }
    
    public void tampilHariFitur4() {
        int no = 0;
        tabHari_TMA4 = new TabelHari_TMA4();
        try {
            ResultSet rs = con.getTable("select * from dt_tab_hari order by no asc limit 0,75 ");
            while (rs.next()) {
                Hari_TMA4 hari_tma4 = new Hari_TMA4();
                hari_tma4.setNO(no + 1);
                hari_tma4.setHARI(rs.getInt(2));
                hari_tma4.setBULAN(rs.getInt(3));
                hari_tma4.setTAHUN(rs.getInt(4));
                hari_tma4.setFITUR1(rs.getDouble(5));
                hari_tma4.setFITUR2(rs.getDouble(6));
                hari_tma4.setFITUR3(rs.getDouble(7));
                hari_tma4.setFITUR4(rs.getDouble(8));
                hari_tma4.setTMA(rs.getDouble(9));
                tabHari_TMA4.add(hari_tma4);
                no++;
            }
        } catch (SQLException ex) {
            System.err.print(ex);
        }
        tblPelatihanJam.setModel(tabJam_TMA4);
    }
    
    public void tampilHariFitur5() {
        int no = 0;
        tabHari_TMA5 = new TabelHari_TMA5();
        try {
            ResultSet rs = con.getTable("select * from dt_tab_hari order by no asc limit 0,75 ");
            while (rs.next()) {
                Hari_TMA5 hari_tma5 = new Hari_TMA5();
                hari_tma5.setNO(no + 1);
                hari_tma5.setHARI(rs.getInt(2));
                hari_tma5.setBULAN(rs.getInt(3));
                hari_tma5.setTAHUN(rs.getInt(4));
                hari_tma5.setFITUR1(rs.getDouble(5));
                hari_tma5.setFITUR2(rs.getDouble(6));
                hari_tma5.setFITUR3(rs.getDouble(7));
                hari_tma5.setFITUR4(rs.getDouble(8));
                hari_tma5.setFITUR5(rs.getDouble(9));
                hari_tma5.setTMA(rs.getDouble(10));
                tabHari_TMA5.add(hari_tma5);
                no++;
            }
        } catch (SQLException ex) {
            System.err.print(ex);
        }
        tblPelatihanHari.setModel(tabHari_TMA5);
    }

    ////////////////////TAMPIL MINGGU///////////////////
    public void tampilMingguFitur1() {
        int no = 0;
        tabMinggu_TMA1 = new TabelMinggu_TMA1();
        try {
            ResultSet rs = con.getTable("select * from dt_tab_minggu order by no asc limit 0,75  ");
            while (rs.next()) {
                Minggu_TMA1 minggu_tma1 = new Minggu_TMA1();
                minggu_tma1.setNO(no + 1);
                minggu_tma1.setMINGGU(rs.getInt(2));
                minggu_tma1.setTAHUN(rs.getInt(3));
                minggu_tma1.setFITUR1(rs.getDouble(4));
                minggu_tma1.setTMA(rs.getDouble(5));
                tabMinggu_TMA1.add(minggu_tma1);
                no++;
            }
        } catch (SQLException ex) {
            System.err.print(ex);
        }
        tblPelatihanMinggu.setModel(tabMinggu_TMA1);
    }
    
    public void tampilMingguFitur2() {
        int no = 0;
        tabMinggu_TMA2 = new TabelMinggu_TMA2();
        try {
            ResultSet rs = con.getTable("select * from dt_tab_minggu order by no asc limit 0,75  ");
            while (rs.next()) {
                Minggu_TMA2 minggu_tma2 = new Minggu_TMA2();
                minggu_tma2.setNO(no + 1);
                minggu_tma2.setMINGGU(rs.getInt(2));
                minggu_tma2.setTAHUN(rs.getInt(3));
                minggu_tma2.setFITUR1(rs.getDouble(4));
                minggu_tma2.setFITUR2(rs.getDouble(5));
                minggu_tma2.setTMA(rs.getDouble(6));
                tabMinggu_TMA2.add(minggu_tma2);
                no++;
            }
        } catch (SQLException ex) {
            System.err.print(ex);
        }
        tblPelatihanMinggu.setModel(tabMinggu_TMA2);
    }
    
    public void tampilMingguFitur3() {
        int no = 0;
        tabMinggu_TMA3 = new TabelMinggu_TMA3();
        try {
            ResultSet rs = con.getTable("select * from dt_tab_minggu order by no asc limit 0,75  ");
            while (rs.next()) {
                Minggu_TMA3 minggu_tma3 = new Minggu_TMA3();
                minggu_tma3.setNO(no + 1);
                minggu_tma3.setMINGGU(rs.getInt(2));
                minggu_tma3.setTAHUN(rs.getInt(3));
                minggu_tma3.setFITUR1(rs.getDouble(4));
                minggu_tma3.setFITUR2(rs.getDouble(5));
                minggu_tma3.setFITUR3(rs.getDouble(6));
                minggu_tma3.setTMA(rs.getDouble(7));
                tabMinggu_TMA3.add(minggu_tma3);
                no++;
            }
        } catch (SQLException ex) {
            System.err.print(ex);
        }
        tblPelatihanMinggu.setModel(tabMinggu_TMA3);
    }
    
    public void tampilMingguFitur4() {
        int no = 0;
        tabMinggu_TMA4 = new TabelMinggu_TMA4();
        try {
            ResultSet rs = con.getTable("select * from dt_tab_minggu order by no asc limit 0,75  ");
            while (rs.next()) {
                Minggu_TMA4 minggu_tma4 = new Minggu_TMA4();
                minggu_tma4.setNO(no + 1);
                minggu_tma4.setMINGGU(rs.getInt(2));
                minggu_tma4.setTAHUN(rs.getInt(3));
                minggu_tma4.setFITUR1(rs.getDouble(4));
                minggu_tma4.setFITUR2(rs.getDouble(5));
                minggu_tma4.setFITUR3(rs.getDouble(6));
                minggu_tma4.setFITUR4(rs.getDouble(7));
                minggu_tma4.setTMA(rs.getDouble(8));
                tabMinggu_TMA4.add(minggu_tma4);
                no++;
            }
        } catch (SQLException ex) {
            System.err.print(ex);
        }
        tblPelatihanMinggu.setModel(tabMinggu_TMA4);
    }
    
    public void tampilMingguFitur5() {
        int no = 0;
        tabMinggu_TMA5 = new TabelMinggu_TMA5();
        try {
            ResultSet rs = con.getTable("select * from dt_tab_minggu order by no asc limit 0,75  ");
            while (rs.next()) {
                Minggu_TMA5 minggu_tma5 = new Minggu_TMA5();
                minggu_tma5.setNO(no + 1);
                minggu_tma5.setMINGGU(rs.getInt(2));
                minggu_tma5.setTAHUN(rs.getInt(3));
                minggu_tma5.setFITUR1(rs.getDouble(4));
                minggu_tma5.setFITUR2(rs.getDouble(5));
                minggu_tma5.setFITUR3(rs.getDouble(6));
                minggu_tma5.setFITUR4(rs.getDouble(7));
                minggu_tma5.setFITUR5(rs.getDouble(8));
                minggu_tma5.setTMA(rs.getDouble(9));
                tabMinggu_TMA5.add(minggu_tma5);
                no++;
            }
        } catch (SQLException ex) {
            System.err.print(ex);
        }
        tblPelatihanMinggu.setModel(tabMinggu_TMA5);
    }
    
    public void tampilDataLatihDataUji() {
        
    }
    
    public void ambilData() {
        if (comboData.getSelectedIndex() == 0 || comboFitur.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(null, "Silahkan melakukan proses pemilihan \n\tdata dan fitur secara tepat");
        } else {
            cekData();
            cekFitur();
        }
        
    }

//    public double evaluate(Posisi posisi) {
//        double result = 0;
//        double C = posisi.getLoc()[0]; // the "x" part of the location
//        double Lamda = posisi.getLoc()[1]; // the "y" part of the location
//        double LR = posisi.getLoc()[2]; // the "y" part of the location
//        double Sigma = posisi.getLoc()[3]; // the "y" part of the location
//        double Epsilon = posisi.getLoc()[4]; // the "y" part of the location
//
////        result = Math.pow(2.8125 - C + C * Math.pow(LR, 4), 2)//calc fitness
////                + Math.pow(2.25 - C + C * Math.pow(LR, 2), 2)
////                + Math.pow(1.5 - C + C * LR, 2);
//        tabelAlpha = new TabelAlphaSVRLatih();
//        probR = new Problem();
//        probR.l = dataArrTMABaru.length;
//        probR.n = getFitur();
////        System.out.println("n : " + probR.n);
//        probR.ySVR = new double[probR.l];
//        for (int i = 0; i < probR.l; i++) {
//            probR.ySVR[i] = Double.parseDouble(dataArrTMABaru[i][getFitur()] + "");
//
//        }
//        probR.x = new ProsesSVR.FeatureNode[probR.l][getFitur()];
//        for (int i = 0; i < probR.l; i++) {
//            for (int j = 0; j < getFitur(); j++) {
//                probR.x[i][j] = new ProsesSVR.FeatureNode(j + 1, Double.parseDouble(dataArrTMABaru[i][j] + ""));
//            }
//        }
//        double gamma = 1 / (2 * (Sigma * Sigma));
//        kernelR = new ProsesSVR.KernelParams(3, gamma, 1, 1);
//        svmR = new ProsesSVR.SVM();
//        svmR.setLamda(Lamda);
//        svmR.setEpsilon(Epsilon);
//        svmR.setC(C);
//        svmR.setMaxpass(1000);
//        svmR.setLR(LR);
//        svmR.svmTrain(probR, kernelR, 0);
//        double[] alpha = svmR.getModel().alpha;
//        double[] alphaStar = svmR.getModel().alphaStar;
//        for (int i = 0; i < probR.l; i++) {
//            AlphaSVRLatih aS = new AlphaSVRLatih();
//            aS.setDataKe((i + 1));
//            aS.setAlphaStar(alphaStar[i]);
//            aS.setAlpha(alpha[i]);
//            tabelAlpha.add(aS);
////            System.out.println("ALpha : " + (i + 1) + ": " + alpha[i]);
////            System.out.println("ALphaS : " + (i + 1) + ": " + alphaStar[i]);
//
//        }
////        setMSE(0);
//        tampilPelatihanSVR.setModel(tabelAlpha);
//        tampilDataUji(21980, 21990.95833, getFitur());
//        result = 1 / getMSE();
//        return result;
//
//    }
    public void cekData() {
        if (comboData.getSelectedIndex() == 1) {
            data = "Jam";
        } else if (comboData.getSelectedIndex() == 2) {
            data = "Hari";
        } else if (comboData.getSelectedIndex() == 3) {
            data = "Minggu";
        }
    }
    
    public void cekFitur() {
        if (comboFitur.getSelectedIndex() == 1) {
            fitur = 1;
        } else if (comboFitur.getSelectedIndex() == 2) {
            fitur = 2;
        } else if (comboFitur.getSelectedIndex() == 3) {
            fitur = 3;
        } else if (comboFitur.getSelectedIndex() == 4) {
            fitur = 4;
        } else if (comboFitur.getSelectedIndex() == 5) {
            fitur = 5;
        }
    }
    
    public void setFitur(int fitur) {
        this.fitur = fitur;
    }
    
    public static double getMSE() {
        return MSE;
    }
    
    public static void setMSE(double MSE) {
        FRAME_UTAMA.MSE = MSE;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(FRAME_UTAMA.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(FRAME_UTAMA.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(FRAME_UTAMA.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(FRAME_UTAMA.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
            UIManager.setLookAndFeel("com.jtattoo.plaf.luna.LunaLookAndFeel");
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FRAME_UTAMA.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FRAME_UTAMA.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FRAME_UTAMA.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FRAME_UTAMA.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FRAME_UTAMA().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox comboData;
    private javax.swing.JComboBox comboFitur;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTabbedPane jTabbedPane3;
    private javax.swing.JTabbedPane jTabbedPane4;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTable tblPelatihanHari;
    private javax.swing.JTable tblPelatihanJam;
    private javax.swing.JTable tblPelatihanMinggu;
    private javax.swing.JTable tblPengujianHari;
    private javax.swing.JTable tblPengujianJam;
    private javax.swing.JTable tblPengujianMinggu;
    private javax.swing.JTable tblharisvr_pso;
    private javax.swing.JTable tbljamsvr_pso;
    private javax.swing.JTable tblminggusvr_pso;
    // End of variables declaration//GEN-END:variables
}
