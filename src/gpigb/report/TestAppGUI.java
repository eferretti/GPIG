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
import java.awt.Dimension;
import java.awt.EventQueue;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

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
	private JLabel lblSD;
	private JFrame frame;
	private JTextField[] textField;
	private JTextField[] textField2;
	private Canvas[] canvas;
	private JTextArea textArea;
	
	private int period;
	private int[] meanUpperThreshold;
	private int[] meanMidThreshold;
	private int[] sdUpperThreshold;
	private int[] sdMidThreshold;
	private int sensorNumber;
	private Integer[] sensorID;
	private int[] mode;
	
	private StrongReference<Analyser> analyser;

	private StrongReference<Reporter> systemSignal;

	private StrongReference<Analyser> analyser2;


	/**
	 * Create the application.
	 */
	public TestAppGUI() 
	{
		configSteps = 2;
		currentConfigStep = 1;
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
	private void initialize() 
	{
		frame = new JFrame();
		frame.setBounds(100, 100, 400, 250);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{20, 115, 20, 34, 80, 20, 80, 20};
		gridBagLayout.rowHeights = new int[]{30};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0};
		frame.getContentPane().setLayout(gridBagLayout);
		frame.pack();
		
		textField = new JTextField[sensorNumber];
		textField2 = new JTextField[sensorNumber];
		canvas = new Canvas[sensorNumber];
		
			for (int count = 0; count < sensorNumber; count++)
			{
				JLabel lblSensor1 = new JLabel("Sensor " + (count+1));
				GridBagConstraints g_lblSensor = new GridBagConstraints();
				g_lblSensor.fill = GridBagConstraints.BOTH;
				g_lblSensor.insets = new Insets(0, 0, 5, 5);
				g_lblSensor.gridx = 1;
				g_lblSensor.gridy = 1 + count;
				frame.getContentPane().add(lblSensor1, g_lblSensor);
				
				textField[count] = new JTextField("0");
				textField[count].setEditable(false);
				textField[count].setHorizontalAlignment(JTextField.CENTER);
				textField[count].setMaximumSize(new Dimension(10, 5));
				GridBagConstraints g_textField = new GridBagConstraints();
				g_textField.fill = GridBagConstraints.BOTH;
				g_textField.insets = new Insets(0, 0, 5, 5);
				g_textField.gridx = 4;
				g_textField.gridy = 1 + count;
				frame.getContentPane().add(textField[count], g_textField);
				
				textField2[count] = new JTextField("0");
				textField2[count].setEditable(false);
				textField2[count].setHorizontalAlignment(JTextField.CENTER);
				textField2[count].setMaximumSize(new Dimension(10, 5));
				GridBagConstraints g_textField2 = new GridBagConstraints();
				g_textField2.fill = GridBagConstraints.BOTH;
				g_textField2.insets = new Insets(0, 0, 5, 5);
				g_textField2.gridx = 6;
				g_textField2.gridy = 1 + count;
				frame.getContentPane().add(textField2[count], g_textField2);
				
				canvas[count] = new Canvas();
				canvas[count].setSize(40,20);
				canvas[count].setBackground(Color.GREEN);
				GridBagConstraints gbc_canvas = new GridBagConstraints();
				gbc_canvas.insets = new Insets(0, 0, 5, 5);
				gbc_canvas.gridx = 2;
				gbc_canvas.gridy = 1 + count;
				frame.getContentPane().add(canvas[count], gbc_canvas);
			}

		JLabel lblMode = new JLabel("Mode");
		GridBagConstraints gbc_lblMode = new GridBagConstraints();
		gbc_lblMode.anchor = GridBagConstraints.NORTH;
		gbc_lblMode.insets = new Insets(0, 0, 0, 5);
		gbc_lblMode.gridx = 2;
		gbc_lblMode.gridy = 0;
		frame.getContentPane().add(lblMode, gbc_lblMode);
		
		lblAverage = new JLabel("Mean");
		GridBagConstraints gbc_lblAverage = new GridBagConstraints();
		gbc_lblAverage.anchor = GridBagConstraints.NORTH;
		gbc_lblAverage.gridx = 4;
		gbc_lblAverage.gridy = 0;
		frame.getContentPane().add(lblAverage, gbc_lblAverage);
		
		lblSD= new JLabel("<html>Standard<br> Deviation");
		GridBagConstraints gbc_lblSD = new GridBagConstraints();
		gbc_lblSD.anchor = GridBagConstraints.NORTH;
		gbc_lblSD.gridx = 6;
		gbc_lblSD.gridy = 0;
		frame.getContentPane().add(lblSD, gbc_lblSD);
		
		textArea = new JTextArea(6,32);
		textArea.setEditable(false);
		JScrollPane scroll = new JScrollPane(textArea);
		scroll.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );
		
		GridBagConstraints gbc_textArea = new GridBagConstraints();
		gbc_textArea.gridwidth = 6;
		gbc_textArea.anchor = GridBagConstraints.NORTH;
		gbc_textArea.insets = new Insets(0, 0, 5, 5);
		gbc_textArea.gridx = 1;
		gbc_textArea.gridy = sensorNumber + 1;
		frame.getContentPane().add(textArea, gbc_textArea);
		
		frame.pack();
	}

	@Override
	public Map<String, ConfigurationValue> getConfigSpec() {

		switch(currentConfigStep){
		case 1 : {
			HashMap<String, ConfigurationValue> map = new HashMap<>();
			map.put("MeanAnalyser", new ConfigurationValue(ValueType.Analyser, 0));
			map.put("SDAnalyser", new ConfigurationValue(ValueType.Analyser, 0));
			map.put("Sensor Count", new ConfigurationValue(ValueType.Integer, 0));
			map.put("Period", new ConfigurationValue(ValueType.Integer, 0));
			return map;
		}
		case 2 : {
			HashMap<String, ConfigurationValue> map = new HashMap<>();
			for(int i = 0; i < sensorNumber; ++i)
			{
				map.put("Sensor " + i , new ConfigurationValue(ValueType.Sensor, 0));
				map.put("Mean Upper Threshold " + i , new ConfigurationValue(ValueType.Integer, 0));
				map.put("Mean Mid Threshold " + i , new ConfigurationValue(ValueType.Integer, 0));
				map.put("SD Upper Threshold " + i, new ConfigurationValue(ValueType.Integer, 0));
				map.put("SD Mid Threshold " + i, new ConfigurationValue(ValueType.Integer, 0));
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
				this.analyser = aMgr.getObjectByID(newConfig.get("MeanAnalyser").intValue);
				this.analyser2 = aMgr.getObjectByID(newConfig.get("SDAnalyser").intValue);
				this.sensorNumber = (Integer) newConfig.get("Sensor Count").intValue;
				this.period = newConfig.get("Period").intValue;
				sensorID = new Integer[sensorNumber];
				meanUpperThreshold = new int[sensorNumber];
				meanMidThreshold = new int[sensorNumber];
				sdUpperThreshold = new int[sensorNumber];
				sdMidThreshold = new int[sensorNumber];
				mode = new int[sensorNumber];
				Arrays.fill(mode, 1);
				currentConfigStep = 2;
				return true;
			}
			case 2 : {
				for(int i =0; i < sensorNumber; ++i)
				{
					this.sensorID[i] = (Integer) newConfig.get("Sensor " + i).intValue;
					this.meanUpperThreshold[i] = newConfig.get("Mean Upper Threshold " + i).intValue;
					this.meanMidThreshold[i] = newConfig.get("Mean Mid Threshold " + i).intValue;
					this.sdUpperThreshold[i] = newConfig.get("SD Upper Threshold " + i).intValue;
					this.sdMidThreshold[i] = newConfig.get("SD Mid Threshold " + i).intValue;
				}
				currentConfigStep = 1;
				initialize();
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

	@Override
	public void generateReport(List<RecordSet<?>> data) 
	{
		if(analyser == null || analyser2 == null)
		{
			return;
		}
		Calendar c = Calendar.getInstance();
		//Set from and to dates.
		Date d2 = c.getTime();
		Date d1 = c.getTime();
		d1.setMinutes(d2.getMinutes() - period);
		
		//For each sensor set mean and sd and check for mode change
		for(int i = 0; i < sensorNumber; i++)
		{
			//Get mean from analyser
			RecordSet<Double> rs = new RecordSet<Double>(d1, d2, sensorID[i]);
			analyser.get().analyse(rs);
			double mean = rs.getDataAtPosition(0).getData();		
			
			//Get sd from analyser
			RecordSet<Double> rs2 = new RecordSet<Double>(d1, d2, sensorID[i]);
			analyser2.get().analyse(rs2);
			double sd = rs2.getDataAtPosition(0).getData();
			
			Date d = c.getTime();
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			
			//Set mode depending on the mean, sd and thresholds
			if (mean > meanMidThreshold[i] || sd > sdMidThreshold[i]) 
			{
				if (mean > meanUpperThreshold[i] || sd > sdUpperThreshold[i]) 
				{
					if (mode[i] != 3) 
					{
						canvas[i].setBackground(Color.RED);
						mode[i] = 3;
						textArea.append(sdf.format(d) + " Mode change detected. Sensor " + (i+1) + ": ERROR\n");
					}
				} 
				else 
				{
					if (mode[i] != 2) 
					{
						canvas[i].setBackground(Color.ORANGE);
						mode[i] = 2;
						textArea.append(sdf.format(d) + ": Mode change detected. Sensor " + (i+1) + ": Extreme\n");
					}
				}
			} 
			else 
			{
				if (mode[i] != 1) 
				{
					canvas[i].setBackground(Color.GREEN);
					mode[i] = 1;
					textArea.append(sdf.format(d) + " Mode change detected. Sensor " + (i+1) + ": Normal\n");
				}
			}
			
			//Update mean and SD
			textField[i].setText(format(mean));
			textField2[i].setText(format(sd));
			frame.pack();
		}
		
	}
	
	public static String format(Number n) 
	{
        NumberFormat format = DecimalFormat.getInstance();
        //format.setRoundingMode(RoundingMode.FLOOR);
        format.setMinimumFractionDigits(0);
        format.setMaximumFractionDigits(2);
        return format.format(n);
    }
	
	private int id;

	@Override
	public void setID(int newID) 
	{
		this.id = newID;
	}
	
	@Override
	public int getID() 
	{
		return this.id;
	}
	
	@Override
	public int getConfigurationStepNumber() 
	{
		
		return configSteps;
	}
}
