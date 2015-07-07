/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.svr_pso.tabelsvr_pso.minggu;

import com.svr_pso.tabelsvr_pso.hari.*;
import com.svr_pso.tabelsvr_pso.jam.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author nuhuyanan
 */

public class TabelMinggu_TMA2 extends AbstractTableModel {

    private List<Minggu_TMA2> list = new ArrayList<Minggu_TMA2>();

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
                return list.get(rowIndex).getMINGGU();
            case 2:
                return list.get(rowIndex).getTAHUN();
            case 3:
                return list.get(rowIndex).getFITUR1();
            case 4:
                return list.get(rowIndex).getFITUR2();
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
                return "MINGGU";
            case 2:
                return "TAHUN";
            case 3:
                return "FITUR1";
            case 4:
                return "FITUR2";
            case 5:
                return "Tinggi Muka Air (TMA)";
            default:
                return null;
        }
    }

    public void add(Minggu_TMA2 data) {
        list.add(data);
        fireTableRowsInserted(getRowCount(), getColumnCount());
    }

    public void delete(int i, int baris) {
        list.remove(i);
        fireTableRowsDeleted(i, baris);
    }

    public Minggu_TMA2 get(int baris) {
        return (Minggu_TMA2) list.get(baris);
    }
}
