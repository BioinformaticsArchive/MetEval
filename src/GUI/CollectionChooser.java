/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * CollectionChooser.java
 *
 * Created on Sep 11, 2011, 2:46:11 PM
 */
package GUI;

import MetEvalmain.MetEvalApp;
import java.util.HashMap;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import meteval.types.MetModelCollection;

/**
 *
 * @author gieku
 */
public class CollectionChooser extends javax.swing.JDialog {
    private  HashMap<String, MetModelCollection> availableCollections;

    /** Creates new form CollectionChooser */
    private  CollectionChooser(java.awt.Frame parent, HashMap<String,MetModelCollection> availableCollections ){
        super(parent, true);
        initComponents();
        this.availableCollections =  availableCollections;
        populateList();
        this.setVisible(true);
    }

    private CollectionChooser(JFrame parent, HashMap<String, MetModelCollection> availableCollections, String message) {
        super(parent, true);
        initComponents();
        this.availableCollections =  availableCollections;
        populateList();
        this.setTitle(message);
        this.setVisible(true);
    }
    private void populateList(){
        DefaultListModel listModel = new DefaultListModel();
        this.collectionsList.setModel(listModel);
        for(MetModelCollection collection: availableCollections.values()){
            listModel.addElement(collection);
        }
    }
    public static MetModelCollection showDialog(JFrame parent, MetEvalApp app,String message){
        CollectionChooser chooser = new CollectionChooser(parent, app.getAvailableCollections(),message);
        return value;
    }
    public static MetModelCollection showDialog(JFrame parent, MetEvalApp app){
        CollectionChooser chooser = new CollectionChooser(parent, app.getAvailableCollections());
        return value;
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        collectionsList = new javax.swing.JList();
        jToggleButton1 = new javax.swing.JToggleButton();
        jToggleButton2 = new javax.swing.JToggleButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        collectionsList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        collectionsList.setName("collectionsList"); // NOI18N
        jScrollPane1.setViewportView(collectionsList);

        jToggleButton1.setText("Select");
        jToggleButton1.setName("jToggleButton1"); // NOI18N
        jToggleButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jToggleButton1MouseClicked(evt);
            }
        });

        jToggleButton2.setText("Cancel");
        jToggleButton2.setName("jToggleButton2"); // NOI18N
        jToggleButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jToggleButton2MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jToggleButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jToggleButton1)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 264, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jToggleButton1)
                    .addComponent(jToggleButton2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private MetModelCollection choosenCollection;
    private void jToggleButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jToggleButton1MouseClicked
        choosenCollection = (MetModelCollection) this.collectionsList.getSelectedValue();
        if(choosenCollection != null){
            setValue(choosenCollection);
            this.dispose();
        }
    }//GEN-LAST:event_jToggleButton1MouseClicked

    private void jToggleButton2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jToggleButton2MouseClicked
        setValue(null);
        this.dispose();
    }//GEN-LAST:event_jToggleButton2MouseClicked

    /**
     * @param args the command line arguments
     */
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList collectionsList;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JToggleButton jToggleButton2;
    // End of variables declaration//GEN-END:variables
    public static MetModelCollection value;
    private void setValue(MetModelCollection choosenCollection) {
        value = choosenCollection;
        
    }
}