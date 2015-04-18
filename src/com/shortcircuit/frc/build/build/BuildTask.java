package com.shortcircuit.frc.build.build;

import com.intellij.openapi.compiler.CompileContext;
import com.intellij.openapi.compiler.CompileStatusNotification;
import com.intellij.openapi.compiler.CompilerManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.packaging.artifacts.Artifact;
import com.intellij.packaging.artifacts.ArtifactManager;
import com.shortcircuit.frc.build.ui.ConfigDialog;
import com.shortcircuit.frc.build.utils.Encrypt;
import com.shortcircuit.frc.build.utils.Startup;

import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileInputStream;

/**
 * @author ShortCircuit908
 */
public class BuildTask {
	private final Project project;
	private final Module module;
	private Artifact artifact;

	public BuildTask(Module module) throws Exception {
		this.module = module;
		this.project = module.getProject();
		for (Artifact artifact : ArtifactManager.getInstance(project).getArtifacts()) {
			if (artifact.getRootElement().getName().split("\\.")[0].equalsIgnoreCase(module.getName())) {
				this.artifact = artifact;
				break;
			}
		}
		if (artifact == null) {
			throw new IllegalArgumentException("The selected module has no artifacts");
		}
		build();
	}

	private void build() {
		try {
			CompileStatusNotification x = new CompileStatusNotification() {
				@Override
				public void finished(boolean aborted, int errors, int warnings, CompileContext context) {
					if (!aborted) {
						copyJar(new File(artifact.getOutputFilePath()));
					}
				}
			};
			CompilerManager.getInstance(project).compile(module, x);
		}
		catch (Throwable e) {
			Startup.EXCEPTION_HANDLER.uncaughtException(null, e);
		}
	}

	private void copyJar(File source) {
		try {
			FTPClient client = new FTPClient();
			String password = Encrypt.decrypt(ConfigDialog.GSON.fromJson(
					ConfigDialog.CONFIG.getValue("com.shortcircuit.frc.build:FTP_PASS"), byte[].class));
			if (password == null || password.trim().isEmpty()) {
				password = "";
			}
			client.connect(ConfigDialog.CONFIG.getValue("com.shortcircuit.frc.build:FTP_HOST"),
					ConfigDialog.CONFIG.getOrInitInt("com.shortcircuit.frc.build:FTP_PORT", 22));
			client.login(ConfigDialog.CONFIG.getValue("com.shortcircuit.frc.build:FTP_USER"), password);
			FileInputStream in = new FileInputStream(source);
			client.storeFile("/home/" + ConfigDialog.CONFIG.getValue("com.shortcircuit.frc.build:FTP_USER")
					+ "/FRCUserProgram.jar", in);
		}
		catch (Throwable e) {
			Startup.EXCEPTION_HANDLER.uncaughtException(null, e);
		}
	}
}
