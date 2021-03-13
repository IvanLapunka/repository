package all.repos;

import exceptions.DatabaseException;
import by.pojo.AbstractEntity;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Slf4j
public abstract class AbstractRepositoryPostgres<T extends AbstractEntity> implements Repository<T>
{
    protected final Datasource datasource = Datasource.getInstance();

    protected abstract Set<T> getItems(ResultSet res) throws SQLException ;

    protected abstract String getSqlAllItems();

    protected abstract String getSqlItemById();

    protected abstract String getSqlUpdate();

    protected abstract void setPrepareUpdateStatementParameters(PreparedStatement ps, T entity)  throws SQLException ;

    protected abstract String getSqlInsert();

    protected abstract void setPrepareInsertStatementParameters(PreparedStatement ps, T entity)  throws SQLException ;

    protected abstract String getSqlDeleteById();

    @Override
    public Set<T> findAll() {
        Set<T> result = new HashSet<>();
        String sqlAllItems = getSqlAllItems();
        try (Connection conn = datasource.getConnection();
             PreparedStatement stat = conn.prepareStatement(sqlAllItems);
             ResultSet res = stat.executeQuery()
        ) {
             result = getItems(res);
        } catch (SQLException e) {
            log.error("sql error, during reading items");
            throw new DatabaseException(e);
        } catch (ClassNotFoundException e) {
            log.error("driver class not found");
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Optional<T> find(Integer id) {
        Set<T> result = new HashSet<>();
        String sqlFindItemById = getSqlItemById();
        try (Connection conn = datasource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sqlFindItemById))
        {
            ps.setInt(1, id);
            ResultSet res = ps.executeQuery();
            result = getItems(res);
        } catch (SQLException e) {
            log.error("sql error, during reading");
            throw new DatabaseException(e);
        } catch (ClassNotFoundException e) {
            log.error("driver class not found");
            e.printStackTrace();
        }
        return result.stream().findAny();
    }

    @Override
    public T save(T entity) {
        return entity.getId() == null ? insert(entity) : update(entity);
    }

    protected T insert(T entity) {
        ResultSet resultSet = null;
        String sqlInsert = getSqlInsert();
        try (Connection connection = datasource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert);
        ) {
            setPrepareInsertStatementParameters(preparedStatement, entity);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                entity.setId(resultSet.getInt(SqlColumns.ID));
                return entity;
            }
        } catch (SQLException throwables) {
            log.error("error while reading from student");
            throw new DatabaseException(throwables);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            close(resultSet);
        }
        return null;
    }

    protected T update(T entity) {
        Optional<T> entity1 = find(entity.getId());
        if (entity1.isEmpty()) {
            return null;
        }
        ResultSet resultSet = null;
        String sqlUpdate = getSqlUpdate();
        try (Connection connection = datasource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlUpdate)
        ) {
            setPrepareUpdateStatementParameters(preparedStatement, entity);
            if (preparedStatement.executeUpdate() > 0) {
                return entity;
            }
        } catch (SQLException e) {
            log.error("error during updating items", e);
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Optional<T> remove(Integer id) {
        String sqlRemove = getSqlDeleteById();
        try(Connection connection = datasource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlRemove)
        ) {
            final Optional<T> entity = find(id);
            if (entity.isEmpty())
                return entity;
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            return entity;
        } catch (SQLException e) {
            log.error("error during deleting student", e);
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return  Optional.empty();
    }

    private void close(AutoCloseable closeable) {
        try {
            if (closeable == null) {
                return;
            }
            closeable.close();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    protected static Integer getRsInteger(ResultSet rs, String colName) throws SQLException {
        return rs.getObject(colName, Integer.class);
    }

    protected static String getRsString(ResultSet rs, String colName) throws SQLException {
        return rs.getString(colName);
    }

    protected static <V, K> V putIfAbsentAndReturn (Map<K, V> map, K key, V value) {
        if (key == null){
            return null;
        }
        map.putIfAbsent(key, value);
        return map.get(key);
    }
}
