import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

class Movie {
    private String title;
    private String genre;
    private double rating;
    private int releaseYear;

    public Movie(String title, String genre, double rating, int releaseYear) {
        this.title = title;
        this.genre = genre;
        this.rating = rating;
        this.releaseYear = releaseYear;
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public double getRating() {
        return rating;
    }

    public int getReleaseYear() {
        return releaseYear;
    }
}

public class MovieDatabaseGUI extends JFrame {
    private Connection connection;
    private JTextField textFieldTitle;
    private JTextField textFieldGenre;
    private JTextField textFieldRating;
    private JTextField textFieldReleaseYear;
    private JTextArea textAreaMovies;

    public MovieDatabaseGUI() {
        setTitle("Movie Database");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panelInput = new JPanel(new GridLayout(0, 2, 5, 5));
        panelInput.setBorder(new EmptyBorder(10, 10, 10, 10));

        panelInput.add(new JLabel("Title:"));
        textFieldTitle = new JTextField();
        panelInput.add(textFieldTitle);

        panelInput.add(new JLabel("Genre:"));
        textFieldGenre = new JTextField();
        panelInput.add(textFieldGenre);

        panelInput.add(new JLabel("Rating:"));
        textFieldRating = new JTextField();
        panelInput.add(textFieldRating);

        panelInput.add(new JLabel("Release Year:"));
        textFieldReleaseYear = new JTextField();
        panelInput.add(textFieldReleaseYear);

        JButton addButton = new JButton("Add Movie");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = textFieldTitle.getText();
                String genre = textFieldGenre.getText();
                double rating = Double.parseDouble(textFieldRating.getText());
                int releaseYear = Integer.parseInt(textFieldReleaseYear.getText());
                addMovie(title, genre, rating, releaseYear);
                refreshMovies();
            }
        });
        panelInput.add(addButton);

        JButton removeButton = new JButton("Remove Movie");
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = textFieldTitle.getText(); // Assuming title is used as the identifier for movie removal
                removeMovie(title);
                refreshMovies();
            }
        });
        panelInput.add(removeButton);

        add(panelInput, BorderLayout.NORTH);

        textAreaMovies = new JTextArea();
        textAreaMovies.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textAreaMovies);
        scrollPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }


    private void addMovie(String title, String genre, double rating, int releaseYear) {
        try {
			Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/assingment","admin","1234");
            String sql = "INSERT INTO Movies (Title, Genre, Rating, ReleaseYear) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, title);
            statement.setString(2, genre);
            statement.setDouble(3, rating);
            statement.setInt(4, releaseYear);
            statement.executeUpdate();
			connection.close();
            JOptionPane.showMessageDialog(null, "Movie added successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to add movie!");
        }
    }

    private void removeMovie(String title) {
        try {
			Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/assingment","admin","1234");
            String sql = "DELETE FROM Movies WHERE Title = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, title);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Movie removed successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "Movie with title \"" + title + "\" not found!");
            }
			connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to remove movie!");
        }
    }

    private void refreshMovies() {
        try {
			Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/assingment","admin","1234");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Movies");
            StringBuilder moviesText = new StringBuilder();
            while (resultSet.next()) {
                String title = resultSet.getString("Title");
                String genre = resultSet.getString("Genre");
                double rating = resultSet.getDouble("Rating");
                int releaseYear = resultSet.getInt("ReleaseYear");
                moviesText.append("Title: ").append(title)
                        .append(", Genre: ").append(genre)
                        .append(", Rating: ").append(rating)
                        .append(", Release Year: ").append(releaseYear)
                        .append("\n");
            }
			connection.close();
            textAreaMovies.setText(moviesText.toString());
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to fetch movies!");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MovieDatabaseGUI();
            }
        });
    }
}
