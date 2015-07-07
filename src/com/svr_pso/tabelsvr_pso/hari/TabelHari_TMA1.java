/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.svr_pso.tabelsvr_pso.hari;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author nuhuyanan
 */

public class TabelHari_TMA1 extends AbstractTableModel {

    private List<Hari_TMA1> list = new ArrayList<Hari_TMA1>();

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
                return list.get(rowIndex).getHARI();
            case 2:
                return list.get(rowIndex).getBULAN();
            case 3:
                return list.get(rowIndex).getTAHUN();
            case 4:
                return list.get(rowIndex).getFITUR1();
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
                return "HARI";
            case 2:
                return "BULAN";
            case 3:
                return "TAHUN";
            case 4:
                return "FITUR 1";
            case 5:
                return "Tinggi Muka Air (TMA)";
            default:
                return null;
        }
    }

    public void add(Hari_TMA1 data) {
        list.add(data);
        fireTableRowsInserted(getRowCount(), getColumnCount());
    }

    public void delete(int i, int baris) {
        list.remove(i);
        fireTableRowsDeleted(i, baris);
    }

    public Hari_TMA1 get(int baris) {
        return (Hari_TMA1) list.get(baris);
    }
}
