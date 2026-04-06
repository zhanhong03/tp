package seedu.equipmentmaster.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static seedu.equipmentmaster.common.Messages.MESSAGE_NAME_CONTAINS_RESERVED_CHARS;

import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.equipmentmaster.context.Context;
import seedu.equipmentmaster.equipment.Equipment;
import seedu.equipmentmaster.equipmentlist.EquipmentList;
import seedu.equipmentmaster.exception.EquipmentMasterException;
import seedu.equipmentmaster.modulelist.ModuleList;
import seedu.equipmentmaster.semester.AcademicSemester;
import seedu.equipmentmaster.storage.Storage;
import seedu.equipmentmaster.ui.Ui;

public class SetStatusCommandTest {

    @TempDir
    Path tempDir;

    private Storage storage;
    private Ui ui;
    private EquipmentList equipments;
    private ModuleList moduleList;
    private AcademicSemester currentSystemSemester;

    @BeforeEach
    public void setUp() throws EquipmentMasterException {
        ui = new Ui();
        storage = new Storage(tempDir.resolve("test.txt").toString(),
                ui, tempDir.resolve("test_setting.txt").toString(), tempDir.resolve("test_module.txt").toString());
        equipments = new EquipmentList();
        moduleList = new ModuleList();
        currentSystemSemester = new AcademicSemester("AY2024/25 Sem1");
    }

    private Context createContext() {
        return new Context(equipments, moduleList, ui, storage, currentSystemSemester);
    }

    private void addEquipment(String name, int quantity, int available, int loaned) throws EquipmentMasterException {
        Equipment eq = new Equipment(name, quantity, available, loaned);
        equipments.addEquipment(eq);
    }

    private void addEquipmentWithSemester(String name, int quantity, int available, int loaned,
                                          AcademicSemester sem, double lifespan) throws EquipmentMasterException {
        Equipment eq = new Equipment(name, quantity, available, loaned, sem, lifespan, 0);
        equipments.addEquipment(eq);
    }

    @Test
    public void executeByName_loanPositive_updates() throws EquipmentMasterException {
        AcademicSemester testSem = new AcademicSemester("AY2025/26 Sem2");
        addEquipmentWithSemester("Basys3 FPGA", 40, 40, 0, testSem, 5.0);

        SetStatusCommand command = new SetStatusCommand("Basys3 FPGA", 5, "loaned");
        command.execute(createContext());

        Equipment eq = equipments.getEquipment(0);
        assertEquals(35, eq.getAvailable());
        assertEquals(5, eq.getLoaned());
    }

    @Test
    public void executeByName_loanNegative_noChange() throws EquipmentMasterException {
        addEquipment("Basys3 FPGA", 40, 40, 0);

        SetStatusCommand command = new SetStatusCommand("Basys3 FPGA", -5, "loaned");
        command.execute(createContext());

        Equipment eq = equipments.getEquipment(0);
        assertEquals(40, eq.getAvailable());
        assertEquals(0, eq.getLoaned());
    }

    @Test
    public void executeByIndex_returnPositive_updates() throws EquipmentMasterException {
        AcademicSemester testSem = new AcademicSemester("AY2025/26 Sem2");
        addEquipmentWithSemester("Basys3 FPGA", 40, 30, 10, testSem, 5.0);

        SetStatusCommand command = new SetStatusCommand(1, 3, "available");
        command.execute(createContext());

        Equipment eq = equipments.getEquipment(0);
        assertEquals(33, eq.getAvailable());
        assertEquals(7, eq.getLoaned());
    }

    @Test
    public void executeByIndex_returnNegative_noChange() throws EquipmentMasterException {
        addEquipment("Basys3 FPGA", 40, 30, 10);

        SetStatusCommand command = new SetStatusCommand(1, -3, "available");
        command.execute(createContext());

        Equipment eq = equipments.getEquipment(0);
        assertEquals(30, eq.getAvailable());
        assertEquals(10, eq.getLoaned());
    }

    @Test
    public void executeByName_loanExceedsAvailable_noChange() throws EquipmentMasterException {
        addEquipment("Basys3 FPGA", 40, 10, 30);

        SetStatusCommand command = new SetStatusCommand("Basys3 FPGA", 20, "loaned");
        command.execute(createContext());

        Equipment eq = equipments.getEquipment(0);
        assertEquals(10, eq.getAvailable());
        assertEquals(30, eq.getLoaned());
    }

