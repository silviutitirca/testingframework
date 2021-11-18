import java.awt.event.InputEvent;
import java.lang.Thread;
import java.awt.AWTException;
import java.io.IOException;


public class AutoClickerMain {
	
	public static long clicks;
	public static long delay;
	public static String manualEnteredValues = "false";
	
	public static void main(String[] args) throws AWTException, IOException{
		
		System.out.println("======== AutoClicker =======");
		Utils util = new Utils();
		
		switch(manualEnteredValues){
			case "true":
				delay = util.readParameterFromKeyboard("delay");
				clicks = util.readParameterFromKeyboard("clicks");
				break;
			
			case "false":
				delay = util.readParameterFromFile("delay");
				clicks = util.readParameterFromFile("clicks");					
				break;
		}
		
		
		System.out.println("Program will start in 3 seconds.");
		try {
			Thread.sleep(3000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		AutoClicker clicker = new AutoClicker();
		clicker.setDelay(delay);
		util.printTimeStamp();
				
		for (int i = 0; i < clicks; i++){
			clicker.clickMouse(InputEvent.BUTTON1_DOWN_MASK);
			//util.printTimeStamp();
		}
		
		System.out.println("AutoClicker complete.");
	} 
	
	
}