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
package de.flapdoodle.embed.process.distribution;

import de.flapdoodle.embed.process.extract.ArchiveIsFileExtractor;
import de.flapdoodle.embed.process.extract.Extractor;
import de.flapdoodle.embed.process.extract.Tbz2Extractor;
import de.flapdoodle.embed.process.extract.TgzExtractor;
import de.flapdoodle.embed.process.extract.TxzExtractor;
import de.flapdoodle.embed.process.extract.ZipExtractor;

/**
 *
 */
public enum ArchiveType {
	TGZ(new TgzExtractor()),
	TBZ2(new Tbz2Extractor()),
	ZIP(new ZipExtractor()),
	EXE(new ArchiveIsFileExtractor()),
	TXZ(new TxzExtractor());

	private final Extractor extractor;

	ArchiveType(Extractor extractor) {
		this.extractor = extractor;
	}

	public Extractor getExtractor() {
		return extractor;
	}
}
