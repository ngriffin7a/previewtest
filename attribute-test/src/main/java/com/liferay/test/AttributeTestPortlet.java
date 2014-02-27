package com.liferay.test;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.theme.PortletDisplay;
import com.liferay.portal.theme.ThemeDisplay;

public class AttributeTestPortlet extends GenericPortlet {

	private static final String CLIENT_WINDOW_COUNTER_KEY = "com.liferay.faces.bridge.lifecycle.CLIENT_WINDOW_COUNTER_KEY";

	@Override
	protected void doView(RenderRequest renderRequest,
			RenderResponse renderResponse) throws PortletException, IOException {

		PrintWriter writer = renderResponse.getWriter();
		writer.write("<span>Client Window ID " + getId(renderRequest)
				+ "</span>");
	}

	public String getId(PortletRequest portletRequest) {

		String id = null;

		String portletNamespace = StringPool.BLANK;
		ThemeDisplay themeDisplay = (ThemeDisplay) portletRequest
				.getAttribute(WebKeys.THEME_DISPLAY);

		if (themeDisplay != null) {
			PortletDisplay portletDisplay = themeDisplay.getPortletDisplay();
			portletNamespace = portletDisplay.getNamespace();
		}

		char separatorChar = ':';
		String clientWindowCounterKey = CLIENT_WINDOW_COUNTER_KEY
				+ separatorChar + portletNamespace;
		PortletSession portletSession = portletRequest.getPortletSession(true);
		String sessionId = portletSession.getId();

		synchronized (portletSession) {
			Integer clientWindowCounter = (Integer) portletSession
					.getAttribute(clientWindowCounterKey);

			if (clientWindowCounter == null) {
				clientWindowCounter = new Integer(0);
			}

			id = sessionId + separatorChar + portletNamespace + separatorChar
					+ clientWindowCounter;
			portletSession.setAttribute(clientWindowCounterKey,
					++clientWindowCounter);
		}
		System.err
				.println("!@#$ GETTING VALUE AGAIN FROM SESSION AFTER PUTTING portletSession="
						+ portletSession
						+ " value="
						+ portletSession.getAttribute(clientWindowCounterKey));

		return id;
	}
}
