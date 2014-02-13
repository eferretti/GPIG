package gpigb.sense;


import com.ibm.iris.IRIS;
import com.ibm.saguaro.logger.Logger;
import com.ibm.saguaro.system.ADC;
import com.ibm.saguaro.system.Assembly;
import com.ibm.saguaro.system.DataHandler;
import com.ibm.saguaro.system.DevCallback;
import com.ibm.saguaro.system.Device;
import com.ibm.saguaro.system.GPIO;
import com.ibm.saguaro.system.LIP;
import com.ibm.saguaro.system.Mote;
import com.ibm.saguaro.system.Radio;
import com.ibm.saguaro.system.Time;
import com.ibm.saguaro.system.Timer;
import com.ibm.saguaro.system.TimerEvent;
import com.ibm.saguaro.system.Util;
import com.ibm.saguaro.system.csr;
/**
 * 
 * Sensor driver which reads data from a light sensor via the Mote Runner infrastructure  platform 
 *
 */
public class MoteLightSensor {
	 private static byte[] frame;
	    
	    private static final int MOTE_PORT = 6;

	    
	    private static final byte MR_APP_PORT = 111;
	    
	    private static final int MDA100_ADC_CHANNEL_MASK = 0x02;

	    
	    private static int length;
	    
	    
	    private static byte channel = (byte) 0; // channel 11
	    private static byte panId = 0x11;
	    private static byte address = 0x11;
	    
	    private static byte[] reply;
	    private static byte port;
	    private static int offset;
	    
	    private static byte [] test;
	    private static int value;

		/** Create radio object. */
	    static Radio radio = new Radio();
	    private static GPIO gpioDev = new GPIO();
	    private static ADC adcDev = new ADC();
	    private static Timer adcTimer;
	    
	    static {
	    	test = new byte[]{(byte)0x1, (byte)0xF4};;

	    	value = Util.get16be(test,0); 

	    	
	        initialiseRadio();
	        initialiseFrame();
	        initialiseDelegates();
	        
	        gpioDev.open();
	        gpioDev.configureOutput(IRIS.PIN_INT5, GPIO.OUT_SET);
	        
	        adcDev.open(MDA100_ADC_CHANNEL_MASK,
	    			GPIO.NO_PIN/*use manual power */,
	    			0/*no warmup*/,
	    			0/*no interval, sample as fast as possible*/);
	        

	        reply = new byte[64];
	        port = Assembly.getActiveAsmId();
	        
	        Util.set16be(reply, MOTE_PORT, port);
	        
	        Logger.appendString(csr.s2b("Initialised"));
	    	Logger.flush(Mote.INFO);
	        
	        // trigger a read over the ADC 
	        //adcDev.read(Device.TIMED, 1 /*read open channels once*/, 
	        //Time.currentTicks()+Time.toTickSpan(Time.MILLISECS,500));
	    	
	    	adcTimer = new Timer();
	        adcTimer.setCallback(new TimerEvent(null){
	                public void invoke(byte param, long time){
	                    MoteLightSensor.timerCallback(param, time);
	                }
	            });

	        //Turn on receiving on the radio
	        radio.startRx(Device.ASAP, 0, Time.currentTicks()+0x7FFFFFFF);      
	    }
	    
