package gpigb.report;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class BaseGUI {

	private JFrame frmHumsBase;
	private JTextField tf1M;
	private JTextField tf2M;
	private JTextField tf1F;
	private JTextField tf2F;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BaseGUI window = new BaseGUI();
					window.frmHumsBase.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public BaseGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmHumsBase = new JFrame();
		frmHumsBase.setTitle("HUMS Base");
		frmHumsBase.setBounds(100, 100, 450, 250);
		frmHumsBase.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{107, 34, 127, 132, 0};
		gridBagLayout.rowHeights = new int[]{52, 25, 34, 25, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		frmHumsBase.getContentPane().setLayout(gridBagLayout);
		
		JLabel lblMode = new JLabel("<html>Mode Changes<br> Per Minute");
		GridBagConstraints gbc_lblMode = new GridBagConstraints();
		gbc_lblMode.fill = GridBagConstraints.BOTH;
		gbc_lblMode.insets = new Insets(0, 0, 5, 5);
		gbc_lblMode.gridx = 2;
		gbc_lblMode.gridy = 0;
		frmHumsBase.getContentPane().add(lblMode, gbc_lblMode);
		
		JLabel lblFaults = new JLabel("Number of Faults");
		GridBagConstraints gbc_lblFaults = new GridBagConstraints();
		gbc_lblFaults.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblFaults.insets = new Insets(0, 0, 5, 0);
		gbc_lblFaults.gridx = 3;
		gbc_lblFaults.gridy = 0;
		frmHumsBase.getContentPane().add(lblFaults, gbc_lblFaults);
		
		JLabel lblApp1 = new JLabel("Application 1");
		GridBagConstraints gbc_lblApp1 = new GridBagConstraints();
		gbc_lblApp1.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblApp1.insets = new Insets(0, 0, 5, 5);
		gbc_lblApp1.gridx = 0;
		gbc_lblApp1.gridy = 1;
		frmHumsBase.getContentPane().add(lblApp1, gbc_lblApp1);
		
		tf1M = new JTextField();
		tf1M.setEditable(false);
		GridBagConstraints gbc_tf1M = new GridBagConstraints();
		gbc_tf1M.anchor = GridBagConstraints.WEST;
		gbc_tf1M.fill = GridBagConstraints.VERTICAL;
		gbc_tf1M.insets = new Insets(0, 0, 5, 5);
		gbc_tf1M.gridx = 2;
		gbc_tf1M.gridy = 1;
		frmHumsBase.getContentPane().add(tf1M, gbc_tf1M);
		tf1M.setColumns(10);
		tf1M.setText("0");
		tf1M.setHorizontalAlignment(JTextField.CENTER);
		
		tf1F = new JTextField();
		tf1F.setEditable(false);
		tf1F.setColumns(10);
		GridBagConstraints gbc_tf1F = new GridBagConstraints();
		gbc_tf1F.anchor = GridBagConstraints.WEST;
		gbc_tf1F.fill = GridBagConstraints.VERTICAL;
		gbc_tf1F.insets = new Insets(0, 0, 5, 0);
		gbc_tf1F.gridx = 3;
		gbc_tf1F.gridy = 1;
		frmHumsBase.getContentPane().add(tf1F, gbc_tf1F);
		tf1F.setText("0");
		tf1F.setHorizontalAlignment(JTextField.CENTER);
		
		JLabel lblApp2 = new JLabel("Application 2");
		GridBagConstraints gbc_lblApp2 = new GridBagConstraints();
		gbc_lblApp2.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblApp2.insets = new Insets(0, 0, 0, 5);
		gbc_lblApp2.gridx = 0;
		gbc_lblApp2.gridy = 3;
		frmHumsBase.getContentPane().add(lblApp2, gbc_lblApp2);
		
		tf2M = new JTextField();
		tf2M.setEditable(false);
		tf2M.setColumns(10);
		GridBagConstraints gbc_tf2M = new GridBagConstraints();
		gbc_tf2M.anchor = GridBagConstraints.WEST;
		gbc_tf2M.fill = GridBagConstraints.VERTICAL;
		gbc_tf2M.insets = new Insets(0, 0, 0, 5);
		gbc_tf2M.gridx = 2;
		gbc_tf2M.gridy = 3;
		frmHumsBase.getContentPane().add(tf2M, gbc_tf2M);
		tf2M.setText("0");
		tf2M.setHorizontalAlignment(JTextField.CENTER);
		
		tf2F = new JTextField();
		tf2F.setEditable(false);
		tf2F.setColumns(10);
		GridBagConstraints gbc_tf2F = new GridBagConstraints();
		gbc_tf2F.anchor = GridBagConstraints.WEST;
		gbc_tf2F.fill = GridBagConstraints.VERTICAL;
		gbc_tf2F.gridx = 3;
		gbc_tf2F.gridy = 3;
		frmHumsBase.getContentPane().add(tf2F, gbc_tf2F);
		tf2F.setText("0");
		tf2F.setHorizontalAlignment(JTextField.CENTER);
		
		frmHumsBase.pack();
	}

}
