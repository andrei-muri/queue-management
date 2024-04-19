package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class SimulationView extends JFrame {
    private JTextField maxSimulationTimeField;
    private JTextField maxArrivalTimeField;
    private JTextField minArrivalTimeField;
    private JTextField maxServiceTimeField;
    private JTextField minServiceTimeField;
    private JTextField numberOfQueuesField;
    private JTextField numberOfClientsField;
    private JButton startButton;
    private JTextArea simulationArea;
    private JComboBox<String> schedulerPolicyBox;

    public SimulationView() {
        setTitle("Queue Management Simulation");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 500);

        initializeComponents();
        layoutComponents();
        setVisible(true);
    }

    private void initializeComponents() {
        maxSimulationTimeField = new JTextField(5);
        maxArrivalTimeField = new JTextField(5);
        minArrivalTimeField = new JTextField(5);
        maxServiceTimeField = new JTextField(5);
        minServiceTimeField = new JTextField(5);
        numberOfQueuesField = new JTextField(5);
        numberOfClientsField = new JTextField(5);
        String[] policies = {"SHORTEST_QUEUE", "SHORTEST_TIME"};
        schedulerPolicyBox = new JComboBox<>(policies);
        startButton = new JButton("Start Simulation");
        simulationArea = new JTextArea(10, 40);
        simulationArea.setEditable(false);
    }

    private void layoutComponents() {
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS)); // Set layout to BoxLayout.Y_AXIS

        // Create individual panels for each label and text field pair to align them horizontally
        inputPanel.add(createInputField("Max Simulation Time:", maxSimulationTimeField));
        inputPanel.add(createInputField("Max Arrival Time:", maxArrivalTimeField));
        inputPanel.add(createInputField("Min Arrival Time:", minArrivalTimeField));
        inputPanel.add(createInputField("Max Service Time:", maxServiceTimeField));
        inputPanel.add(createInputField("Min Service Time:", minServiceTimeField));
        inputPanel.add(createInputField("Number of Queues:", numberOfQueuesField));
        inputPanel.add(createInputField("Number of Clients:", numberOfClientsField));
        inputPanel.add(schedulerPolicyBox);

        startButton.setAlignmentX(Component.CENTER_ALIGNMENT); // Align the start button to center
        inputPanel.add(startButton);

        // Align the input panel to the center of the frame
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(inputPanel, BorderLayout.CENTER);

        // Add a scroll pane for the simulation area
        add(new JScrollPane(simulationArea), BorderLayout.SOUTH);
    }

    private JPanel createInputField(String label, JTextField textField) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel(label));
        panel.add(textField);
        return panel;
    }
    public void setStartButtonActionListener(ActionListener actionListener) {
        startButton.addActionListener(actionListener);
    }



    public String[] getUserInputs() {
        return new String[]{
                maxSimulationTimeField.getText(),
                maxArrivalTimeField.getText(),
                minArrivalTimeField.getText(),
                maxServiceTimeField.getText(),
                minServiceTimeField.getText(),
                numberOfQueuesField.getText(),
                numberOfClientsField.getText(),
                (String) schedulerPolicyBox.getSelectedItem()
        };
    }

    public void updateSimulationArea(String text) {
        simulationArea.setText(text);
    }
}

