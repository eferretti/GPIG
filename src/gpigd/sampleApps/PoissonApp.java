package gpigd.sampleApps;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.Math;
import java.net.ServerSocket;
import java.net.Socket;

public class PoissonApp {
	
	private int[] currentDist;
	private int currentLambda;
	int[] poissonLambda = {30, 50, 90};
	private ServerSocket serverSocket;
	private Socket clientSocket;
	private PrintWriter outStream;
	private BufferedReader inStream;
	@SuppressWarnings("unchecked")

	
	public PoissonApp(int portNumber)
	{
//		try {
//	            serverSocket = new ServerSocket(portNumber);
//	            clientSocket = serverSocket.accept();    
//	            outStream = new PrintWriter(clientSocket.getOutputStream(), true);                  
//	            inStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//	            
//	        } catch (IOException e) {
//	           
//	        }
		currentLambda = 0;
	}

	public void runSensor()
	{
		int val;
		double rndNum;
		String inputLine;
		 
			while (true) {
				val = sampleDist();
//			    outStream.println(val);
			    System.out.println(val);
			    try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			    rndNum = Math.random();
			    if(rndNum > 0.90)
			    {
			    	if(currentLambda == 2)
			    	{
			    		currentLambda = 2;
			    	}else {
			    		currentLambda++;
			    	}
			    }
			 }
		
	}
	
	private int sampleDist()
	{
		double sampleObservation = Math.random();
		double cumProb = 0;
		double singleProb =0;
		boolean valueFound = false;
		double val = 1.0;
		double  x , y, z;
		while(!valueFound)
		{
			double cVal = poissonLambda[currentLambda]*0.01;
			x = Math.pow(cVal, val);
			y = Math.exp(-1 * cVal);
			z = factorial(val);
					
			singleProb = (x * y) / z ;
			cumProb += singleProb;
			if(cumProb > sampleObservation)
			{
				valueFound = true;
			}else
			{
				val += 1.0;
			}
		}
		
		return (int) Math.round(val);
	}


	
    private int factorial(double n) {
        int fact = 1; 
        for (int i = 1; i <= n; i++) {
            fact *= i;
        }
        return fact;
    }
}



