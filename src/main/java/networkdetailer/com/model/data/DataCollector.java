package networkdetailer.com.model.data;

import networkdetailer.com.model.hardware.CPUGenerationGetter;
import networkdetailer.com.model.hardware.HardwareGetter;
import networkdetailer.com.model.hardware.RequirementsChecker;
import networkdetailer.com.model.hardware.HardwareDownloader;
import networkdetailer.com.model.network.IPGetter;
import networkdetailer.com.model.network.MacGetterOffline;
import networkdetailer.com.model.network.MacGetterOnline;
import networkdetailer.com.model.network.NetworkDownloader;
import networkdetailer.com.model.util.UndefinedHostReadWrite;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DataCollector {
    private CPUData cpuData;
    private MOBOData moboData;
    private MemoryData memoryData;
    private NetworkData networkData;

    private NetworkDownloader networkDownloader;
    private HardwareDownloader hardwareDownloader;
    private RequirementsChecker requirementsChecker;
    private ComputerData data = null;


    public DataCollector() {
        networkDownloader = new NetworkDownloader(new IPGetter(), new MacGetterOnline(), new MacGetterOffline());
        hardwareDownloader = new HardwareDownloader(new HardwareGetter(), new CPUGenerationGetter());
        requirementsChecker = new RequirementsChecker();
    }

    public ComputerData refreshData() {
        boolean firstTime = data == null;
        //każde odpalenie aplikacji inkrementuje
        data = generateActualData();
        if (firstTime && !data.networkData().online()) {
            UndefinedHostReadWrite.incrementNumber();
        }
        return data;
    }

    private ComputerData generateActualData() {
        networkData = networkDownloader.getData();
        cpuData = hardwareDownloader.getCpuData();
        moboData = hardwareDownloader.getMoboData();
        memoryData = hardwareDownloader.getMemoryData();
        String windowsRequirements = String.valueOf(isWindowsRequirements());
        return new ComputerData(cpuData, memoryData, moboData, networkData, windowsRequirements);
    }

    public int saveToExcel() {
        /**
         * int = 0 Error
         * int = 1 Success
         * int = 2 Hostname already exists
         */
        Path jarPath;
        try {
            jarPath = Paths.get(DataCollector.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return 0;
        }

        String filePath = jarPath.resolve("network_details.xlsx").toString();
        File file = new File(filePath);
        Workbook workbook;
        Sheet sheet;
        boolean hostnameExists = false;

        try {
            if (file.exists()) {
                // Load existing workbook
                FileInputStream fis = new FileInputStream(file);
                workbook = new XSSFWorkbook(fis);
                sheet = workbook.getSheetAt(0);
                fis.close();

                // Check for existing hostname and update if found
                int rowCount = sheet.getLastRowNum();
                for (int i = 1; i <= rowCount; i++) {
                    Row row = sheet.getRow(i);
                    if (row != null && row.getCell(0) != null && row.getCell(0).getStringCellValue().equals(networkData.hostname())) {
                        // Update existing row
                        row.getCell(0).setCellValue(networkData.hostname());
                        row.getCell(1).setCellValue(networkData.ip());
                        row.getCell(2).setCellValue(networkData.mac());
                        row.getCell(3).setCellValue(cpuData.generation().cpuManufacturer().toString());
                        row.getCell(4).setCellValue(cpuData.name());
                        row.getCell(5).setCellValue(cpuData.generation().generation());
                        row.getCell(6).setCellValue(cpuData.ghz());
                        row.getCell(7).setCellValue(memoryData.ramGB());
                        row.getCell(8).setCellValue(memoryData.diskSpaceGB());
                        row.getCell(9).setCellValue(memoryData.diskType().toString());
                        row.getCell(10).setCellValue(moboData.bios());
                        row.getCell(11).setCellValue(isWindowsRequirements() ? "Yes" : "No");
                        hostnameExists = true;
                        break;
                    }
                }

                // Add new row if hostname does not exist
                if (!hostnameExists) {
                    Row newRow = sheet.createRow(sheet.getLastRowNum() + 1);
                    newRow.createCell(0).setCellValue(networkData.hostname());
                    newRow.createCell(1).setCellValue(networkData.ip());
                    newRow.createCell(2).setCellValue(networkData.mac());
                    newRow.createCell(3).setCellValue(cpuData.generation().cpuManufacturer().toString());
                    newRow.createCell(4).setCellValue(cpuData.name());
                    newRow.createCell(5).setCellValue(cpuData.generation().generation());
                    newRow.createCell(6).setCellValue(cpuData.ghz());
                    newRow.createCell(7).setCellValue(memoryData.ramGB());
                    newRow.createCell(8).setCellValue(memoryData.diskSpaceGB());
                    newRow.createCell(9).setCellValue(memoryData.diskType().toString());
                    newRow.createCell(10).setCellValue(moboData.bios());
                    newRow.createCell(11).setCellValue(isWindowsRequirements() ? "Yes" : "No");
                }
            } else {
                // Create new workbook and sheet
                workbook = new XSSFWorkbook();
                sheet = workbook.createSheet("Network Details");
                Row headerRow = sheet.createRow(0);
                headerRow.createCell(0).setCellValue("Hostname");
                headerRow.createCell(1).setCellValue("IP Address");
                headerRow.createCell(2).setCellValue("Physical Address");
                headerRow.createCell(3).setCellValue("CPU Manufacturer");
                headerRow.createCell(4).setCellValue("CPU Name");
                headerRow.createCell(5).setCellValue("CPU Generation");
                headerRow.createCell(6).setCellValue("CPU GHz");
                headerRow.createCell(7).setCellValue("RAM (GB)");
                headerRow.createCell(8).setCellValue("Disk Space (GB)");
                headerRow.createCell(9).setCellValue("Disk Type");
                headerRow.createCell(10).setCellValue("BIOS");
                headerRow.createCell(11).setCellValue("Windows Requirements");

                // Add new row
                Row newRow = sheet.createRow(1);
                newRow.createCell(0).setCellValue(networkData.hostname());
                newRow.createCell(1).setCellValue(networkData.ip());
                newRow.createCell(2).setCellValue(networkData.mac());
                newRow.createCell(3).setCellValue(cpuData.generation().cpuManufacturer().toString());
                newRow.createCell(4).setCellValue(cpuData.name());
                newRow.createCell(5).setCellValue(cpuData.generation().generation());
                newRow.createCell(6).setCellValue(cpuData.ghz());
                newRow.createCell(7).setCellValue(memoryData.ramGB());
                newRow.createCell(8).setCellValue(memoryData.diskSpaceGB());
                newRow.createCell(9).setCellValue(memoryData.diskType().toString());
                newRow.createCell(10).setCellValue(moboData.bios());
                newRow.createCell(11).setCellValue(isWindowsRequirements() ? "Yes" : "No");
            }

            // Write the changes to the file
            try (FileOutputStream fos = new FileOutputStream(file)) {
                workbook.write(fos);
            }
            workbook.close();

            return hostnameExists ? 2 : 1;
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public boolean saveToTxt() {
        try {
            Path jarPath = Paths.get(DataCollector.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent();
            String filePath = jarPath.resolve(networkData.hostname() + ".txt").toString();

            File file = new File(filePath);

            FileWriter writer = new FileWriter(file);
            writer.write("Hostname: " + networkData.hostname() + System.lineSeparator());
            writer.write("IP Address: " + networkData.ip() + System.lineSeparator());
            writer.write("Physical Address: " + networkData.mac() + System.lineSeparator());
            writer.write("CPU Manufacturer: " + cpuData.generation().cpuManufacturer() + System.lineSeparator());
            writer.write("CPU Name: " + cpuData.name() + System.lineSeparator());
            writer.write("CPU Generation: " + cpuData.generation().generation() + System.lineSeparator());
            writer.write("CPU GHz: " + cpuData.ghz() + System.lineSeparator());
            writer.write("RAM (GB): " + memoryData.ramGB() + System.lineSeparator());
            writer.write("Disk Space (GB): " + memoryData.diskSpaceGB() + System.lineSeparator());
            writer.write("Disk Type: " + memoryData.diskType().toString() + System.lineSeparator());
            writer.write("BIOS Version: " + moboData.bios() + System.lineSeparator());
            writer.write("Windows Requirements: " + (isWindowsRequirements() ? "Yes" : "No") + System.lineSeparator());
            writer.close();

            return true;
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isWindowsRequirements() {
        return requirementsChecker.check(cpuData, memoryData);
    }

    public CPUData getCpuData() {
        return cpuData;
    }

    public MOBOData getMoboData() {
        return moboData;
    }

    public MemoryData getMemoryData() {
        return memoryData;
    }

    public NetworkData getNetworkData() {
        return networkData;
    }

    public void setNetworkDownloader(NetworkDownloader networkDownloader) {
        this.networkDownloader = networkDownloader;
    }

    public void setHardwareDownloader(HardwareDownloader hardwareDownloader) {
        this.hardwareDownloader = hardwareDownloader;
    }

    public void setRequirementsChecker(RequirementsChecker requirementsChecker) {
        this.requirementsChecker = requirementsChecker;
    }
}
