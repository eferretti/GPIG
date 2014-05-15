package gpigb.configuration.handlers;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import third_party.SpringUtilities;

import gpigb.analyse.Analyser;
import gpigb.classloading.ComponentManager;
import gpigb.classloading.ComponentManager.InstanceSummary;
import gpigb.classloading.StrongReference;
import gpigb.configuration.ConfigurationHandler;
import gpigb.configuration.ConfigurationValue;
import gpigb.report.Reporter;
import gpigb.sense.Sensor;
import gpigb.store.Store;

public class GUIConfigHandler extends JFrame implements ConfigurationHandler
{
	private final List<InstanceSummary> analysers;
	private final List<InstanceSummary> reporters;
	private final List<InstanceSummary> stores;
	private final List<InstanceSummary> sensors;
	
	public GUIConfigHandler(List<InstanceSummary> analysers, List<InstanceSummary> reporters, List<InstanceSummary> stores, List<InstanceSummary> sensors)
	{
		this.analysers = analysers;
		this.reporters = reporters;
		this.stores = stores;
		this.sensors = sensors;
	}
	
	boolean waiting = true;
	
	@Override
	public void getConfiguration(final Map<String, ConfigurationValue> configSpec)
	{
		SpringLayout layout = new SpringLayout();
		getContentPane().setLayout(layout);
		
		int rows = 0;
		for(String key : configSpec.keySet())
		{
			JLabel label = new JLabel(key);
			JComponent component = null;
			
			switch(configSpec.get(key).type)
			{
				case Analyser:
				{
					JComboBox<InstanceSummary> combo = new JComboBox<>();
					combo.setModel(new DefaultComboBoxModel<InstanceSummary>(analysers.toArray(new InstanceSummary[0])));
					component = combo;
					++rows;
					break;
				}
					
				case Float:
					JSpinner floatSpinner = new JSpinner();
					component = floatSpinner;
					++rows;
					break;
				
				case Integer:
					JSpinner intSpinner = new JSpinner();
					component = intSpinner;
					++rows;
					break;
				
				case Reporter:
				{
					JComboBox<InstanceSummary> combo = new JComboBox<>();
					combo.setModel(new DefaultComboBoxModel<InstanceSummary>(reporters.toArray(new InstanceSummary[0])));
					component = combo;
					++rows;
					break;
				}
				
				case Sensor:
				{
					JComboBox<InstanceSummary> combo = new JComboBox<>();
					combo.setModel(new DefaultComboBoxModel<InstanceSummary>(sensors.toArray(new InstanceSummary[0])));
					component = combo;
					++rows;
					break;
				}
				
				case Store:
				{
					JComboBox<InstanceSummary> combo = new JComboBox<>();
					combo.setModel(new DefaultComboBoxModel<InstanceSummary>(stores.toArray(new InstanceSummary[0])));
					component = combo;
					++rows;
					break;
				}
				
				case String:
					JTextField textField = new JTextField(30);
					component = textField;
					++rows;
					break;
			}

			getContentPane().add(label);
			getContentPane().add(component);
		}
		
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowListener()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				close(configSpec);
			}
			
			@Override
			public void windowOpened(WindowEvent e) { }
			@Override
			public void windowIconified(WindowEvent e) { }
			@Override
			public void windowDeiconified(WindowEvent e) { }
			@Override
			public void windowDeactivated(WindowEvent e) { }
			@Override
			public void windowClosed(WindowEvent e) { }
			@Override
			public void windowActivated(WindowEvent e)	{ }
		});
		
		SpringUtilities.makeCompactGrid(getContentPane(), rows, 2, 10, 10, 10, 10);
		pack();
		setVisible(true);
	}
	
	private void close(Map<String, ConfigurationValue> configSpec)
	{
		String key = null;
		for(int i = 0; i < getContentPane().getComponentCount(); ++i)
		{
			if(i%2 == 0)
			{
				key = ((JLabel)getContentPane().getComponent(i)).getText();
			}
			else
			{
				Object val = null;
				switch(configSpec.get(key).type)
				{
				case Analyser:
					configSpec.get(key).intValue = ((InstanceSummary)((JComboBox<InstanceSummary>) getContentPane().getComponent(i)).getSelectedItem()).instanceID;
					break;
				case Float:
					configSpec.get(key).fltValue = (Float)((JSpinner)getContentPane().getComponent(i)).getValue();
					break;
				case Integer:
					configSpec.get(key).intValue = (Integer)((JSpinner)getContentPane().getComponent(i)).getValue();
					break;
				case Reporter:
					configSpec.get(key).intValue = ((InstanceSummary)((JComboBox<InstanceSummary>) getContentPane().getComponent(i)).getSelectedItem()).instanceID;
					break;
				case Sensor:
					configSpec.get(key).intValue = ((InstanceSummary)((JComboBox<InstanceSummary>) getContentPane().getComponent(i)).getSelectedItem()).instanceID;
					break;
				case Store:
					configSpec.get(key).intValue = ((InstanceSummary)((JComboBox<InstanceSummary>) getContentPane().getComponent(i)).getSelectedItem()).instanceID;
					break;
				case String:
					configSpec.get(key).strValue = ((JTextField)getContentPane().getComponent(i)).getText();
					break;				
				}
			}
		}
			
		setVisible(false);
	}
}
