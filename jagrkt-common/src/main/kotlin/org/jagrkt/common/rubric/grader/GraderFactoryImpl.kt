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

import com.google.inject.Inject
import org.jagrkt.api.inspect.CodeContext
import org.jagrkt.api.inspect.ContextResolver
import org.jagrkt.api.rubric.Grader
import org.slf4j.Logger

class GraderFactoryImpl @Inject constructor(
  private val logger: Logger,
) : Grader.Factory {
  override fun testAwareBuilder() = TestAwareGraderBuilderImpl()
  override fun <C : CodeContext> contextAwareBuilder(resolver: ContextResolver<C>): Grader.ContextAwareBuilder<C> = ContextAwareGraderBuilderImpl(resolver)
  override fun descendingPriority(vararg graders: Grader) = DescendingPriorityGrader(logger, *graders)
  override fun minIfAllUnchanged(vararg contexts: ContextResolver<*>): Grader = MinIfAllUnchangedGrader(*contexts)
}
