package networkdetailer.com.view.main;

import networkdetailer.com.controller.Controller;
import networkdetailer.com.view.Frame;

import javax.swing.*;
import java.awt.*;

public class ButtonsPanel extends JPanel {
    private Controller controller;
    JButton saveToTxt = new JButton("Save as TXT");
    JButton saveToExcel = new JButton("Export to Excel");
    JButton refresh = new JButton("Refresh Data");

    public ButtonsPanel(Controller controller) {
        this.controller = controller;
        setSize(250, 80);
        setLayout(new FlowLayout());

        saveToTxt.setFocusPainted(false);
        saveToExcel.setFocusPainted(false);
        refresh.setFocusPainted(false);

        saveToExcel.addActionListener(e -> {
            /**
             * int = 0 Error
             * int = 1 success
             * int = 2 hostname already exist
             */
            int outputValue = controller.exportToExcel();
            switch (outputValue) {
                case 0 -> Frame.getInstance().informationPanelPlayer("Something went wrong.");
                case 1 -> Frame.getInstance().informationPanelPlayer("Data has been exported to Excel.");
                case 2 -> Frame.getInstance().informationPanelPlayer("Hostname already added. Details updated again.");
            }
        });
        saveToTxt.addActionListener(e -> {
            boolean didWork = controller.saveAsTxt();
            if (didWork) {
                Frame.getInstance().informationPanelPlayer("Data has been saved as txt file.");
            } else {
                Frame.getInstance().informationPanelPlayer("Something went wrong.");
            }
        });
        refresh.addActionListener(e -> {
            VariablesPanel.updateStaticData();
        });

        add(saveToExcel);
        add(saveToTxt);
        add(refresh);
    }
}
