package com.roosi.utils;

import com.roosi.utils.net.Network;
import com.roosi.utils.net.PerformRequestServiceImp;

import android.app.Application;

public class UtilsApplication extends Application {

	public UtilsApplication() {
		final Network net = Network.getInstance();
		net.setPerformRequestService(new PerformRequestServiceImp());
	}

}
