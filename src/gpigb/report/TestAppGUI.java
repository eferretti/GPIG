package gpigb.report;

import gpigb.analyse.Analyser;
import gpigb.classloading.ComponentManager;
import gpigb.classloading.StrongReference;
import gpigb.configuration.ConfigurationValue;
import gpigb.configuration.ConfigurationValue.ValueType;
import gpigb.data.RecordSet;
import gpigb.report.Reporter;
import gpigb.sense.Sensor;
import gpigb.store.Store;

import java.awt.Canvas;
import java.awt.Color;
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
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class TestAppGUI implements Reporter{
	
	private Integer configSteps;
	private Integer currentConfigStep;
	private JLabel lblAverage;
	private JFrame frame;
	private JTextField[] textField;
	private Canvas[] canvas;
	private JTextArea textArea;
	
	private int period;
	private int upperThreshold;
	private int midThreshold;
	private int sensorNumber;
	private String measureName;
	private Integer[] sensorID;
	
	
	private StrongReference<Analyser> analyser;

	/**
	 * Create the application.
	 */
	public TestAppGUI() {
		sensorNumber = 4;
		measureName = "Average";
		configSteps = 2;
		currentConfigStep = 1;
		
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
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0};
		frame.getContentPane().setLayout(gridBagLayout);
		frame.pack();
		
		textField = new JTextField[sensorNumber];
		canvas = new Canvas[sensorNumber];
		
			for (int count = 0; count < sensorNumber; count++){
				
				JLabel lblSensor1 = new JLabel("Sensor " + (count+1));
				GridBagConstraints g_lblSensor = new GridBagConstraints();
				g_lblSensor.fill = GridBagConstraints.BOTH;
				g_lblSensor.insets = new Insets(0, 0, 5, 5);
				g_lblSensor.gridx = 0;
				g_lblSensor.gridy = 1 + count;
				frame.getContentPane().add(lblSensor1, g_lblSensor);
				
				textField[count] = new JTextField("0");
				textField[count].setEditable(false);
				textField[count].setHorizontalAlignment(JTextField.CENTER);
				GridBagConstraints g_textField = new GridBagConstraints();
				g_textField.fill = GridBagConstraints.BOTH;
				g_textField.insets = new Insets(0, 0, 5, 5);
				g_textField.gridx = 3;
				g_textField.gridy = 1 + count;
				frame.getContentPane().add(textField[count], g_textField);
				
				canvas[count] = new Canvas();
				canvas[count].setSize(40,20);
				canvas[count].setBackground(Color.GREEN);
				GridBagConstraints gbc_canvas = new GridBagConstraints();
				gbc_canvas.insets = new Insets(0, 0, 5, 5);
				gbc_canvas.gridx = 1;
				gbc_canvas.gridy = 1 + count;
				frame.getContentPane().add(canvas[count], gbc_canvas);
				
			}

		JLabel lblMode = new JLabel("Mode");
		GridBagConstraints gbc_lblMode = new GridBagConstraints();
		gbc_lblMode.anchor = GridBagConstraints.NORTH;
		gbc_lblMode.insets = new Insets(0, 0, 0, 5);
		gbc_lblMode.gridx = 1;
		gbc_lblMode.gridy = 0;
		frame.getContentPane().add(lblMode, gbc_lblMode);
		
		lblAverage = new JLabel(measureName);
		GridBagConstraints gbc_lblAverage = new GridBagConstraints();
		gbc_lblAverage.anchor = GridBagConstraints.NORTH;
		gbc_lblAverage.gridx = 3;
		gbc_lblAverage.gridy = 0;
		frame.getContentPane().add(lblAverage, gbc_lblAverage);
		
		textArea = new JTextArea("Messages:");
		GridBagConstraints gbc_textArea = new GridBagConstraints();
		gbc_textArea.gridwidth = 5;
		gbc_textArea.anchor = GridBagConstraints.NORTH;
		gbc_textArea.insets = new Insets(0, 0, 5, 5);
		gbc_textArea.gridx = 0;
		gbc_textArea.gridy = sensorNumber + 1;
		frame.getContentPane().add(textArea, gbc_textArea);
		
		frame.pack();
	}

	@Override
	public Map<String, ConfigurationValue> getConfigSpec() {

		switch(currentConfigStep){
		case 1 : {
			HashMap<String, ConfigurationValue> map = new HashMap<>();
			map.put("AnalyserReference", new ConfigurationValue(ValueType.Analyser, 0));
			map.put("Sensor Count", new ConfigurationValue(ValueType.Integer, 0));
			return map;
		}
		case 2 : {
			HashMap<String, ConfigurationValue> map = new HashMap<>();
			for(int i = 0; i < sensorNumber; ++i)
			{
				map.put("Sensor " + i , new ConfigurationValue(ValueType.Sensor, 0));
			}
			return map;
		}
		default : break;
		}
		return  new HashMap<>();

	}
	
	public boolean setConfig(Map<String, ConfigurationValue> newConfig, ComponentManager<Analyser> aMgr, ComponentManager<Reporter> rMgr, ComponentManager<Sensor> seMgr, ComponentManager<Store> stMgr)
	{
		try
		{
			switch(currentConfigStep){
			case 1 : {
				this.analyser = aMgr.getObjectByID(newConfig.get("AnalyserReference").intValue);
				this.sensorNumber = (Integer) newConfig.get("Sensor Count").intValue;
				currentConfigStep = 2;
				return true;
			}
			case 2 : {
				for(int i =0; i < sensorNumber; ++i)
				{
					this.sensorID[i] = newConfig.get("Sensor " + i).intValue;
				}
				currentConfigStep = 1;
				show();
			}
			default : return true;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	private int mode = 1;
	@Override
	public void generateReport(List<RecordSet<?>> data) {
		if(analyser == null)
		{
			return;
		}
		Calendar c = Calendar.getInstance();
		//Set from and to dates.
		Date d2 = c.getTime();
		Date d1 = c.getTime();
		d1.setMinutes(d2.getMinutes() - period);
		
		//For each sensor set average and check for mode change
		int sensors = 1;
		for(int i = 0; i < sensorNumber; i++){
			//Get the measure from analyser
			//analyser.get().analyse(rs);
			//double measure = rs.getDataAtPosition(0).getData();
			double measure = 11;

			//Get average from analyser
			RecordSet<Double> rs = new RecordSet<Double>(d1, d2, sensorID[i]);
			analyser.get().analyse(rs);
			double average = rs.getDataAtPosition(0).getData();
			//double average = 11;
			
			
			//Set mode depending on the measure and thresholds
			if (measure > midThreshold) {
				if (measure > upperThreshold) {
					if (mode != 3) {
						canvas[i].setBackground(Color.RED);
						mode = 3;
						textArea.append("Mode change detected. ERROR\n");
					}
				} else {
					if (mode != 2) {
						canvas[i].setBackground(Color.ORANGE);
						mode = 2;
						textArea.append("Mode change detected.\n");
					}
				}
			} else {
				if (mode != 1) {
					canvas[i].setBackground(Color.GREEN);
					mode = 1;
					textArea.append("Mode change detected.\n");
				}
			}
			
			//Update measure
			textField[i].setText("" + measure);
			
		}
		
	}
	
	private int id;

	@Override
	public void setID(int newID) {
		this.id = newID;
	}
	
	@Override
	public int getID() {
		return this.id;
	}
	
	@Override
	public int getConfigurationStepNumber() {
		
		return configSteps;
	}
}
