package pl.javastart.jdbc2;

import java.sql.*;
import java.util.Scanner;

public class WorldApp {

    private Connection connection;

    //tworzymy połączenie z bazą w konstruktorze
    public WorldApp() {
        //wczytanie sterownika
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Błąd wczytywania sterownika");
            e.printStackTrace();
        }

        //url
        String url = "jdbc:mysql://localhost:3306/world?serverTimezone=UTC";

        try {
            connection = DriverManager.getConnection(url, "root", "zioom1");
        } catch (SQLException e) {
            System.out.println("Błąd nawiązywania połączenia");
            e.printStackTrace();
        }
    }

    public void run() {

        Scanner scanner = new Scanner(System.in);

        System.out.println("Podaj nazwę miasta");
        String cityName = scanner.nextLine();
        System.out.println("Podaj nową liczbę ludności");
        int newPopulation = scanner.nextInt();
        scanner.nextLine();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE city SET Population = ? WHERE NAME = ?");
            preparedStatement.setInt(1, newPopulation);
            preparedStatement.setString(2, cityName);
            //zwraca informacje o tym ile zostało zmienionych wierszy
            int rowsChanged = preparedStatement.executeUpdate();

            //wyświetlamy zaktualizowane rekordy
            System.out.println("Zaktualizowane rekordy: " + rowsChanged);

            PreparedStatement queryStatement = connection.prepareStatement("SELECT * FROM City WHERE Name = ?");
            queryStatement.setString(1, cityName);
            ResultSet resultSet = queryStatement.executeQuery();
            if (resultSet.next()) {
                long id = resultSet.getLong("ID");
                String name = resultSet.getString("Name");
                int population = resultSet.getInt("Population");
                System.out.println("Id: " + id + " " + name + " ludność: " + population);

                System.out.println();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
