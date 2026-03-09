package seedu.duke.ui;

import java.util.ArrayList;
import java.util.stream.IntStream;

public class UiTable {
    private ArrayList<UiTableRow> rows;

    public UiTable(){
        rows=new ArrayList<>();
    }

    public void addRow(UiTableRow row) {
        if(rows.isEmpty()){
            rows.add(row);
        }else{
            if(rows.getFirst().size()!=row.size()){
                throw new IllegalArgumentException("All rows must have the same number of columns");
            }

            rows.add(row);
        }
    }

    public int getColumnWidth(int columnIndex) {
        int maxWidth = 0;
        for (UiTableRow row : rows) {
            if (columnIndex < row.size()) {
                int cellWidth = row.length(columnIndex);
                if (cellWidth > maxWidth) {
                    maxWidth = cellWidth;
                }
            }
        }
        return maxWidth;
    }

    public int getColumnCount() {
        if (rows.isEmpty()) {
            throw new IllegalArgumentException("No rows have been added");
        }
        return rows.getFirst().size();
    }

    public int getRowCount() {
        return rows.size();
    }

    @Override
    public String toString() {
        int indexLength = (""+getRowCount()).length();

        var widths = IntStream.range(0, getColumnCount()).map(this::getColumnWidth).toArray();

        StringBuilder tableString = new StringBuilder();

        int i=1;
        for (UiTableRow row : rows) {
            //plus 1 for the dot and 1 for the space after the dot
            String indexString = String.format("%-" + (indexLength+2) + "s", i+".");
            tableString.append(indexString).append(row.toString(widths)).append("\n");

            i++;
        }

        return tableString.toString();
    }
}
