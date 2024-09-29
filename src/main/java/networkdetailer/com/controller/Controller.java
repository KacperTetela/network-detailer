package networkdetailer.com.controller;

import networkdetailer.com.model.data.ComputerData;
import networkdetailer.com.model.data.DataCollector;

public class Controller {
    private DataCollector dataCollector;

    public Controller(DataCollector dataCollector) {
        this.dataCollector = dataCollector;
    }

    public ComputerData getData(){
        return dataCollector.getActualData();
    }

    public int exportToExcel() {
        return dataCollector.saveToExcel();
    }

    public boolean saveAsTxt() {
        return dataCollector.saveToTxt();
    }
}
