package main;

import view.LoginFrame;
import controller.LoginController;

public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            LoginController controller = new LoginController(loginFrame);
            loginFrame.setController(controller);
            loginFrame.setVisible(true);
        });
    }
}
