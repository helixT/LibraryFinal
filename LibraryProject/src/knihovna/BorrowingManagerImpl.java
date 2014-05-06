package knihovna;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Honza
 */
public class BorrowingManagerImpl implements BorrowingManager{

    private static final Logger log = LoggerFactory.getLogger(BorrowingManagerImpl.class);
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
    public void addBorrowing(Borrowing borrowing) throws ServiceFailureException {
        checkDataSource();
        correctInputBorrowing(borrowing);
        
        if(borrowing.getId() != null) {
            String msg = "Borrowing's id isn't null!";
            log.info(msg);
            throw new IllegalArgumentException("Borrowing's id isn't null!");            
        }
        
        Connection conn = null;
        PreparedStatement st = null;
        
        try{
            conn = dataSource.getConnection();
            conn.setAutoCommit(false); 
            st = conn.prepareStatement("INSERT INTO BORROWING (bookborrowedfrom,bookborrowedto,readerid,bookid)"
                    + " VALUES (?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            
            st.setTimestamp(1, new Timestamp(borrowing.getBookBorrowedFrom().getTimeInMillis()));
            st.setTimestamp(2, new Timestamp(borrowing.getBookBorrowedTo().getTimeInMillis()));
            st.setLong(3, borrowing.getReader().getId());
            st.setLong(4, borrowing.getBook().getId());
            
            int addedRows = st.executeUpdate();
            DBUtils.checkUpdatesCount(addedRows, borrowing, true);
            
            Long id = DBUtils.getId(st.getGeneratedKeys());
            borrowing.setId(id);
            conn.commit();
            log.info("Borrowing " + borrowing + " was added to database.");
            
        } catch (SQLException ex) {
            String msg = "Error when inserting borrowing " + borrowing;
            log.error(msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            DBUtils.doRollback(conn);
            DBUtils.closeConnection(conn, st);
        }
    }

    @Override
    public void updateBorrowing(Borrowing borrowing) throws ServiceFailureException {
        checkDataSource();
        correctInputBorrowing(borrowing);
        
        if(borrowing.getId() == null){
            String msg = "Borrowing's id isn't set!";
            log.info(msg);
            throw new IllegalArgumentException("Borrowing's id isn't set!");
        }
        
        Connection conn = null;
        PreparedStatement st = null;
        
        try{
            conn = dataSource.getConnection();
            conn.setAutoCommit(false); 
            st = conn.prepareStatement("UPDATE BORROWING SET bookborrowedfrom=?,"
                    + "bookborrowedto=?,readerid=?,bookid=? WHERE id=?");
            
            st.setTimestamp(1, new Timestamp(borrowing.getBookBorrowedFrom().getTimeInMillis()));
            st.setTimestamp(2, new Timestamp(borrowing.getBookBorrowedTo().getTimeInMillis()));
            st.setLong(3, borrowing.getReader().getId());
            st.setLong(4, borrowing.getBook().getId());
            st.setLong(5, borrowing.getId());
            
            int updatedRows = st.executeUpdate();
            DBUtils.checkUpdatesCount(updatedRows, borrowing, false);
            
            conn.commit();
            log.info("Borrowing " + borrowing + " was updated.");
            
        } catch (SQLException ex) {
            String message = "Error when updating borrowing " + borrowing;
            log.error(message, ex);
            throw new ServiceFailureException(message, ex);
        } finally {
            DBUtils.doRollback(conn);
            DBUtils.closeConnection(conn, st);
        }
    }

    @Override
    public void deleteBorrowing(Borrowing borrowing) throws ServiceFailureException {
        checkDataSource();
        if(borrowing == null){
            String msg = "Input borrowing is null!";
            log.info(msg);
            throw new IllegalArgumentException(msg);
        }
        if(borrowing.getId() == null){
            String msg = "Input borrowing's id is null!";
            log.info(msg);
            throw new IllegalArgumentException("Input borrowing's id is null!");
        }
        
        Connection conn = null;
        PreparedStatement st = null;
        
        try{
            conn = dataSource.getConnection();
            conn.setAutoCommit(false); 
            st = conn.prepareStatement("DELETE FROM BORROWING WHERE id=? AND "
                    + "bookborrowedfrom=? AND bookborrowedto=? AND readerid=? AND bookid=?");
            
            st.setLong(1, borrowing.getId());
            st.setTimestamp(2, new Timestamp(borrowing.getBookBorrowedFrom().getTimeInMillis()));
            st.setTimestamp(3, new Timestamp(borrowing.getBookBorrowedTo().getTimeInMillis()));
            st.setLong(4, borrowing.getReader().getId());
            st.setLong(5, borrowing.getBook().getId());
            
            int deletedRows = st.executeUpdate();
            DBUtils.checkUpdatesCount(deletedRows, borrowing, false);
            
            conn.commit();
            log.info("Borrowing " + borrowing + " was deleted from database.");
            
        } catch (SQLException ex) {
            String message = "Error when deleting borrowing " + borrowing;
            log.error(message, ex);
            throw new ServiceFailureException(message, ex);
        } finally {
            DBUtils.doRollback(conn);
            DBUtils.closeConnection(conn, st);
        }
    }

    @Override
    public List<Borrowing> findAllBorrowing() {
        checkDataSource();
        
        Connection conn = null;
        PreparedStatement st = null;
        
        try{
            conn = dataSource.getConnection();
            st = conn.prepareStatement("SELECT id,bookborrowedfrom,bookborrowedto,readerid,bookid FROM BORROWING");
            
            ResultSet rs = st.executeQuery();
            List<Borrowing> listOfBorrowings = new ArrayList<>();
            
            while(rs.next()){
                listOfBorrowings.add(resultToBorrowing(rs));
            }
            
            log.info("All borrowings were found.");
            return listOfBorrowings;
        } catch (SQLException ex) {
            String message = "Error when finding all borrowings";
            log.error(message, ex);
            throw new ServiceFailureException(message, ex);
        } finally {
            DBUtils.closeConnection(conn, st);
        }
    }

    @Override
    public Borrowing findBorrowingById(Long id) {
        checkDataSource();
        
        if(id.intValue() <= 0){
            String msg = "Input id isn't positive number!";
            log.info(msg);
            throw new IllegalArgumentException("Input id isn't positive number!");
        }
        
        Connection conn = null;
        PreparedStatement st = null;
        
        try{
            conn = dataSource.getConnection();
            st = conn.prepareStatement("SELECT id,bookborrowedfrom,bookborrowedto,"
                    + "readerid,bookid FROM BORROWING WHERE id=?");
            
            st.setLong(1, id);
            ResultSet rs = st.executeQuery();
            
            if (rs.next()) {
                Borrowing borrowing = resultToBorrowing(rs);

                if (rs.next()) {
                    String msg = "Was founded more than one borrowing "
                            + "(Input id: " + id + " and were found " + borrowing + " and " + resultToBorrowing(rs);
                    log.info(msg);
                    throw new ServiceFailureException(msg);
                }            
                
                log.info("Borrowing with id " + id + " was found.");
                return borrowing;
            } else {
                return null;
            }
            
        } catch (SQLException ex) {
            String message = "Error when finding borrowing by id " + id;
            log.error(message, ex);
            throw new ServiceFailureException(message, ex);
        } finally {
            DBUtils.closeConnection(conn, st);
        }
    }

    @Override
    public List<Borrowing> findBorrowingByReader(Reader reader) {
        checkDataSource();
        
        if(reader == null){
            String msg = "Input reader is null!";
            log.info(msg);
            throw new IllegalArgumentException("Input reader is null!");
        }
        
        Connection conn = null;
        PreparedStatement st = null;
        
        try{
            conn = dataSource.getConnection();
            st = conn.prepareStatement("SELECT id,bookborrowedfrom,bookborrowedto,"
                    + "readerid,bookid FROM BORROWING WHERE readerid=?");
            
            st.setLong(1, reader.getId());
            ResultSet rs = st.executeQuery();
            List<Borrowing> listOfBorrowings = new ArrayList<>();
            
            while(rs.next()){
                listOfBorrowings.add(resultToBorrowing(rs));
            }
            
            log.info("Borrowings with reader " + reader + " were found.");
            return listOfBorrowings;
        } catch (SQLException ex) {
            String message = "Error when finding borrowings by name " + reader;
            log.error(message, ex);
            throw new ServiceFailureException(message, ex);
        } finally {
            DBUtils.closeConnection(conn, st);
        }
    }

    private void correctInputBorrowing(Borrowing borrowing) {
        String msg = null;
        if(borrowing == null){
            msg = "Input Borrowing is null!";
        }
        if(borrowing.getBookBorrowedFrom() == null){
            msg = "Not set when the book was borrowed!";
        }
        if(borrowing.getBookBorrowedTo() == null){
            msg = "Not set when the book will be returned!";
        }
        if(borrowing.getReader() == null){
            msg = "Reader who borrowed the book isn't set!";
        }
        if(borrowing.getBook() == null){
            msg = "The book that is borrowed isn't set!";
        }
        if(msg != null){
            log.info(msg);
            throw new IllegalArgumentException(msg);
        }
    }
    
    private Borrowing resultToBorrowing(ResultSet rs) throws SQLException {
        Borrowing borrowing = new Borrowing();
        borrowing.setId(rs.getLong("id"));
        
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(rs.getTimestamp("bookborrowedFrom").getTime());
        borrowing.setBookBorrowedFrom(cal);
        
        cal = Calendar.getInstance();
        cal.setTimeInMillis(rs.getTimestamp("bookborrowedTo").getTime());
        borrowing.setBookBorrowedTo(cal);
        
        ReaderManagerImpl managerOfReaders = new ReaderManagerImpl();
        managerOfReaders.setDataSource(dataSource);
        Reader reader = managerOfReaders.findReaderById(rs.getLong("readerid"));
        borrowing.setReader(reader);
        
        BookManagerImpl managerOfBooks = new BookManagerImpl();
        managerOfBooks.setDataSource(dataSource);
        Book book = managerOfBooks.getBookById(rs.getLong("bookid"));
        borrowing.setBook(book);
        
        return borrowing;
    }
}
