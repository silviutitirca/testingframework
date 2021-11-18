import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.awt.*;
import java.awt.TrayIcon.MessageType;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.util.Properties;
import java.util.Scanner;


public class Utils {
	

	public void printTimeStamp(){
		System.out.println("Clicked at: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss,SSS")));	
		try{
            displayTray();
            }catch(AWTException ex){
            ex.printStackTrace(); 
            }      
		}
		
	public void displayTray() throws AWTException {
        //Obtain only one instance of the SystemTray object
        SystemTray tray = SystemTray.getSystemTray();

        //If the icon is a file
        Image image = Toolkit.getDefaultToolkit().createImage("icon.png");
        //Alternative (if the icon is on the classpath):
        //Image image = Toolkit.getDefaultToolkit().createImage(getClass().getResource("icon.png"));

        TrayIcon trayIcon = new TrayIcon(image, "Java AWT Tray Demo");
        //Let the system resize the image if needed
        trayIcon.setImageAutoSize(true);
        //Set tooltip text for the tray icon
        trayIcon.setToolTip("System tray icon demo");
        tray.add(trayIcon);

        trayIcon.displayMessage("AutoClicker", "Running of autoclicker started", MessageType.INFO);
		
		//Remove tray after displaying it
		if (tray != null && trayIcon != null) {
			tray.remove(trayIcon);
			tray = null;
			trayIcon = null;
		}
    }
	
	public long readParameterFromFile(String parameter) throws IOException{
		InputStream input = new FileInputStream("config.properties");
		Properties prop = new Properties();
		prop.load(input);
		long value = Long.parseLong(prop.getProperty(parameter));
		System.out.println("Values picked from property file: " + parameter + " = " + value);
		return value;
	}
	
	public long readParameterFromKeyboard(String parameter){
		Scanner scanner = new Scanner(System.in);
		System.out.print("Enter the " + parameter + " value: ");
		long value =  scanner.nextLong();
		return value;
	}
	
}