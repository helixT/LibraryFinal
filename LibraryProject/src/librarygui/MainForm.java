/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package librarygui;

import java.awt.Component;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import javax.swing.table.AbstractTableModel;
import knihovna.Borrowing;
import knihovna.BorrowingManagerImpl;
import knihovna.DBUtils;
import knihovna.Reader;
import knihovna.ReaderManagerImpl;
import org.apache.derby.jdbc.ClientDataSource;

/**
 *
 * @author Helix
 */
public class MainForm extends javax.swing.JFrame {

    private static ReaderManagerImpl readerManager = new ReaderManagerImpl();
    private static BorrowingManagerImpl borrowingManager = new BorrowingManagerImpl();
    private static DataSource dataSource;

    /**
     * Creates new form MainForm
     */
    public MainForm() {
        try {
            dataSource = prepareDataSource();
            readerManager.setDataSource(dataSource);
            borrowingManager.setDataSource(dataSource);
            DBUtils.executeSqlScript(dataSource, ReaderManagerImpl.class.getResource("createTables.sql"));
        } catch (SQLException ex) {
            String message = "Error when connecting with database";
            //log.log(Level.SEVERE, message, ex);
        } catch (IOException ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToolBar1 = new javax.swing.JToolBar();
        addButton = new javax.swing.JButton();
        editButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        bookTable = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        readerTable = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        borrowingTable = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        findByItems = new javax.swing.JComboBox();
        foundItems = new javax.swing.JComboBox();
        jMenuBar1 = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        exitItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Library App");

        jToolBar1.setRollover(true);

        addButton.setText("Add");
        addButton.setFocusable(false);
        addButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        addButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(addButton);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("string"); // NOI18N
        addButton.getAccessibleContext().setAccessibleName(bundle.getString("add")); // NOI18N

        editButton.setText("Edit");
        editButton.setFocusable(false);
        editButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        editButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        editButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(editButton);
        editButton.getAccessibleContext().setAccessibleName(bundle.getString("edit")); // NOI18N

        deleteButton.setText("Delete");
        deleteButton.setFocusable(false);
        deleteButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        deleteButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(deleteButton);
        deleteButton.getAccessibleContext().setAccessibleName(bundle.getString("delete")); // NOI18N

        bookTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Title", "Author", "Genre", "Quantity", "ISBN"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(bookTable);

        jTabbedPane1.addTab(bundle.getString("books"), jScrollPane1); // NOI18N

        readerTable.setModel(new ReadersTableModel());
        jScrollPane2.setViewportView(readerTable);

        jTabbedPane1.addTab(bundle.getString("readers"), jScrollPane2); // NOI18N

        borrowingTable.setModel(new BorrowingsTableModel());
        jScrollPane3.setViewportView(borrowingTable);

        jTabbedPane1.addTab(bundle.getString("borrowings"), jScrollPane3); // NOI18N

        jButton1.setText("Find By");

        findByItems.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Title", "Author", "Genre", "ISBN" }));
        findByItems.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                findByItemsActionPerformed(evt);
            }
        });

        foundItems.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        fileMenu.setText("File");

        exitItem.setText("Exit");
        exitItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitItemActionPerformed(evt);
            }
        });
        fileMenu.add(exitItem);
        exitItem.getAccessibleContext().setAccessibleName(bundle.getString("exit")); // NOI18N

        jMenuBar1.add(fileMenu);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 752, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(findByItems, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(foundItems, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton1)
                .addGap(14, 14, 14))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(findByItems, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(foundItems, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 286, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.getAccessibleContext().setAccessibleName(bundle.getString("books")); // NOI18N

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        openOnMenu("add");
    }//GEN-LAST:event_addButtonActionPerformed

    private void editButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editButtonActionPerformed
        openOnMenu("edit");
    }//GEN-LAST:event_editButtonActionPerformed

    private void openOnMenu(String name) {
        switch (jTabbedPane1.getSelectedIndex()) {
            case 0:
                EditBook editB = new EditBook();
                editB.name(ResourceBundle.getBundle("string").getString(name + "Book"));
                editB.setDefaultCloseOperation(EditReader.HIDE_ON_CLOSE);
                editB.setVisible(true);
                break;
            case 1:
                EditReader editR = new EditReader(readerManager);
                editR.setName(ResourceBundle.getBundle("string").getString(name + "Reader"));
                editR.setDefaultCloseOperation(EditReader.HIDE_ON_CLOSE);
                editR.setVisible(true);
                break;
            case 2:
                EditBorrowing editBorrowing = new EditBorrowing();
                editBorrowing.name(ResourceBundle.getBundle("string").getString(name + "Borrowing"));
                editBorrowing.setDefaultCloseOperation(EditReader.HIDE_ON_CLOSE);
                editBorrowing.setVisible(true);
                break;
            default:
                throw new IllegalArgumentException("TabbedPane wrong index.");
        }
    }

    private void exitItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitItemActionPerformed
        System.exit(0);
    }//GEN-LAST:event_exitItemActionPerformed

    private void findByItemsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_findByItemsActionPerformed

    }//GEN-LAST:event_findByItemsActionPerformed

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_deleteButtonActionPerformed

    private static DataSource prepareDataSource() throws SQLException, IOException {
        Properties myconf = new Properties();
        myconf.load(MainForm.class.getResourceAsStream("resources/conf.properties"));

        ClientDataSource ds = new ClientDataSource();
        ds.setDatabaseName(myconf.getProperty("jdbc.dbname"));
        ds.setUser(myconf.getProperty("jdbc.user"));
        ds.setPassword(myconf.getProperty("jdbc.password"));
        return ds;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainForm mainForm = new MainForm();
                mainForm.setDefaultCloseOperation(MainForm.EXIT_ON_CLOSE);
                mainForm.setName("Library");
                mainForm.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JTable bookTable;
    private javax.swing.JTable borrowingTable;
    private javax.swing.JButton deleteButton;
    private javax.swing.JButton editButton;
    private javax.swing.JMenuItem exitItem;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JComboBox findByItems;
    private javax.swing.JComboBox foundItems;
    private javax.swing.JButton jButton1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JTable readerTable;
    // End of variables declaration//GEN-END:variables

    private static class ReadersTableModel extends AbstractTableModel {

        @Override
        public int getRowCount() {
            return readerManager.findAllReaders().size();
        }

        @Override
        public int getColumnCount() {
            return 3;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            if (rowIndex >= readerManager.findAllReaders().size()) {
                throw new IllegalArgumentException("RowIndex > " + readerManager.findAllReaders().size());
            }
            if (columnIndex >= 3) {
                throw new IllegalArgumentException("ColumnIndex > 3");
            }
            Reader reader = readerManager.findAllReaders().get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return reader.getFullName();
                case 1:
                    return reader.getAddress();
                case 2:
                    return reader.getPhoneNumber();
                default:
                    throw new IllegalArgumentException("ColumnIndex < 0");
            }
        }

        @Override
        public String getColumnName(int columnIndex) {
            switch (columnIndex) {
                case 0:
                    return ResourceBundle.getBundle("string").getString("fullName");
                case 1:
                    return ResourceBundle.getBundle("string").getString("address");
                case 2:
                    return ResourceBundle.getBundle("string").getString("phoneNumber");
                default:
                    throw new IllegalArgumentException("columnIndex");
            }
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            switch (columnIndex) {
                case 0:
                case 1:
                    return String.class;
                case 2:
                    return Integer.class;
                default:
                    throw new IllegalArgumentException("columnIndex");
            }
        }
    }

    private static class BorrowingsTableModel extends AbstractTableModel {

        @Override
        public int getRowCount() {
            return borrowingManager.findAllBorrowing().size();
        }

        @Override
        public int getColumnCount() {
            return 4;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            if (rowIndex >= borrowingManager.findAllBorrowing().size()) {
                throw new IllegalArgumentException("RowIndex > " + borrowingManager.findAllBorrowing().size());
            }
            if (columnIndex >= 3) {
                throw new IllegalArgumentException("ColumnIndex > 3");
            }
            Borrowing borrowing = borrowingManager.findAllBorrowing().get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return borrowing.getReader().getFullName();
                case 1:
                    return borrowing.getBook().getTitle();
                case 2:
                    return borrowing.getBookBorrowedFrom();
                case 3:
                    return borrowing.getBookBorrowedTo();
                default:
                    throw new IllegalArgumentException("ColumnIndex < 0");
            }
        }

        @Override
        public String getColumnName(int columnIndex) {
            switch (columnIndex) {
                case 0:
                    return ResourceBundle.getBundle("string").getString("reader");
                case 1:
                    return ResourceBundle.getBundle("string").getString("book");
                case 2:
                    return ResourceBundle.getBundle("string").getString("borrowedFrom");
                case 3:
                    return ResourceBundle.getBundle("string").getString("borrowedTo");
                default:
                    throw new IllegalArgumentException("columnIndex");
            }
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            switch (columnIndex) {
                case 0:
                case 1:
                    return String.class;
                case 2:
                case 3:
                    return Calendar.class;
                default:
                    throw new IllegalArgumentException("columnIndex");
            }
        }

    }
}
