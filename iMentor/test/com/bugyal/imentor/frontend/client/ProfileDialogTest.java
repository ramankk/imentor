package com.bugyal.imentor.frontend.client;

import org.junit.Test;

import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.ui.TextArea;

public class ProfileDialogTest extends GWTTestCase {

	@Override
	public String getModuleName() {
		return "com.bugyal.imentor.frontend.client.ProfileDialog";
	}

	@Test
	public void checkDisplay() {
		ProfileDialogBox profileDialogBox = new ProfileDialogBox("test1@k.com",
				"test1");
		@SuppressWarnings("unused")
		TextArea location = profileDialogBox.tbLocation;
		//
		// TextArea t1= profileDialogBox.textArea;
		// SuggestBox sugBox1 =profileDialogBox.suggestBox_1;
		// sugBox1.setText("tel");
		// sugBox1.fireEvent(KeyEvent.VK_ENTER);
		// assertEquals(sugBox1.getText(),"TELUGU");

	}

}
