package com.dslplatform.client.exceptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpUnexpectedCodeException extends HttpException {
	private final List<Integer> expectedCode = new ArrayList<Integer>(2);

	public HttpUnexpectedCodeException(
			final String response,
			final int[] expectedCode,
			final int receivedCode,
			final Map<String, List<String>> headers) {
		super(response, receivedCode, headers);

		if (expectedCode != null) {
			for (final int ec : expectedCode) {
				this.expectedCode.add(ec);
			}
		}
	}

	public List<Integer> getExpectedCode() {
		return expectedCode;
	}

	private static final long serialVersionUID = 0x0097000a;
}
