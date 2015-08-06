package com.sksamuel.scapegoat.inspections.string

import com.sksamuel.scapegoat.test.ScapegoatTestPluginRunner
import org.scalatest.{ FreeSpec, Matchers, OneInstancePerTest }

/** @author Stephen Samuel */
class InvalidRegexTest
    extends FreeSpec
    with Matchers
    with ScapegoatTestPluginRunner
    with OneInstancePerTest {

  override val inspections = Seq(new InvalidRegex)

  "invalid regex" - {
    "should report warning" in {

      val code = """object Test {
                      val regex = "?".r
                    } """.stripMargin

      compileCodeSnippet(code)
      compiler.scapegoat.feedback.warnings.size shouldBe 1
    }
  }
}
