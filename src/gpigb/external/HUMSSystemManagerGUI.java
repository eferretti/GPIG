package gpigb.external;

import gpigb.classloading.ComponentManager.InstanceSummary;
import gpigb.classloading.ComponentManager.ModuleSummary;
import gpigb.configuration.ConfigurationValue;
import gpigb.configuration.handlers.GUIConfigHandler;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;

public class HUMSSystemManagerGUI extends JFrame implements ActionListener
{

	private JPanel contentPane;
	private JTextField txtHostName;
	private JButton btnConnectToSystem;
	private JButton btnUploadJar;
	private HUMSSystem system = null;

	private JComboBox<ModuleSummary> cboSensorModules;
	private JComboBox<InstanceSummary> cboSensorInstances;
	private JButton btnConfigureSensor;
	private JButton btnNewSensor;

	private JComboBox<ModuleSummary> cboAnalyserModules;
	private JComboBox<InstanceSummary> cboAnalyserInstances;
	private JButton btnConfigureAnalyser;
	private JButton btnNewAnalyser;

	private JComboBox<ModuleSummary> cboStoreModules;
	private JComboBox<InstanceSummary> cboStoreInstances;
	private JButton btnConfigureStore;
	private JButton btnNewStore;

	private JComboBox<ModuleSummary> cboReporterModules;
	private JComboBox<InstanceSummary> cboReporterInstances;
	private JButton btnConfigureReporter;
	private JButton btnNewReporter;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try {
					HUMSSystemManagerGUI frame = new HUMSSystemManagerGUI();
					frame.setVisible(true);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public HUMSSystemManagerGUI()
	{
		setTitle("Configure Remote HUMS");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 190);
		contentPane = new JPanel();
		setContentPane(contentPane);
		SpringLayout sl_contentPane = new SpringLayout();
		contentPane.setLayout(sl_contentPane);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		sl_contentPane.putConstraint(SpringLayout.WEST, tabbedPane, 10, SpringLayout.WEST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, tabbedPane, -10, SpringLayout.SOUTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, tabbedPane, -10, SpringLayout.EAST, contentPane);
		contentPane.add(tabbedPane);
		
		JLabel lblNewLabel = new JLabel("Host:");
		sl_contentPane.putConstraint(SpringLayout.NORTH, tabbedPane, 10, SpringLayout.SOUTH, lblNewLabel);
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblNewLabel, 10, SpringLayout.NORTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.WEST, lblNewLabel, 10, SpringLayout.WEST, contentPane);
		contentPane.add(lblNewLabel);

		setupSensorTab(tabbedPane);
		setupStoreTab(tabbedPane);
		setupAnalyserTab(tabbedPane);
		setupReporterTab(tabbedPane);
		
		txtHostName = new JTextField();
		sl_contentPane.putConstraint(SpringLayout.NORTH, txtHostName, 0, SpringLayout.NORTH, lblNewLabel);
		sl_contentPane.putConstraint(SpringLayout.WEST, txtHostName, 5, SpringLayout.EAST, lblNewLabel);
		contentPane.add(txtHostName);
		txtHostName.setColumns(10);
		
