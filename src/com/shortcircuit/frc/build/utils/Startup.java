package com.shortcircuit.frc.build.utils;

import com.intellij.openapi.components.ApplicationComponent;
import com.shortcircuit.frc.build.utils.ErrorReporting;

import org.jetbrains.annotations.NotNull;

/**
 * @author ShortCircuit908
 */
public class Startup implements ApplicationComponent {
	public static final Thread.UncaughtExceptionHandler EXCEPTION_HANDLER = new ErrorReporting();
	public Startup() {

	}

	public void initComponent() {
		Thread.currentThread().setUncaughtExceptionHandler(EXCEPTION_HANDLER);
	}

	public void disposeComponent() {
		// TODO: insert component disposal logic here
	}

	@NotNull
	public String getComponentName() {
		return "Startup";
	}
}
