package com.dslplatform.client.exceptions;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class HttpException extends IOException {
	private final int receivedCode;
	private final Map<String, List<String>> headers;

	public HttpException(final String response, final int receivedCode, final Map<String, List<String>> headers) {
		super(response);

		this.receivedCode = receivedCode;
		this.headers = headers;
	}

	public int getReceivedCode() {
		return receivedCode;
	}
	public Map<String, List<String>> getHeaders() {
		return headers;
	}

	private static final long serialVersionUID = 0x0097000a;
}
