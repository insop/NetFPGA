package org.netfpga.router;
/*
 * MACTxQInternalFrame.java
 *
 * Created on May 7, 2007, 9:35 PM
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.netfpga.backend.NFDevice;
import org.netfpga.backend.NFDeviceConsts;
import org.netfpga.backend.RegTableModel;
import org.netfpga.backend.Register;

/**
 *
 * @author  jnaous
 */
@SuppressWarnings("serial")
public class MACTxQInternalFrame extends javax.swing.JInternalFrame {

    private int index;
    private StatsRegTableModel statsRegTableModel;
    private ControlRegGroup ctrlRegGrp;

    private Timer timer;
    private ActionListener timerActionListener;

    private static final int STATS_NUM_REGS_USED = 2;

    /** Creates new form MACRxQInternalFrame
     * @param nf2 the device to connect to
     * @param index has to be between 0 and 3 inclusive
     */
    public MACTxQInternalFrame(NFDevice nf2, Timer timer, int index) {
        this.index = index;
        initComponents();

        this.timer = timer;
        setupStatsTable(nf2, index);
        this.statsRegTable.setModel(statsRegTableModel);
        ((StatsRegTable)this.statsRegTable).setDefaults();

        AbstractButton[] buttons = new AbstractButton[32];
        boolean[] invert = new boolean[32];
        for(int i=0; i<32; i++){
            invert[i] = false;
        }

        /* setup the register hooks for each button */
        buttons[NFDeviceConsts.RESET_MAC_BIT_NUM] = this.resetMacButton;
        buttons[NFDeviceConsts.MAC_DIS_JUMBO_TX_BIT_NUM] = this.enableJumboCheckBox;
        buttons[NFDeviceConsts.MAC_DISABLE_TX_BIT_NUM] = this.enabledCheckbox;
        invert[NFDeviceConsts.MAC_DISABLE_TX_BIT_NUM] = true;

        int indexDif = NFDeviceConsts.MAC_GRP_1_CONTROL_REG - NFDeviceConsts.MAC_GRP_0_CONTROL_REG;
        ctrlRegGrp = new ControlRegGroup(nf2, NFDeviceConsts.MAC_GRP_0_CONTROL_REG+indexDif*index,
                                          buttons, invert);

        /* add listeners to the update the tables */
        timerActionListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                statsRegTableModel.updateTable();
                ctrlRegGrp.updateFromRegs();
            }
        };

        /* add action listener to the timer */
        timer.addActionListener(timerActionListener);
    }

    private void setupStatsTable(NFDevice nf2, int index) {
        /* add the addresses to monitor through statsRegTableModel */
        long[] aAddresses = new long[STATS_NUM_REGS_USED];
        /* get the difference between two MAC blocks of aAddresses */
        long indexDif = NFDeviceConsts.MAC_GRP_1_CONTROL_REG - NFDeviceConsts.MAC_GRP_0_CONTROL_REG;
        aAddresses[0] = NFDeviceConsts.TX_QUEUE_0_NUM_PKTS_SENT_REG;
        aAddresses[1] = NFDeviceConsts.TX_QUEUE_0_NUM_BYTES_PUSHED_REG;

        /* add the difference between address blocks */
        for(int i=0; i<STATS_NUM_REGS_USED; i++){
            aAddresses[i] += indexDif*index;
        }

        String[] descriptions = new String[STATS_NUM_REGS_USED];
        descriptions[0] = "Total packets sent";
        descriptions[1] = "Total bytes sent";

        /* create the register table model which we want to monitor */
        statsRegTableModel = new StatsRegTableModel(nf2, aAddresses, descriptions);

        statsRegTableModel.setGraph(0, (GraphPanel)this.pktThroughputPanel);
        statsRegTableModel.setDifferentialGraph(0, true);

        statsRegTableModel.setGraph(1, (GraphPanel)this.byteThroughputPanel);
        statsRegTableModel.setDifferentialGraph(1, true);
        statsRegTableModel.setDivider(1, 1024);
        statsRegTableModel.setUnits(1, "kB");
    }


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        MACTxQScrollPane = new javax.swing.JScrollPane();
        MACTxQPanel = new javax.swing.JPanel();
        pageTitleLabel = new javax.swing.JLabel();
        enabledCheckbox = new ControlCheckBox();
        enableJumboCheckBox = new ControlCheckBox();
        resetMacButton = new ControlButton();
        titleConfigSeparator = new javax.swing.JSeparator();
        configStatsSeparator = new javax.swing.JSeparator();
        pktThroughputPanel = new BarGraphPanel("Tx Throughput", "Sent Packets", "time", "Number of sent packets", 2000);
        byteThroughputPanel = new BarGraphPanel("Tx Throughput", "Sent bytes", "time", "Number of sent bytes (kB)", 2000);
        jScrollPane1 = new javax.swing.JScrollPane();
        statsRegTable = new StatsRegTable();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("MAC Tx Queue " + this.index);
        setMinimumSize(new java.awt.Dimension(0, 0));
        setPreferredSize(new java.awt.Dimension(540, 500));
        setVisible(true);
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameClosed(evt);
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
            }
        });

        pageTitleLabel.setFont(new java.awt.Font("Dialog", 1, 18));
        pageTitleLabel.setText("MAC Tx Queue " + this.index);

        enabledCheckbox.setText("Enabled");
        enabledCheckbox.setToolTipText("Uncheck to disable receiving any packet on this port");
        enabledCheckbox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        enabledCheckbox.setMargin(new java.awt.Insets(0, 0, 0, 0));

        enableJumboCheckBox.setText("Enable Jumbo Frames");
        enableJumboCheckBox.setToolTipText("Check to enable sendin  jumbo packets");
        enableJumboCheckBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        enableJumboCheckBox.setMargin(new java.awt.Insets(0, 0, 0, 0));

        resetMacButton.setText("Reset MAC");
        resetMacButton.setToolTipText("Click to reset the Ethernet MAC port");

        pktThroughputPanel.setPreferredSize(new java.awt.Dimension(300, 300));
        org.jdesktop.layout.GroupLayout pktThroughputPanelLayout = new org.jdesktop.layout.GroupLayout(pktThroughputPanel);
        pktThroughputPanel.setLayout(pktThroughputPanelLayout);
        pktThroughputPanelLayout.setHorizontalGroup(
            pktThroughputPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 235, Short.MAX_VALUE)
        );
        pktThroughputPanelLayout.setVerticalGroup(
            pktThroughputPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 229, Short.MAX_VALUE)
        );

        byteThroughputPanel.setPreferredSize(new java.awt.Dimension(300, 300));
        org.jdesktop.layout.GroupLayout byteThroughputPanelLayout = new org.jdesktop.layout.GroupLayout(byteThroughputPanel);
        byteThroughputPanel.setLayout(byteThroughputPanelLayout);
        byteThroughputPanelLayout.setHorizontalGroup(
            byteThroughputPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 235, Short.MAX_VALUE)
        );
        byteThroughputPanelLayout.setVerticalGroup(
            byteThroughputPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 229, Short.MAX_VALUE)
        );

        jScrollPane1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        statsRegTable.setBackground(javax.swing.UIManager.getDefaults().getColor("Label.background"));
        statsRegTable.setFont(new java.awt.Font("Dialog", 1, 12));
        statsRegTable.setGridColor(javax.swing.UIManager.getDefaults().getColor("Label.background"));
        statsRegTable.setRowSelectionAllowed(false);
        statsRegTable.setShowHorizontalLines(false);
        statsRegTable.setShowVerticalLines(false);
        jScrollPane1.setViewportView(statsRegTable);

        org.jdesktop.layout.GroupLayout MACTxQPanelLayout = new org.jdesktop.layout.GroupLayout(MACTxQPanel);
        MACTxQPanel.setLayout(MACTxQPanelLayout);
        MACTxQPanelLayout.setHorizontalGroup(
            MACTxQPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(MACTxQPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(pageTitleLabel)
                .add(352, 352, 352))
            .add(org.jdesktop.layout.GroupLayout.TRAILING, titleConfigSeparator, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 527, Short.MAX_VALUE)
            .add(MACTxQPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(MACTxQPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(MACTxQPanelLayout.createSequentialGroup()
                        .add(enabledCheckbox)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 336, Short.MAX_VALUE)
                        .add(resetMacButton))
                    .add(enableJumboCheckBox))
                .addContainerGap())
            .add(configStatsSeparator, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 527, Short.MAX_VALUE)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, MACTxQPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(pktThroughputPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE)
                .add(33, 33, 33)
                .add(byteThroughputPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE)
                .addContainerGap())
            .add(MACTxQPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 503, Short.MAX_VALUE)
                .addContainerGap())
        );
        MACTxQPanelLayout.setVerticalGroup(
            MACTxQPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(MACTxQPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(pageTitleLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(titleConfigSeparator, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(MACTxQPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(enabledCheckbox)
                    .add(resetMacButton))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(enableJumboCheckBox)
                .add(28, 28, 28)
                .add(configStatsSeparator, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 64, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 14, Short.MAX_VALUE)
                .add(MACTxQPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                    .add(byteThroughputPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 229, Short.MAX_VALUE)
                    .add(pktThroughputPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 229, Short.MAX_VALUE))
                .addContainerGap())
        );
        MACTxQScrollPane.setViewportView(MACTxQPanel);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(MACTxQScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 530, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(MACTxQScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 468, Short.MAX_VALUE)
        );
        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formInternalFrameClosed(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosed
        this.timer.removeActionListener(this.timerActionListener);
    }//GEN-LAST:event_formInternalFrameClosed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel MACTxQPanel;
    private javax.swing.JScrollPane MACTxQScrollPane;
    private javax.swing.JPanel byteThroughputPanel;
    private javax.swing.JSeparator configStatsSeparator;
    private javax.swing.JCheckBox enableJumboCheckBox;
    private javax.swing.JCheckBox enabledCheckbox;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel pageTitleLabel;
    private javax.swing.JPanel pktThroughputPanel;
    private javax.swing.JButton resetMacButton;
    private javax.swing.JTable statsRegTable;
    private javax.swing.JSeparator titleConfigSeparator;
    // End of variables declaration//GEN-END:variables

}
