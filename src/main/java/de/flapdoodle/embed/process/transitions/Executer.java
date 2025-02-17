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
package de.flapdoodle.embed.process.transitions;

import de.flapdoodle.embed.process.archives.ExtractedFileSet;
import de.flapdoodle.embed.process.config.SupportConfig;
import de.flapdoodle.embed.process.io.ProcessOutput;
import de.flapdoodle.embed.process.types.*;
import de.flapdoodle.reverse.State;
import de.flapdoodle.reverse.StateID;
import de.flapdoodle.reverse.StateLookup;
import de.flapdoodle.reverse.naming.HasLabel;
import org.immutables.builder.Builder;
import org.immutables.value.Value;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Value.Immutable
public abstract class Executer<R extends RunningProcess, T extends ExecutedProcess> extends RunAProcess<R, T> implements HasLabel {

	@Override
	@Value.Default
	public String transitionLabel() {
		return "Execute";
	}

	@Value.Default
	public StateID<ExtractedFileSet> processExecutable() {
		return StateID.of(ExtractedFileSet.class);
	}

	@Value.Default
	public StateID<ProcessConfig> processConfig() {
		return StateID.of(ProcessConfig.class);
	}

	@Value.Default
	public StateID<ProcessEnv> processEnv() {
		return StateID.of(ProcessEnv.class);
	}

	@Value.Default
	public StateID<ProcessArguments> arguments() {
		return StateID.of(ProcessArguments.class);
	}

	@Value.Default
	public StateID<ProcessOutput> processOutput() {
		return StateID.of(ProcessOutput.class);
	}

	@Value.Default
	public StateID<SupportConfig> supportConfig() {
		return StateID.of(SupportConfig.class);
	}

	@Override
	public abstract StateID<T> destination();

	@Builder.Parameter
	protected abstract RunningProcessFactory<R> runningProcessFactory();

	@Builder.Parameter
	protected abstract ExecutedProcessFactory<R, T> executedProcessFactory();

	@Override
	public Set<StateID<?>> sources() {
		return StateID.setOf(
			processWorkingDir(),
			processExecutable(),
			processConfig(),
			processEnv(),
			arguments(),
			processOutput(),
			supportConfig()
		);
	}

	@Override
	public State<T> result(StateLookup lookup) {
		Path processWorkingDir = lookup.of(processWorkingDir()).value();
		ExtractedFileSet fileSet = lookup.of(processExecutable());
		List<String> arguments = lookup.of(arguments()).value();
		Map<String, String> environment = lookup.of(processEnv()).value();
		ProcessConfig processConfig = lookup.of(processConfig());
		ProcessOutput processOutput = lookup.of(processOutput());
		SupportConfig supportConfig = lookup.of(supportConfig());

		try {
			R running = RunningProcess.start(runningProcessFactory(), processWorkingDir, fileSet.executable(), arguments, environment, processConfig, processOutput, supportConfig);
			T executedProcess = executedProcessFactory().stop(running);
			return State.of(executedProcess);
		}
		catch (IOException ix) {
			throw new RuntimeException("could not execute process", ix);
		}
	}

	public static <R extends RunningProcess, T extends ExecutedProcess> ImmutableExecuter.Builder<R, T> with(
		RunningProcessFactory<R> runningProcessFactory, ExecutedProcessFactory<R, T> executedProcessFactory) {
		return ImmutableExecuter.builder(runningProcessFactory, executedProcessFactory);
	}

	public static ImmutableExecuter<RunningProcessImpl, ExecutedProcess> withDefaults() {
		return with(RunningProcessImpl::new, ExecutedProcessImpl::stop)
			.destination(StateID.of(ExecutedProcess.class))
			.build();
	}
}