    @Test
    public void executeByName_returnExceedsLoaned_noChange() throws EquipmentMasterException {
        addEquipment("Basys3 FPGA", 40, 35, 5);

        SetStatusCommand command = new SetStatusCommand("Basys3 FPGA", 10, "available");
        command.execute(createContext());

        Equipment eq = equipments.getEquipment(0);
        assertEquals(35, eq.getAvailable());
        assertEquals(5, eq.getLoaned());
    }

    @Test
    public void executeByName_equipmentNotFound_noChange() throws EquipmentMasterException {
        addEquipment("Basys3 FPGA", 40, 40, 0);

        SetStatusCommand command = new SetStatusCommand("NonExistent", 5, "loaned");
        command.execute(createContext());

        Equipment eq = equipments.getEquipment(0);
        assertEquals(40, eq.getAvailable());
        assertEquals(0, eq.getLoaned());
    }

    @Test
    public void executeByIndex_outOfBounds_noChange() throws EquipmentMasterException {
        addEquipment("Basys3 FPGA", 40, 40, 0);

        SetStatusCommand command = new SetStatusCommand(99, 5, "loaned");
        command.execute(createContext());

        Equipment eq = equipments.getEquipment(0);
        assertEquals(40, eq.getAvailable());
        assertEquals(0, eq.getLoaned());
    }

    @Test
    public void executeByIndex_zeroQuantity_noChange() throws EquipmentMasterException {
        addEquipment("Basys3 FPGA", 40, 40, 0);

        SetStatusCommand command = new SetStatusCommand(1, 0, "loaned");
        command.execute(createContext());

        Equipment eq = equipments.getEquipment(0);
        assertEquals(40, eq.getAvailable());
        assertEquals(0, eq.getLoaned());
    }

    @Test
    public void executeByIndex_loanExceedsAvailable_noChange() throws EquipmentMasterException {
        addEquipment("Basys3 FPGA", 40, 10, 30);

        SetStatusCommand command = new SetStatusCommand(1, 20, "loaned");
        command.execute(createContext());

        Equipment eq = equipments.getEquipment(0);
        assertEquals(10, eq.getAvailable());
        assertEquals(30, eq.getLoaned());
    }

    @Test
    public void executeByIndex_returnExceedsLoaned_noChange() throws EquipmentMasterException {
        addEquipment("Basys3 FPGA", 40, 35, 5);

        SetStatusCommand command = new SetStatusCommand(1, 10, "available");
        command.execute(createContext());

        Equipment eq = equipments.getEquipment(0);
        assertEquals(35, eq.getAvailable());
        assertEquals(5, eq.getLoaned());
    }

    @Test
    public void parseByName_validLoaned_success() throws EquipmentMasterException {
        Command command = SetStatusCommand.parse("setstatus n/Basys3 FPGA q/5 s/loaned");
        assertEquals(SetStatusCommand.class, command.getClass());
    }

    @Test
    public void parseByIndex_validAvailable_success() throws EquipmentMasterException {
        Command command = SetStatusCommand.parse("setstatus 1 q/3 s/available");
        assertEquals(SetStatusCommand.class, command.getClass());
    }

    @Test
    public void parse_missingQFlag_throwsException() {
        assertThrows(EquipmentMasterException.class,
                () -> SetStatusCommand.parse("setstatus n/Basys3 FPGA 5 s/loaned"));
    }

    @Test
    public void parse_invalidStatus_throwsException() {
        assertThrows(EquipmentMasterException.class,
                () -> SetStatusCommand.parse("setstatus n/Basys3 FPGA q/5 s/broken"));
    }

    @Test
    public void parse_nonNumericQuantity_throwsException() {
        assertThrows(EquipmentMasterException.class,
                () -> SetStatusCommand.parse("setstatus n/Basys3 FPGA q/abc s/loaned"));
    }

    @Test
    public void parse_nonNumericIndex_throwsException() {
        assertThrows(EquipmentMasterException.class,
                () -> SetStatusCommand.parse("setstatus abc q/5 s/loaned"));
    }

    @Test
    public void parse_zeroQuantity_throwsException() {
        assertThrows(EquipmentMasterException.class,
                () -> SetStatusCommand.parse("setstatus n/Basys3 FPGA q/0 s/loaned"));
    }

