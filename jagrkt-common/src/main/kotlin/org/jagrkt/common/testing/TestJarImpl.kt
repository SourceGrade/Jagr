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

package org.jagrkt.common.testing

import com.google.common.base.MoreObjects
import org.jagrkt.api.rubric.RubricForSubmission
import org.jagrkt.api.rubric.RubricProvider
import org.jagrkt.api.rubric.TestForSubmission
import org.jagrkt.api.testing.CompileResult
import org.jagrkt.api.testing.JavaCompiledProgram
import org.jagrkt.api.testing.SourceFile
import org.jagrkt.api.testing.TestJar
import org.jagrkt.common.compiler.java.CompiledClass
import org.jagrkt.common.compiler.java.JavaCompileResult
import org.jagrkt.common.compiler.java.RuntimeClassLoader
import org.slf4j.Logger
import spoon.reflect.CtModel

class TestJarImpl(
  private val logger: Logger,
  private val compileResult: JavaCompileResult,
  solutionClasses: Map<String, CompiledClass>,
) : TestJar, JavaCompiledProgram {

  val file = compileResult.file

  val name: String = with(file.name) { substring(0, indexOf(".jar")) }

  /**
   * A map of assignments ids to classes of rubric providers (in the base classloader).
   *
   * Classes in this map are guaranteed to have an accessible no-args constructor.
   */
  val rubricProviders: Map<String, List<String>>

  /**
   * A map of assignment ids to JUnit test classes
   */
  val testProviders: Map<String, List<String>>

  init {
    val rubricProviders: MutableMap<String, MutableList<String>> = mutableMapOf()
    val testProviders: MutableMap<String, MutableList<String>> = mutableMapOf()
    val compiledClasses = compileResult.compiledClasses
    val baseClassLoader = RuntimeClassLoader(compiledClasses + solutionClasses)
    for (className in compiledClasses.keys) {
      val clazz = baseClassLoader.loadClass(className)
      rubricProviders.putIfRubric(clazz)
      testProviders.putIfTest(clazz)
    }
    this.rubricProviders = rubricProviders
    this.testProviders = testProviders
  }

  override fun getCompileResult() = compileResult

  override fun getSourceFile(fileName: String?): SourceFile? {
    TODO("Not yet implemented")
  }

  override fun getModel(): CtModel {
    TODO("Not yet implemented")
  }

  private fun MutableMap<String, MutableList<String>>.putIfRubric(clazz: Class<*>) {
    val filter = clazz.getAnnotation(RubricForSubmission::class.java) ?: return
    val asRubricProvider = try {
      clazz.asSubclass(RubricProvider::class.java)
    } catch (e: ClassCastException) {
      logger.error("Class annotated with @RubricForSubmission does not implement RubricProvider! Ignoring...")
      return
    }
    val assignmentId = filter.value
    val className = clazz.name
    try {
      clazz.getConstructor()
    } catch (e: NoSuchMethodException) {
      logger.error("Rubric provider $className in file $file must have a no-args public constructor!")
      return
    }
    logger.info("${file.name} Discovered rubric provider $className for assignment $assignmentId")
    computeIfAbsent(filter.value) { mutableListOf() }.add(asRubricProvider.name)
  }

  private fun MutableMap<String, MutableList<String>>.putIfTest(clazz: Class<*>) {
    val filter = clazz.getAnnotation(TestForSubmission::class.java) ?: return
    computeIfAbsent(filter.value) { mutableListOf() }.add(clazz.name)
  }

  private val stringRep: String by lazy {
    MoreObjects.toStringHelper(this)
      .add("file", file)
      .add("applicableSubmissions", rubricProviders.keys.joinToString(", "))
      .toString()
  }

  override fun toString(): String = stringRep
}
