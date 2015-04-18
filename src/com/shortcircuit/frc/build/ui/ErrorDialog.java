package com.shortcircuit.frc.build.ui;

import com.google.common.net.UrlEscapers;
import com.shortcircuit.frc.build.FRC_Build;
import com.shortcircuit.frc.build.utils.Email;

import java.awt.Desktop;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URI;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

public class ErrorDialog extends JDialog {
	private static final String MAILTO_START = "<a href=\"mailto:" + Email.APP_EMAIL + "&subject=Error%20report&body=";
	private static final String MAILTO_END = "\">PIrAtEs@comcast.net</a>";
	private JPanel content_panel;
	private JButton button_ok;
	private javax.swing.JTextPane message_label;
	private javax.swing.JTextArea err_pane;
	private JButton sendErrorReportButton;
	private JPanel error_button_panel;
	private JScrollPane err_panel;
	private final Throwable throwable;

	public ErrorDialog(String title, String message, String body, Throwable throwable, boolean show_report_button) {
		this.throwable = throwable;
		setResizable(false);
		err_pane.setText(body == null ? "" : body + "\n");
		try {
			setIconImage(ImageIO.read(FRC_Build.class.getResourceAsStream("/error_icon.png")));
		}
		catch (Throwable e) {
			e.printStackTrace();
		}
		setTitle(title);
		message_label.setFont(new Font("dialog", Font.BOLD, 12));
		message_label.setHighlighter(null);
		message_label.setText(message);
		if (throwable != null) {
			err_pane.append(getStackTrace(throwable));
		}
		else {
			err_panel.setEnabled(false);
			err_pane.setEnabled(false);
			err_panel.setVisible(false);
			err_pane.setVisible(false);
		}
		button_ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onOK();
			}
		});
		if (!show_report_button) {
			sendErrorReportButton.setVisible(false);
			sendErrorReportButton.setEnabled(false);
			error_button_panel.setEnabled(false);
			error_button_panel.setVisible(false);
		}
		HyperlinkListener link_listener = new HyperlinkListener() {
			@Override
			public void hyperlinkUpdate(HyperlinkEvent event) {
				if (event.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED)) {
					try {
						if (event.getURL().getProtocol().equalsIgnoreCase("mailto")) {
							Desktop.getDesktop().mail(new URI(event.getURL().toString()));
						}
						else if (event.getURL().getProtocol().equalsIgnoreCase("file")) {
							Desktop.getDesktop().open(new File(event.getURL().getFile()));
						}
						else {
							Desktop.getDesktop().browse(new URI(event.getURL().toString()));
						}
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		};
		message_label.addHyperlinkListener(link_listener);
		setContentPane(content_panel);
		setModal(true);
		getRootPane().setDefaultButton(button_ok);
		sendErrorReportButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				sendErrorReportButton.setEnabled(false);
				try {
					Email.sendMessage("Error report", err_pane.getText());
				}
				catch (Throwable e) {
					new ErrorDialog("Wow. Such error. Very issue. Many problem.",
							"An error occurred while reporting the previous error<br>Please contact " +
									"the vendor directly at " + MAILTO_START +
									UrlEscapers.urlFragmentEscaper().escape(err_pane.getText())
									+ MAILTO_END + " with the information below",
							e,
							false);
				}
			}
		});
		message_label.setText(message_label.getText().trim());
		pack();
		setLocationByPlatform(true);
		setVisible(true);
	}

	public ErrorDialog(String title, String message, Throwable throwable, boolean show_report_button) {
		this(title, message, null, throwable, show_report_button);
	}

	private void onOK() {
		dispose();
	}

	public static String getStackTrace(Throwable e) {
		String stack = "";
		Throwable current_stack = e;
		boolean first_stack = true;
		do {
			stack += (!first_stack ? "Caused by: " : "") + e.getClass().getCanonicalName() + ": "
					+ e.getLocalizedMessage() + "\n";
			for (StackTraceElement element : current_stack.getStackTrace()) {
				stack += "\tat " + element.toString() + "\n";
			}
			current_stack = current_stack.getCause();
			first_stack = false;
		}
		while (current_stack != null);
		return stack;
	}
}
