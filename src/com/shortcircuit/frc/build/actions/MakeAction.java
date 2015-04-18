package com.shortcircuit.frc.build.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataConstants;
import com.intellij.openapi.module.Module;
import com.shortcircuit.frc.build.build.BuildTask;
import com.shortcircuit.frc.build.ui.ConfigDialog;
import com.shortcircuit.frc.build.ui.ErrorDialog;
import com.shortcircuit.frc.build.utils.Startup;

/**
 * @author ShortCircuit908
 */
public class MakeAction extends AnAction {
	public void actionPerformed(AnActionEvent event) {
		try {
			boolean config_missing = !ConfigDialog.CONFIG.isValueSet("com.shortcircuit.frc.build:FTP_USER");
			config_missing |= !ConfigDialog.CONFIG.isValueSet("com.shortcircuit.frc.build:FTP_PORT");
			config_missing |= !ConfigDialog.CONFIG.isValueSet("com.shortcircuit.frc.build:FTP_HOST");
			if (config_missing) {
				new ErrorDialog("Configuration Error", "Please check the plugin configuration", null, false);
			}
			else {
				Module module = (Module) event.getDataContext().getData(DataConstants.MODULE);
				if (module != null) {
					new BuildTask(module);
				}
				else {
					new ErrorDialog("No Module", "Please select a module to build", null, false);
				}
			}
		}
		catch(Throwable e){
			Startup.EXCEPTION_HANDLER.uncaughtException(null, e);
		}
	}
}