    @Test
    public void parse_negativeIndex_throwsException() {
        assertThrows(EquipmentMasterException.class,
                () -> SetStatusCommand.parse("setstatus -1 q/5 s/loaned"));
    }

    @Test
    public void executeByName_loanWithMixedCaseStatus_updates() throws EquipmentMasterException {
        AcademicSemester testSem = new AcademicSemester("AY2025/26 Sem2");
        addEquipmentWithSemester("Oscilloscope", 20, 20, 0, testSem, 5.0);

        SetStatusCommand command = new SetStatusCommand("Oscilloscope", 3, "LOANED");
        command.execute(createContext());

        Equipment eq = equipments.getEquipment(0);
        assertEquals(17, eq.getAvailable());
        assertEquals(3, eq.getLoaned());
    }

    @Test
    public void executeByName_returnWithMixedCaseStatus_updates() throws EquipmentMasterException {
        addEquipment("Multimeter", 15, 10, 5);

        SetStatusCommand command = new SetStatusCommand("Multimeter", 2, "AVAILABLE");
        command.execute(createContext());

        Equipment eq = equipments.getEquipment(0);
        assertEquals(12, eq.getAvailable());
        assertEquals(3, eq.getLoaned());
    }

    @Test
    public void executeByName_invalidStatus_showsNoChange() throws EquipmentMasterException {
        addEquipment("PowerSupply", 10, 10, 0);

        SetStatusCommand command = new SetStatusCommand("PowerSupply", 2, "damaged");
        command.execute(createContext());

        Equipment eq = equipments.getEquipment(0);
        assertEquals(10, eq.getAvailable());
        assertEquals(0, eq.getLoaned());
    }

    @Test
    public void executeByIndex_invalidStatus_showsNoChange() throws EquipmentMasterException {
        addEquipment("FunctionGen", 8, 8, 0);

        SetStatusCommand command = new SetStatusCommand(1, 2, "reserved");
        command.execute(createContext());

        Equipment eq = equipments.getEquipment(0);
        assertEquals(8, eq.getAvailable());
        assertEquals(0, eq.getLoaned());
    }

    @Test
    public void parseByName_extraSpacesInCommand_success() throws EquipmentMasterException {
        Command command = SetStatusCommand.parse("setstatus   n/STM32   q/10   s/loaned  ");
        assertEquals(SetStatusCommand.class, command.getClass());
    }

    @Test
    public void parseByIndex_extraSpacesInCommand_success() throws EquipmentMasterException {
        Command command = SetStatusCommand.parse("setstatus   2   q/5   s/available  ");
        assertEquals(SetStatusCommand.class, command.getClass());
    }

    @Test
    public void parseByName_emptyName_throwsException() {
        EquipmentMasterException exception = assertThrows(EquipmentMasterException.class,
                () -> SetStatusCommand.parse("setstatus n/ q/5 s/loaned"));
        assertEquals("Equipment name cannot be empty.", exception.getMessage());
    }

    @Test
    public void parseByName_missingStatus_throwsException() {
        assertThrows(EquipmentMasterException.class,
                () -> SetStatusCommand.parse("setstatus n/Arduino q/5"));
    }

    @Test
    public void parseByName_missingQuantity_throwsException() {
        assertThrows(EquipmentMasterException.class,
                () -> SetStatusCommand.parse("setstatus n/Arduino s/loaned"));
    }

    @Test
    public void parseByIndex_missingQuantity_throwsException() {
        assertThrows(EquipmentMasterException.class,
                () -> SetStatusCommand.parse("setstatus 1 s/loaned"));
    }

    @Test
    public void parseByIndex_missingStatus_throwsException() {
        assertThrows(EquipmentMasterException.class,
                () -> SetStatusCommand.parse("setstatus 1 q/5"));
    }

    @Test
    public void parseByIndex_missingIndex_throwsException() {
        EquipmentMasterException exception = assertThrows(EquipmentMasterException.class,
                () -> SetStatusCommand.parse("setstatus q/5 s/loaned"));
        assertEquals("Please enter a valid whole number for index", exception.getMessage());
    }

    @Test
    public void executeByName_loanWithZeroQuantity_noChange() throws EquipmentMasterException {
        addEquipment("Camera", 5, 5, 0);

        SetStatusCommand command = new SetStatusCommand("Camera", 0, "loaned");
        command.execute(createContext());

        Equipment eq = equipments.getEquipment(0);
        assertEquals(5, eq.getAvailable());
        assertEquals(0, eq.getLoaned());
    }

