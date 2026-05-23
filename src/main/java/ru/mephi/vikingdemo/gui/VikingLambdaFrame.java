package ru.mephi.vikingdemo.gui;

import ru.mephi.vikingdemo.model.*;
import ru.mephi.vikingdemo.service.VikingLambdaService;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.List;
import java.util.Optional;

public class VikingLambdaFrame extends JFrame {

    private final VikingLambdaService analysisWorker;
    private final JTextArea outputZone;
    private final VikingTableModel resultTableModel;

    public VikingLambdaFrame(VikingLambdaService lambdaService) {
        this.analysisWorker = lambdaService;
        this.resultTableModel = new VikingTableModel();

        setTitle("Stream API Analytics Dashboard");
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTabbedPane tabContainer = new JTabbedPane();
        tabContainer.addTab("Statistical Counts", buildCountingSection());
        tabContainer.addTab("Warrior Display", buildDisplaySection());
        tabContainer.addTab("Index Operations", buildIndexSection());

        add(tabContainer, BorderLayout.CENTER);

        outputZone = new JTextArea(8, 50);
        outputZone.setEditable(false);
        outputZone.setFont(new Font("Monospaced", Font.PLAIN, 12));
        add(new JScrollPane(outputZone), BorderLayout.SOUTH);
    }

    private JPanel buildCountingSection() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Age panel
        JPanel agePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        agePanel.setBorder(BorderFactory.createTitledBorder("Age Analytics"));

        JTextField ageField = new JTextField(5);
        JButton olderBtn = new JButton("Count >");
        JButton youngerBtn = new JButton("Count <");

        JTextField rangeLow = new JTextField(4);
        JTextField rangeHigh = new JTextField(4);
        JButton rangeBtn = new JButton("Count between");
        JButton outsideBtn = new JButton("Count outside");

        olderBtn.addActionListener(e -> {
            try {
                int val = Integer.parseInt(ageField.getText());
                long cnt = analysisWorker.getWarriorsOlderThan(val);
                outputZone.append("Warriors older than " + val + ": " + cnt + "\n");
            } catch(Exception ex) {
                outputZone.append("Invalid age value\n");
            }
        });

        youngerBtn.addActionListener(e -> {
            try {
                int val = Integer.parseInt(ageField.getText());
                long cnt = analysisWorker.getWarriorsYoungerThan(val);
                outputZone.append("Warriors younger than " + val + ": " + cnt + "\n");
            } catch(Exception ex) {
                outputZone.append("Invalid age value\n");
            }
        });

        rangeBtn.addActionListener(e -> {
            try {
                int lo = Integer.parseInt(rangeLow.getText());
                int hi = Integer.parseInt(rangeHigh.getText());
                long cnt = analysisWorker.getWarriorsAgeBetween(lo, hi);
                outputZone.append("Warriors between " + lo + " and " + hi + ": " + cnt + "\n");
            } catch(Exception ex) {
                outputZone.append("Invalid range values\n");
            }
        });

        outsideBtn.addActionListener(e -> {
            try {
                int lo = Integer.parseInt(rangeLow.getText());
                int hi = Integer.parseInt(rangeHigh.getText());
                long cnt = analysisWorker.getWarriorsAgeOutsideRange(lo, hi);
                outputZone.append("Warriors outside [" + lo + "-" + hi + "]: " + cnt + "\n");
            } catch(Exception ex) {
                outputZone.append("Invalid range values\n");
            }
        });

        agePanel.add(new JLabel("Age:"));
        agePanel.add(ageField);
        agePanel.add(olderBtn);
        agePanel.add(youngerBtn);
        agePanel.add(new JLabel("Range:"));
        agePanel.add(rangeLow);
        agePanel.add(new JLabel("-"));
        agePanel.add(rangeHigh);
        agePanel.add(rangeBtn);
        agePanel.add(outsideBtn);

        // Beard + Hair panel
        JPanel comboPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        comboPanel.setBorder(BorderFactory.createTitledBorder("Beard & Hair Combo"));

        JComboBox<BeardStyle> beardSelector = new JComboBox<>(BeardStyle.values());
        JComboBox<HairColor> hairSelector = new JComboBox<>(HairColor.values());
        JButton comboCountBtn = new JButton("Count matching");

        comboCountBtn.addActionListener(e -> {
            BeardStyle selectedBeard = (BeardStyle) beardSelector.getSelectedItem();
            HairColor selectedHair = (HairColor) hairSelector.getSelectedItem();
            long cnt = analysisWorker.countWithSpecificBeardAndHair(selectedBeard, selectedHair);
            outputZone.append("Warriors with " + selectedBeard + " beard and " + selectedHair + " hair: " + cnt + "\n");
        });

        comboPanel.add(new JLabel("Beard:"));
        comboPanel.add(beardSelector);
        comboPanel.add(new JLabel("Hair:"));
        comboPanel.add(hairSelector);
        comboPanel.add(comboCountBtn);