		btnConnectToSystem = new JButton("Connect");
		btnConnectToSystem.addActionListener(this);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, lblNewLabel, 0, SpringLayout.SOUTH, btnConnectToSystem);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, txtHostName, 0, SpringLayout.SOUTH, btnConnectToSystem);
		
		sl_contentPane.putConstraint(SpringLayout.EAST, txtHostName, -5, SpringLayout.WEST, btnConnectToSystem);
		sl_contentPane.putConstraint(SpringLayout.NORTH, btnConnectToSystem, 10, SpringLayout.NORTH, contentPane);
		contentPane.add(btnConnectToSystem);
		
		btnUploadJar = new JButton("Upload JAR");
		btnUploadJar.addActionListener(this);
		sl_contentPane.putConstraint(SpringLayout.EAST, btnConnectToSystem, -10, SpringLayout.WEST, btnUploadJar);
		sl_contentPane.putConstraint(SpringLayout.NORTH, btnUploadJar, 0, SpringLayout.NORTH, btnConnectToSystem);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, btnUploadJar, 0, SpringLayout.SOUTH, btnConnectToSystem);
		sl_contentPane.putConstraint(SpringLayout.EAST, btnUploadJar, -10, SpringLayout.EAST, contentPane);
		contentPane.add(btnUploadJar);
	}

	private void setupSensorTab(JTabbedPane tabbedPane)
	{
		JPanel panel = new JPanel();
		tabbedPane.addTab("Sensors", null, panel, null);
		SpringLayout sl_panel = new SpringLayout();
		panel.setLayout(sl_panel);
		
		cboSensorModules = new JComboBox<ModuleSummary>();
		sl_panel.putConstraint(SpringLayout.NORTH, cboSensorModules, 10, SpringLayout.NORTH, panel);
		panel.add(cboSensorModules);
		
		btnNewSensor = new JButton("Create New");
		btnNewSensor.addActionListener(this);
		sl_panel.putConstraint(SpringLayout.EAST, cboSensorModules, -10, SpringLayout.WEST, btnNewSensor);
		sl_panel.putConstraint(SpringLayout.NORTH, btnNewSensor, 0, SpringLayout.NORTH, cboSensorModules);
		sl_panel.putConstraint(SpringLayout.SOUTH, btnNewSensor, 0, SpringLayout.SOUTH, cboSensorModules);
		sl_panel.putConstraint(SpringLayout.EAST, btnNewSensor, -10, SpringLayout.EAST, panel);
		panel.add(btnNewSensor);
		
		cboSensorInstances = new JComboBox<InstanceSummary>();
		sl_panel.putConstraint(SpringLayout.WEST, cboSensorModules, 0, SpringLayout.WEST, cboSensorInstances);
		sl_panel.putConstraint(SpringLayout.EAST, cboSensorInstances, 0, SpringLayout.EAST, cboSensorModules);
		panel.add(cboSensorInstances);
		
		JLabel lblModules = new JLabel("Modules:");
		lblModules.setHorizontalAlignment(SwingConstants.TRAILING);
		sl_panel.putConstraint(SpringLayout.NORTH, lblModules, 0, SpringLayout.NORTH, cboSensorModules);
		sl_panel.putConstraint(SpringLayout.WEST, lblModules, 10, SpringLayout.WEST, panel);
		sl_panel.putConstraint(SpringLayout.SOUTH, lblModules, 0, SpringLayout.SOUTH, cboSensorModules);
		panel.add(lblModules);
		
		JLabel lblInstances = new JLabel("Instances:");
		sl_panel.putConstraint(SpringLayout.SOUTH, lblInstances, 0, SpringLayout.SOUTH, cboSensorInstances);
		lblInstances.setHorizontalAlignment(SwingConstants.TRAILING);
		sl_panel.putConstraint(SpringLayout.EAST, lblModules, 0, SpringLayout.EAST, lblInstances);
		sl_panel.putConstraint(SpringLayout.WEST, cboSensorInstances, 10, SpringLayout.EAST, lblInstances);
		sl_panel.putConstraint(SpringLayout.NORTH, cboSensorInstances, 0, SpringLayout.NORTH, lblInstances);
		sl_panel.putConstraint(SpringLayout.NORTH, lblInstances, 10, SpringLayout.SOUTH, lblModules);
		sl_panel.putConstraint(SpringLayout.WEST, lblInstances, 0, SpringLayout.WEST, lblModules);
		panel.add(lblInstances);
		
		btnConfigureSensor = new JButton("Configure");
		btnConfigureSensor.addActionListener(this);
		sl_panel.putConstraint(SpringLayout.NORTH, btnConfigureSensor, 0, SpringLayout.NORTH, cboSensorInstances);
		sl_panel.putConstraint(SpringLayout.WEST, btnConfigureSensor, 0, SpringLayout.WEST, btnNewSensor);
		sl_panel.putConstraint(SpringLayout.SOUTH, btnConfigureSensor, 0, SpringLayout.SOUTH, cboSensorInstances);
		sl_panel.putConstraint(SpringLayout.EAST, btnConfigureSensor, 0, SpringLayout.EAST, btnNewSensor);
		panel.add(btnConfigureSensor);
	}

	private void setupAnalyserTab(JTabbedPane tabbedPane)
	{
		JPanel panel = new JPanel();
		tabbedPane.addTab("Analysers", null, panel, null);
		SpringLayout sl_panel = new SpringLayout();
		panel.setLayout(sl_panel);
		
		cboAnalyserModules = new JComboBox<ModuleSummary>();
		sl_panel.putConstraint(SpringLayout.NORTH, cboAnalyserModules, 10, SpringLayout.NORTH, panel);
		panel.add(cboAnalyserModules);
		
		btnNewAnalyser = new JButton("Create New");
		btnNewAnalyser.addActionListener(this);
		sl_panel.putConstraint(SpringLayout.EAST, cboAnalyserModules, -10, SpringLayout.WEST, btnNewAnalyser);
		sl_panel.putConstraint(SpringLayout.NORTH, btnNewAnalyser, 0, SpringLayout.NORTH, cboAnalyserModules);
		sl_panel.putConstraint(SpringLayout.SOUTH, btnNewAnalyser, 0, SpringLayout.SOUTH, cboAnalyserModules);
		sl_panel.putConstraint(SpringLayout.EAST, btnNewAnalyser, -10, SpringLayout.EAST, panel);
		panel.add(btnNewAnalyser);
		
		cboAnalyserInstances = new JComboBox<InstanceSummary>();
		sl_panel.putConstraint(SpringLayout.WEST, cboAnalyserModules, 0, SpringLayout.WEST, cboAnalyserInstances);
		sl_panel.putConstraint(SpringLayout.EAST, cboAnalyserInstances, 0, SpringLayout.EAST, cboAnalyserModules);
		panel.add(cboAnalyserInstances);
		
		JLabel lblModules = new JLabel("Modules:");
		lblModules.setHorizontalAlignment(SwingConstants.TRAILING);
		sl_panel.putConstraint(SpringLayout.NORTH, lblModules, 0, SpringLayout.NORTH, cboAnalyserModules);
		sl_panel.putConstraint(SpringLayout.WEST, lblModules, 10, SpringLayout.WEST, panel);
		sl_panel.putConstraint(SpringLayout.SOUTH, lblModules, 0, SpringLayout.SOUTH, cboAnalyserModules);
		panel.add(lblModules);
		
		JLabel lblInstances = new JLabel("Instances:");
		sl_panel.putConstraint(SpringLayout.SOUTH, lblInstances, 0, SpringLayout.SOUTH, cboAnalyserInstances);
		lblInstances.setHorizontalAlignment(SwingConstants.TRAILING);
		sl_panel.putConstraint(SpringLayout.EAST, lblModules, 0, SpringLayout.EAST, lblInstances);
		sl_panel.putConstraint(SpringLayout.WEST, cboAnalyserInstances, 10, SpringLayout.EAST, lblInstances);
		sl_panel.putConstraint(SpringLayout.NORTH, cboAnalyserInstances, 0, SpringLayout.NORTH, lblInstances);
		sl_panel.putConstraint(SpringLayout.NORTH, lblInstances, 10, SpringLayout.SOUTH, lblModules);
		sl_panel.putConstraint(SpringLayout.WEST, lblInstances, 0, SpringLayout.WEST, lblModules);
		panel.add(lblInstances);
		
		btnConfigureAnalyser = new JButton("Configure");
		btnConfigureAnalyser.addActionListener(this);
		sl_panel.putConstraint(SpringLayout.NORTH, btnConfigureAnalyser, 0, SpringLayout.NORTH, cboAnalyserInstances);
		sl_panel.putConstraint(SpringLayout.WEST, btnConfigureAnalyser, 0, SpringLayout.WEST, btnNewAnalyser);
		sl_panel.putConstraint(SpringLayout.SOUTH, btnConfigureAnalyser, 0, SpringLayout.SOUTH, cboAnalyserInstances);
		sl_panel.putConstraint(SpringLayout.EAST, btnConfigureAnalyser, 0, SpringLayout.EAST, btnNewAnalyser);
		panel.add(btnConfigureAnalyser);
	}
	
	private void setupReporterTab(JTabbedPane tabbedPane)
	{
		JPanel panel = new JPanel();
		tabbedPane.addTab("Reporters", null, panel, null);
		SpringLayout sl_panel = new SpringLayout();
		panel.setLayout(sl_panel);
		
		cboReporterModules = new JComboBox<ModuleSummary>();
		sl_panel.putConstraint(SpringLayout.NORTH, cboReporterModules, 10, SpringLayout.NORTH, panel);
		panel.add(cboReporterModules);
		
		btnNewReporter = new JButton("Create New");
		btnNewReporter.addActionListener(this);
		sl_panel.putConstraint(SpringLayout.EAST, cboReporterModules, -10, SpringLayout.WEST, btnNewReporter);
		sl_panel.putConstraint(SpringLayout.NORTH, btnNewReporter, 0, SpringLayout.NORTH, cboReporterModules);
		sl_panel.putConstraint(SpringLayout.SOUTH, btnNewReporter, 0, SpringLayout.SOUTH, cboReporterModules);
		sl_panel.putConstraint(SpringLayout.EAST, btnNewReporter, -10, SpringLayout.EAST, panel);
		panel.add(btnNewReporter);
		
		cboReporterInstances = new JComboBox<InstanceSummary>();
		sl_panel.putConstraint(SpringLayout.WEST, cboReporterModules, 0, SpringLayout.WEST, cboReporterInstances);
		sl_panel.putConstraint(SpringLayout.EAST, cboReporterInstances, 0, SpringLayout.EAST, cboReporterModules);
		panel.add(cboReporterInstances);
		
		JLabel lblModules = new JLabel("Modules:");
		lblModules.setHorizontalAlignment(SwingConstants.TRAILING);
		sl_panel.putConstraint(SpringLayout.NORTH, lblModules, 0, SpringLayout.NORTH, cboReporterModules);
		sl_panel.putConstraint(SpringLayout.WEST, lblModules, 10, SpringLayout.WEST, panel);
		sl_panel.putConstraint(SpringLayout.SOUTH, lblModules, 0, SpringLayout.SOUTH, cboReporterModules);
		panel.add(lblModules);
		
		JLabel lblInstances = new JLabel("Instances:");
		sl_panel.putConstraint(SpringLayout.SOUTH, lblInstances, 0, SpringLayout.SOUTH, cboReporterInstances);
		lblInstances.setHorizontalAlignment(SwingConstants.TRAILING);
		sl_panel.putConstraint(SpringLayout.EAST, lblModules, 0, SpringLayout.EAST, lblInstances);
		sl_panel.putConstraint(SpringLayout.WEST, cboReporterInstances, 10, SpringLayout.EAST, lblInstances);
		sl_panel.putConstraint(SpringLayout.NORTH, cboReporterInstances, 0, SpringLayout.NORTH, lblInstances);
		sl_panel.putConstraint(SpringLayout.NORTH, lblInstances, 10, SpringLayout.SOUTH, lblModules);
		sl_panel.putConstraint(SpringLayout.WEST, lblInstances, 0, SpringLayout.WEST, lblModules);
		panel.add(lblInstances);
		
		btnConfigureReporter = new JButton("Configure");
		btnConfigureReporter.addActionListener(this);
		sl_panel.putConstraint(SpringLayout.NORTH, btnConfigureReporter, 0, SpringLayout.NORTH, cboReporterInstances);
		sl_panel.putConstraint(SpringLayout.WEST, btnConfigureReporter, 0, SpringLayout.WEST, btnNewReporter);
		sl_panel.putConstraint(SpringLayout.SOUTH, btnConfigureReporter, 0, SpringLayout.SOUTH, cboReporterInstances);
		sl_panel.putConstraint(SpringLayout.EAST, btnConfigureReporter, 0, SpringLayout.EAST, btnNewReporter);
		panel.add(btnConfigureReporter);
	}
	
	private void setupStoreTab(JTabbedPane tabbedPane)
	{
		JPanel panel = new JPanel();
		tabbedPane.addTab("Stores", null, panel, null);
		SpringLayout sl_panel = new SpringLayout();
		panel.setLayout(sl_panel);
		
		cboStoreModules = new JComboBox<ModuleSummary>();
		sl_panel.putConstraint(SpringLayout.NORTH, cboStoreModules, 10, SpringLayout.NORTH, panel);
		panel.add(cboStoreModules);
		
		btnNewStore = new JButton("Create New");
		btnNewStore.addActionListener(this);
		sl_panel.putConstraint(SpringLayout.EAST, cboStoreModules, -10, SpringLayout.WEST, btnNewStore);
		sl_panel.putConstraint(SpringLayout.NORTH, btnNewStore, 0, SpringLayout.NORTH, cboStoreModules);
		sl_panel.putConstraint(SpringLayout.SOUTH, btnNewStore, 0, SpringLayout.SOUTH, cboStoreModules);
		sl_panel.putConstraint(SpringLayout.EAST, btnNewStore, -10, SpringLayout.EAST, panel);
		panel.add(btnNewStore);
		
		cboStoreInstances = new JComboBox<InstanceSummary>();
		sl_panel.putConstraint(SpringLayout.WEST, cboStoreModules, 0, SpringLayout.WEST, cboStoreInstances);
		sl_panel.putConstraint(SpringLayout.EAST, cboStoreInstances, 0, SpringLayout.EAST, cboStoreModules);
		panel.add(cboStoreInstances);
		
		JLabel lblModules = new JLabel("Modules:");
		lblModules.setHorizontalAlignment(SwingConstants.TRAILING);
		sl_panel.putConstraint(SpringLayout.NORTH, lblModules, 0, SpringLayout.NORTH, cboStoreModules);
		sl_panel.putConstraint(SpringLayout.WEST, lblModules, 10, SpringLayout.WEST, panel);
		sl_panel.putConstraint(SpringLayout.SOUTH, lblModules, 0, SpringLayout.SOUTH, cboStoreModules);
		panel.add(lblModules);
		
		JLabel lblInstances = new JLabel("Instances:");
		sl_panel.putConstraint(SpringLayout.SOUTH, lblInstances, 0, SpringLayout.SOUTH, cboStoreInstances);
		lblInstances.setHorizontalAlignment(SwingConstants.TRAILING);
		sl_panel.putConstraint(SpringLayout.EAST, lblModules, 0, SpringLayout.EAST, lblInstances);
		sl_panel.putConstraint(SpringLayout.WEST, cboStoreInstances, 10, SpringLayout.EAST, lblInstances);
		sl_panel.putConstraint(SpringLayout.NORTH, cboStoreInstances, 0, SpringLayout.NORTH, lblInstances);
		sl_panel.putConstraint(SpringLayout.NORTH, lblInstances, 10, SpringLayout.SOUTH, lblModules);
		sl_panel.putConstraint(SpringLayout.WEST, lblInstances, 0, SpringLayout.WEST, lblModules);
		panel.add(lblInstances);
		
		btnConfigureStore = new JButton("Configure");
		btnConfigureStore.addActionListener(this);
		sl_panel.putConstraint(SpringLayout.NORTH, btnConfigureStore, 0, SpringLayout.NORTH, cboStoreInstances);
		sl_panel.putConstraint(SpringLayout.WEST, btnConfigureStore, 0, SpringLayout.WEST, btnNewStore);
		sl_panel.putConstraint(SpringLayout.SOUTH, btnConfigureStore, 0, SpringLayout.SOUTH, cboStoreInstances);
		sl_panel.putConstraint(SpringLayout.EAST, btnConfigureStore, 0, SpringLayout.EAST, btnNewStore);
		panel.add(btnConfigureStore);
	}
	
	@Override
	public void actionPerformed(ActionEvent event)
	{
		if(event.getSource() == this.btnConnectToSystem)
		{
			try 
			{
				this.system = (HUMSSystem)Naming.lookup("//" + this.txtHostName.getText() + "/HUMS");
				refresh();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		
		if(event.getSource() == this.btnUploadJar)
		{
			JFileChooser chooser = new JFileChooser();
			chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			
			if(chooser.showOpenDialog(this.getContentPane()) == JFileChooser.APPROVE_OPTION)
			{
				File f = chooser.getSelectedFile();
				
				try
				{
					this.system.uploadJarFile(readFile(f));
				}
				catch(RemoteException re)
				{
					System.out.println("Failed to upload file");
				}
				catch(IOException io)
				{
					System.out.println("Failed to read file");
				}
			}
			
			refresh();
		}
		
		if(event.getSource() == this.btnNewSensor)
		{
			try
			{
				this.system.createSensor(((ModuleSummary)this.cboSensorModules.getSelectedItem()).moduleID);
				refresh();
			}
			catch(Exception e)
			{
				
			}
		}
		
		if (event.getSource() == this.btnConfigureSensor) {
			new Thread(new Runnable()
			{
				public void run()
				{
					try {
						int id = ((InstanceSummary) cboSensorInstances.getSelectedItem()).instanceID;
						Map<String, ConfigurationValue> config = system.getSensorConfig(id);
						new GUIConfigHandler(system.listAnalysers(), system.listReporters(), system.listStores(),
								system.listSensors()).getConfiguration(config);
						system.setSensorConfig(id, config);
						refresh();
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start();
		}
		
		if(event.getSource() == this.btnNewAnalyser)
		{
			try
			{
				this.system.createAnalyser(((ModuleSummary)this.cboAnalyserModules.getSelectedItem()).moduleID);
				refresh();
			}
			catch(Exception e)
			{
				
			}
		}
		
		if(event.getSource() == this.btnConfigureAnalyser)
		{
			new Thread(new Runnable()
			{
				public void run()
				{
					try
					{
						int id = ((InstanceSummary)cboAnalyserInstances.getSelectedItem()).instanceID;
						Map<String, ConfigurationValue> config = system.getAnalyserConfig(id);
						new GUIConfigHandler(system.listAnalysers(), system.listReporters(), system.listStores(), system.listSensors()).getConfiguration(config);
						system.setAnalyserConfig(id, config);
						refresh();
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
				}
			}).start();
		}
		
		if(event.getSource() == this.btnNewReporter)
		{
			try
			{
				this.system.createReporter(((ModuleSummary)this.cboReporterModules.getSelectedItem()).moduleID);
				refresh();
			}
			catch(Exception e)
			{
				
			}
		}
		
		if(event.getSource() == this.btnConfigureReporter)
		{
			new Thread(new Runnable()
			{
				public void run()
				{
					try
					{
						int id = ((InstanceSummary)cboReporterInstances.getSelectedItem()).instanceID;
						Map<String, ConfigurationValue> config = system.getAnalyserConfig(id);
						new GUIConfigHandler(system.listAnalysers(), system.listReporters(), system.listStores(), system.listSensors()).getConfiguration(config);
						system.setReporterConfig(id, config);
						refresh();
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
				}
			}).start();
		}
		
		if(event.getSource() == this.btnNewStore)
		{
			try
			{
				this.system.createStore(((ModuleSummary)this.cboStoreModules.getSelectedItem()).moduleID);
				refresh();
			}
			catch(Exception e)
			{
				
			}
		}
		
		if(event.getSource() == this.btnConfigureStore)
		{
			new Thread(new Runnable()
			{
				public void run()
				{
					try
					{
						int id = ((InstanceSummary)cboStoreInstances.getSelectedItem()).instanceID;
						Map<String, ConfigurationValue> config = system.getAnalyserConfig(id);
						new GUIConfigHandler(system.listAnalysers(), system.listReporters(), system.listStores(), system.listSensors()).getConfiguration(config);
						system.setStoreConfig(id, config);
						refresh();
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
				}
			}).start();
		}
	}
	
	private byte[] readFile(File file) throws IOException
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		FileInputStream fis = new FileInputStream(file);
		
		byte[] buffer = new byte[1024];
		int read;
		while((read = fis.read(buffer)) > 0)
		{
			baos.write(buffer, 0, read);
		}
		
		fis.close();
		baos.flush();
		return baos.toByteArray();
	}
	
	private void refresh()
	{
		refreshSensors();
		refreshAnalysers();
		refreshReporters();
		refreshStores();
	}
	
	private void refreshSensors()
	{
		if(this.system != null)
		{
			try
			{
				List<ModuleSummary> summary = this.system.listSensorModules();
				this.cboSensorModules.setModel(new DefaultComboBoxModel<ModuleSummary>(summary.toArray(new ModuleSummary[]{})));
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
			try
			{
				List<InstanceSummary> summary = this.system.listSensors();
				this.cboSensorInstances.setModel(new DefaultComboBoxModel<InstanceSummary>(summary.toArray(new InstanceSummary[]{})));
			}
			catch(Exception e)
			{
				
			}
		}
	}
	
	private void refreshAnalysers()
	{
		if(this.system != null)
		{
			try
			{
				List<ModuleSummary> summary = this.system.listAnalyserModules();
				this.cboAnalyserModules.setModel(new DefaultComboBoxModel<ModuleSummary>(summary.toArray(new ModuleSummary[]{})));
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
			try
			{
				List<InstanceSummary> summary = this.system.listAnalysers();
				this.cboAnalyserInstances.setModel(new DefaultComboBoxModel<InstanceSummary>(summary.toArray(new InstanceSummary[]{})));
			}
			catch(Exception e)
			{
				
			}
		}
	}
	
	private void refreshReporters()
	{
		if(this.system != null)
		{
			try
			{
				List<ModuleSummary> summary = this.system.listReporterModules();
				this.cboReporterModules.setModel(new DefaultComboBoxModel<ModuleSummary>(summary.toArray(new ModuleSummary[]{})));
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
			try
			{
				List<InstanceSummary> summary = this.system.listReporters();
				this.cboReporterInstances.setModel(new DefaultComboBoxModel<InstanceSummary>(summary.toArray(new InstanceSummary[]{})));
			}
			catch(Exception e)
			{
				
			}
		}
	}
	
	private void refreshStores()
	{
		if(this.system != null)
		{
			try
			{
				List<ModuleSummary> summary = this.system.listStoreModules();
				this.cboStoreModules.setModel(new DefaultComboBoxModel<ModuleSummary>(summary.toArray(new ModuleSummary[]{})));
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
			try
			{
				List<InstanceSummary> summary = this.system.listStores();
				this.cboStoreInstances.setModel(new DefaultComboBoxModel<InstanceSummary>(summary.toArray(new InstanceSummary[]{})));
			}
			catch(Exception e)
			{
				
			}
		}
	}
}
