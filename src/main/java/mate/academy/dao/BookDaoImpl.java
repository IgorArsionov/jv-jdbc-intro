package mate.academy.dao;

import mate.academy.MyConnection;
import mate.academy.entity.Book;
import mate.academy.exception.DataProcessingException;
import mate.academy.lib.Dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Dao
public class BookDaoImpl implements BookDao{
    @Override
    public Book create(Book book) {
        String sql = "INSERT INTO books (title, price) VALUES (?, ?)";
        try (Connection connection = MyConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, book.getTitle());
            statement.setBigDecimal(2, book.getPrice());

            int rows = statement.executeUpdate();

            if (rows < 1) {
                throw new RuntimeException("Expected to insert at leas one row, but was 0 rows.");
            }

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                Long id = generatedKeys.getObject(1, Long.class);
                book.setId(id);
            }

        } catch (SQLException e) {
            throw new DataProcessingException("Create - Can not connect to database.", e);
        }
        return book;
    }

    @Override
    public Optional<Book> findById(Long id) {
        String sql = "SELECT * FROM books WHERE id = ?";
        try (Connection connection = MyConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Book book = new Book();
                book.setId(resultSet.getLong("id"));
                book.setTitle(resultSet.getString("title"));
                book.setPrice(resultSet.getBigDecimal("price"));

                return Optional.of(book);
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Find by Id - Can not connect to database.", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Book> findAll() {
        String sql = "SELECT * FROM books";
        List<Book> books = new ArrayList<>();
        try (Connection connection = MyConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery();) {

            while (resultSet.next()) {
                Book book = new Book();
                book.setId(resultSet.getLong("id"));
                book.setTitle(resultSet.getString("title"));
                book.setPrice(resultSet.getBigDecimal("price"));

                books.add(book);
            }

        } catch (SQLException e) {
            throw new DataProcessingException("Find All - Can not connect to database.", e);
        }
        return books;
    }

    @Override
    public Book update(Book book) {
        String sql = "UPDATE books SET title = ?, price = ? WHERE id = ?";
        try (Connection connection = MyConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, book.getTitle());
            statement.setBigDecimal(2, book.getPrice());
            statement.setLong(3, book.getId());

            int rows = statement.executeUpdate();

            if (rows < 1) {
                throw new RuntimeException("Expected to update at leas one row, but was 0 rows.");
            }

        } catch (SQLException e) {
            throw new DataProcessingException("Update - Can not connect to database.", e);
        }
        return book;
    }

    @Override
    public boolean deleteById(Long id) {
        String sql = "DELETE FROM books WHERE id = ?";
        try (Connection connection = MyConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            int rows = statement.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            throw new DataProcessingException("Delete by id - Can not connect to database.", e);
        }
    }
}
