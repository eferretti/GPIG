package gpigb.configuration.handlers;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
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
	private final ComponentManager<Analyser> aMgr;
	private final ComponentManager<Reporter> rMgr;
	private final ComponentManager<Store> stMgr;
	private final ComponentManager<Sensor> seMgr;
	
	public GUIConfigHandler(ComponentManager<Analyser> aMgr, ComponentManager<Reporter> rMgr, ComponentManager<Store> stMgr, ComponentManager<Sensor> seMgr)
	{
		this.aMgr = aMgr;
		this.rMgr = rMgr;
		this.stMgr = stMgr;
		this.seMgr = seMgr;
	}
	
	boolean waiting = true;
	
	@Override
	public void getConfiguration(Map<String, ConfigurationValue> configSpec)
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
					JComboBox<StrongReference<Analyser>> combo = new JComboBox<>();
					ArrayList<StrongReference<Analyser>> items = new ArrayList<>();
					for(InstanceSummary summary : aMgr.getAvailableObjects())
					{
						items.add(aMgr.getObjectByID(summary.instanceID));
					}
					ComboBoxModel<StrongReference<Analyser>> model = new DefaultComboBoxModel<StrongReference<Analyser>>(items.toArray(new StrongReference[0]));
					combo.setModel(model);
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
					JComboBox<StrongReference<Reporter>> combo = new JComboBox<>();
					ArrayList<StrongReference<Reporter>> items = new ArrayList<>();
					for(InstanceSummary summary : rMgr.getAvailableObjects())
					{
						items.add(rMgr.getObjectByID(summary.instanceID));
					}
					ComboBoxModel<StrongReference<Reporter>> model = new DefaultComboBoxModel<StrongReference<Reporter>>(items.toArray(new StrongReference[0]));
					combo.setModel(model);
					component = combo;
					++rows;
					break;
				}
				
				case Sensor:
				{
					JComboBox<StrongReference<Sensor>> combo = new JComboBox<>();
					ArrayList<StrongReference<Sensor>> items = new ArrayList<>();
					for(InstanceSummary summary : seMgr.getAvailableObjects())
					{
						items.add(seMgr.getObjectByID(summary.instanceID));
					}
					ComboBoxModel<StrongReference<Sensor>> model = new DefaultComboBoxModel<StrongReference<Sensor>>(items.toArray(new StrongReference[0]));
					combo.setModel(model);
					component = combo;
					++rows;
					break;
				}
				
				case Store:
				{
					JComboBox<StrongReference<Store>> combo = new JComboBox<>();
					ArrayList<StrongReference<Store>> items = new ArrayList<>();
					for(InstanceSummary summary : stMgr.getAvailableObjects())
					{
						items.add(stMgr.getObjectByID(summary.instanceID));
					}
					ComboBoxModel<StrongReference<Store>> model = new DefaultComboBoxModel<StrongReference<Store>>(items.toArray(new StrongReference[0]));
					combo.setModel(model);
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
				waiting = false;
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
		
		
		waiting = true;
		
		while(waiting) 
			try 
			{
				Thread.sleep(10);
			}
			catch(Exception e)
			{
				
			};
			
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
					val = ((JComboBox<Analyser>)getContentPane().getComponent(i)).getSelectedItem();
					break;
				case Float:
					val = (Float)((JSpinner)getContentPane().getComponent(i)).getValue();
					break;
				case Integer:
					val = (Integer)((JSpinner)getContentPane().getComponent(i)).getValue();
					break;
				case Reporter:
					val = ((JComboBox<Reporter>)getContentPane().getComponent(i)).getSelectedItem();
					break;
				case Sensor:
					val = ((JComboBox<Sensor>)getContentPane().getComponent(i)).getSelectedItem();
					break;
				case Store:
					val = ((JComboBox<Store>)getContentPane().getComponent(i)).getSelectedItem();
					break;
				case String:
					val = ((JTextField)getContentPane().getComponent(i)).getText();
					break;				
				}
				configSpec.get(key).value = val;
			}
		}
			
		setVisible(false);
	}
}
