package gpigb.report;

import gpigb.analyse.Analyser;
import gpigb.analyse.NullAnalyser;
import gpigb.data.RecordSet;
import gpigb.data.SensorRecord;
import gpigb.store.InMemoryStore;
import gpigb.store.Store;

import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * Invokes an analyser from a reporter interface to provide analysis over the
 * provided date range.
 */
public class ReporterGUI {

	private JFrame frame;
	private final Action action = new SwingAction();
	private JTextArea textArea;
	private JComboBox cbMonthsFrom;
	private JComboBox cbMonthsTo;
	private JComboBox cbYearFrom;
	private JComboBox cbYearTo;
	private JComboBox cbDateFrom;
	private JComboBox cbDateTo;
	private JComboBox cbMinFrom;
	private JComboBox cbHoursFrom;
	private JComboBox cbMinTo;
	private JComboBox cbHoursTo;
	private JLabel lblFrom;
	


	public Analyser analyser; 

	/**
	 * Create the application.
	 */
	public ReporterGUI() {
		initialize();
	}

	public void show()
	{
		this.frame.setVisible(true);
		
	}
	
	/**
	 * Initialise the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 609, 497);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[] {0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 1.0, 0.0, 1.0, 0.0, 0.0, 1.0, 1.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 1.0};
		frame.getContentPane().setLayout(gridBagLayout);
		
		//Drop down box contents
		String[] years = {"2015","2014","2013","2012","2011","2010","2009","2008","2007","2006","2005"};
		String[] months = {"January", "February", "March", "April", "May","June","July","August","September","October","November","December"};
		
		String[] days = new String[31];
		for(int i=1; i<32; i++)
		{
			days[i-1] = Integer.toString(i);
		}
		
		String[] hours = new String[24];
		for(int i=10; i<24; i++)
		{
			hours[i] = Integer.toString(i);
		}
		
		String[] minutes = new String[60];
		for(int i=10; i<60; i++)
		{
			minutes[i] = Integer.toString(i);
		}
		
		//Leading 0s for hours and minutes on <10
		for(int i=0; i<10; i++)
		{
			hours[i] = "0" + Integer.toString(i);
			minutes[i] = "0" + Integer.toString(i);
		}
		
		JButton btnNewButton = new JButton("Get Analysis");
		btnNewButton.setAction(action);
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton.gridx = 4;
		gbc_btnNewButton.gridy = 0;
		frame.getContentPane().add(btnNewButton, gbc_btnNewButton);
		
		lblFrom = new JLabel("From:");
		GridBagConstraints gbc_lblFrom = new GridBagConstraints();
		gbc_lblFrom.insets = new Insets(0, 0, 5, 5);
		gbc_lblFrom.gridx = 1;
		gbc_lblFrom.gridy = 1;
		frame.getContentPane().add(lblFrom, gbc_lblFrom);
		
		cbHoursFrom = new JComboBox(hours);
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.anchor = GridBagConstraints.EAST;
		gbc_comboBox.insets = new Insets(0, 0, 5, 5);
		gbc_comboBox.gridx = 1;
		gbc_comboBox.gridy = 2;
		frame.getContentPane().add(cbHoursFrom, gbc_comboBox);
		gbc_comboBox.insets = new Insets(0, 0, 5, 5);
		gbc_comboBox.gridx = 2;
		gbc_comboBox.gridy = 2;
		
		JLabel lblNewLabel = new JLabel(":");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 2;
		gbc_lblNewLabel.gridy = 2;
		frame.getContentPane().add(lblNewLabel, gbc_lblNewLabel);
		
		JLabel lblc = new JLabel(":");
		GridBagConstraints gbc_lblc = new GridBagConstraints();
		gbc_lblc.insets = new Insets(0, 0, 5, 5);
		gbc_lblc.gridx = 2;
		gbc_lblc.gridy = 4;
		frame.getContentPane().add(lblc, gbc_lblc);
		
		cbMinFrom= new JComboBox(minutes);
		GridBagConstraints gbc_comboBox_8 = new GridBagConstraints();
		gbc_comboBox_8.anchor = GridBagConstraints.WEST;
		gbc_comboBox_8.insets = new Insets(0, 0, 5, 5);
		gbc_comboBox_8.gridx = 3;
		gbc_comboBox_8.gridy = 2;
		frame.getContentPane().add(cbMinFrom, gbc_comboBox_8);
		
		cbDateFrom = new JComboBox(days);
		GridBagConstraints gbc_comboBox_2 = new GridBagConstraints();
		gbc_comboBox_2.anchor = GridBagConstraints.EAST;
		gbc_comboBox_2.insets = new Insets(0, 0, 5, 5);
		gbc_comboBox_2.gridx = 4;
		gbc_comboBox_2.gridy = 2;
		frame.getContentPane().add(cbDateFrom, gbc_comboBox_2);
		
		cbMonthsFrom = new JComboBox(months);
		GridBagConstraints gbc_comboBox_7 = new GridBagConstraints();
		gbc_comboBox_7.insets = new Insets(0, 0, 5, 5);
		gbc_comboBox_7.gridx = 5;
		gbc_comboBox_7.gridy = 2;
		frame.getContentPane().add(cbMonthsFrom , gbc_comboBox_7);
		
		cbYearFrom = new JComboBox(years);
		GridBagConstraints gbc_comboBox_1 = new GridBagConstraints();
		gbc_comboBox_1.insets = new Insets(0, 0, 5, 5);
		gbc_comboBox_1.gridx = 6;
		gbc_comboBox_1.gridy = 2;
		frame.getContentPane().add(cbYearFrom, gbc_comboBox_1);
		
		JLabel lblTo = new JLabel("To:");
		GridBagConstraints gbc_lblTo = new GridBagConstraints();
		gbc_lblTo.insets = new Insets(0, 0, 5, 5);
		gbc_lblTo.gridx = 1;
		gbc_lblTo.gridy = 3;
		frame.getContentPane().add(lblTo, gbc_lblTo);
	
		
		cbHoursTo= new JComboBox(hours);
		GridBagConstraints gbc_comboBox_10 = new GridBagConstraints();
		gbc_comboBox_10.anchor = GridBagConstraints.EAST;
		gbc_comboBox_10.insets = new Insets(0, 0, 5, 5);
		gbc_comboBox_10.gridx = 1;
		gbc_comboBox_10.gridy = 4;
		frame.getContentPane().add(cbHoursTo, gbc_comboBox_10);
		
		cbMinTo = new JComboBox(minutes);
		GridBagConstraints gbc_comboBox_9 = new GridBagConstraints();
		gbc_comboBox_9.anchor = GridBagConstraints.WEST;
		gbc_comboBox_9.insets = new Insets(0, 0, 5, 5);
		gbc_comboBox_9.gridx = 3;
		gbc_comboBox_9.gridy = 4;
		frame.getContentPane().add(cbMinTo, gbc_comboBox_9);
		
		cbDateTo = new JComboBox(days);
		GridBagConstraints gbc_comboBox_3 = new GridBagConstraints();
		gbc_comboBox_3.anchor = GridBagConstraints.EAST;
		gbc_comboBox_3.insets = new Insets(0, 0, 5, 5);
		gbc_comboBox_3.gridx = 4;
		gbc_comboBox_3.gridy = 4;
		frame.getContentPane().add(cbDateTo, gbc_comboBox_3);
		
		cbMonthsTo = new JComboBox(months);
		GridBagConstraints gbc_comboBox_4 = new GridBagConstraints();
		gbc_comboBox_4.insets = new Insets(0, 0, 5, 5);
		gbc_comboBox_4.gridx = 5;
		gbc_comboBox_4.gridy = 4;
		frame.getContentPane().add(cbMonthsTo, gbc_comboBox_4);
		
		cbYearTo = new JComboBox(years);
		GridBagConstraints gbc_comboBox_5 = new GridBagConstraints();
		gbc_comboBox_5.insets = new Insets(0, 0, 5, 5);
		gbc_comboBox_5.gridx = 6;
		gbc_comboBox_5.gridy = 4;
		frame.getContentPane().add(cbYearTo, gbc_comboBox_5);
		
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridwidth = 7;
		gbc_scrollPane.insets = new Insets(0, 0, 0, 5);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 1;
		gbc_scrollPane.gridy = 5;
		frame.getContentPane().add(scrollPane, gbc_scrollPane);
		
		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
	}
	
	private class SwingAction extends AbstractAction {
		public SwingAction() {
			putValue(NAME, "Get Analysis");
			putValue(SHORT_DESCRIPTION, "Retrieves the analysis between the specified times");
		}
		
		public int getMonth(String month){
			Calendar c = Calendar.getInstance();
			Date date = c.getTime();
			
			try {
				date = new SimpleDateFormat("MMM", Locale.ENGLISH).parse(month);
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			c.setTime(date);
			return c.get(Calendar.MONTH);
		}
		
		/**
		 * Call analysis on data between the selected dates and times
		 */
		public void actionPerformed(ActionEvent e) {
			Calendar c = Calendar.getInstance();
			
			//Retrieve date and time from and to values
			int dtF = Integer.parseInt((String) cbDateFrom.getSelectedItem());
			int dtT = Integer.parseInt((String) cbDateTo.getSelectedItem());
			int yrF = Integer.parseInt((String) cbYearFrom.getSelectedItem());
			int yrT = Integer.parseInt((String) cbYearTo.getSelectedItem());
			int hrF = Integer.parseInt((String) cbHoursFrom.getSelectedItem());
			int hrT = Integer.parseInt((String) cbHoursTo.getSelectedItem());
			int mnF = Integer.parseInt((String) cbMinFrom.getSelectedItem());
			int mnT = Integer.parseInt((String) cbMinTo.getSelectedItem());
			int mtF = getMonth((String) cbMonthsFrom.getSelectedItem());
			int mtT = getMonth((String) cbMonthsTo.getSelectedItem());
			
			//Set the from date
			c.set(yrF, mtF, dtF, hrF, mnF, 00);
			Date d1 = c.getTime();
			
			//Set the to date
			c.set(yrT, mtT, dtT, hrT, mnT, 00);
			Date d2 = c.getTime();	
			
			
			//Perform analysis and output results
			RecordSet<Integer> rs = new RecordSet<Integer>(d1, d2, 0);
			analyser.analyse(rs);
			textArea.setText("");
			textArea.append("Analysis between: \n\n"); 
			textArea.append(d1 + "\n\n");
			textArea.append("and: \n\n");
			textArea.append(d2 + "\n\n");
			textArea.append("Retrieved " + rs.getRecordCount() + " records");
			
		}
	}

}
