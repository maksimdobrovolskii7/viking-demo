package ru.mephi.vikingdemo.gui;

import ru.mephi.vikingdemo.model.EquipmentItem;
import ru.mephi.vikingdemo.model.Viking;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class VikingTableModel extends AbstractTableModel {

    private final String[] columns = {"Имя", "Возраст", "Рост (см)", "Цвет волос", "Стиль бороды", "Экипировка"};
    private final List<Viking> data = new ArrayList<>();

    public void addViking(Viking viking) {
        int row = data.size();
        data.add(viking);
        fireTableRowsInserted(row, row);
    }

    public void updateData(List<Viking> vikings) {
        data.clear();
        data.addAll(vikings);
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Viking viking = data.get(rowIndex);

        // Перевод цвета волос на русский
        String hairColorRu = switch (viking.getHairColor()) {
            case BLOND -> "Блондин";
            case RED -> "Рыжий";
            case BROWN -> "Шатен";
            case BLACK -> "Чёрный";
            case GREY -> "Седой";
            case WHITE -> "Белый";
        };

        // Перевод стиля бороды на русский
        String beardStyleRu = switch (viking.getBeardStyle()) {
            case CLEAN_SHAVEN -> "Бритый";
            case SHORT_BEARD -> "Короткая борода";
            case LONG_BEARD -> "Длинная борода";
            case BRAIDED_BEARD -> "Заплетённая борода";
            case FORKED_BEARD -> "Раздвоенная борода";
            case MUSTACHE_ONLY -> "Только усы";
        };

        return switch (columnIndex) {
            case 0 -> viking.getName();
            case 1 -> viking.getAge() + " лет";
            case 2 -> viking.getHeightCm() + " см";
            case 3 -> hairColorRu;
            case 4 -> beardStyleRu;
            case 5 -> formatEquipment(viking.getEquipment());
            default -> "";
        };
    }

    private String formatEquipment(List<EquipmentItem> equipment) {
        if (equipment == null || equipment.isEmpty()) {
            return "Нет экипировки";
        }
        return equipment.stream()
                .map(item -> item.getName() + " (" + item.getType() + ")")
                .collect(Collectors.joining(", "));
    }

    public String getVikingIdAt(int row) {
        return data.get(row).getId();
    }

}