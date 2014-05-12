package gpigb.report;

import gpigb.analyse.Analyser;
import gpigb.analyse.NullAnalyser;
<<<<<<< HEAD
import gpigb.data.RecordSet;
import gpigb.data.SensorRecord;
=======
import gpigb.classloading.StrongReference;
import gpigb.configuration.ConfigurationHandler;
import gpigb.configuration.ConfigurationValue;
import gpigb.configuration.ConfigurationValue.ValueType;
import gpigb.data.DataRecord;
import gpigb.data.DataSet;
>>>>>>> d912525ba8ae6d017e0972ac8d0d661ccc716a89
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
import java.util.HashMap;
import java.util.List;
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
<<<<<<< HEAD
public class ReporterGUI {

=======
public class ReporterGUI implements Reporter
{
>>>>>>> d912525ba8ae6d017e0972ac8d0d661ccc716a89
	private JFrame frame;
	private final Action action = new SwingAction();
	private JTextArea textArea;
	private JComboBox<String> cbMonthsFrom;
	private JComboBox<String> cbMonthsTo;
	private JComboBox<String> cbYearFrom;
	private JComboBox<String> cbYearTo;
	private JComboBox<String> cbDateFrom;
	private JComboBox<String> cbDateTo;
	private JComboBox<String> cbMinFrom;
	private JComboBox<String> cbHoursFrom;
	private JComboBox<String> cbMinTo;
	private JComboBox<String> cbHoursTo;
	private JLabel lblFrom;
	

<<<<<<< HEAD

	public Analyser analyser; 
=======
	private StrongReference<Analyser> analyser;
	
	public Store store;
>>>>>>> d912525ba8ae6d017e0972ac8d0d661ccc716a89

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
<<<<<<< HEAD
		
		cbHoursFrom = new JComboBox(hours);
=======

		cbHoursFrom = new JComboBox<>(hours);
>>>>>>> d912525ba8ae6d017e0972ac8d0d661ccc716a89
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
<<<<<<< HEAD
		
		cbMinFrom= new JComboBox(minutes);
=======

		cbMinFrom = new JComboBox<>(minutes);
>>>>>>> d912525ba8ae6d017e0972ac8d0d661ccc716a89
		GridBagConstraints gbc_comboBox_8 = new GridBagConstraints();
		gbc_comboBox_8.anchor = GridBagConstraints.WEST;
		gbc_comboBox_8.insets = new Insets(0, 0, 5, 5);
		gbc_comboBox_8.gridx = 3;
		gbc_comboBox_8.gridy = 2;
		frame.getContentPane().add(cbMinFrom, gbc_comboBox_8);
<<<<<<< HEAD
		
		cbDateFrom = new JComboBox(days);
=======

		cbDateFrom = new JComboBox<>(days);
>>>>>>> d912525ba8ae6d017e0972ac8d0d661ccc716a89
		GridBagConstraints gbc_comboBox_2 = new GridBagConstraints();
		gbc_comboBox_2.anchor = GridBagConstraints.EAST;
		gbc_comboBox_2.insets = new Insets(0, 0, 5, 5);
		gbc_comboBox_2.gridx = 4;
		gbc_comboBox_2.gridy = 2;
		frame.getContentPane().add(cbDateFrom, gbc_comboBox_2);
<<<<<<< HEAD
		
		cbMonthsFrom = new JComboBox(months);
=======

		cbMonthsFrom = new JComboBox<>(months);
>>>>>>> d912525ba8ae6d017e0972ac8d0d661ccc716a89
		GridBagConstraints gbc_comboBox_7 = new GridBagConstraints();
		gbc_comboBox_7.insets = new Insets(0, 0, 5, 5);
		gbc_comboBox_7.gridx = 5;
		gbc_comboBox_7.gridy = 2;
<<<<<<< HEAD
		frame.getContentPane().add(cbMonthsFrom , gbc_comboBox_7);
		
		cbYearFrom = new JComboBox(years);
=======
		frame.getContentPane().add(cbMonthsFrom, gbc_comboBox_7);

		cbYearFrom = new JComboBox<>(years);
>>>>>>> d912525ba8ae6d017e0972ac8d0d661ccc716a89
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
<<<<<<< HEAD
	
		
		cbHoursTo= new JComboBox(hours);
=======

		cbHoursTo = new JComboBox<>(hours);
>>>>>>> d912525ba8ae6d017e0972ac8d0d661ccc716a89
		GridBagConstraints gbc_comboBox_10 = new GridBagConstraints();
		gbc_comboBox_10.anchor = GridBagConstraints.EAST;
		gbc_comboBox_10.insets = new Insets(0, 0, 5, 5);
		gbc_comboBox_10.gridx = 1;
		gbc_comboBox_10.gridy = 4;
		frame.getContentPane().add(cbHoursTo, gbc_comboBox_10);
<<<<<<< HEAD
		
		cbMinTo = new JComboBox(minutes);
=======

		cbMinTo = new JComboBox<>(minutes);
>>>>>>> d912525ba8ae6d017e0972ac8d0d661ccc716a89
		GridBagConstraints gbc_comboBox_9 = new GridBagConstraints();
		gbc_comboBox_9.anchor = GridBagConstraints.WEST;
		gbc_comboBox_9.insets = new Insets(0, 0, 5, 5);
		gbc_comboBox_9.gridx = 3;
		gbc_comboBox_9.gridy = 4;
		frame.getContentPane().add(cbMinTo, gbc_comboBox_9);
<<<<<<< HEAD
		
		cbDateTo = new JComboBox(days);
=======

