package com.bikmop.weather_checker.view;

import com.bikmop.weather_checker.Controller;
import com.bikmop.weather_checker.model.strategy.Provider;
import com.bikmop.weather_checker.weather.Location;
import com.bikmop.weather_checker.weather.Weather;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.List;

public class SwingFrameView extends JFrame implements View {


    private static List<Provider> providers;
    private static List<Location> locations;
    private static boolean isUa;
    private static List<Properties> rowsNames;
    private static Properties frameText;

    @Override
    public void init(List<Provider> providers, List<Location> locations, boolean isUa) {

        SwingFrameView.providers = providers;
        SwingFrameView.locations = locations;
        SwingFrameView.isUa = isUa;

        // Get rows names for View-frame (from the properties files)
        List<Properties> props = new ArrayList<>();
        String resources;
        String fileName;
        if (isUa) {
            fileName = "fields_ua.properties";
        } else {
            fileName = "fields_ru.properties";
        }
        for (Provider provider : providers) {
            resources = provider.getStrategy().getDirectoryPath();

            try (FileInputStream in = new FileInputStream(resources + fileName)) {
                Properties properties = new Properties();
                properties.load(in);
                props.add(properties);
            } catch (IOException e) {
                // TODO - add to log
            }
        }
        rowsNames = props;

        // Get another frame text
        if (isUa) {
            fileName = "resources/frame_ua.properties";
        } else {
            fileName = "resources/frame_ru.properties";
        }
        Properties frameText = new Properties();
        try (FileInputStream in = new FileInputStream(fileName)) {
            frameText.load(in);
        } catch (IOException e) {
            // TODO - add to log
        }

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame.setDefaultLookAndFeelDecorated(true);
                createGUI();
            }
        });
    }



    static class WeatherTable extends JTable {

        public boolean isCellEditable(int row, int column){
            return false;
        }

        public WeatherTable(Object[][] rowData, Object[] columnNames) {
            super(rowData, columnNames);
        }

/*        @Override
        public String getToolTipText(MouseEvent event) {
            String result = "";
            int column = columnAtPoint(event.getPoint());
            int row = rowAtPoint(event.getPoint());
            if (column != - 1 && row != -1) {
                result = toolTipText[row][column];
            }
            return result;
        }*/
    }




    static class WeatherTableRenderer extends DefaultTableCellRenderer
    {
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
        {
            Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            this.setHorizontalAlignment(JLabel.CENTER);

            // Font types
            if (column == 0) {
                // Set properties for text in the first column
                cellComponent.setFont(new Font("", Font.BOLD, 13));
                if (row == 0) {
                    // Location properties
                    cellComponent.setFont(new Font("Tahoma", Font.ITALIC + Font.BOLD, 20));
                }
            }
            if (row == 1) {
                if (column == 0) {
                    // Date
                    cellComponent.setFont(new Font("", Font.BOLD, 17));
                } else {
                    // Times
                    cellComponent.setFont(new Font("", Font.BOLD, 14));
                }
            }
            if (row == 2 && column == 0) {
                // Day of week
                cellComponent.setFont(new Font("", Font.BOLD, 17));
            }

            int currentRaw = 3;
            for (Properties prop : rowsNames) {
                if (prop.getProperty("picture.height") != null)
                    currentRaw++;
                // Temperature
                if (prop.getProperty("temperature") != null && row == currentRaw++ && column > 0)
                    cellComponent.setFont(new Font("", Font.BOLD, 16));
                // Feel
                if (prop.getProperty("feel") != null && row == currentRaw++ && column > 0)
                    cellComponent.setFont(new Font("", Font.BOLD, 11));
                // Precipitation probability
                if (prop.getProperty("precipitation.probability") != null && row == currentRaw++ && column > 0)
                    cellComponent.setFont(new Font("", Font.ITALIC, 14));
                // Precipitation description
                if (prop.getProperty("precipitation.description") != null && row == currentRaw++ && column > 0)
                    cellComponent.setFont(new Font("", Font.ITALIC, 14));
                // Wind
                if (prop.getProperty("wind") != null && row == currentRaw++  && column > 0)
                    cellComponent.setFont(new Font("", Font.BOLD, 14));
                if (prop.getProperty("humidity") != null)
                    currentRaw++;
                if (prop.getProperty("pressure") != null)
                    currentRaw++;
                //Blank rows
                if (row == currentRaw++ && column > 0)
                    currentRaw++;
            }


            // Background colors
            cellComponent.setBackground(Color.WHITE);
            // Light grey color background for first three rows
            if (row < 3 || column == 0)
                cellComponent.setBackground(new Color(250, 250, 250));

            currentRaw = 3;
            for (Properties prop : rowsNames) {
                if (prop.getProperty("picture.height") != null)
                    currentRaw++;
                // Temperature
                if (prop.getProperty("temperature") != null && row == currentRaw++ && column > 0 && value != "")
                    cellComponent.setBackground(new Color(240, 255, 240));
                // Feel
                if (prop.getProperty("feel") != null && row == currentRaw++ && column > 0 && value != "")
                    cellComponent.setBackground(new Color(240, 255, 240));
                // Precipitation probability
                if (prop.getProperty("precipitation.probability") != null && row == currentRaw++ && column > 0 && value != "")
                    cellComponent.setBackground(new Color(230, 230, 230));
                // Precipitation description
                if (prop.getProperty("precipitation.description") != null && row == currentRaw++ && column > 0 && value != "")
                    cellComponent.setBackground(new Color(230, 230, 230));
                if (prop.getProperty("wind") != null)
                    currentRaw++;
                if (prop.getProperty("humidity") != null)
                    currentRaw++;
                if (prop.getProperty("pressure") != null)
                    currentRaw++;
                //Blank rows
                if (row == currentRaw++ && column > 0)
                    cellComponent.setBackground(new Color(250, 250, 250));
            }


            // Font colors
            cellComponent.setForeground(Color.BLACK);
            if (row == 1 && column == 0) {
                // Date color
                cellComponent.setForeground(Color.BLUE);
            } else if (row == 2 && column == 0) {
                // Day of week color
                cellComponent.setForeground(Color.RED);
            } else if (row == 0 && column == 0) {
                // Location color
                cellComponent.setForeground(new Color(0, 128, 0));
            }

            currentRaw = 3;
            for (Properties prop : rowsNames) {
                if (prop.getProperty("picture.height") != null)
                    currentRaw++;
                    // Temperature font color
                if (prop.getProperty("temperature") != null && row == currentRaw++ && column > 0)
                    cellComponent.setForeground(new Color(0, 100, 150));
                if (prop.getProperty("feel") != null)
                    currentRaw++;
                if (prop.getProperty("precipitation.probability") != null)
                    currentRaw++;
                if (prop.getProperty("precipitation.description") != null)
                    currentRaw++;
                if (prop.getProperty("wind") != null && row == currentRaw++  && column > 0)
                    // Wind color
                    cellComponent.setForeground(new Color(200, 60, 60));
                if (prop.getProperty("humidity") != null)
                    currentRaw++;
                if (prop.getProperty("pressure") != null)
                    currentRaw++;
                currentRaw++;
            }




            // Pictures of providers and weather
//            this.setIcon(null);
//            if (column == 0 && row == 4) {
//                this.setIcon(new ImageIcon("resources/sinoptik/provider.gif"));
//            } else if (column == 0 && row == 13) {
//                this.setIcon(new ImageIcon("resources/gismeteo/provider.gif"));
//            } else if (column == 0 && row == 21) {
//                this.setIcon(new ImageIcon("resources/wwo/provider.gif"));
//            } else if (row == 4 && column > 0) {
//                this.setIcon(new ImageIcon(sinPicPath[column - 1]));
//            } else if (row == 13 && column > 0) {
//                this.setIcon(new ImageIcon(gisPicPath[column - 1]));
//            } else if (row == 21 && column > 0) {
//                this.setIcon(new ImageIcon(wwoPicPath[column - 1]));
//            } else {
//                this.setIcon(null);
//            }

            return cellComponent;
        }
    }


    @Override
    public void update(List<Map<Integer, Weather>> forecasts) {

    }

    @Override
    public void setController(Controller controller) {

    }



    private static void createGUI() {

        String title;
        if (isUa) {
            title = "Мультипрогноз погоди";
        } else {
            title = "Мультипрогноз погоды";
        }

        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

//        getPlaceList();

/*        Properties props1 = new Properties();
        try {
            String resources = providers.get(0).getStrategy().getDirectoryPath();

            FileOutputStream out = new FileOutputStream("resources/frame_ru.properties");
            props1.setProperty("title", "Мультипрогноз погоды");
            props1.setProperty("day", "День:");
            props1.setProperty("day0", "Сегодня");
            props1.setProperty("day1", "Завтра");
            props1.setProperty("day2", "+2");
            props1.setProperty("day3", "+3");
            props1.setProperty("day4", "+4");
            props1.setProperty("day5", "+5");
            props1.setProperty("day6", "+6");
            props1.setProperty("location", "Населённый пункт:");



            props1.store(out, null);
            FileOutputStream out = new FileOutputStream(resources + "fields_ua.properties");
            props.setProperty("temperature", "Температура, ℃:");
            props.setProperty("feel", "Відчувається, ℃:");
            props.setProperty("precipitation.probability", "Опади, % ймов.:");
            props.setProperty("precipitation.description", "Опади, опис:");
            props.setProperty("wind", "Вітер, м/с:");
            props.setProperty("humidity", "Вологість, %:");
            props.setProperty("pressure", "Тиск, мм:");
            props.store(out, null);

            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }*/


        // rowsNumber - total number of rows in the View-table
        // First three rows - location, date, day of week
        int rowsNumber = 3;
        for (Properties properties : rowsNames) {
            // + blank row
            rowsNumber += properties.size() + 1;
        }

        Object[] columnNames = {"", "00:00", "03:00", "06:00", "09:00", "12:00", "15:00", "18:00", "21:00"};


        Object[][] tableData = new Object[rowsNumber][9];
        for (int i = 0; i < rowsNumber; i++) {
            for (int j = 0; j < 9; j++) {
                tableData[i][j] = "";
            }
        }
        // Times string for View
//        tableData[1][0] = "";
        tableData[1][1] = "00:00";
        tableData[1][2] = "03:00";
        tableData[1][3] = "06:00";
        tableData[1][4] = "09:00";
        tableData[1][5] = "12:00";
        tableData[1][6] = "15:00";
        tableData[1][7] = "18:00";
        tableData[1][8] = "21:00";

        // Fill first column with Weather parameter names depends of Provider
        int currentRaw = 3;
        for (Properties prop : rowsNames) {
            String currentField = prop.getProperty("picture.height");
            if (currentField != null)
                tableData[currentRaw++][0] = "";
            currentField = prop.getProperty("temperature");
            if (currentField != null)
                tableData[currentRaw++][0] = currentField;
            currentField = prop.getProperty("feel");
            if (currentField != null)
                tableData[currentRaw++][0] = currentField;
            currentField = prop.getProperty("precipitation.probability");
            if (currentField != null)
                tableData[currentRaw++][0] = currentField;
            currentField = prop.getProperty("precipitation.description");
            if (currentField != null)
                tableData[currentRaw++][0] = currentField;
            currentField = prop.getProperty("wind");
            if (currentField != null)
                tableData[currentRaw++][0] = currentField;
            currentField = prop.getProperty("humidity");
            if (currentField != null)
                tableData[currentRaw++][0] = currentField;
            currentField = prop.getProperty("pressure");
            if (currentField != null)
                tableData[currentRaw++][0] = currentField;
            currentRaw++;
        }


        final JTable table = new WeatherTable(tableData, columnNames);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

//        table2.setValueAt(sinoptik.get(0).getTemperature().toString(), 1, 1);
//
//        getPlaces_ua();

        table.getColumnModel().getColumn(0).setCellRenderer(new WeatherTableRenderer());
        table.getColumnModel().getColumn(1).setCellRenderer(new WeatherTableRenderer());
        table.getColumnModel().getColumn(2).setCellRenderer(new WeatherTableRenderer());
        table.getColumnModel().getColumn(3).setCellRenderer(new WeatherTableRenderer());
        table.getColumnModel().getColumn(4).setCellRenderer(new WeatherTableRenderer());
        table.getColumnModel().getColumn(5).setCellRenderer(new WeatherTableRenderer());
        table.getColumnModel().getColumn(6).setCellRenderer(new WeatherTableRenderer());
        table.getColumnModel().getColumn(7).setCellRenderer(new WeatherTableRenderer());
        table.getColumnModel().getColumn(8).setCellRenderer(new WeatherTableRenderer());

        for (int i = 1; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setMinWidth(70);
            table.getColumnModel().getColumn(i).setMaxWidth(70);
        }

        table.getColumnModel().getColumn(0).setMinWidth(140);
        table.getColumnModel().getColumn(0).setMaxWidth(140);
        for (int i = 0; i < table.getRowCount(); i++) {
            table.setRowHeight(i, 17);
        }
        table.setRowHeight(0, 23);
        table.setRowHeight(1, 23);
        table.setRowHeight(2, 30);

        currentRaw = 3;
        int currentProp = 0;
        for (Properties prop : rowsNames) {

            // default if no parameters in init-file
            int pictureHeight = 0;
            try {
                pictureHeight = Integer.parseInt(prop.getProperty("picture.height"));
            } catch (NumberFormatException e) {
                // TODO - add to log
            }
            table.setRowHeight(currentRaw++, pictureHeight);

            if (prop.getProperty("temperature") != null)
                currentRaw++;
            if (prop.getProperty("feel") != null)
                currentRaw++;
            if (prop.getProperty("precipitation.probability") != null)
                currentRaw++;
            if (prop.getProperty("precipitation.description") != null)
                currentRaw++;
            if (prop.getProperty("wind") != null)
                currentRaw++;
            if (prop.getProperty("humidity") != null)
                currentRaw++;
            if (prop.getProperty("pressure") != null)
                currentRaw++;

            if (currentProp == rowsNames.size() - 1) {
                // last row of the table
                table.setRowHeight(currentRaw++, 15);
            } else {
                table.setRowHeight(currentRaw++, 30);
            }

            currentProp++;
        }

        table.setShowGrid(false);
        table.setShowVerticalLines(true);



        frame.add(table, BorderLayout.PAGE_START);

//        System.out.println("⟳");


//        final JButton getWeather = new JButton("↻");



        frame.setIconImage(new ImageIcon("resources/icon.png").getImage());

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setResizable(false);
    }


}
