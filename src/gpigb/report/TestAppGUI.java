package gpigb.report;

import gpigb.analyse.Analyser;
import gpigb.classloading.StrongReference;
import gpigb.configuration.ConfigurationHandler;
import gpigb.configuration.ConfigurationValue;
import gpigb.configuration.ConfigurationValue.ValueType;
import gpigb.data.RecordSet;

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
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class TestAppGUI implements Reporter{
	
	private JLabel lblAverage;
	private JFrame frame;
	private JTextField _textField;
	//private 
	
	int period;
	int upperThreshold;
	int midThreshold;
	int sensors;
	
	private StrongReference<Analyser> analyser;

	/**
	 * Create the application.
	 */
	public TestAppGUI() {
		sensors = 4;
		
		initialize();
		
		period = 3;
		upperThreshold = 10;
		midThreshold = 5;
		
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TestAppGUI window = new TestAppGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
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
		frame.setBounds(100, 100, 400, 250);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{115, 70, 34, 80, 0};
		gridBagLayout.rowHeights = new int[]{30};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0};
		frame.getContentPane().setLayout(gridBagLayout);
		frame.pack();
		
			for (int count = 1; count < sensors + 1; count++){
				
				JLabel lblSensor1 = new JLabel("Sensor " + count);
				GridBagConstraints g_lblSensor = new GridBagConstraints();
				g_lblSensor.fill = GridBagConstraints.BOTH;
				g_lblSensor.insets = new Insets(0, 0, 5, 5);
				g_lblSensor.gridx = 0;
				g_lblSensor.gridy = 1 + count;
				frame.getContentPane().add(lblSensor1, g_lblSensor);
				
				_textField = new JTextField("0");
				_textField.setEditable(false);
				_textField.setHorizontalAlignment(JTextField.CENTER);
				GridBagConstraints g_textField = new GridBagConstraints();
				g_textField.fill = GridBagConstraints.BOTH;
				g_textField.insets = new Insets(0, 0, 5, 5);
				g_textField.gridx = 3;
				g_textField.gridy = 1 + count;
				frame.getContentPane().add(_textField, g_textField);
				
				Canvas canvas = new Canvas();
				canvas.setSize(40,20);
				canvas.setBackground(Color.GREEN);
				GridBagConstraints gbc_canvas = new GridBagConstraints();
				gbc_canvas.insets = new Insets(0, 0, 5, 5);
				gbc_canvas.gridx = 1;
				gbc_canvas.gridy = 1 + count;
				frame.getContentPane().add(canvas, gbc_canvas);
				
				frame.getContentPane().revalidate();
				frame.getContentPane().repaint();
				
				frame.pack();
			}

		JLabel lblMode = new JLabel("Mode");
		GridBagConstraints gbc_lblMode = new GridBagConstraints();
		gbc_lblMode.anchor = GridBagConstraints.NORTH;
		gbc_lblMode.insets = new Insets(0, 0, 0, 5);
		gbc_lblMode.gridx = 1;
		gbc_lblMode.gridy = 0;
		frame.getContentPane().add(lblMode, gbc_lblMode);
		
		lblAverage = new JLabel("Average");
		GridBagConstraints gbc_lblAverage = new GridBagConstraints();
		gbc_lblAverage.anchor = GridBagConstraints.NORTH;
		gbc_lblAverage.gridx = 3;
		gbc_lblAverage.gridy = 0;
		frame.getContentPane().add(lblAverage, gbc_lblAverage);
	}

	@Override
	public Map<String, ConfigurationValue> getConfigSpec() {
		HashMap<String, ConfigurationValue> map = new HashMap<>();
		map.put("AnalyserReference", new ConfigurationValue(ValueType.Analyser, null));
		return map;
	}
	
	public boolean setConfig(Map<String, ConfigurationValue> newConfig)
	{
		try
		{
			this.analyser = (StrongReference<Analyser>) newConfig.get("AnalyserReference").value;
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}

	private int mode = 1;
	@Override
	public void generateReport(List<RecordSet<?>> data) {
		
		Calendar c = Calendar.getInstance();
		//Set from and to dates.
		Date d2 = c.getTime();
		Date d1 = c.getTime();
		d1.setMinutes(d2.getMinutes() - period);
		
		//For each sensor set average and check for mode change
		for(int i = 1; i < sensors + 1; i++){
			
			//Get average from analyser
			RecordSet<Double> rs = new RecordSet<Double>(d1, d2, i);
			//analyser.get().analyse(rs);
			//double average = rs.getDataAtPosition(0).getData();
			double average = 11;
			
			
			//Set mode depending on average
			if (average > midThreshold){
				if (average > upperThreshold){
					//Red
					if (mode != 3){
						//changeMode(i, 3);
						mode = 3;
					}
				} else {
					//Orange
					if (mode != 2){
						//changeMode(i, 2);
						mode = 2;
					}
				}
			} else {
				//Green
				if (mode != 1){
					//changeMode(i, 1);
					mode = 1;
				}
				
			}
			
			if (i == 1){
//				_textField.
			//	avrg1.setText("" + average);
			} else {
			//	avrg2.setText("" + average);
			}
		}
		
	}
	
	private int id;

	@Override
	public void setID(int newID) {
		this.id = newID;
	}
	
	/*
	 * Depending on the sensor and mode change the colour of the canvas and output text to the text area
	 */
	/*
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
		//	textArea.append("Sensor 1: Mode change detected.\n");
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
		//	textArea.append("Sensor 2: Mode change detected.\n");
		}
		
	}
	*/
	@Override
	public int getID() {
		return this.id;
	}
}
