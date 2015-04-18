package com.shortcircuit.frc.build.ui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.ui.Gray;
import com.shortcircuit.frc.build.utils.Encrypt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SpinnerNumberModel;

public class ConfigDialog extends JDialog {
	public static final Gson GSON = new GsonBuilder().serializeNulls().disableHtmlEscaping().create();
	private JPanel contentPane;
	private JButton buttonOK;
	private JButton buttonCancel;
	private JTextField text_username;
	private JPasswordField text_password;
	private JTextField text_host;
	private JSpinner spinner_port;
	public static final PropertiesComponent CONFIG = PropertiesComponent.getInstance();

	public ConfigDialog() {
		setContentPane(contentPane);
		setModal(true);
		getRootPane().setDefaultButton(buttonOK);

		buttonOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onOK();
			}
		});

		buttonCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onCancel();
			}
		});

		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				onCancel();
			}
		});

		text_password.setEchoChar('*');

		spinner_port.setModel(new SpinnerNumberModel(22, 0, 9999, 1));
		text_host.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent event) {
				text_host.setForeground(Gray._187);
			}

			@Override
			public void focusLost(FocusEvent event) {
				text_host.setText(text_host.getText().trim());
				try {
					new URL(text_host.getText());
				}
				catch (MalformedURLException e) {
					new ErrorDialog("Malformed URL", "Please enter a valid URL", null, false);
				}
				if (text_host.getText().equalsIgnoreCase("http://")) {
					text_host.setForeground(Gray._137);
				}
			}
		});
		contentPane.registerKeyboardAction(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onCancel();
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		setResizable(false);
		pack();
		String temp_user = CONFIG.getValue("com.shortcircuit.frc.build:FTP_USER");
		if (temp_user == null || temp_user.trim().isEmpty()) {
			temp_user = "";
		}
		String temp_pass = Encrypt.decrypt(GSON.fromJson(CONFIG.getValue("com.shortcircuit.frc.build:FTP_PASS"), byte[].class));
		if (temp_pass == null || temp_pass.trim().isEmpty()) {
			temp_pass = "";
		}
		String temp_host = CONFIG.getValue("com.shortcircuit.frc.build:FTP_HOST");
		if (temp_host == null || temp_host.trim().isEmpty()) {
			temp_host = "";
		}
		String temp_port = CONFIG.getValue("com.shortcircuit.frc.build:FTP_PORT");
		if (temp_port == null || temp_port.trim().isEmpty()) {
			temp_port = "22";
		}
		text_username.setText(temp_user);
		text_password.setText(temp_pass);
		text_host.setText(temp_host);
		spinner_port.setValue(Integer.parseInt(temp_port));
		setLocationByPlatform(true);
		setVisible(true);
	}

	private void onOK() {
		CONFIG.setValue("com.shortcircuit.frc.build:FTP_USER", text_username.getText());
		CONFIG.setValue("com.shortcircuit.frc.build:FTP_PASS", GSON.toJson(Encrypt.encrypt(new String(text_password.getPassword()))));
		CONFIG.setValue("com.shortcircuit.frc.build:FTP_HOST", text_host.getText());
		CONFIG.setValue("com.shortcircuit.frc.build:FTP_PORT", spinner_port.getValue() + "");
		dispose();
	}

	private void onCancel() {
		//Config.saveConfig();
		dispose();
	}
}
