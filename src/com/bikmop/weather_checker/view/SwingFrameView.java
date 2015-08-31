package com.bikmop.weather_checker.view;

import com.bikmop.weather_checker.Controller;
import com.bikmop.weather_checker.model.strategy.Provider;
import com.bikmop.weather_checker.weather.Location;
import com.bikmop.weather_checker.weather.Weather;

import javax.swing.*;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

public class SwingFrameView implements View {
    @Override
    public void update(List<Map<Integer, Weather>> forecasts) {

    }

    @Override
    public void setController(Controller controller) {

    }

    @Override
    public void init(List<Provider> providers, List<Location> locations, boolean isUa) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame.setDefaultLookAndFeelDecorated(true);
                createGUI(providers, locations, isUa);
            }
        });
    }

    private static void createGUI(List<Provider> providers, List<Location> locations, boolean isUa) {

        String title;
        if (isUa) {
            title = "Мультипрогноз погоди";
        } else {
            title = "Мультипрогноз погоды";
        }

        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

//        getPlaceList();

        String resources = providers.get(0).getStrategy().getDirectoryPath().replaceAll("/", ".");
        Properties props = new Properties();
        ResourceBundle res = ResourceBundle.getBundle("." + resources + "fields_ru");

        Object[] columnNames = {"", "00:00", "03:00", "06:00", "09:00", "12:00", "15:00", "18:00", "21:00"};


        Object[][] tableData = new Object[][]{ {"", "", "", "", "", "", "", "", ""},
                {"", "00:00", "03:00", "06:00", "09:00", "12:00", "15:00", "18:00", "21:00"},
                {"", "", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", "", ""},
                {"Температура, ℃:", "", "", "", "", "", "", "", ""},
                {"Відчувається, ℃:", "", "", "", "", "", "", "", ""},
                {"Опади, % ймов.:", "", "", "", "", "", "", "", ""},
                {"Вітер, м/с:", "", "", "", "", "", "", "", ""},
                {"Вологість, %:", "", "", "", "", "", "", "", ""},
                {"Тиск, мм:", "", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", "", "",},
                {"", "", "", "", "", "", "", "", "",},
                {"", "", "", "", "", "", "", "", ""},
                {"Температура, ℃:", "", "", "", "", "", "", "", ""},
                {"Відчувається, ℃:", "", "", "", "", "", "", "", ""},
                {"Вітер, м/с:", "", "", "", "", "", "", "", ""},
                {"Вологість, %:", "", "", "", "", "", "", "", ""},
                {"Тиск, мм:", "", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", "", "",},
                {"", "", "", "", "", "", "", "", "",},
                {"", "", "", "", "", "", "", "", ""},
                {"Температура, ℃:", "", "", "", "", "", "", "", ""},
                {"Відчувається, ℃:", "", "", "", "", "", "", "", ""},
                {"Опади, % ймов.:", "", "", "", "", "", "", "", ""},
                {"Опади, опис:", "", "", "", "", "", "", "", ""},
                {"Вітер, м/с:", "", "", "", "", "", "", "", ""},
                {"Вологість, %:", "", "", "", "", "", "", "", ""},
                {"Тиск, мм:", "", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", "", "",} };



//        /*JTable table = new JTable(new RssFeedTableModel());
//table.setShowGrid(false);
//table.setIntercellSpacing(new Dimension(0, 0));
//table.setRowHeight(30);
//table.setTableHeader(null);*/
//
//
///*        JLabel titleLabel = new JLabel("Xakep RSS");
//        Font titleFont = new Font("Arial", Font.BOLD, 20);
//        titleLabel.setFont(titleFont);
//        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
//        titleLabel.setForeground(Color.WHITE);
//        titleLabel.setPreferredSize(new Dimension(0, 40));
//        frame.getContentPane().add(titleLabel, BorderLayout.NORTH);*/
//
////        JTable table = new JTable(data, columnNames);
//        final JTable table2 = new WeatherTable(tableData, columnNames);
//        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
//        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
//
////        table2.setValueAt(sinoptik.get(0).getTemperature().toString(), 1, 1);
//
//        getPlaces_ua();
//
//        table2.getColumnModel().getColumn(0).setCellRenderer(new WeatherTableRenderer());
//        table2.getColumnModel().getColumn(1).setCellRenderer(new WeatherTableRenderer());
//        table2.getColumnModel().getColumn(2).setCellRenderer(new WeatherTableRenderer());
//        table2.getColumnModel().getColumn(3).setCellRenderer(new WeatherTableRenderer());
//        table2.getColumnModel().getColumn(4).setCellRenderer(new WeatherTableRenderer());
//        table2.getColumnModel().getColumn(5).setCellRenderer(new WeatherTableRenderer());
//        table2.getColumnModel().getColumn(6).setCellRenderer(new WeatherTableRenderer());
//        table2.getColumnModel().getColumn(7).setCellRenderer(new WeatherTableRenderer());
//        table2.getColumnModel().getColumn(8).setCellRenderer(new WeatherTableRenderer());
//
//        for (int i = 1; i < table2.getColumnCount(); i++) {
//            table2.getColumnModel().getColumn(i).setMinWidth(70);
//            table2.getColumnModel().getColumn(i).setMaxWidth(70);
////            table2.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
//        }
//
//        for (int i = 0; i < table2.getRowCount(); i++) {
//            table2.setRowHeight(i, 17);
//        }
//
//        table2.getColumnModel().getColumn(0).setMinWidth(140);
//        table2.getColumnModel().getColumn(0).setMaxWidth(140);
//        table2.setRowHeight(4, 40);
//        table2.setRowHeight(3, 1);
//        table2.setRowHeight(13, 55);
//        table2.setRowHeight(21, 64);
//        table2.setRowHeight(1, 23);
//        table2.setRowHeight(0, 23);
//        table2.setRowHeight(2, 30);
//        table2.setRowHeight(29, 15);
//        table2.setRowHeight(20, 30);
//        table2.setRowHeight(12, 30);
//        table2.setRowHeight(11, 1);
//        table2.setRowHeight(19, 1);
////        table2.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
//
//
//
////        table.setTableHeader(null);
//
//        table2.setShowGrid(false);
//        table2.setShowVerticalLines(true);
//
//        table2.getSelectionModel().addListSelectionListener(new ListSelectionListener()
//        {
//            public void valueChanged(ListSelectionEvent e)
//            {
//                String[] tmp = e.getSource().toString().split("=");
//                if ((tmp.length > 1) && (table2.getSelectedColumn() == 0) && (tmp[tmp.length - 1].split(",").length == 1)) {
//
//                    String urlStr = null;
//                    if (table2.getSelectedRow() == 4) {
//                        urlStr = sinURL;
//                    } else if (table2.getSelectedRow() == 13) {
//                        urlStr = gisURL;
//                    } else if (table2.getSelectedRow() == 21) {
//                        urlStr = wwoURL;
//                    }
//
//                    if (urlStr != null) {
//                        try {
//                            Desktop.getDesktop().browse(new URI(urlStr));
//                        } catch (IOException | URISyntaxException ignore) {}
//                    }
//                }
//
///*                System.out.println("1:" + e.getSource());
//                System.out.println("2:" + e.getFirstIndex());
//                System.out.println("3:" + e.getLastIndex());
//                System.out.println("4:" + table2.getSelectedColumn());
//                System.out.println("5:" + table2.getSelectedRow());*/
//            }
//        });
//
////        JScrollPane scrollPane = new JScrollPane(table);
////        JScrollPane scrollPane2 = new JScrollPane(table2);
//
////        frame.add(table, BorderLayout.NORTH);
//        frame.add(table2, BorderLayout.PAGE_START);
//
//
//        final String[] itemsShiftDays = {"Сьогодні", "Завтра", "+2", "+3", "+4", "+5", "+6"};
//        final JComboBox comboShiftDays = new JComboBox(itemsShiftDays);
////        comboShiftDays.setAlignmentX(CENTER_ALIGNMENT);
//        final String currentItem = "";
//
//
//
//
//        JPanel panel = new JPanel();
//
//        final JButton getPrevDay = new JButton("<<");
//        getPrevDay.setEnabled(false);
//        panel.add(getPrevDay);
//
////        JPanel panel2 = new JPanel();
////        panel.setLayout(new GridLayout(4,0));
////        panel2.setLayout(new GridLayout(2,0));
//        panel.add(new Label("        День:"));
////        panel2.add(new Label("TEST222"));
////        panel.setWidth(new Dimension(20, 50));
//
////        frame.add(panel2, BorderLayout.PAGE_END);
//
//
//
////        comboShiftDays.addActionListener(actionListener);
//        panel.add(comboShiftDays);
//        panel.add(new Label("         Населений пункт:"));
//
//        String[] itemsPlace = getPlaces_ua();
//        final JComboBox comboPlace = new JComboBox(itemsPlace);
////        final String currentPlace = "";
//
//        panel.add(comboPlace);
//
//
//
//
//        panel.add(new Label("    "));
////        frame.add(comboShiftDays, BorderLayout.AFTER_LAST_LINE);
//
//        final JButton getWeather = new JButton("Отримати");
//
//
//        ActionListener actionListenerGetWeatherButton = new ActionListener() {
//
//            public void actionPerformed(ActionEvent e) {
////                JComboBox box = (JComboBox)e.getSource();
//                String item = (String) comboShiftDays.getSelectedItem();
//                final String placeName = (String) comboPlace.getSelectedItem();
//
//                clearTable(table2);
//
//                int shift = 0;
//                switch (item) {
//                    case "Завтра":
//                        shift = 1;
//                        break;
//                    case "+2":
//                        shift = 2;
//                        break;
//                    case "+3":
//                        shift = 3;
//                        break;
//                    case "+4":
//                        shift = 4;
//                        break;
//                    case "+5":
//                        shift = 5;
//                        break;
//                    case "+6":
//                        shift = 6;
//                        break;
//                }
//
//                final int finalShift = shift;
//
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        fillTable(table2, placeName, finalShift, true);
//                    }
//                }).start();
//
//            }
//        };
//
//        getPrevDay.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                int currentIndex = comboShiftDays.getSelectedIndex();
//                if (currentIndex > 0) {
//                    comboShiftDays.setSelectedIndex(currentIndex - 1);
//                    getWeather.doClick();
//                }
//            }
//        });
//
//        getWeather.addActionListener(actionListenerGetWeatherButton);
//        panel.add(getWeather);
//
//        panel.add(new Label("    "));
//
//        final JButton getNextDay = new JButton(">>");
//        getNextDay.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                int currentIndex = comboShiftDays.getSelectedIndex();
//                if (currentIndex < 6) {
//                    comboShiftDays.setSelectedIndex(currentIndex + 1);
//                    getWeather.doClick();
//                }
//            }
//        });
//        panel.add(getNextDay);
//
//        comboShiftDays.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                int currentIndex = comboShiftDays.getSelectedIndex();
//                if (currentIndex == 0) {
//                    getPrevDay.setEnabled(false);
//                } else if (currentIndex == 6) {
//                    getNextDay.setEnabled(false);
//                }
//                if (!getPrevDay.isEnabled() && currentIndex != 0) {
//                    getPrevDay.setEnabled(true);
//                }
//                if (!getNextDay.isEnabled() && currentIndex != 6) {
//                    getNextDay.setEnabled(true);
//                }
//            }
//        });
//
//        frame.add(panel, BorderLayout.AFTER_LAST_LINE);
//
//
////        comboPlace.setAlignmentX(CENTER_ALIGNMENT);
//
////        table2.getCellRenderer(0, 0).
//
////        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
////        frame.getContentPane().add(scrollPane2, BorderLayout.CENTER);
////        frame.setPreferredSize(new Dimension(1100, 500));

        frame.setIconImage(new ImageIcon("resources/icon.png").getImage());

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setResizable(false);
    }


}