		cbDateTo = new JComboBox<>(days);
>>>>>>> d912525ba8ae6d017e0972ac8d0d661ccc716a89
		GridBagConstraints gbc_comboBox_3 = new GridBagConstraints();
		gbc_comboBox_3.anchor = GridBagConstraints.EAST;
		gbc_comboBox_3.insets = new Insets(0, 0, 5, 5);
		gbc_comboBox_3.gridx = 4;
		gbc_comboBox_3.gridy = 4;
		frame.getContentPane().add(cbDateTo, gbc_comboBox_3);
<<<<<<< HEAD
		
		cbMonthsTo = new JComboBox(months);
=======

		cbMonthsTo = new JComboBox<>(months);
>>>>>>> d912525ba8ae6d017e0972ac8d0d661ccc716a89
		GridBagConstraints gbc_comboBox_4 = new GridBagConstraints();
		gbc_comboBox_4.insets = new Insets(0, 0, 5, 5);
		gbc_comboBox_4.gridx = 5;
		gbc_comboBox_4.gridy = 4;
		frame.getContentPane().add(cbMonthsTo, gbc_comboBox_4);
<<<<<<< HEAD
		
		cbYearTo = new JComboBox(years);
=======

		cbYearTo = new JComboBox<>(years);
>>>>>>> d912525ba8ae6d017e0972ac8d0d661ccc716a89
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
<<<<<<< HEAD
	
	private class SwingAction extends AbstractAction {
		public SwingAction() {
=======

	@SuppressWarnings("serial")
	private class SwingAction extends AbstractAction
	{
		public SwingAction()
		{
>>>>>>> d912525ba8ae6d017e0972ac8d0d661ccc716a89
			putValue(NAME, "Get Analysis");
			putValue(SHORT_DESCRIPTION, "Retrieves the analysis between the specified times");
		}
		
		public int getMonth(String month){
			Calendar c = Calendar.getInstance();
			Date date = c.getTime();
			
			try {
				date = new SimpleDateFormat("MMM", Locale.ENGLISH).parse(month);
<<<<<<< HEAD
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
=======
			}
			catch (ParseException e1) {
>>>>>>> d912525ba8ae6d017e0972ac8d0d661ccc716a89
				e1.printStackTrace();
			}
			c.setTime(date);
			return c.get(Calendar.MONTH);
		}
		
		/**
		 * Call analysis on data between the selected dates and times
		 */
<<<<<<< HEAD
		public void actionPerformed(ActionEvent e) {
=======
		public void actionPerformed(ActionEvent e)
		{
			if(analyser == null)
			{
				textArea.setText("No analyser configured");
				return;
			}
			
>>>>>>> d912525ba8ae6d017e0972ac8d0d661ccc716a89
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
			
<<<<<<< HEAD
			//Set the from date
=======
			// Set the from date
>>>>>>> d912525ba8ae6d017e0972ac8d0d661ccc716a89
			c.set(yrF, mtF, dtF, hrF, mnF, 00);
			Date d1 = c.getTime();
			
			//Set the to date
			c.set(yrT, mtT, dtT, hrT, mnT, 00);
<<<<<<< HEAD
			Date d2 = c.getTime();	
			
			
			//Perform analysis and output results
			RecordSet<Integer> rs = new RecordSet<Integer>(d1, d2, 0);
			analyser.analyse(rs);
=======
			Date d2 = c.getTime();

			// Perform analysis and output results
			DataSet<Integer> rs = new DataSet<Integer>(d1, d2, 1);
			analyser.get().analyse(rs);
>>>>>>> d912525ba8ae6d017e0972ac8d0d661ccc716a89
			textArea.setText("");
			textArea.append("Analysis between: \n\n"); 
			textArea.append(d1 + "\n\n");
			textArea.append("and: \n\n");
			textArea.append(d2 + "\n\n");
<<<<<<< HEAD
			textArea.append("Retrieved " + rs.getRecordCount() + " records");
			
=======
			textArea.append("Result1: " + rs.getDataAtPosition(0).getData() + "\n");
			textArea.append("Result2: " + rs.getDataAtPosition(1).getData() + "\n");
>>>>>>> d912525ba8ae6d017e0972ac8d0d661ccc716a89
		}
	}
	
	@Override
	public void generateReport(List<DataSet<?>> data)
	{
		Date fromTime = data.get(0).getFromTime();
		Calendar cal = Calendar.getInstance();
		
		cal.setTime(fromTime);
		cbYearFrom.setSelectedItem(cal.get(Calendar.YEAR));
		cbMonthsFrom.setSelectedItem(cal.get(Calendar.MONTH));
		cbDateFrom.setSelectedItem(cal.get(Calendar.DATE));
		cbHoursFrom.setSelectedItem(cal.get(Calendar.HOUR_OF_DAY));
		cbMinFrom.setSelectedItem(cal.get(Calendar.MINUTE));
		
		Date toTime = data.get(0).getToTime();
		cal.setTime(toTime);
		cbYearTo.setSelectedItem(cal.get(Calendar.YEAR));
		cbMonthsTo.setSelectedItem(cal.get(Calendar.MONTH));
		cbDateTo.setSelectedItem(cal.get(Calendar.DATE));
		cbHoursTo.setSelectedItem(cal.get(Calendar.HOUR_OF_DAY));
		cbMinTo.setSelectedItem(cal.get(Calendar.MINUTE));
		
		action.actionPerformed(new ActionEvent(null, 0, null));
	}

	@SuppressWarnings("unchecked")
	@Override
	public void configure(ConfigurationHandler handler)
	{
		HashMap<String, ConfigurationValue> map = new HashMap<>();
		
		map.put("AnalyserReference", new ConfigurationValue(ValueType.Analyser, null));
		
		handler.getConfiguration(map);
		
		this.analyser = (StrongReference<Analyser>) map.get("AnalyserReference").value;
	}
}
