/*
 *   JagrKt - JagrKt.org
 *   Copyright (C) 2021 Alexander Staeding
 *   Copyright (C) 2021 Contributors
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.jagrkt.common.rubric.grader

import org.jagrkt.api.rubric.GradeResult
import org.jagrkt.api.rubric.Grader
import org.jagrkt.api.rubric.JUnitTestRef

class TestAwareGraderBuilderImpl : AbstractGraderBuilder<Grader.TestAwareBuilder>(), Grader.TestAwareBuilder {

  override fun getThis(): Grader.TestAwareBuilder = this

  private val requirePass: MutableMap<JUnitTestRef, String?> = mutableMapOf()
  private val requireFail: MutableMap<JUnitTestRef, String?> = mutableMapOf()

  override fun requirePass(testRef: JUnitTestRef, comment: String?): Grader.TestAwareBuilder {
    requirePass[testRef] = comment
    return this
  }

  override fun requireFail(testRef: JUnitTestRef, comment: String?): Grader.TestAwareBuilder {
    requireFail[testRef] = comment
    return this
  }

  override fun build(): Grader {
    return TestAwareGraderImpl(
      predicate,
      pointCalculatorPassed ?: { _, _ -> GradeResult.ofNone() },
      pointCalculatorFailed ?: { _, _ -> GradeResult.ofNone() },
      requirePass.mapKeys { it.key.testSource },
      requireFail.mapKeys { it.key.testSource },
      commentIfFailed,
    )
  }
}
