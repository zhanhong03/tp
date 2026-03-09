package seedu.duke;

import org.junit.jupiter.api.Test;
import seedu.duke.ui.UiTable;
import seedu.duke.ui.UiTableRow;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UiTableTest {
    @Test
    public void uiTableTest(){
        UiTable uiTable = new UiTable();
        uiTable.addRow(new UiTableRow("STM32 Development Board","Total: 50","Available: 45","Loaned: 5"));
        uiTable.addRow(new UiTableRow("Basys3 FPGA","Total: 20","Available: 20","Loaned: 0"));
        uiTable.addRow(new UiTableRow("HDMI Cable","Total: 100","Available: <N/A>","Loaned: <N/A>"));

        String expectedOutput = """
1. STM32 Development Board | Total: 50  | Available: 45    | Loaned: 5   \s
2. Basys3 FPGA             | Total: 20  | Available: 20    | Loaned: 0   \s
3. HDMI Cable              | Total: 100 | Available: <N/A> | Loaned: <N/A>
                """.trim();

        assertEquals(expectedOutput, uiTable.toString().trim());
    }
}
