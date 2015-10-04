package com.daghan.rgb.led.service;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.kura.configuration.ConfigurableComponent;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jdk.dio.ClosedDeviceException;
import jdk.dio.DeviceConfig;
import jdk.dio.DeviceManager;
import jdk.dio.DeviceNotFoundException;
import jdk.dio.UnavailableDeviceException;
import jdk.dio.gpio.GPIOPin;
import jdk.dio.gpio.GPIOPinConfig;

public class LedDriverComponent implements ConfigurableComponent {
	private static final Logger s_logger = LoggerFactory.getLogger(LedDriverComponent.class);
	private volatile boolean runThread = true;
	GPIOPin redLed, greenLed, blueLed;
	private int discoPeriod;
	private boolean discoEnable;
	// Constants from properties
	private final String DISCO_LIGHTS_PARAM_KEY = "Disco Lights";
	private final String DISCO_LIGHTS_PERIOD_PARAM_KEY = "Period";
	
	
	
	/**
	 * Starts the application
	 * 
	 * @throws IOException
	 * @throws UnavailableDeviceException
	 * @throws DeviceNotFoundException
	 * @throws InterruptedException
	 */
	public void startMeUp(ComponentContext componentContext, Map<String, Object> properties)
			throws IOException, InterruptedException {
		GPIOPinConfig pinConfigRed = new GPIOPinConfig(DeviceConfig.DEFAULT, // GPIO
				13, // GPIO Pin number
				GPIOPinConfig.DIR_OUTPUT_ONLY, // Pin direction
				GPIOPinConfig.MODE_OUTPUT_OPEN_DRAIN, // Pin resistor
				GPIOPinConfig.TRIGGER_BOTH_EDGES, // Triggers
				false // initial value (for outputs)
		);

		GPIOPinConfig pinConfigBlue = new GPIOPinConfig(DeviceConfig.DEFAULT, // GPIO
				19, // GPIO Pin number
				GPIOPinConfig.DIR_OUTPUT_ONLY, // Pin direction
				GPIOPinConfig.MODE_OUTPUT_OPEN_DRAIN, // Pin resistor
				GPIOPinConfig.TRIGGER_BOTH_EDGES, // Triggers
				false // initial value (for outputs)
		);

		GPIOPinConfig pinConfigGreen = new GPIOPinConfig(DeviceConfig.DEFAULT, // GPIO
				26, // GPIO Pin number
				GPIOPinConfig.DIR_OUTPUT_ONLY, // Pin direction
				GPIOPinConfig.MODE_OUTPUT_OPEN_DRAIN, // Pin resistor
				GPIOPinConfig.TRIGGER_BOTH_EDGES, // Triggers
				false // initial value (for outputs)
		);
		redLed = (GPIOPin) DeviceManager.open(GPIOPin.class, pinConfigRed);
		greenLed = (GPIOPin) DeviceManager.open(GPIOPin.class, pinConfigBlue);
		blueLed = (GPIOPin) DeviceManager.open(GPIOPin.class, pinConfigGreen);
		updateMe(properties);
		new DiscoThread().start();
	}

	private void updateMe(Map<String, Object> properties) {
		s_logger.info("Entering config update");
		discoEnable = (boolean) properties.get(DISCO_LIGHTS_PARAM_KEY);
		discoPeriod = (int) properties.get(DISCO_LIGHTS_PERIOD_PARAM_KEY);
		s_logger.info("Exiting config update");
	}

	public void shutMedown(ComponentContext componentContext)
			throws UnavailableDeviceException, ClosedDeviceException, IOException {
		runThread = false;
		redLed.setValue(false);
		greenLed.setValue(false);
		blueLed.setValue(false);
		redLed.close();
		greenLed.close();
		blueLed.close();
		s_logger.info("stopping now");
	}

	private class DiscoThread extends Thread {
		@Override
		public void run() {
			int colorScheme = 0;
			while (runThread) {
				// disco ball!!!!
				try {
					updateColor(colorScheme);
				} catch (IOException | InterruptedException e) {
					break;
				}

				colorScheme += 1;
				if (colorScheme % 3 == 0) {
					colorScheme = 0;
				}
			}
		}

		private void updateColor(int colorScheme)
				throws UnavailableDeviceException, ClosedDeviceException, IOException, InterruptedException {
			switch (colorScheme) {
			case 0:
				redLed.setValue(discoEnable);
				greenLed.setValue(false);
				blueLed.setValue(false);
				break;
			case 1:
				redLed.setValue(false);
				greenLed.setValue(discoEnable);
				blueLed.setValue(false);
				break;
			case 2:
				redLed.setValue(false);
				greenLed.setValue(false);
				blueLed.setValue(discoEnable);
				break;
			}
			Thread.sleep(discoPeriod);
		}
	}
}