    @Test
    public void executeByName_loanExactAvailable_works() throws EquipmentMasterException {
        addEquipment("Projector", 3, 3, 0);

        SetStatusCommand command = new SetStatusCommand("Projector", 3, "loaned");
        command.execute(createContext());

        Equipment eq = equipments.getEquipment(0);
        assertEquals(0, eq.getAvailable());
        assertEquals(3, eq.getLoaned());
    }

    @Test
    public void executeByName_returnExactLoaned_works() throws EquipmentMasterException {
        addEquipment("Headphones", 10, 5, 5);

        SetStatusCommand command = new SetStatusCommand("Headphones", 5, "available");
        command.execute(createContext());

        Equipment eq = equipments.getEquipment(0);
        assertEquals(10, eq.getAvailable());
        assertEquals(0, eq.getLoaned());
    }

    @Test
    public void executeByName_caseInsensitiveNameMatch_works() throws EquipmentMasterException {
        addEquipment("RaspberryPi", 25, 25, 0);

        SetStatusCommand command = new SetStatusCommand("raspberrypi", 5, "loaned");
        command.execute(createContext());

        Equipment eq = equipments.getEquipment(0);
        assertEquals(20, eq.getAvailable());
        assertEquals(5, eq.getLoaned());
    }

    @Test
    public void executeByIndex_loanMultipleTimes_accumulates() throws EquipmentMasterException {
        addEquipment("SolderingIron", 50, 50, 0);

        SetStatusCommand command1 = new SetStatusCommand(1, 10, "loaned");
        SetStatusCommand command2 = new SetStatusCommand(1, 5, "loaned");

        Context context = createContext();
        command1.execute(context);
        command2.execute(context);

        Equipment eq = equipments.getEquipment(0);
        assertEquals(35, eq.getAvailable());
        assertEquals(15, eq.getLoaned());
    }

    @Test
    public void executeByIndex_returnPartialLoaned_works() throws EquipmentMasterException {
        addEquipment("LogicAnalyzer", 20, 10, 10);

        SetStatusCommand command = new SetStatusCommand(1, 4, "available");
        command.execute(createContext());

        Equipment eq = equipments.getEquipment(0);
        assertEquals(14, eq.getAvailable());
        assertEquals(6, eq.getLoaned());
    }

    @Test
    public void parseByName_withSpecialCharactersInName_throwsException() {
        EquipmentMasterException exception = assertThrows(EquipmentMasterException.class,
                () -> SetStatusCommand.parse("setstatus n/Test|Name q/5 s/loaned"));
        assertEquals(MESSAGE_NAME_CONTAINS_RESERVED_CHARS, exception.getMessage());
    }

    @Test
    public void parse_emptyCommand_throwsException() {
        EquipmentMasterException exception = assertThrows(EquipmentMasterException.class,
                () -> SetStatusCommand.parse(""));
        assertEquals("Empty command.", exception.getMessage());
    }

    @Test
    public void parse_whitespaceOnly_throwsException() {
        EquipmentMasterException exception = assertThrows(EquipmentMasterException.class,
                () -> SetStatusCommand.parse("   "));
        assertEquals("Empty command.", exception.getMessage());
    }

    @Test
    public void executeByName_loanThenReturn_restoresOriginal() throws EquipmentMasterException {
        addEquipment("TestGear", 100, 100, 0);

        SetStatusCommand loanCmd = new SetStatusCommand("TestGear", 30, "loaned");
        SetStatusCommand returnCmd = new SetStatusCommand("TestGear", 30, "available");

        Context context = createContext();
        loanCmd.execute(context);
        returnCmd.execute(context);

        Equipment eq = equipments.getEquipment(0);
        assertEquals(100, eq.getAvailable());
        assertEquals(0, eq.getLoaned());
    }

    @Test
    public void executeByName_loanWithAvailableZero_showsNoChange() throws EquipmentMasterException {
        addEquipment("EmptyStock", 10, 0, 10);

        SetStatusCommand command = new SetStatusCommand("EmptyStock", 1, "loaned");
        command.execute(createContext());

        Equipment eq = equipments.getEquipment(0);
        assertEquals(0, eq.getAvailable());
        assertEquals(10, eq.getLoaned());
    }
}
