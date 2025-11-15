import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

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
    private ArrayList<Movie> movies = new ArrayList<>();
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
        Movie movie = new Movie(title, genre, rating, releaseYear);
        movies.add(movie);
        JOptionPane.showMessageDialog(null, "Movie added successfully!");
    }

    private void removeMovie(String title) {
        for (int i = 0; i < movies.size(); i++) {
            if (movies.get(i).getTitle().equalsIgnoreCase(title)) {
                movies.remove(i);
                JOptionPane.showMessageDialog(null, "Movie removed successfully!");
                return;
            }
        }
        JOptionPane.showMessageDialog(null, "Movie with title \"" + title + "\" not found!");
    }

    private void refreshMovies() {
        StringBuilder moviesText = new StringBuilder();
        for (Movie movie : movies) {
            moviesText.append("Title: ").append(movie.getTitle())
                    .append(", Genre: ").append(movie.getGenre())
                    .append(", Rating: ").append(movie.getRating())
                    .append(", Release Year: ").append(movie.getReleaseYear())
                    .append("\n");
        }
        textAreaMovies.setText(moviesText.toString());
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
