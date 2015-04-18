package com.shortcircuit.frc.build.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.shortcircuit.frc.build.utils.Startup;
import com.shortcircuit.frc.build.ui.ConfigDialog;

/**
 * @author ShortCircuit908
 */
public class ConfigureAction extends AnAction {
	public void actionPerformed(AnActionEvent event) {
		try {
			new ConfigDialog();
		}
		catch(Throwable e){
			Startup.EXCEPTION_HANDLER.uncaughtException(null, e);
		}
	}
}
