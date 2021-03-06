/*
 *   Jagr - SourceGrade.org
 *   Copyright (C) 2021-2022 Alexander Staeding
 *   Copyright (C) 2021-2022 Contributors
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.sourcegrade.jagr.api.testing;

import org.jetbrains.annotations.ApiStatus;

/**
 * Represents the contents of a submission-info.json file
 */
@ApiStatus.NonExtendable
public interface SubmissionInfo {

    /**
     * The assignment id for which this submission was created.
     *
     * @return The assignment id for which this submission was created
     */
    String getAssignmentId();

    /**
     * The student id of the student who submitted this submission.
     *
     * @return The student id of the student who submitted this submission
     */
    String getStudentId();

    /**
     * The first name of the student who submitted this submission.
     *
     * @return The first name of the student who submitted this submission
     */
    String getFirstName();

    /**
     * The last name of the student who submitted this submission.
     *
     * @return The last name of the student who submitted this submission
     */
    String getLastName();
}
