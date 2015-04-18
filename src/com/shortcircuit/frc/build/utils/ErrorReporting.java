package com.shortcircuit.frc.build.utils;

import com.shortcircuit.frc.build.ui.ErrorDialog;

/**
 * @author ShortCircuit908
 */
public class ErrorReporting implements Thread.UncaughtExceptionHandler {
	@Override
	public void uncaughtException(Thread t, Throwable e) {
		new ErrorDialog("Error", "An error has occurred", e, true);
	}
}
