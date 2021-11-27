/**
 * Copyright (C) 2011
 *   Michael Mosmann <michael@mosmann.de>
 *   Martin Jöhren <m.joehren@googlemail.com>
 *
 * with contributions from
 * 	konstantin-ba@github,
	Archimedes Trajano (trajano@github),
	Kevin D. Keck (kdkeck@github),
	Ben McCann (benmccann@github)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.flapdoodle.embed.process.io.progress;

import de.flapdoodle.checks.Preconditions;

import java.io.Closeable;
import java.util.Optional;

public class ProgressListeners {

	private static final ThreadLocal<ProgressListener> threadLocal=new ThreadLocal<>();

	private static final ProgressListener NOOP = new ProgressListener() {
		@Override public void progress(String label, int percent) {

		}
		@Override public void done(String label) {

		}
		@Override public void start(String label) {

		}
		@Override public void info(String label, String message) {

		}
	};

	public static Optional<ProgressListener> progressListener() {
		return Optional.ofNullable(threadLocal.get());
	}

	public static RemoveProgressListener setProgressListener(ProgressListener progressListener) {
		Preconditions.checkNotNull(progressListener,"progressListener is null");
		threadLocal.set(progressListener);

		return () -> clearProgressListener();
	}

	public static void clearProgressListener() {
		threadLocal.set(null);
	}
	
	public static ProgressListener noop() {
		return NOOP;
	}

	public interface RemoveProgressListener extends AutoCloseable {
		@Override void close();
	}
}
