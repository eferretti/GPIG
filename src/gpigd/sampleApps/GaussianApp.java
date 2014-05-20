package gpigd.sampleApps;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


public class GaussianApp {
	private int[] gaussianMean;
	private int currentSD;
	private int currentMean;
	private int[] gaussianSD;
	private ServerSocket serverSocket, controlSocket;
	private Socket clientSocket, controlSocketClient ;
	private PrintWriter outStream;
	private BufferedReader controlStream;
	private int portNumberOut;
	private int portNumberIn;
	@SuppressWarnings("unchecked")

	
	public GaussianApp(int portNumberOut, int portNumberIn, int[] SD, int[] mean)
	{
		this.portNumberOut = portNumberOut;
		this.portNumberIn = portNumberIn;
		currentSD = 0;
		currentMean = 0;
		gaussianSD = SD;
		gaussianMean = mean;
	}

	public void runSensor()
	{
		try {
			
            controlSocket = new ServerSocket(portNumberIn);
            controlSocketClient = controlSocket.accept();
            controlStream = new BufferedReader(new InputStreamReader(controlSocketClient.getInputStream()));
            controlSocketClient.setSoTimeout(250);
            
            serverSocket = new ServerSocket(portNumberOut);
            clientSocket = serverSocket.accept();    
            outStream = new PrintWriter(clientSocket.getOutputStream(), true);                  
           
           
        } catch (IOException e) {
           System.out.println("Control Socket Fail");
        }
		int val;
		double rndNum;
		String inputLine;
		 
			while (true) {
				val = sampleDist();
			    outStream.println(val);
			    //System.out.println(val);
			    try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			    rndNum = Math.random();
			    if(rndNum > 0.90)
			    {
			    	if(currentSD == 2)
			    	{
			    		try {
			    			System.out.println("Checking control Stream");
							if(controlStream.readLine() != null)
							{
								System.out.println("Control Signal Recived, Resetting...");
								currentSD = 0;
								currentMean = 0;
							}else {
								currentSD = 2;
								currentMean = 2;
							}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			    	}else {
			    		System.out.println("Moving");
			    		currentSD++;
			    		currentMean++;
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
		int val = 1;
		double x , y, z;
		while(!valueFound && val < 100)
		{
			x = gaussianSD[currentSD]* Math.sqrt(2* Math.PI);
			y = -1 * Math.pow((val - gaussianMean[currentMean]), 2);
			z = 2 * Math.pow(gaussianSD[currentSD], 2);
					
			singleProb = (1/ x) * Math.exp(y / z) ;
			cumProb += singleProb;
			if(cumProb > sampleObservation)
			{
				valueFound = true;
			}else
			{
				val++;
			}
		}
		
		return val;
	}
}
