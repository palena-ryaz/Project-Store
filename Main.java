package market;

import javax.swing.*;

public class Main {
    public static void main(String[] args){
        SwingUtilities.invokeLater(() -> {
            SimulationGUI gui = new SimulationGUI();
            gui.setVisible(true);
        });
    }
}
