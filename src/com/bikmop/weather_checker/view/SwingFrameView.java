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
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;


/** View implementation using Swing and Awt */
public class SwingFrameView extends JFrame implements View {

    // Controller
    private Controller controller;
    // Weather providers (to get resources)
    private static List<Provider> providers;
    // Geographical locations for selection
    private static List<Location> locations;
    // If Ukrainian language
    private static boolean isUa;
    // Text for View from properties-files
    private static List<Properties> rowsNames;
    private static Properties frameText;
    // Path for picture-files
    private static List<String[]> picPath;
    // JTable of View
    private static JTable mainTable;
    // Array for tooltip text
    private static String[][] toolTipText;
    // Map to store internet-address and row number to open link when clicked
    private static Map<Integer, String> clickedLinks = new HashMap<>();


    @Override
    /** Initialization - get parameters from files and create GUI-Frame */
    public void init(List<Provider> providers, List<Location> locations, boolean isUa) {

        SwingFrameView.providers = providers;
        SwingFrameView.locations = locations;
        SwingFrameView.isUa = isUa;

        // Init weather-pictures path
        picPath = new ArrayList<>();

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
            // Add blank path for weather-pictures
            picPath.add(new String[]{"", "", "", "", "", "", "", ""});

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


        // rowsNumber - total number of rows in the View-table
        // First three rows - location, date, day of week
        int rowsNumber = 3;
        for (Properties properties : rowsNames) {
            // + blank row
            rowsNumber += properties.size() + 1;
        }
        toolTipText = new String[rowsNumber][9];


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
        SwingFrameView.frameText = frameText;


        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame.setDefaultLookAndFeelDecorated(true);
                createGUI();
            }
        });
    }



    @Override
    public void update(List<Map<Integer, Weather>> forecasts) {
        fillTable(mainTable, forecasts);
    }

    @Override
    public void setController(Controller controller) {
        this.controller = controller;
    }




    /** Table to display weather forecasts data */
    static class WeatherTable extends JTable {

        public WeatherTable(Object[][] rowData, Object[] columnNames) {
            super(rowData, columnNames);
        }

        // Not editable cells
        public boolean isCellEditable(int row, int column){
            return false;
        }


        @Override
        // tooltips handling
        public String getToolTipText(MouseEvent event) {
            String result = "";
            int column = columnAtPoint(event.getPoint());
            int row = rowAtPoint(event.getPoint());
            if (column != -1 && row != -1) {
                result = toolTipText[row][column];
            }
            return result;
        }
    }



    //** Renderer to set parameters of table cells */
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

            // Background colors
            cellComponent.setBackground(Color.WHITE);
            // Light grey color background for first three rows
            if (row < 3 || column == 0)
                cellComponent.setBackground(new Color(250, 250, 250));

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

            this.setIcon(null);


            // Cells parameters
            int currentRaw = 3;
            int providerNumber = 0;
            for (Properties prop : rowsNames) {
                // Pictures
                if (prop.getProperty("picture.height") != null && row == currentRaw++) {
                    if (column == 0)
                        this.setIcon(new ImageIcon(providers.get(providerNumber).getStrategy().getDirectoryPath() + "provider.gif"));
                    else
                        this.setIcon(new ImageIcon(picPath.get(providerNumber)[column - 1]));
                }
                // Temperature
                if (prop.getProperty("temperature") != null && row == currentRaw++ && column > 0 && value != "") {
                    cellComponent.setFont(new Font("", Font.BOLD, 16));
                    cellComponent.setBackground(new Color(240, 255, 240));
                    cellComponent.setForeground(new Color(0, 100, 150));
                }
                // Feel
                if (prop.getProperty("feel") != null && row == currentRaw++ && column > 0 && value != "") {
                    cellComponent.setFont(new Font("", Font.BOLD, 11));
                    cellComponent.setBackground(new Color(240, 255, 240));
                }
                // Precipitation probability
                if (prop.getProperty("precipitation.probability") != null && row == currentRaw++ && column > 0 && value != "") {
                    cellComponent.setFont(new Font("", Font.ITALIC, 14));
                    cellComponent.setBackground(new Color(230, 230, 230));
                }
                // Precipitation description
                if (prop.getProperty("precipitation.description") != null && row == currentRaw++ && column > 0 && value != "") {
                    cellComponent.setFont(new Font("", Font.ITALIC, 14));
                    cellComponent.setBackground(new Color(230, 230, 230));
                }
                // Wind
                if (prop.getProperty("wind") != null && row == currentRaw++  && column > 0) {
                    cellComponent.setFont(new Font("", Font.BOLD, 14));
                    cellComponent.setForeground(new Color(200, 60, 60));
                }
                if (prop.getProperty("humidity") != null)
                    currentRaw++;
                if (prop.getProperty("pressure") != null)
                    currentRaw++;
                //Blank rows
                if (row == currentRaw++ && column > 0) {
                    cellComponent.setBackground(new Color(250, 250, 250));
                }
                providerNumber++;
            }

            return cellComponent;
        }
    }


    /** Create JFrame for GUI */
    private static void createGUI() {

        JFrame frame = new JFrame(frameText.getProperty("title"));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

/*        Properties props1 = new Properties();
        try {
            String resources = providers.get(0).getStrategy().getDirectoryPath();

            FileOutputStream out = new FileOutputStream("resources/frame_ru.properties");
//            props1.setProperty("title", "Мультипрогноз погоди");
            props1.setProperty("title", "Мультипрогноз погоды");
            props1.setProperty("day", "День:");
//            props1.setProperty("day0", "Сьогодні");
            props1.setProperty("day0", " Сегодня");
            props1.setProperty("day1", "Завтра");
            props1.setProperty("day2", "+2");
            props1.setProperty("day3", "+3");
            props1.setProperty("day4", "+4");
            props1.setProperty("day5", "+5");
            props1.setProperty("day6", "+6");
//            props1.setProperty("location", " Населений пункт:");
            props1.setProperty("location", "Населённый пункт:");
            props1.setProperty("previous", "<<");
            props1.setProperty("next", ">>");
            props1.setProperty("refresh", "↻");
            props1.setProperty("ua", "UA");
            props1.setProperty("ru", "RU");



            props1.store(out, null);
//            FileOutputStream out = new FileOutputStream(resources + "fields_ua.properties");
//            props.setProperty("temperature", "Температура, ℃:");
//            props.setProperty("feel", "Відчувається, ℃:");
//            props.setProperty("precipitation.probability", "Опади, % ймов.:");
//            props.setProperty("precipitation.description", "Опади, опис:");
//            props.setProperty("wind", "Вітер, м/с:");
//            props.setProperty("humidity", "Вологість, %:");
//            props.setProperty("pressure", "Тиск, мм:");
//            props.store(out, null);

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

        // Fill default cells data before table creation
        Object[][] tableData = new Object[rowsNumber][9];
        for (int i = 0; i < rowsNumber; i++) {
            for (int j = 0; j < 9; j++) {
                tableData[i][j] = "";
            }
        }
        // Times string for View
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

        // Create table and set cells parameters
        JTable table = new WeatherTable(tableData, columnNames);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

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

        // Open weather-forecast link when weather-picture clicked.
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener()
        {
            public void valueChanged(ListSelectionEvent e)
            {
                String[] tmp = e.getSource().toString().split("=");
                // If only one cell selected - open link in default browser
                if ((tmp.length > 1) && (table.getSelectedColumn() == 0) && (tmp[tmp.length - 1].split(",").length == 1)) {

                    String urlStr = clickedLinks.get(table.getSelectedRow());
                    table.clearSelection();

                    if (urlStr != null) {
                        try {
                            Desktop.getDesktop().browse(new URI(urlStr));
                        } catch (IOException | URISyntaxException ignore) {}
                    }
                }
            }
        });






        // Add table to the frame
        frame.add(table, BorderLayout.PAGE_START);


        SwingFrameView.mainTable = table;

        // Create panel for request parameters selection
        JPanel bottomPanel = new JPanel();

        // Button to select previous day
        JButton getPrevDay = new JButton(frameText.getProperty("previous"));
        getPrevDay.setEnabled(false);
        bottomPanel.add(getPrevDay);

        // Day label
        bottomPanel.add(new Label("     " + frameText.getProperty("day")));

        // ComboBox for day selection
        String[] itemsShiftDays = {frameText.getProperty("day0"),
                frameText.getProperty("day1"),
                frameText.getProperty("day2"),
                frameText.getProperty("day3"),
                frameText.getProperty("day4"),
                frameText.getProperty("day5"),
                frameText.getProperty("day6") };
        JComboBox comboShiftDays = new JComboBox(itemsShiftDays);
        bottomPanel.add(comboShiftDays);

        // Location label
        bottomPanel.add(new Label("    " + frameText.getProperty("location")));

        // ComboBox for location selection
        String[] itemsLocations = getLocations();
        JComboBox comboLocations = new JComboBox(itemsLocations);
        bottomPanel.add(comboLocations);

        // indent label
        bottomPanel.add(new Label(" "));

        // Refresh button
        JButton refresh = new JButton(frameText.getProperty("refresh"));
        bottomPanel.add(refresh);

        // indent label
        bottomPanel.add(new Label(" "));

        // ComboBox for language selection
        String[] itemsLanguage = {frameText.getProperty("ua"),
                frameText.getProperty("ru") };
        JComboBox comboLanguage = new JComboBox(itemsLanguage);
        bottomPanel.add(comboLanguage);

        // indent label
        bottomPanel.add(new Label(" "));

        // Button to select next day
        JButton getNextDay = new JButton(frameText.getProperty("next"));
        getPrevDay.setEnabled(false);
        bottomPanel.add(getNextDay);





        // Add panel to JFrame
        frame.add(bottomPanel, BorderLayout.AFTER_LAST_LINE);

        // Set icon and other parameters of JFrame
        frame.setIconImage(new ImageIcon("resources/icon.png").getImage());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setResizable(false);
    }


    /** Get locations for  */
    private static String[] getLocations() {
        List<String> locs = new ArrayList<>();
        for (Location location : locations) {
            if (isUa) {
                locs.add(location.getNameUa());
            } else {
                locs.add(location.getNameRu());
            }
        }

        String[] result = new String[locs.size()];
        result = locs.toArray(result);

        return result;
    }


    /** Fill JTable with data from List<Map<Integer, Weather>> */
    private static void fillTable(JTable table, List<Map<Integer, Weather>> forecasts) {

        // Show location and current date
        Weather weatherTmp = forecasts.get(0).entrySet().iterator().next().getValue();  // Get some Weather from forecast
        String location;
        Date day = weatherTmp.getDateTime();
        if (isUa) {
            location = weatherTmp.getLocation().getNameUa() + ":";
            table.setValueAt(new SimpleDateFormat("dd MMMM", new Locale("uk","UA")).format(day), 1, 0);
            table.setValueAt(new SimpleDateFormat("EEEE", new Locale("uk","UA")).format(day), 2, 0);
        } else {
            location = weatherTmp.getLocation().getNameRu() + ":";
            table.setValueAt(new SimpleDateFormat("dd MMMM", new Locale("ru","RU")).format(day), 1, 0);
            table.setValueAt(new SimpleDateFormat("EEEE", new Locale("ru","RU")).format(day), 2, 0);
        }
        table.setValueAt(location, 0, 0);

        // Feel table
        int currentRaw = 3;
        int currentPropertyRawName = 0;
        for (Map<Integer, Weather> mapTmp : forecasts) {
            Properties tmpRawName = rowsNames.get(currentPropertyRawName);

            if (tmpRawName.getProperty("picture.height") != null) {
                for (Map.Entry<Integer, Weather> entry : mapTmp.entrySet()) {
                    int currentColumn = entry.getKey() / 3 + 1;
                    if (entry.getKey() % 3 ==0) {
                        picPath.get(currentPropertyRawName)[currentColumn - 1] =
                                entry.getValue().getPictureWeather().getWeatherImage();
                        toolTipText[currentRaw][currentColumn] = entry.getValue().getPictureWeather().getWeatherDescription();
                        clickedLinks.put(currentRaw, entry.getValue().getUrl());
                    }
                }
                currentRaw++;
            }
            // Fill all temperatures for current forecast
            if (tmpRawName.getProperty("temperature") != null) {
                for (Map.Entry<Integer, Weather> entry : mapTmp.entrySet()) {
                    int currentColumn = entry.getKey() / 3 + 1;
                    if (entry.getKey() % 3 ==0) {       // Need to get only 0:00, 3:00, 6:00, 9:00, etc.
                        int temperature = entry.getValue().getTemperature();
                        if (temperature > 0) {
                            table.setValueAt("+" + temperature, currentRaw, currentColumn);
                        } else {
                            table.setValueAt(temperature, currentRaw, currentColumn);
                        }
                    }
                }
                currentRaw++;
            }
            // Fill all temperature-feels for current forecast
            if (tmpRawName.getProperty("feel") != null) {
                for (Map.Entry<Integer, Weather> entry : mapTmp.entrySet()) {
                    int currentColumn = entry.getKey() / 3 + 1;
                    if (entry.getKey() % 3 ==0) {
                        int feel = entry.getValue().getTempFeel();
                        if (feel > 0) {
                            table.setValueAt("+" + feel, currentRaw, currentColumn);
                        } else {
                            table.setValueAt(feel, currentRaw, currentColumn);
                        }
                    }
                }
                currentRaw++;
            }
            // Fill all precipitation probabilities for current forecast
            if (tmpRawName.getProperty("precipitation.probability") != null) {
                for (Map.Entry<Integer, Weather> entry : mapTmp.entrySet()) {
                    int currentColumn = entry.getKey() / 3 + 1;
                    if (entry.getKey() % 3 ==0) {
                        int precipitationProb = entry.getValue().getPrecipitation().getProbability();
                        if (precipitationProb != -1000) {
                            table.setValueAt(precipitationProb, currentRaw, currentColumn);
                        } else {
                            table.setValueAt("-", currentRaw, currentColumn);
                        }
                    }
                }
                currentRaw++;
            }
            // Fill all precipitation descriptions for current forecast
            if (tmpRawName.getProperty("precipitation.description") != null) {
                for (Map.Entry<Integer, Weather> entry : mapTmp.entrySet()) {
                    int currentColumn = entry.getKey() / 3 + 1;
                    if (entry.getKey() % 3 ==0) {
                        table.setValueAt(entry.getValue().getPrecipitation().getDescription(), currentRaw, currentColumn);
                    }
                }
                currentRaw++;
            }
            // Fill all winds for current forecast
            if (tmpRawName.getProperty("wind") != null) {
                for (Map.Entry<Integer, Weather> entry : mapTmp.entrySet()) {
                    int currentColumn = entry.getKey() / 3 + 1;
                    if (entry.getKey() % 3 ==0) {
                        table.setValueAt(entry.getValue().getWind().getPower(), currentRaw, currentColumn);
                        toolTipText[currentRaw][currentColumn] = entry.getValue().getWind().getDirection();
                    }
                }
                currentRaw++;
            }
            // Fill all humidity for current forecast
            if (tmpRawName.getProperty("humidity") != null) {
                for (Map.Entry<Integer, Weather> entry : mapTmp.entrySet()) {
                    int currentColumn = entry.getKey() / 3 + 1;
                    if (entry.getKey() % 3 ==0) {
                        table.setValueAt(entry.getValue().getHumidity(), currentRaw, currentColumn);
                    }
                }
                currentRaw++;
            }
            // Fill all pressures for current forecast
            if (tmpRawName.getProperty("pressure") != null) {
                for (Map.Entry<Integer, Weather> entry : mapTmp.entrySet()) {
                    int currentColumn = entry.getKey() / 3 + 1;
                    if (entry.getKey() % 3 ==0) {
                        table.setValueAt(entry.getValue().getPressure(), currentRaw, currentColumn);
                    }
                }
                currentRaw++;
            }
            currentRaw++;


            currentPropertyRawName++;

        }
    }



}
