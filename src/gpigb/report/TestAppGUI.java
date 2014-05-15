package gpigb.report;

import gpigb.analyse.Analyser;
import gpigb.classloading.ComponentManager;
import gpigb.classloading.StrongReference;
import gpigb.configuration.ConfigurationHandler;
import gpigb.configuration.ConfigurationValue;
import gpigb.configuration.ConfigurationValue.ValueType;
import gpigb.data.RecordSet;
import gpigb.sense.Sensor;
import gpigb.report.Reporter;
import gpigb.sense.Sensor;
import gpigb.store.Store;
import gpigb.analyse.Analyser;

import java.awt.EventQueue;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.AbstractAction;
import javax.swing.Action;
import java.awt.Canvas;
import java.awt.Color;

public class TestAppGUI implements Reporter{

	private JFrame frame;
	private JTextField avrg1;
	private JTextField avrg2;
	private Canvas mode1;
	private Canvas mode2;
	private JTextArea textArea;
	
	int period;
	int upperThreshold;
	int midThreshold;
	
	private StrongReference<Analyser> analyser;


	/**
	 * Create the application.
	 */
	public TestAppGUI() {
		initialize();
		
		period = 3;
		upperThreshold = 10;
		midThreshold = 5;
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
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblSensor1 = new JLabel("Sensor 1");
		lblSensor1.setBounds(75, 59, 91, 33);
		frame.getContentPane().add(lblSensor1);
		
		JLabel lblSensor2 = new JLabel("Sensor 2");
		lblSensor2.setBounds(75, 104, 91, 33);
		frame.getContentPane().add(lblSensor2);
		
		JLabel lblMode = new JLabel("Mode");
		lblMode.setBounds(183, 32, 70, 15);
		frame.getContentPane().add(lblMode);
		
		JLabel lblAverage = new JLabel("Average");
		lblAverage.setBounds(287, 32, 70, 15);
		frame.getContentPane().add(lblAverage);
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setBounds(51, 163, 362, 75);
		frame.getContentPane().add(textArea);
		
		avrg1 = new JTextField();
		avrg1.setEditable(false);
		avrg1.setBounds(287, 66, 51, 19);
		frame.getContentPane().add(avrg1);
		avrg1.setColumns(10);
		avrg1.setHorizontalAlignment(JTextField.CENTER);
		avrg1.setText("0");
		
		avrg2 = new JTextField();
		avrg2.setEditable(false);
		avrg2.setColumns(10);
		avrg2.setBounds(287, 111, 51, 19);
		frame.getContentPane().add(avrg2);
		avrg2.setHorizontalAlignment(JTextField.CENTER);
		avrg2.setText("0");
		
		mode1 = new Canvas();
		mode1.setBackground(Color.GREEN);
		mode1.setBounds(183, 65, 39, 25);
		frame.getContentPane().add(mode1);
		
		mode2 = new Canvas();
		mode2.setBackground(Color.GREEN);
		mode2.setBounds(183, 110, 39, 25);
		frame.getContentPane().add(mode2);
	}

	@Override
	public Map<String, ConfigurationValue> getConfigSpec() {
		HashMap<String, ConfigurationValue> map = new HashMap<>();
		map.put("AnalyserReference", new ConfigurationValue(ValueType.Analyser, 0));
		return map;
	}
	
	public boolean setConfig(Map<String, ConfigurationValue> newConfig, ComponentManager<Analyser> aMgr, ComponentManager<Reporter> rMgr, ComponentManager<Sensor> seMgr, ComponentManager<Store> stMgr)
	{
		try
		{
			this.analyser = aMgr.getObjectByID(newConfig.get("AnalyserReference").intValue);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}

	@Override
	public void generateReport(List<RecordSet<?>> data) {
		
		// TODO Auto-generated method stub
		Calendar c = Calendar.getInstance();
		//Set from and to dates.
		Date d2 = c.getTime();
		Date d1 = c.getTime();
		d1.setMinutes(d2.getMinutes() - period);
		
		//For each sensor set average and check for mode change
		int sensors = 1;
		for(int i = 1; i < sensors + 1; i++){
			
			//Get average from analyser
			RecordSet<Double> rs = new RecordSet<Double>(d1, d2, i);
			analyser.get().analyse(rs);
			double average = rs.getDataAtPosition(0).getData();
			//double average = 11;
			
			
			//Set mode depending on average
			if (average > midThreshold){
				if (average > upperThreshold){
					changeMode(i, 3);
				} else {
					changeMode(i, 2);
				}
			} else {
				changeMode(i, 1);
			}
			
			if (i == 1){
				avrg1.setText("" + average);
			} else {
				avrg2.setText("" + average);
			}
		}
		
	}
	
	private int id;
	@Override
	public void setID(int newID) {
		this.id = newID;
	}
	
	public void changeMode(int sensor, int mode){
		if (sensor == 1){
			switch (mode){
				case 1: 	mode1.setBackground(Color.GREEN);
							break;
				
				case 2: 	mode1.setBackground(Color.ORANGE);
							break;
						
				case 3:		mode1.setBackground(Color.RED);
							break;
						
				default:	mode1.setBackground(Color.GREEN);
			}
			textArea.append("Sensor 1: Mode change detected.\n");
		} else {
			switch (mode){
				case 1: 	mode2.setBackground(Color.GREEN);
							break;
			
				case 2: 	mode2.setBackground(Color.ORANGE);
							break;
					
				case 3:		mode2.setBackground(Color.RED);
							break;
					
				default:	mode2.setBackground(Color.GREEN);
			}
			textArea.append("Sensor 2: Mode change detected.\n");
		}
		
	}

	@Override
	public int getID() {
		return this.id;
	}
}
