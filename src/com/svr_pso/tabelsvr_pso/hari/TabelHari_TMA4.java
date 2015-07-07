/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.svr_pso.tabelsvr_pso.hari;

import com.svr_pso.tabelsvr_pso.jam.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author nuhuyanan
 */

public class TabelHari_TMA4 extends AbstractTableModel {

    private List<Hari_TMA4> list = new ArrayList<Hari_TMA4>();

    @Override
    public int getRowCount() {
        return list.size();
    }

    @Override
    public int getColumnCount() {
        return 10;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return list.get(rowIndex).getNO();
            case 1:
                return list.get(rowIndex).getHARI();
            case 2:
                return list.get(rowIndex).getHARI();
            case 3:
                return list.get(rowIndex).getBULAN();
            case 4:
                return list.get(rowIndex).getTAHUN();
            case 5:
                return list.get(rowIndex).getFITUR1();
            case 6:
                return list.get(rowIndex).getFITUR2();
            case 7:
                return list.get(rowIndex).getFITUR3();
            case 8:
                return list.get(rowIndex).getFITUR4();
            case 9:
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
                return "HARI";
            case 3:
                return "BULAN";
            case 4:
                return "TAHUN";
            case 5:
                return "FITUR1";
            case 6:
                return "FITUR2";
            case 7:
                return "FITUR3";
            case 8:
                return "FITUR4";
            case 9:
                return "Tinggi Muka Air (TMA)";
            default:
                return null;
        }
    }

    public void add(Hari_TMA4 data) {
        list.add(data);
        fireTableRowsInserted(getRowCount(), getColumnCount());
    }

    public void delete(int i, int baris) {
        list.remove(i);
        fireTableRowsDeleted(i, baris);
    }

    public Hari_TMA4 get(int baris) {
        return (Hari_TMA4) list.get(baris);
    }
}
