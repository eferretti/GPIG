package gpigb.report;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

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
		frmHumsBase.getContentPane().setLayout(null);
		
		JLabel lblApp1 = new JLabel("Application 1");
		lblApp1.setBounds(24, 96, 107, 15);
		frmHumsBase.getContentPane().add(lblApp1);
		
		JLabel lblApp2 = new JLabel("Application 2");
		lblApp2.setBounds(24, 155, 107, 15);
		frmHumsBase.getContentPane().add(lblApp2);
		
		JLabel lblMode = new JLabel("<html>Mode Changes<br> Per Minute");
		lblMode.setBounds(165, 23, 127, 52);
		frmHumsBase.getContentPane().add(lblMode);
		
		JLabel lblFaults = new JLabel("Number of Faults");
		lblFaults.setBounds(304, 44, 132, 15);
		frmHumsBase.getContentPane().add(lblFaults);
		
		tf1M = new JTextField();
		tf1M.setEditable(false);
		tf1M.setBounds(186, 91, 54, 25);
		frmHumsBase.getContentPane().add(tf1M);
		tf1M.setColumns(10);
		tf1M.setText("0");
		tf1M.setHorizontalAlignment(JTextField.CENTER);
		
		tf2M = new JTextField();
		tf2M.setEditable(false);
		tf2M.setColumns(10);
		tf2M.setBounds(186, 150, 54, 25);
		frmHumsBase.getContentPane().add(tf2M);
		tf2M.setText("0");
		tf2M.setHorizontalAlignment(JTextField.CENTER);
		
		tf1F = new JTextField();
		tf1F.setEditable(false);
		tf1F.setColumns(10);
		tf1F.setBounds(340, 91, 54, 25);
		frmHumsBase.getContentPane().add(tf1F);
		tf1F.setText("0");
		tf1F.setHorizontalAlignment(JTextField.CENTER);
		
		tf2F = new JTextField();
		tf2F.setEditable(false);
		tf2F.setColumns(10);
		tf2F.setBounds(340, 150, 54, 25);
		frmHumsBase.getContentPane().add(tf2F);
		tf2F.setText("0");
		tf2F.setHorizontalAlignment(JTextField.CENTER);
	}

}
