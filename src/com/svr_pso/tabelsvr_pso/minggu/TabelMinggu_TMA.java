/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.svr_pso.tabelsvr_pso.minggu;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author nuhuyanan
 */

public class TabelMinggu_TMA extends AbstractTableModel {

    private List<Minggu_TMA> list = new ArrayList<Minggu_TMA>();

    @Override
    public int getRowCount() {
        return list.size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return list.get(rowIndex).getNO();
            case 1:
                return list.get(rowIndex).getMINGGU();
            case 2:
                return list.get(rowIndex).getTAHUN();
            case 3:
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
                return "MINGGU";
            case 2:
                return "TAHUN";
            case 3:
                return "Tinggi Muka Air (TMA)";
            default:
                return null;
        }
    }

    public void add(Minggu_TMA data) {
        list.add(data);
        fireTableRowsInserted(getRowCount(), getColumnCount());
    }

    public void delete(int i, int baris) {
        list.remove(i);
        fireTableRowsDeleted(i, baris);
    }

    public Minggu_TMA get(int baris) {
        return (Minggu_TMA) list.get(baris);
    }
}
