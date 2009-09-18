/*
 * VoicePanel.java
 *
 * Created on 17. September 2009, 17:25
 */

package marytts.tools.installvoices;

import java.util.Locale;

/**
 *
 * @author  marc
 */
public class VoicePanel extends javax.swing.JPanel {
    
    private VoiceComponentDescription desc;
    
    /** Creates new form VoicePanel */
    public VoicePanel(VoiceComponentDescription desc) {
        this.desc = desc;
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        cbSelect = new javax.swing.JCheckBox();
        lName = new javax.swing.JLabel();
        lNameValue = new javax.swing.JLabel();
        lName1 = new javax.swing.JLabel();
        lNameValue1 = new javax.swing.JLabel();
        lName2 = new javax.swing.JLabel();
        lNameValue2 = new javax.swing.JLabel();
        lNameValue3 = new javax.swing.JLabel();
        lName3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        lName4 = new javax.swing.JLabel();
        lNameValue4 = new javax.swing.JLabel();
        jProgressBar1 = new javax.swing.JProgressBar();

        setBorder(javax.swing.BorderFactory.createTitledBorder(desc.getLocale().getDisplayName(Locale.ENGLISH)+" voice "+desc.getName()));
        cbSelect.setSelected(desc.isSelected());
        cbSelect.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        cbSelect.setMargin(new java.awt.Insets(0, 0, 0, 0));

        lName.setFont(new java.awt.Font("Lucida Grande", 1, 13));
        lName.setText("Name:");

        lNameValue.setText(desc.getName());

        lName1.setFont(new java.awt.Font("Lucida Grande", 1, 13));
        lName1.setText("Type:");

        lNameValue1.setText("unit selection");

        lName2.setFont(new java.awt.Font("Lucida Grande", 1, 13));
        lName2.setText("Version:");

        lNameValue2.setText("150MB");

        lNameValue3.setText("4.0.0");

        lName3.setFont(new java.awt.Font("Lucida Grande", 1, 13));
        lName3.setText("Size:");

        jScrollPane1.setFont(new java.awt.Font("Courier New", 0, 10));
        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("Courier New", 0, 10));
        jTextArea1.setRows(3);
        jTextArea1.setText("A German male unit selection voice");
        jScrollPane1.setViewportView(jTextArea1);

        lName4.setFont(new java.awt.Font("Lucida Grande", 1, 13));
        lName4.setText("Status:");

        lNameValue4.setText("available");

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 519, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(cbSelect)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(lName)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(lNameValue)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(lName1)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(lNameValue1)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 33, Short.MAX_VALUE)
                        .add(lName2)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(lNameValue3)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(lName3)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(lNameValue2))
                    .add(layout.createSequentialGroup()
                        .add(lName4)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(lNameValue4)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 266, Short.MAX_VALUE)
                        .add(jProgressBar1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lName)
                    .add(lNameValue)
                    .add(lName1)
                    .add(lNameValue1)
                    .add(lNameValue2)
                    .add(lName3)
                    .add(lNameValue3)
                    .add(lName2)
                    .add(cbSelect, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 18, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 38, Short.MAX_VALUE)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(lName4)
                        .add(lNameValue4))
                    .add(jProgressBar1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox cbSelect;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JLabel lName;
    private javax.swing.JLabel lName1;
    private javax.swing.JLabel lName2;
    private javax.swing.JLabel lName3;
    private javax.swing.JLabel lName4;
    private javax.swing.JLabel lNameValue;
    private javax.swing.JLabel lNameValue1;
    private javax.swing.JLabel lNameValue2;
    private javax.swing.JLabel lNameValue3;
    private javax.swing.JLabel lNameValue4;
    // End of variables declaration//GEN-END:variables
    
}
