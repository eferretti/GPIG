import gpigb.report.ReporterGUI;
import gpigb.store.InMemoryStore;

import java.awt.EventQueue;


public class GUIRun {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ReporterGUI window = new ReporterGUI();
					
					InMemoryStore<Integer> store = new InMemoryStore<>();
					window.store = store;
					
					window.show();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
