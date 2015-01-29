package com.dslplatform.patterns;

import org.joda.time.DateTime;

/**
 * Snapshot of some past state of an {@link AggregateRoot aggregate root}
 *
 * @param <T> type of aggregate root
 */
public class Snapshot<T extends AggregateRoot> {
	private final DateTime at;
	private final String action;
	private final T value;

	@SuppressWarnings("unused")
	private Snapshot() {
		this(null, null, null);
	}

	public Snapshot(
			final DateTime at,
			final String action,
			final T value) {
		this.at = at;
		this.action = action;
		this.value = value;
	}

	/**
	 * Date and time when snapshot was created.
	 *
	 * @return DateTime of snapshot
	 */
	public DateTime getAt() {
		return at;
	}

	/**
	 * Which action was performed (INSERT|UPDATE|DELETE)
	 *
	 * @return Action type
	 */
	public String getAction() {
		return action;
	}

	/**
	 * Instance of an aggregate root at that time
	 *
	 * @return aggregate root snapshot
	 */
	public T getValue() {
		return value;
	}
}
