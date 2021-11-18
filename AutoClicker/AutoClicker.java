import java.awt.Robot;
import java.awt.*;
import java.awt.AWTException;

public class AutoClicker {
	
	private Robot robot;
	private long delay;
	
	public AutoClicker() throws AWTException{
		robot = new Robot();
		delay = 300;	
	}
	
	public void clickMouse(int button) throws AWTException{
			
			robot.mousePress(button);
			robot.delay(100);
			robot.mouseRelease(button);
				try {
					Thread.sleep(delay);
				} catch (Exception e){
					e.printStackTrace();
				}		
		
	}
	
	public void setDelay(long ms) {
		this.delay = ms * 1000;
	}
	
}
