package logic;

import model.SelectionPolicy;
import view.SimulationView;

import javax.swing.*;
import java.awt.*;

public class SimulationController {
    SimulationView view;

    public SimulationController() {
        this.view = new SimulationView();
        this.view.setStartButtonActionListener((e) -> startSimulation());
    }

    public void startSimulation() {
        String[] inputs = view.getUserInputs();
        if(inputs == null) {
            return;
        }
        int simTime = Integer.parseInt(inputs[0]);
        int maxArr = Integer.parseInt(inputs[1]);
        int minArr = Integer.parseInt(inputs[2]);
        int maxSer = Integer.parseInt(inputs[3]);
        int minSer = Integer.parseInt(inputs[4]);
        int queues = Integer.parseInt(inputs[5]);
        int clients = Integer.parseInt(inputs[6]);
        if(maxArr > simTime || maxArr < 1) {
            JOptionPane.showMessageDialog(view, "Invalid maximum arrival", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        } else if(minArr > simTime || minArr < 0) {
            JOptionPane.showMessageDialog(view, "Invalid minimum arrival", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        } else if(minArr > maxArr) {
            JOptionPane.showMessageDialog(view, "Minimum arrival cannot be greater than maximum arrival", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        } else if(maxSer > simTime || maxSer < 1) {
            JOptionPane.showMessageDialog(view, "Invalid maximum service", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        } else if(minSer > simTime || minSer < 0) {
            JOptionPane.showMessageDialog(view, "Invalid minimum service", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        } else if(minSer > maxSer) {
            JOptionPane.showMessageDialog(view, "Minimum service cannot be greater than maximum service", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        SelectionPolicy policy = SelectionPolicy.SHORTEST_QUEUE;
        if(inputs[7].equals("SHORTEST_QUEUE")) {
            policy = SelectionPolicy.SHORTEST_QUEUE;
        } else if(inputs[7].equals("SHORTEST_TIME")) {
            policy = SelectionPolicy.SHORTEST_TIME;
        }
        SimulationManager simulationManager = new SimulationManager(simTime,
                maxSer,
                minSer,
                maxArr,
                minArr,
                queues,
                clients,
                policy,
                view);
        //EventQueue.invokeLater(() -> view.setVisible(true));
        Thread t = new Thread(simulationManager);
        t.start();
    }

    public static void main(String[] args) {
        new SimulationController();

    }
}
