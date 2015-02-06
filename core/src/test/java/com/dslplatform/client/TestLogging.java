package com.dslplatform.client;

import com.dslplatform.client.xml.XMLHelpers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

public abstract class TestLogging {
//	private final Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());

	protected long now() {
		return System.currentTimeMillis();
	}

	private void debugInner(final String message) {
//		logger.debug(message);
//		System.out.println(message);
	}

	private void infoInner(final String message) {
//		logger.info(message);
		System.out.println(message);
	}

	protected void debug(final String format, final Object... parts) {
//		if (logger.isDebugEnabled()) {
			debugInner(String.format(format, parts));
//		}
	}

	protected void debug(final Document doc) {
//		if (logger.isInfoEnabled()) {
			debug(XMLHelpers.xmlDocumentToString(doc));
//		}
	}

	protected void info(final String format, final Object... parts) {
//		if (logger.isInfoEnabled()) {
			infoInner(String.format(format, parts));
//		}
	}
}
