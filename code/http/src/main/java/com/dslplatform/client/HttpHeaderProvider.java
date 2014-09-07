package com.dslplatform.client;

import java.util.List;
import java.util.Map;

public interface HttpHeaderProvider {
	public List<Map.Entry<String, String>> getHeaders();
}
