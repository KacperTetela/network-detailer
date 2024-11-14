package networkdetailer.com.view.main;

import networkdetailer.com.controller.Controller;

import javax.swing.*;
import java.awt.*;

public class MainPanel extends JPanel {
    JPanel buttonsPanel;
    JPanel variablesPanel;

    public MainPanel(Controller controller) {
        buttonsPanel = new ButtonsPanel(controller);
        variablesPanel = new VariablesPanel(controller);
        setLayout(new GridLayout(2, 1));
        add(variablesPanel);
        add(buttonsPanel);
    }
}
