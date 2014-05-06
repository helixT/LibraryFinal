package knihovna;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jduda
 */
public class ReaderManagerImpl implements ReaderManager {
    
    private static final Logger log = LoggerFactory.getLogger(ReaderManagerImpl.class.getName());    
    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    private void checkDataSource() {
        if (dataSource == null) {
            String msg = "DataSource is not set";
            log.error(msg);
            throw new IllegalStateException(msg);
        }
    } 

    @Override
    public void addReader(Reader reader) throws ServiceFailureException {
        checkDataSource();
        correctInputReader(reader);
        
        if(reader.getId() != null) {
            String msg = "Reader's id isn't null!";
            log.info(msg);
            throw new IllegalArgumentException(msg);            
        }
        
        Connection conn = null;
        PreparedStatement st = null;
        
        try{
            conn = dataSource.getConnection();
            conn.setAutoCommit(false); 
            st = conn.prepareStatement("INSERT INTO READER (fullname,adress,"
                    + "phonenumber) VALUES (?,?,?)", Statement.RETURN_GENERATED_KEYS);
            
            st.setString(1, reader.getFullName());
            st.setString(2, reader.getAddress());
            
            if(reader.getPhoneNumber() == null){
                st.setNull(3, java.sql.Types.INTEGER);
            } else{
                st.setInt(3, reader.getPhoneNumber());
            }
            
            int addedRows = st.executeUpdate();
            DBUtils.checkUpdatesCount(addedRows, reader, true);

            Long id = DBUtils.getId(st.getGeneratedKeys());
            reader.setId(id);
            
            conn.commit();
            log.info("Reader " + reader + " was added to database.");
            
        } catch (SQLException ex) {
            String message = "Error when inserting reader" + reader;
            log.error(message, ex);
            throw new ServiceFailureException(message, ex);
        } finally {
            DBUtils.doRollback(conn);
            DBUtils.closeConnection(conn, st);
        }
    }

    @Override
    public void updateReader(Reader reader) throws ServiceFailureException {
        checkDataSource();
        correctInputReader(reader);
        if(reader.getId() == null) {
            String msg = "Readed's id is null!";
            log.info(msg);
            throw new IllegalArgumentException(msg);            
        }
        
        Connection conn = null;
        PreparedStatement st = null;
        
        try{
            conn = dataSource.getConnection();
            conn.setAutoCommit(false); 
            st = conn.prepareStatement("UPDATE READER SET fullname=?,adress=?,phonenumber=? WHERE id=?");
            
            st.setString(1, reader.getFullName());
            st.setString(2, reader.getAddress());
            if(reader.getPhoneNumber() == null){
                st.setNull(3, java.sql.Types.INTEGER);
            } else{
                st.setInt(3, reader.getPhoneNumber());
            }
            st.setLong(4, reader.getId());
            
            int updatedRows = st.executeUpdate();
            DBUtils.checkUpdatesCount(updatedRows, reader, false);
            
            conn.commit();
            log.info("Reader " + reader + " was updated.");
                        
        }catch(SQLException ex){
            String message = "Error when updating reader" + reader;
            log.error(message, ex);
            throw new ServiceFailureException(message, ex);
        } finally {
            DBUtils.doRollback(conn);
            DBUtils.closeConnection(conn, st);
        }
    }

    @Override
    public void deleteReader(Reader reader) throws ServiceFailureException {
        checkDataSource();
        if(reader == null){
            String msg = "Input reader is null!";
            log.info(msg);
            throw new IllegalArgumentException(msg);
        }
        if(reader.getId() == null){
            String msg = "Input reader's id is null!";
            log.info(msg);
            throw new IllegalArgumentException(msg);
        }
        
        Connection conn = null;
        PreparedStatement st = null;
        
        try{
            conn = dataSource.getConnection();
            conn.setAutoCommit(false); 
            
            if(reader.getPhoneNumber() != null){
                st = conn.prepareStatement("DELETE FROM READER WHERE id=? AND fullname=? AND adress=? AND phonenumber=?");
                st.setInt(4, reader.getPhoneNumber());
            } else {
                st = conn.prepareStatement("DELETE FROM READER WHERE id=? AND fullname=? AND adress=? AND phonenumber IS NULL");
            }
            st.setLong(1, reader.getId());
            st.setString(2, reader.getFullName());
            st.setString(3, reader.getAddress());
            
            int deletedRows = st.executeUpdate();
            DBUtils.checkUpdatesCount(deletedRows, reader, false);
            
            conn.commit();
            log.info("Reader " + reader + " was deleted from database.");
            
        } catch (SQLException ex) {
            String message = "Error when deleting reader" + reader;
            log.error(message, ex);
            throw new ServiceFailureException(message, ex);
        } finally {
            DBUtils.doRollback(conn);
            DBUtils.closeConnection(conn, st);
        }
    }

