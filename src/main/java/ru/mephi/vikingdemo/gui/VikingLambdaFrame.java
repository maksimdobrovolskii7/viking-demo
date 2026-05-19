package ru.mephi.vikingdemo.gui;

import ru.mephi.vikingdemo.model.*;
import ru.mephi.vikingdemo.service.VikingLambdaService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class VikingLambdaFrame extends JFrame {
    private final VikingLambdaService lambdaService;
    private JTextArea resultArea;

    public VikingLambdaFrame(VikingLambdaService lambdaService) {
        this.lambdaService = lambdaService;
        setTitle("Lambda Analysis");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel buttonPanel = new JPanel(new GridLayout(6, 2, 5, 5));

        // Кнопки для всех методов анализа
        JButton btnCountOlder = new JButton("Старше 30 лет");
        JButton btnCountYounger = new JButton("Младше 25 лет");
        JButton btnCountRange = new JButton("Возраст 20-40 лет");
        JButton btnCountOutside = new JButton("Вне диапазона 20-50");
        JButton btnBeardHair = new JButton("Борода+Волосы");
        JButton btnAxeCount = new JButton("Кол-во топоров");
        JButton btnRandomTall = new JButton("Случайный высокий");
        JButton btnLegendary = new JButton("Легендарное снаряжение");
        JButton btnRedHaired = new JButton("Рыжие по возрасту");
        JButton btnMaxId = new JButton("Максимальный ID");
        JButton btnEvenPositions = new JButton("ID на четных позициях");
        JButton btnRefresh = new JButton("Обновить");

        buttonPanel.add(btnCountOlder);
        buttonPanel.add(btnCountYounger);
        buttonPanel.add(btnCountRange);
        buttonPanel.add(btnCountOutside);
        buttonPanel.add(btnBeardHair);
        buttonPanel.add(btnAxeCount);
        buttonPanel.add(btnRandomTall);
        buttonPanel.add(btnLegendary);
        buttonPanel.add(btnRedHaired);
        buttonPanel.add(btnMaxId);
        buttonPanel.add(btnEvenPositions);
        buttonPanel.add(btnRefresh);

        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        add(buttonPanel, BorderLayout.NORTH);
        add(new JScrollPane(resultArea), BorderLayout.CENTER);

        // Обработчики событий
        btnCountOlder.addActionListener(e -> {
            long count = lambdaService.getWarriorsOlderThan(30);
            resultArea.setText("Викингов старше 30 лет: " + count);
        });

        btnCountYounger.addActionListener(e -> {
            long count = lambdaService.getWarriorsYoungerThan(25);
            resultArea.setText("Викингов младше 25 лет: " + count);
        });

        btnCountRange.addActionListener(e -> {
            long count = lambdaService.getWarriorsAgeBetween(20, 40);
            resultArea.setText("Викингов в возрасте 20-40 лет: " + count);
        });

        btnCountOutside.addActionListener(e -> {
            long count = lambdaService.getWarriorsAgeOutsideRange(20, 50);
            resultArea.setText("Викингов вне диапазона 20-50 лет: " + count);
        });

        btnBeardHair.addActionListener(e -> {
            BeardStyle beard = BeardStyle.LONG_BEARD;
            HairColor hair = HairColor.RED;
            long count = lambdaService.countWithSpecificBeardAndHair(beard, hair);
            resultArea.setText("Викингов с длинной бородой и рыжими волосами: " + count);
        });

        btnAxeCount.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(this, "Количество топоров (1 или 2):", "1");
            try {
                int count = Integer.parseInt(input);
                long result = lambdaService.countWarriorsWithAxeCount(count);
                resultArea.setText("Викингов с " + count + " топором(ами): " + result);
            } catch (Exception ex) {
                resultArea.setText("Ошибка ввода");
            }
        });

        btnRandomTall.addActionListener(e -> {
            var result = lambdaService.pickRandomTallWarrior();
            if (result.isPresent()) {
                Viking v = result.get();
                resultArea.setText("Случайный высокий викинг:\n" +
                        "Имя: " + v.getName() + "\n" +
                        "Возраст: " + v.getAge() + "\n" +
                        "Рост: " + v.getHeightCm() + " см");
            } else {
                resultArea.setText("Нет викингов выше 180 см");
            }
        });

        btnLegendary.addActionListener(e -> {
            List<Viking> legendary = lambdaService.fetchAllWithLegendaryGear();
            StringBuilder sb = new StringBuilder("Викинги с легендарным снаряжением:\n\n");
            for (Viking v : legendary) {
                sb.append(v.getName()).append(" - ").append(v.getAge()).append(" лет\n");
            }
            resultArea.setText(sb.toString());
        });

        btnRedHaired.addActionListener(e -> {
            List<Viking> redHaired = lambdaService.getRedHairedSortedByIncreasingAge();
            StringBuilder sb = new StringBuilder("Рыжеволосые викинги по возрастанию возраста:\n\n");
            for (Viking v : redHaired) {
                sb.append(v.getName()).append(" - ").append(v.getAge()).append(" лет\n");
            }
            resultArea.setText(sb.toString());
        });

        btnMaxId.addActionListener(e -> {
            var maxId = lambdaService.locateMaxId();
            resultArea.setText("Максимальный ID: " + maxId.orElse("Нет викингов"));
        });

        btnEvenPositions.addActionListener(e -> {
            List<String> ids = lambdaService.extractEvenPositionIds();
            StringBuilder sb = new StringBuilder("ID на четных позициях:\n\n");
            for (int i = 0; i < ids.size(); i++) {
                sb.append(i * 2).append(": ").append(ids.get(i)).append("\n");
            }
            resultArea.setText(sb.toString());
        });

        btnRefresh.addActionListener(e -> {
            resultArea.setText("Данные обновлены. Выберите операцию.");
        });
    }
}