package googleweather.threads;

import java.io.IOException;
import java.util.Date;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import processing.core.PApplet;
import processing.data.XML;

/**
 * Java thread for loading googles geocode data concurrently.
 * 
 * @author Marcel Schwittlick
 * @date 27.03.2012
 * 
 */
public class GeocodeXMLThread extends Thread {

	private int updateIntervallMilis;
	private boolean running;
	private PApplet parentApplet;
	private String googleGeocodeLink;
	private Date lastUpdate;

	XML mainXML;

	public GeocodeXMLThread(int updateIntervallSeconds, PApplet parent,
			String googleGeocodeLink) {
		this.updateIntervallMilis = updateIntervallSeconds * 1000;
		this.parentApplet = parent;
		this.googleGeocodeLink = googleGeocodeLink;
		running = false;
	}

	public void start() {
		running = true;
		PApplet.println("Geocode updated every " + updateIntervallMilis / 1000
				+ "s.");
		setMainXML();
		super.start();
	}

	public void run() {
		while (running) {
			setMainXML();
			try {
				sleep(updateIntervallMilis);
			} catch (Exception e) {
				PApplet.println(e);
			}
		}
	}

	public void quit() {
		PApplet.println("Thread quit.");
		running = false;
		interrupt();
	}

	/**
	 * @return mainXML the main xml file from google
	 */
	public XML getMainXML() {
		return mainXML;
	}

	/**
	 * 
	 * @param googleWeatherLink
	 *            link to xml file
	 */
	public void setGoogleGeocodeLink(String googleGeocodeLink) {
		this.googleGeocodeLink = googleGeocodeLink;
		setMainXML();
	}

	/**
	 * 
	 * @param intervall
	 */
	public void setUpdateIntervallInSeconds(int intervall) {
		updateIntervallMilis = intervall * 1000;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	/**
	 * downloads the main xml file
	 */
	private void setMainXML() throws NullPointerException {
		try {
			// mainXML = parentApplet.loadXML(googleGeocodeLink);
			mainXML = new XML(parentApplet, googleGeocodeLink);
			// mainXML = new XMLElement(parentApplet, googleGeocodeLink);
		} catch (NullPointerException n) {
			PApplet.println(n + " not available");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		lastUpdate = new Date();
	}
}
