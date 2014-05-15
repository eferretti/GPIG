package gpigb.external;

import gpigb.classloading.ComponentManager.InstanceSummary;
import gpigb.classloading.ComponentManager.ModuleSummary;
import gpigb.configuration.ConfigurationValue;
import gpigb.configuration.handlers.GUIConfigHandler;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.SpringLayout;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.FlowLayout;
import net.miginfocom.swing.MigLayout;
import javax.swing.JTree;
import javax.swing.JComboBox;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public class HUMSSystemManagerGUI extends JFrame implements ActionListener
{

	private JPanel contentPane;
	private JTextField txtHostName;
	private JComboBox<ModuleSummary> cboSensorModules;
	private JButton btnNewSensor;
	private JButton btnConnectToSystem;
	private JButton btnUploadJar;
	
	private HUMSSystem system = null;
	private JComboBox<InstanceSummary> cboSensorInstances;
	private JButton btnConfigureSensor;

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
		setBounds(100, 100, 450, 190);
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
		
		txtHostName = new JTextField();
		sl_contentPane.putConstraint(SpringLayout.NORTH, txtHostName, 0, SpringLayout.NORTH, lblNewLabel);
		sl_contentPane.putConstraint(SpringLayout.WEST, txtHostName, 5, SpringLayout.EAST, lblNewLabel);
		contentPane.add(txtHostName);
		txtHostName.setColumns(10);
		
		btnConnectToSystem = new JButton("Connect");
		btnConnectToSystem.addActionListener(this);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, lblNewLabel, 0, SpringLayout.SOUTH, btnConnectToSystem);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, txtHostName, 0, SpringLayout.SOUTH, btnConnectToSystem);
		
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
		
		if(event.getSource() == this.btnConfigureSensor)
		{
			try
			{
				int id = ((InstanceSummary)this.cboSensorInstances.getSelectedItem()).instanceID;
				Map<String, ConfigurationValue> config = this.system.getSensorConfig(id);
				new GUIConfigHandler(this.system.listAnalysers(), this.system.listReporters(), this.system.listStores(), this.system.listSensors()).getConfiguration(config);
				this.system.setSensorConfig(id, config);
				refresh();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
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
}
