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
  JPanel serverSettingsPanel;

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
    setSize(650, 400);
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

  /** Check is data not created already.
   * if not JPanel will be created. */
  public void serverSettingsPanelPlayer() {
    if (serverSettingsPanel == null) {
      serverSettingsPanel = new ServerSettingsPanel(controller);
      add(serverSettingsPanel);
      mainPanel.setVisible(false);
    }
    serverSettingsPanel.setVisible(true);
    mainPanel.setVisible(false);
  }
}