        // Axe panel - одна кнопка для 1 или 2 топоров
        JPanel axePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        axePanel.setBorder(BorderFactory.createTitledBorder("Axe Equipment"));

        JButton listAxesBtn = new JButton("Show Warriors with 1 or 2 Axes");

        listAxesBtn.addActionListener(e -> {
            List<Viking> warriorsWithAxes = analysisWorker.getWarriorsWithOneOrTwoAxes();
            resultTableModel.updateData(warriorsWithAxes);
            outputZone.append("Found " + warriorsWithAxes.size() + " warriors with 1 or 2 axes\n");
        });

        axePanel.add(listAxesBtn);

        mainPanel.add(agePanel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(comboPanel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(axePanel);

        return mainPanel;
    }

    private JPanel buildDisplaySection() {
        JPanel container = new JPanel(new BorderLayout());

        JTable displayTable = new JTable(resultTableModel);
        displayTable.setRowHeight(26);

        JPanel actionRow = new JPanel();

        JButton randomTallBtn = new JButton("Pick Random Tall (180+ cm)");
        JButton legendaryBtn = new JButton("Show Legendary Gear Owners");
        JButton redSortedBtn = new JButton("Show Red-haired (sorted by age)");

        randomTallBtn.addActionListener(e -> {
            Optional<Viking> selected = analysisWorker.pickRandomTallWarrior();
            if (selected.isPresent()) {
                resultTableModel.updateData(List.of(selected.get()));
                outputZone.append("Random tall warrior selected\n");
            } else {
                outputZone.append("No warriors taller than 180 cm found\n");
                resultTableModel.updateData(List.of());
            }
        });

        legendaryBtn.addActionListener(e -> {
            List<Viking> legendaryOnes = analysisWorker.fetchAllWithLegendaryGear();
            resultTableModel.updateData(legendaryOnes);
            outputZone.append("Found " + legendaryOnes.size() + " warriors with legendary gear\n");
        });

        redSortedBtn.addActionListener(e -> {
            List<Viking> redSorted = analysisWorker.getRedHairedSortedByIncreasingAge();
            resultTableModel.updateData(redSorted);
            outputZone.append("Displaying " + redSorted.size() + " red-haired warriors\n");
        });

        actionRow.add(randomTallBtn);
        actionRow.add(legendaryBtn);
        actionRow.add(redSortedBtn);

        container.add(new JScrollPane(displayTable), BorderLayout.CENTER);
        container.add(actionRow, BorderLayout.SOUTH);

        return container;
    }

    private JPanel buildIndexSection() {
        JPanel indexPanel = new JPanel(new GridBagLayout());
        indexPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JTextArea indexResultArea = new JTextArea(10, 40);
        indexResultArea.setEditable(false);
        indexResultArea.setFont(new Font("Monospaced", Font.PLAIN, 13));

        JButton maxIdBtn = new JButton("Get Maximum ID");
        JButton evenIdsBtn = new JButton("Get IDs at Even Positions");

        // ← ДОБАВИТЬ ЭТОТ ОБРАБОТЧИК ДЛЯ maxIdBtn
        maxIdBtn.addActionListener(e -> {
            int maxId = analysisWorker.getMaxId();
            if (maxId == -1) {
                indexResultArea.setText("No warriors in storage\n");
            } else {
                indexResultArea.setText("Maximum ID: " + maxId + "\n");
                indexResultArea.append("Total warriors count: " + analysisWorker.getAllIds().length + "\n");
            }
        });

        // Обработчик для evenIdsBtn (оставить один, удалить дубликат)
        evenIdsBtn.addActionListener(e -> {
            List<Integer> evenIds = analysisWorker.getEvenIds();
            if (evenIds.isEmpty()) {
                indexResultArea.setText("No IDs available (list is empty)\n");
            } else {
                indexResultArea.setText("IDs at even positions (0-based index): " + evenIds + "\n");
                indexResultArea.append("Count: " + evenIds.size() + "\n");
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        indexPanel.add(maxIdBtn, gbc);
        gbc.gridx = 1;
        indexPanel.add(evenIdsBtn, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        indexPanel.add(new JScrollPane(indexResultArea), gbc);

        return indexPanel;
    }

    public void showComputedCount(long amount, String description) {
        outputZone.append("Count result [" + description + "]: " + amount + "\n");
    }

    public void showFoundWarriors(List<Viking> warriors, String status) {
        resultTableModel.updateData(warriors);
        outputZone.append(status + "\n");
    }

    public void showMaximumIndex(Optional<Integer> index) {
        if (index.isPresent()) {
            outputZone.append("Maximum index: " + index.get() + "\n");
        } else {
            outputZone.append("No warriors available\n");
        }
    }

    public void showEvenIndexes(List<Integer> indexes) {
        outputZone.append("Even indices: " + indexes + "\n");
    }
}