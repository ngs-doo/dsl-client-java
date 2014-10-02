package com.dslplatform.client.exceptions;

import java.util.List;
import java.util.Map;

public class HttpSecurityException extends HttpException {
	private final int receivedCode;

	public HttpSecurityException(final String response, final int receivedCode, final Map<String, List<String>> headers) {
		super(response, receivedCode, headers);

		this.receivedCode = receivedCode;
	}

	public int getReceivedCode() {
		return receivedCode;
	}

	private static final long serialVersionUID = 0x0097000a;
}