	    static int handleLIP(int info, byte[] data, int len){
	    	Logger.appendString(csr.s2b("Received from LIP ")); 
	    	Logger.flush(Mote.INFO);
	    	
	    	offset = LIP.getPortOff();
	    	
	    	Logger.appendString(csr.s2b("Offset: "));
	        Logger.appendInt(offset);
	        Logger.flush(Mote.INFO);
	    	
	    	Logger.appendString(csr.s2b(" pc port: ")); 
	    	Logger.appendHex(data, offset - 1, 1);
	    	Logger.flush(Mote.INFO);
	    	
	    	Logger.appendString(csr.s2b(" mote port: ")); 
	    	Logger.appendHex(data, offset, 1);
	    	Logger.flush(Mote.INFO);

	    	Logger.appendString(csr.s2b("data: "));
	        Logger.appendHex(data, offset + 1, len - offset - 1);
	        Logger.flush(Mote.INFO);

	        Logger.appendString(csr.s2b("Len: "));
	        Logger.appendInt(len);
	        Logger.flush(Mote.INFO);

	        Logger.appendString(csr.s2b("Flags: "));
	        Logger.appendInt(len);
	        Logger.flush(Mote.INFO);
	        
	        byte[] payload = new byte[len - offset - 2];
	        //data should contain 0101F4, we wish to discard the first 01 and only select 01F4
	        
	        // Util.copyData method does not give
	        Util.copyData(data, offset+2, payload, 0, len - offset - 2);
	        //payload[0] = (byte) data[offset+2];
	        //payload[1] = (byte) data[offset+3];

	        int samplePeriod = Util.get16be(payload, 0);
	        
	        Logger.appendString(csr.s2b("util.get16be: "));
	        Logger.appendInt(Util.get16be(payload, 0));
	        Logger.flush(Mote.INFO);
	        
	        Logger.appendString(csr.s2b("samplePeriod: "));
	        Logger.appendInt(samplePeriod);
	        Logger.flush(Mote.INFO);

	        Logger.appendString(csr.s2b("payload: "));
	        Logger.appendHex(payload, 0, len - offset - 2);
	        Logger.flush(Mote.INFO);
	        
	   	
	        // remember who we got the last serial message from
	    	//copy data( source | source start index | destination | destination start index | length)
	    	Util.copyData(data, 0, reply, 0, MOTE_PORT);  // setup initial LIP header
	    	
	    	length = len;
	    	
	    	adcTimer.setAlarmBySpan(Time.toTickSpan(Time.MILLISECS, samplePeriod));
	    	
	    	return len;
	    }
	    
	    private static int onTempDone(int flags, byte[] data, int len, int info, long time) {
	    	//copy data( source | source start index | destination | destination start index | length)
	    	byte[] temp = new byte[len];
	    	Util.copyData(data,0, temp, 1, 1);
	    	Util.copyData(data,1, temp, 0, 1);
	    	Util.copyData(temp, 0, reply, offset + 1, len);
	    	// send (message buffer | start index | length)
	    	LIP.send(reply, 0, 9);

	    	Logger.flush(Mote.INFO);
	    	return 0; 
	    }
	    
	    private static void timerCallback(byte param, long time) {
	    	adcDev.read(Device.TIMED, 1 /*read open channels once*/, 	 Device.ASAP);
	    	adcTimer.setAlarmBySpan(Time.toTickSpan(Time.MILLISECS, 500));
	    }
	    

	    /**
	     * Initialises the radio starting on the channel specified in the parameters.  Also registers
	     * the delegates for the sending and receiving of frames.  
	     *
	     * @param startingChannel The specified channel to start on
	     */
	    private static void initialiseRadio() {
	    	
	    	// Open the default radio
	        radio.open(Radio.DID, null, 0, 0);
	        // Set the radio to be on channel we decide to start on
	        radio.setChannel(channel);
	        // Set PAN ID
	        radio.setPanId(panId, true);
	        // Set the short address
	        radio.setShortAddr(address);
	        
	    }
	    
	    /**
	     * Initialises the IEEE 802.15.4 radio frame information.  Each sink node has its own
	     * transmission frame byte array with the addressing setup on initialisation meaning the values
	     * can remain the same during execution.  
	     */
	    private static void initialiseFrame() {
	    	// Prepare data frame with source and destination addressing
	        frame = new byte[16];
	        frame[0] = Radio.FCF_DATA;
	        frame[1] = Radio.FCA_SRC_SADDR|Radio.FCA_DST_SADDR;
	        Util.set16le(frame, 3, panId); // A PAN ID 
	        Util.set16le(frame, 5, 0xFFFF); // A broadcast address 
	        Util.set16le(frame, 7, panId); // own PAN address 
	        Util.set16le(frame, 9, address); // own short address
	        frame[11] = (byte) 0; //WARNING: 32-bit int cast to 8-bits
	    }
	    
	    private static void initialiseDelegates() {
	    	Logger.appendString(csr.s2b("ADC read handler set up")); 
	    	Logger.flush(Mote.INFO);
	    	adcDev.setReadHandler(new DevCallback(null)
	    	{ 
	    		 public int invoke (int flags, byte[] data, int len, int info, long time) 
	    		 { 
	    			 return onTempDone(flags, data, len, info, time); 
	    		 } 
	    	}); 
	    	
	    	Logger.appendString(csr.s2b("Data handler set up")); 
	    	Logger.flush(Mote.INFO);
	    	Assembly.setDataHandler(new DataHandler(null)
	    	{
				public int invoke(int info, byte[] data, int len) {
					return handleLIP(info,data,len);
				}
			});
	    	LIP.open(MR_APP_PORT);
	    }
}
