package org.saarus.knime.data.hive;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SpringLayout;

import org.saarus.knime.data.hive.QueryConfigs.QueryConfig;
import org.saarus.knime.uicomp.SpringUtilities;
import org.saarus.swing.sql.SQLOutputTable;
import org.saarus.swing.sql.SQLQuery;
import org.saarus.swing.sql.SQLQueryBuilderUtil;

public class QueryConfigPanel extends JPanel {
  final static int MAX_WIDTH = QueryNodeDialog.WIDTH ;
  
  private JTextField nameInput, descInput ;
  private JTextArea queryArea ;
  private SQLQueryBuilderDialog queryBuilderDialog ;
  
  public QueryConfigPanel(QueryConfig config) {
    setLayout(new BorderLayout()) ;
    add(createDescBox(config),       BorderLayout.NORTH);
    add(createQueryBox(config), BorderLayout.CENTER) ;
  }
  
  private JPanel createDescBox(QueryConfig config) {
    JPanel panel = new JPanel(new SpringLayout()) ;
    panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Description"));
    
    nameInput = new JTextField(config.name) ;
    descInput = new JTextField(config.description) ;
    
    panel.add(new JLabel("Name")) ;
    panel.add(nameInput) ;
    
    panel.add(new JLabel("Description")) ;
    panel.add(descInput) ;
    
    SpringUtilities.makeCompactGrid(panel, /*rows, cols*/ 2, 2, /*initX, initY*/6, 6, /*xPad, yPad*/6, 6);
    return panel ;
  }
  
  private JPanel createQueryBox(QueryConfig config) {
    String query = 
      "CREATE TABLE yelp_features (\n" +
      "  review_id      STRING,\n" +
      "  user_id        STRING,\n" +
      "  user_name      STRING,\n" +
      "  business_id    STRING,\n" +
      "  business_name  STRING,\n" +
      "  vote_useful    INT,\n" +
      "  vote_cool      INT,\n" +
      "  vote_funny     INT,\n" +
      ") ROW FORMAT DELIMITED FIELDS TERMINATED BY ',' ; \n\n" +
      
      "INSERT OVERWRITE TABLE yelp_features \n" +
      "SELECT r.review_id, r.user_id, u.name, r.business_id, b.name, r.vote_funny, r.vote_useful, r.vote_cool\n" +
      "FROM review r \n " +
      "JOIN user u ON(r.user_id = u.user_id) \n " +
      "JOIN business b ON(r.business_id = b.business_id) \n " ;
    if(config.query != null && config.query.length() > 0) {
      query = config.query ;
    }
    JPanel panel = new JPanel(new BorderLayout()) ;
    panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Query"));
    JToolBar toolbar = new JToolBar() ;
    toolbar.add(new AbstractAction("Design") {
      public void actionPerformed(ActionEvent e) {
        SQLQueryBuilderDialog queryBuilder = getSQLQueryBuilderDialog() ;
        queryBuilder.setLocation(50, 20) ;
        queryBuilder.setVisible(true) ;
      }
    });
    panel.add(toolbar, BorderLayout.NORTH);
    
    queryArea = new JTextArea(query) ;
    JScrollPane scrollText = new JScrollPane(queryArea);
    
    panel.add(scrollText, BorderLayout.CENTER);
    return panel ;
  }
  
  QueryConfig getQueryConfig() {
    String name = nameInput.getText() ;
    String desc  = descInput.getText() ;
    String query = queryArea.getText()  ;
    QueryConfig config = new QueryConfig(name, desc, query) ;
    return config ;
  }
  
  SQLQueryBuilderDialog getSQLQueryBuilderDialog() {
    if(queryBuilderDialog == null) {
      queryBuilderDialog = new SQLQueryBuilderDialog() ;
      queryBuilderDialog.addToolbarAction("Generate SQL", new AbstractAction() {
        public void actionPerformed(ActionEvent evt) {
          SQLOutputTable outTable = SQLQueryBuilderUtil.getSQLOutputTable(queryBuilderDialog.getSQLQueryBuilder()) ;
          SQLQuery sqlQuery = outTable.getSQLQuery() ;
          StringBuilder b = new StringBuilder() ;
          b.append(sqlQuery.getOutputSQLTable().buildDropTableSQL() + ";\n\n");
          b.append(sqlQuery.getOutputSQLTable().buildCreateTable() + ";\n\n");
          b.append(sqlQuery.buildInsertSQLQuery() + ";\n\n");
          queryArea.setText(b.toString());
          queryBuilderDialog.setVisible(false) ;
        }
      });
    }
    return queryBuilderDialog ;
  }
}