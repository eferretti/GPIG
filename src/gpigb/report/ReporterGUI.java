package gpigb.report;

import gpigb.analyse.NullAnalyser;
import gpigb.data.RecordSet;
import gpigb.data.SensorRecord;
import gpigb.store.InMemoryStore;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.util.Calendar;
import java.util.Date;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;

public class ReporterGUI {

	private JFrame frame;
	private final Action action = new SwingAction();
	private JButton btnNewButton;
	private JTextArea textArea;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ReporterGUI window = new ReporterGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ReporterGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		
		btnNewButton = new JButton("New button");
		btnNewButton.setAction(action);
		frame.getContentPane().add(btnNewButton, BorderLayout.NORTH);
		
		textArea = new JTextArea();
		frame.getContentPane().add(textArea, BorderLayout.CENTER);
		
	}
	
	private class SwingAction extends AbstractAction {
		public SwingAction() {
			putValue(NAME, "Get Analysis");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
			Calendar c = Calendar.getInstance();
			c.set(2010, 1, 1, 1, 1);
			Date d1 = c.getTime();
			c.set(2015, 1, 1, 1, 1);
			Date d2 = c.getTime();	
			
			NullAnalyser a = new NullAnalyser();
			InMemoryStore store = new InMemoryStore();
			a.store = store;
			
			// Fake data
			RecordSet<Integer> fRS = new RecordSet<Integer>(d1, d2, 1);
			SensorRecord<Integer> data = new SensorRecord<Integer>(1,100);
			SensorRecord<Integer> data2 = new SensorRecord<Integer>(1,50);
			fRS.addRecord((SensorRecord) data);
			fRS.addRecord((SensorRecord) data2); 
			fRS.addRecord((SensorRecord) data); 
			a.store.write(fRS);
			
			RecordSet<Integer> rs = new RecordSet<Integer>(d1, d2, 1);
			a.Analyse(rs);
			textArea.append("Count: " + rs.getRecordCount() + "\n");
			textArea.append("Analysis between: " + d1 + " and " + d2 + "\n");
			textArea.append("Result1: " + rs.getReadingAtPosition(0).getData()+ "\n"); 
			textArea.append("Result2: " + rs.getReadingAtPosition(1).getData()+ "\n");
			
		}
	}
}
