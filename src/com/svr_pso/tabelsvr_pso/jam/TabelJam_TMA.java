/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.svr_pso.tabelsvr_pso.jam;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author nuhuyanan
 */

public class TabelJam_TMA extends AbstractTableModel {

    private List<Jam_TMA> list = new ArrayList<Jam_TMA>();

    @Override
    public int getRowCount() {
        return list.size();
    }

    @Override
    public int getColumnCount() {
        return 6;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return list.get(rowIndex).getNO();
            case 1:
                return list.get(rowIndex).getJAM();
            case 2:
                return list.get(rowIndex).getHARI();
            case 3:
                return list.get(rowIndex).getBULAN();
            case 4:
                return list.get(rowIndex).getTAHUN();
            case 5:
                return list.get(rowIndex).getTMA();
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int kolom) {
        switch (kolom) {
            case 0:
                return "NO";
            case 1:
                return "JAM";
            case 2:
                return "HARI";
            case 3:
                return "BULAN";
            case 4:
                return "TAHUN";
            case 5:
                return "Tinggi Muka Air (TMA)";
            default:
                return null;
        }
    }

    public void add(Jam_TMA data) {
        list.add(data);
        fireTableRowsInserted(getRowCount(), getColumnCount());
    }

    public void delete(int i, int baris) {
        list.remove(i);
        fireTableRowsDeleted(i, baris);
    }

    public Jam_TMA get(int baris) {
        return (Jam_TMA) list.get(baris);
    }
}
