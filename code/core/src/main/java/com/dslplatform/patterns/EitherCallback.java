package com.dslplatform.patterns;

public interface EitherCallback<T> {
	void onCompleted(Either<T> result);
}
