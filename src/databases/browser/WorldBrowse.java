package databases.browser;

import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.sql.*;

public class WorldBrowse extends JFrame {
    private static final String USER = "infis";
    private static final String PASSWORD = "infis";
    private static final String DB_URL = "jdbc:mysql://10.1.12.18:3306/world";

    MyText IDText, countryText, populationText, nameText;

    MyButton saveButton, cancelButton, firstButton, lastButton;

    MyButton prevButton, nextButton;

    static ResultSet set;

    public WorldBrowse() throws SQLException {
        setSize(700, 350);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        JPanel gridPanel = new JPanel(new GridLayout(4, 2, 2, 2));
        JPanel flowPanel = new JPanel(new FlowLayout());
        JPanel flowPanel2 = new JPanel(new FlowLayout());
        JPanel flowPanel3 = new JPanel(new FlowLayout());

        //ID
        MyLabel IDLabel = new MyLabel("ID:");
        gridPanel.add(IDLabel);
        IDText = new MyText(set.getString("ID"));
        IDText.setEnabled(false);
        gridPanel.add(IDText);

        //Name
        MyLabel nameLabel = new MyLabel("City name:");
        gridPanel.add(nameLabel);
        nameText = new MyText(set.getString("Name"));
        nameText.setEnabled(false);
        gridPanel.add(nameText);

        //CountryCode
        MyLabel countryLabel = new MyLabel("Country:");
        gridPanel.add(countryLabel);
        countryText = new MyText(set.getString("CountryCode"));
        countryText.setEnabled(false);
        gridPanel.add(countryText);

        //Population
        MyLabel populationLabel = new MyLabel("Population:");
        gridPanel.add(populationLabel);
        populationText = new MyText(String.valueOf(set.getInt("Population")));
        populationText.setEnabled(false);
        gridPanel.add(populationText);

        //flow-buttons:

        prevButton = new MyButton("Prev");
        nextButton = new MyButton("Next");
        nextButton.addActionListener(e -> next());
        prevButton.addActionListener(e -> previous());


        firstButton = new MyButton("First");
        lastButton = new MyButton("Last");
        firstButton.addActionListener(e -> first());
        lastButton.addActionListener(e -> last());


        flowPanel.add(firstButton);
        flowPanel.add(prevButton);
        flowPanel.add(nextButton);
        flowPanel.add(lastButton);

        //middle row
        MyButton addButton = new MyButton("Add New");
        addButton.addActionListener(e -> addNew());
        MyButton deleteButton = new MyButton("Delete");
        MyButton updateButton = new MyButton("Update");
        updateButton.addActionListener(e -> update());

        flowPanel2.add(addButton);
        flowPanel2.add(deleteButton);
        flowPanel2.add(updateButton);

        //last row
        saveButton = new MyButton("Save");
        saveButton.setEnabled(false);
        cancelButton = new MyButton("Cancel");
        cancelButton.setEnabled(false);
        cancelButton.addActionListener(e -> cancel());


        flowPanel3.add(saveButton);
        flowPanel3.add(cancelButton);

        JPanel gridPanel2 = new JPanel(new GridLayout(3, 1, 2, 2));
        gridPanel2.add(flowPanel);
        gridPanel2.add(flowPanel2);
        gridPanel2.add(flowPanel3);


        add(gridPanel, BorderLayout.CENTER);
        add(gridPanel2, BorderLayout.SOUTH);


    }
    void cancel(){
        try {
            IDText.setText(set.getString("ID"));
            nameText.setText(set.getString("Name"));
            countryText.setText(set.getString("CountryCode"));
            populationText.setText(String.valueOf(set.getInt("Population")));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        saveButton.setEnabled(false);
        cancelButton.setEnabled(false);
        IDText.setEnabled(false);
        nameText.setEnabled(false);
        countryText.setEnabled(false);
        populationText.setEnabled(false);

    }

    void update(){
        saveButton.setEnabled(true);
        cancelButton.setEnabled(true);

        nextButton.setEnabled(false);
        prevButton.setEnabled(false);
        firstButton.setEnabled(false);
        lastButton.setEnabled(false);

        IDText.setEnabled(true);
        nameText.setEnabled(true);
        countryText.setEnabled(true);
        populationText.setEnabled(true);
    }

    void addNew(){
        saveButton.setEnabled(true);
        cancelButton.setEnabled(true);
        IDText.setEnabled(true);
        IDText.setText("");
        nameText.setEnabled(true);
        nameText.setText("");
        countryText.setEnabled(true);
        countryText.setText("");
        populationText.setEnabled(true);
        populationText.setText("");
    }

    void last() {
        try {
            if (set.last()){
                IDText.setText(set.getString("ID"));
                nameText.setText(set.getString("Name"));
                countryText.setText(set.getString("CountryCode"));
                populationText.setText(String.valueOf(set.getInt("Population")));
            } else {
                set.previous();
                JOptionPane.showMessageDialog(this, "Konec seznamu");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Problem s SQL: " + e.getMessage(), ":(", JOptionPane.ERROR_MESSAGE);
        }
    }

    void next() {
        try {
            if (set.next()){
                IDText.setText(set.getString("ID"));
                nameText.setText(set.getString("Name"));
                countryText.setText(set.getString("CountryCode"));
                populationText.setText(String.valueOf(set.getInt("Population")));
            } else {
                set.previous();
                JOptionPane.showMessageDialog(this, "Konec seznamu");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Problem s SQL: " + e.getMessage(), ":(", JOptionPane.ERROR_MESSAGE);
        }
    }

    void previous() {
        try {
            if (set.previous()){
                IDText.setText(set.getString("ID"));
                nameText.setText(set.getString("Name"));
                countryText.setText(set.getString("CountryCode"));
                populationText.setText(String.valueOf(set.getInt("Population")));
            } else {
                set.next();
                JOptionPane.showMessageDialog(this, "Konec seznamu");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Problem s SQL: " + e.getMessage(), ":(", JOptionPane.ERROR_MESSAGE);
        }
    }
    void first() {
        try {
            if (set.first()){
                IDText.setText(set.getString("ID"));
                nameText.setText(set.getString("Name"));
                countryText.setText(set.getString("CountryCode"));
                populationText.setText(String.valueOf(set.getInt("Population")));
            } else {
                set.next();
                JOptionPane.showMessageDialog(this, "Konec seznamu");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Problem s SQL: " + e.getMessage(), ":(", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        try {
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            Statement statement = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String queryAll = "SELECT * FROM city";
            set = statement.executeQuery(queryAll);
            set.next();

            new WorldBrowse().setVisible(true);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Problem s SQL: " + e.getMessage(), ":(", JOptionPane.ERROR_MESSAGE);
        }
    }
}

class MyLabel extends JLabel {
    public MyLabel(String text) {
        super(text);
        setFont(new Font("Consolas", Font.PLAIN, 18));
        // TODO: 17.02.2025 Design

        setOpaque(true);
        setBackground(new Color(0x53A84F));
        setForeground(new Color(0x00ffff));
    }
}

class MyText extends JTextField {
    public MyText(String text) {
        super(text);
        setFont(new Font("Consolas", Font.PLAIN, 18));

        setOpaque(true);
        setBackground(new Color(0x08FF00));
        setForeground(new Color(0xFF001E));

    }
}

class MyButton extends JButton {
    public MyButton(String text) {
        super(text);
        setFont(new Font("Consolas", Font.PLAIN, 18));
        // TODO: 17.02.2025 Design
//
        setOpaque(true);
        setBackground(Color.BLUE);
        setForeground(Color.WHITE);
    }
}