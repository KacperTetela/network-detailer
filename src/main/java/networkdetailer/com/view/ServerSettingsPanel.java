package networkdetailer.com.view;

import networkdetailer.com.controller.Controller;

import javax.swing.*;
import java.awt.*;

public class ServerSettingsPanel extends JPanel {
  private JPanel labelPanel = new JPanel();
  private JPanel buttonsPanel = new JPanel();
  private JLabel messageLabel = new JLabel("Please provide access key:");
  private JButton exitButton = new JButton("Save access key");
  private JTextField accessKeyField = new JFormattedTextField();

  public ServerSettingsPanel(Controller controller) {
    setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
    setLayout(new GridLayout(3, 1, 0, 25));
    labelPanel.add(messageLabel);
    exitButton.addActionListener(
        e -> {
          boolean didWork = controller.overwrite(accessKeyField.getAccessibleContext().toString());
          if (didWork) {
            backToMainPanel();
          } else {
            backToMainPanel();
            Frame.getInstance()
                .informationPanelPlayer(
                    "Something went wrong, make sure the access key is correct");
          }
        });
    exitButton.setFocusPainted(false);
    buttonsPanel.add(exitButton);
    add(labelPanel);
    add(accessKeyField);
    add(buttonsPanel);
  }

  private void backToMainPanel() {
    Frame.getInstance().mainPanel.setVisible(true);
    this.setVisible(false);
  }
}
