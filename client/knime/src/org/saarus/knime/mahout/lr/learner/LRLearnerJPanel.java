package org.saarus.knime.mahout.lr.learner;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import org.saarus.knime.uicomp.SpringUtilities;

public class LRLearnerJPanel extends JPanel {
  final static int MAX_WIDTH = LRLearnerNodeDialog.WIDTH ;

  private JTextField nameInput = new JTextField();
  private JTextField descInput = new JTextField();
  
  private JTextField passes = new JTextField("100"); //  --passes 100,
  private JTextField rate   = new JTextField("50");  // --rate 50
  private JTextField lambda = new JTextField() ;  //--lambda 0.001
  private JTextField features = new JTextField("21"); // --features 21
  private JTextField categories = new JTextField("2"); //  --categories 2
  //--predictors  x, y, xx, xy, yy, a, b, c
  private JTextField predictors = new JTextField("x, y, xx, xy, yy, a, b, c") ; 
  private JTextField types = new JTextField("n, n") ; //--types n, n
  private JComboBox<String> inputType = new JComboBox<String>(); //input type csv , sql table..
  private JTextField input = new JTextField("/path/to/donut.csv"); //--input donut.csv 
  private JComboBox<String> target = new JComboBox<String>() ; //--target color
  private JTextField output = new JTextField() ;// --output donut.model
 

  public LRLearnerJPanel() {
    setLayout(new BorderLayout()) ;
    add(createInputBox(),       BorderLayout.NORTH);
  }

  private JPanel createInputBox() {
    inputType = new JComboBox<String>();
    inputType.setEditable(true);
    inputType.setToolTipText("Select a type");
    inputType.addItemListener(new SelectTableListener());
    
    predictors.addMouseListener(new MouseAdapter() {
      public void mousePressed(MouseEvent e) {
        if(e.getClickCount() > 1) {
          FieldSelector selector = new FieldSelector() ;
          selector.setLocationRelativeTo(predictors) ;
          selector.setVisible(true) ;
        }
      }
    });
    
    JPanel panel = new JPanel(new SpringLayout()) ;
    panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Parameters"));
    addInput(panel, "Name", nameInput) ;
    addInput(panel, "Description", descInput) ;
    addInput(panel, "Passes", passes) ;
    addInput(panel, "Rate", rate) ;
    addInput(panel, "Lambda", lambda) ;
    addInput(panel, "Features", features) ;
    addInput(panel, "Categories", categories) ;
    addInput(panel, "Predictors", predictors) ;
    addInput(panel, "Types", types) ;
    addInput(panel, "Input Type", inputType) ;
    addInput(panel, "Input Location", input) ;
    addInput(panel, "Target", target) ;
    addInput(panel, "Output", output) ;
    
    
    SpringUtilities.makeCompactGrid(panel, /*rows, cols*/13, 2,/*initX, initY*/ 6, 6, 
                                    /*xPad, yPad*/6, 6);       
    return panel ;
  }

  private void addInput(JPanel panel, String label, JComponent comp) {
    panel.add(new JLabel(label)) ;
    panel.add(comp) ;
  }
  
  static public class SelectTableListener implements ItemListener {
    public void itemStateChanged(ItemEvent event) {
    }
  }
  
  static public class FieldSelector extends JDialog {
    public FieldSelector() {
      setTitle("Select The Fields") ;
      setMinimumSize(new Dimension(150, 350)) ;
      setLayout(new BorderLayout()) ;
      setAlwaysOnTop(true) ;
      JButton close = new JButton("OK") ;
      close.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent event) {
          onClose() ;
        }
      }) ;
      JPanel algPanel = new JPanel() ;
      algPanel.setLayout(new SpringLayout()) ;
      String[] fields = {
        "review_id", "user_id", "user_name", "business_id", "business_name",
        "vote_funny", "vote_useful", "vote_cool"
      } ;
      for(int i = 0; i < fields.length; i++) {
        JCheckBox checkBox = new JCheckBox() ;
        checkBox.setName(fields[i]) ;
        algPanel.add(checkBox) ;
        algPanel.add(new JLabel(fields[i])) ;
      }
      SpringUtilities.makeCompactGrid(algPanel, /*rows, cols*/fields.length, 2,  
                                     /*initX, initY*/ 6, 6, /*xPad, yPad*/6, 6);
      add(algPanel, BorderLayout.CENTER) ;
      add(close, BorderLayout.SOUTH) ;
    }
    
    public void onClose() {
      dispose() ;
    }
  }
}