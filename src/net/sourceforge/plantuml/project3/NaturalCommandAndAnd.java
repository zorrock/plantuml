/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * http://plantuml.com/patreon (only 1$ per month!)
 * http://plantuml.com/paypal
 * 
 * This file is part of PlantUML.
 *
 * PlantUML is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PlantUML distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 *
 * Original Author:  Arnaud Roques
 * 
 *
 */
package net.sourceforge.plantuml.project3;

import net.sourceforge.plantuml.LineLocation;
import net.sourceforge.plantuml.command.Command;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexResult;

public class NaturalCommandAndAnd extends SingleLineCommand2<GanttDiagram> {

	private final SubjectPattern subjectPattern;
	private final VerbPattern verbPattern1;
	private final ComplementPattern complementPattern1;
	private final VerbPattern verbPattern2;
	private final ComplementPattern complementPattern2;
	private final VerbPattern verbPattern3;
	private final ComplementPattern complementPattern3;

	private NaturalCommandAndAnd(RegexConcat pattern, SubjectPattern subject, VerbPattern verb1,
			ComplementPattern complement1, VerbPattern verb2, ComplementPattern complement2, VerbPattern verb3,
			ComplementPattern complement3) {
		super(pattern);
		this.subjectPattern = subject;
		this.verbPattern1 = verb1;
		this.complementPattern1 = complement1;
		this.verbPattern2 = verb2;
		this.complementPattern2 = complement2;
		this.verbPattern3 = verb3;
		this.complementPattern3 = complement3;
	}

	@Override
	public String toString() {
		return subjectPattern.toString() + " " + verbPattern1.toString() + " " + complementPattern1.toString()
				+ " and " + verbPattern2.toString() + " " + complementPattern2.toString() + " and "
				+ verbPattern3.toString() + " " + complementPattern3.toString();
	}

	@Override
	protected CommandExecutionResult executeArg(GanttDiagram system, LineLocation location, RegexResult arg) {
		final Subject subject = subjectPattern.getSubject(system, arg);
		final Verb verb1 = verbPattern1.getVerb(system, arg);
		final Complement complement1 = complementPattern1.getComplement(system, arg, "1").get();
		final CommandExecutionResult result1 = verb1.execute(subject, complement1);
		if (result1.isOk() == false) {
			return result1;
		}
		final Verb verb2 = verbPattern2.getVerb(system, arg);
		final Complement complement2 = complementPattern2.getComplement(system, arg, "2").get();
		final CommandExecutionResult result2 = verb2.execute(subject, complement2);
		if (result2.isOk() == false) {
			return result2;
		}
		final Verb verb3 = verbPattern3.getVerb(system, arg);
		final Complement complement3 = complementPattern3.getComplement(system, arg, "3").get();
		return verb3.execute(subject, complement3);
	}

	public static Command create(SubjectPattern subject, VerbPattern verb1, ComplementPattern complement1,
			VerbPattern verb2, ComplementPattern complement2, VerbPattern verb3, ComplementPattern complement3) {
		final RegexConcat pattern = new RegexConcat(//
				new RegexLeaf("^"), //
				subject.toRegex(), //
				new RegexLeaf("[%s]+"), //
				verb1.toRegex(), //
				new RegexLeaf("[%s]+"), //
				complement1.toRegex("1"), //
				new RegexLeaf("[%s]+"), //
				new RegexLeaf("and"), //
				new RegexLeaf("[%s]+"), //
				verb2.toRegex(), //
				new RegexLeaf("[%s]+"), //
				complement2.toRegex("2"), //
				new RegexLeaf("[%s]+"), //
				new RegexLeaf("and"), //
				new RegexLeaf("[%s]+"), //
				verb3.toRegex(), //
				new RegexLeaf("[%s]+"), //
				complement3.toRegex("3"), //
				new RegexLeaf("$"));
		return new NaturalCommandAndAnd(pattern, subject, verb1, complement1, verb2, complement2, verb3, complement3);
	}
}