    @Override
    public Reader findReaderById(Long id) throws ServiceFailureException {
        checkDataSource();
        if(id.intValue() <= 0){
            String msg = "Input id isn't positive number!";
            log.info(msg);
            throw new IllegalArgumentException(msg);
        }
        
        Connection conn = null;
        PreparedStatement st = null;
        
        try{
            conn = dataSource.getConnection();
            st = conn.prepareStatement("SELECT id,fullname,adress,phonenumber FROM reader WHERE id=?");
            
            st.setLong(1, id);
            ResultSet rs = st.executeQuery();
            
            if (rs.next()) {
                Reader reader = resultToReader(rs);

                if (rs.next()) {
                    String msg = "Was founded more than one reader "
                            + "(Input id: " + id + " and were found " + reader + " and " + resultToReader(rs);
                    log.info(msg);
                    throw new ServiceFailureException(msg);
                }            
                
                log.info("Reader with id " + id + " was found.");
                return reader;
            } else {
                return null;
            }
            
        } catch (SQLException ex) {
            String message = "Error when finding reader by " + id;
            log.error(message, ex);
            throw new ServiceFailureException(message, ex);
        } finally {
            DBUtils.closeConnection(conn, st);
        }         
    }

    @Override
    public List<Reader> findReaderByName(String name) throws ServiceFailureException {
        checkDataSource();
        if(name == null){
            String msg = "Input name is null!";
            log.info(msg);
            throw new IllegalArgumentException("Input name is null!");
        }
        
        Connection conn = null;
        PreparedStatement st = null;
        
        try{
            conn = dataSource.getConnection();
            st = conn.prepareStatement("SELECT id,fullname,adress,phonenumber FROM reader WHERE fullname=?");
            
            st.setString(1, name);
            ResultSet rs = st.executeQuery();
            List<Reader> listOfReaders = new ArrayList<>();
            
            while(rs.next()){
                listOfReaders.add(resultToReader(rs));
            }
            
            log.info("Readers with name " + name + " were found.");
            return listOfReaders;
        } catch (SQLException ex) {
            String message = "Error when finding reader name " + name;
            log.error(message, ex);
            throw new ServiceFailureException(message, ex);
        } finally {
            DBUtils.closeConnection(conn, st);
        }  
    }

    @Override
    public List<Reader> findAllReaders() throws ServiceFailureException {
        checkDataSource();
        
        Connection conn = null;
        PreparedStatement st = null;
        
        try{
            conn = dataSource.getConnection();
            st = conn.prepareStatement("SELECT id,fullname,adress,phonenumber FROM reader");
            
            ResultSet rs = st.executeQuery();
            List<Reader> listOfReaders = new ArrayList<>();
            
            while(rs.next()){
                listOfReaders.add(resultToReader(rs));
            }
            
            log.info("All readers were found.");
            return listOfReaders;
        } catch (SQLException ex) {
            String message = "Error when finding readers";
            log.error(message, ex);
            throw new ServiceFailureException(message, ex);
        } finally {
            DBUtils.closeConnection(conn, st);
        } 
    }

    private void correctInputReader(Reader reader) {
        String msg = null;
        if(reader == null) {
            msg = "Input reader is null";            
        }
        if(reader.getFullName() == null) {
            msg = "Reader's name is null";           
        }
        if(reader.getAddress() == null) {
            msg = "Reader's adress is null";           
        }
        if(reader.getPhoneNumber() != null){
            if(reader.getPhoneNumber() <= 0){
                msg = "Reader's phone number is neative!";
            }
            if(reader.getPhoneNumber().toString().length() != 9){
                msg = "Reader's phone number hasn't got 9 digits!";
            }
        }
        if(msg != null){
            log.info(msg);
            throw new IllegalArgumentException(msg);
        }
    }
    
    private Reader resultToReader(ResultSet rs) throws SQLException {
        Reader reader = new Reader();
        reader.setId(rs.getLong("id"));
        reader.setFullName(rs.getString("fullname"));
        reader.setAddress(rs.getString("adress"));
        if(rs.getInt("phonenumber") == 0){
            reader.setPhoneNumber(null);
        }else{
            reader.setPhoneNumber(rs.getInt("phonenumber"));
        }
        return reader;
    }
}
