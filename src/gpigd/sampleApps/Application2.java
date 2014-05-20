package gpigd.sampleApps;
import java.io.IOException;


public class Application2 {
	public static void main(String[] args)  throws IOException 
	{
//		PoissonApp sensor1 = new PoissonApp(4444);
		int[] sd = {20, 20 , 20 };
		int[] mean = {20, 40, 80};
		
		sd = new int[]{10, 20 , 40 };
		mean = new int[]{50, 50, 50};
		
		
		Thread t1 = new Thread(new Runnable() {
	         public void run()
	         {
	        	int[] sd = {20, 20 , 20 };
	     		int[] mean = {20, 40, 80};
	     		GaussianApp sensor1 = new GaussianApp(4444, 4440, sd, mean);
	            sensor1.runSensor(); 
	         }
		});
//		Thread t2 = new Thread(new Runnable() {
//	         public void run()
//	         {
//	        	int[] sd = {10, 20 , 40 };
//	     		int[] mean = {50, 50, 50};
//	     		GaussianApp sensor1 = new GaussianApp(4445, 4441, sd, mean);
//	            sensor1.runSensor(); 
//	         }
//		});
//		
		t1.start();
		//t2.start();
		
	}
}
