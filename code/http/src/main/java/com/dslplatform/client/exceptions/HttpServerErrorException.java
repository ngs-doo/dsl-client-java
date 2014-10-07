package com.dslplatform.client.exceptions;

import java.util.List;
import java.util.Map;

public class HttpServerErrorException extends HttpException {
	public HttpServerErrorException(final String response, final int receivedCode, final Map<String, List<String>> headers) {
		super(response, receivedCode, headers);
	}

	private static final long serialVersionUID = 0x0097000a;
}
