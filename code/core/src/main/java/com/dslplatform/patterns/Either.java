package com.dslplatform.patterns;

public class Either<T> {
	private final T value;
	private final Throwable error;

	private Either(T value, Throwable error) {
		this.value = value;
		this.error = error;
	}

	public boolean isSuccess() { return  error == null;	}
	public T get() { return value; }
	public Throwable whyNot() { return error; }

	public static <T> Either<T> success(final T value) {
		return new Either<T>(value, null);
	}

	public static <T> Either<T> fail(final Throwable error) {
		return new Either<T>(null, error);
	}

	public static <T> Either<T> fail(final String error) {
		return new Either<T>(null, new Throwable(error));
	}
}
