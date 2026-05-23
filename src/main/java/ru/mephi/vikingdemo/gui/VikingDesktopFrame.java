package ru.mephi.vikingdemo.gui;

import ru.mephi.vikingdemo.model.Viking;
import ru.mephi.vikingdemo.service.VikingService;
import ru.mephi.vikingdemo.service.VikingLambdaService;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class VikingDesktopFrame extends JFrame {
    private final VikingService vikingService;
    private final VikingLambdaService lambdaService;
    private final VikingTableModel tableModel;
    private JTable vikingTable;

    public VikingDesktopFrame(VikingService vikingService, VikingLambdaService lambdaService) {
        this.vikingService = vikingService;
        this.lambdaService = lambdaService;
        this.tableModel = new VikingTableModel();

        setTitle("Viking Demo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(1000, 420));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JLabel header = new JLabel("Viking Demo", SwingConstants.CENTER);
        header.setFont(header.getFont().deriveFont(Font.BOLD, 18f));
        add(header, BorderLayout.NORTH);

        vikingTable = new JTable(tableModel);
        vikingTable.setRowHeight(28);
        add(new JScrollPane(vikingTable), BorderLayout.CENTER);

        vikingTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    int row = vikingTable.rowAtPoint(e.getPoint());
                    if (row >= 0) {
                        Integer id = tableModel.getVikingIdAt(row);
                        int confirm = JOptionPane.showConfirmDialog(VikingDesktopFrame.this,
                                "Удалить викинга?", "Подтверждение", JOptionPane.YES_NO_OPTION);
                        if (confirm == JOptionPane.YES_OPTION) {
                            vikingService.deleteById(id);
                            refreshTable();
                        }
                    }
                }
            }
        });

        JButton createButton = new JButton("Create random viking");
        createButton.addActionListener(event -> onCreateViking());

        JButton massGenButton = new JButton("Mass generate");
        massGenButton.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(this, "Enter number of vikings to generate:", "10");
            int count = 10;
            try {
                count = Integer.parseInt(input);
            } catch (Exception ex) { }
            vikingService.generateRandomVikings(count);
            refreshTable();
        });

        JButton analysisButton = new JButton("Lambda Analysis");
        analysisButton.addActionListener(e -> {
            VikingLambdaFrame lambdaFrame = new VikingLambdaFrame(lambdaService);
            lambdaFrame.setVisible(true);
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(createButton);
        bottomPanel.add(massGenButton);
        bottomPanel.add(analysisButton);
        add(bottomPanel, BorderLayout.SOUTH);

        refreshTable();
    }

    private void onCreateViking() {
        vikingService.createRandomViking();
        refreshTable();
    }

    public void refreshTable() {
        tableModel.updateData(vikingService.getAllVikings());
    }
}