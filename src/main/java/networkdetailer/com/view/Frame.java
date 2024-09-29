package networkdetailer.com.view;

import networkdetailer.com.controller.Controller;
import networkdetailer.com.model.data.DataCollector;
import networkdetailer.com.view.main.MainPanel;

import javax.swing.*;
import java.awt.*;

public class Frame extends JFrame {
    private static Frame instance;

    private final ImageIcon icon = new ImageIcon(getClass().getResource("/search.png"));

    DataCollector dataCollector = new DataCollector();
    Controller controller = new Controller(dataCollector);

    JPanel mainPanel = new MainPanel(controller);
    JPanel informationPanel;

    public static synchronized Frame getInstance() {
        if (instance == null) {
            instance = new Frame();
        }
        return instance;
    }

    private Frame() {
        setTitle("Network Detailer");
        setIconImage(icon.getImage());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new FlowLayout());
        setResizable(false);
        setSize(400, 400);
        setVisible(true);
        setLocationRelativeTo(null);
    }

    public void initialMainFrame() {
        mainPanel = new MainPanel(controller);
        add(mainPanel);
    }

    public void informationPanelPlayer(String message) {
        informationPanel = new InformationPanel(message);
        add(informationPanel);
        mainPanel.setVisible(false);
    }
}