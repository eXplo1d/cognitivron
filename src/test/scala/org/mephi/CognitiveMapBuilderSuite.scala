package org.mephi

import akka.actor.ActorSystem
import org.mephi.cm.CognitiveMapBuilder
import org.scalatest.funsuite.AnyFunSuite

class CognitiveMapBuilderSuite extends AnyFunSuite {
  private val actorSystem: ActorSystem = ActorSystem("cm")

  test("CognitiveMapBuilder should build 2 concept map with specified calc states") {
    val cm = CognitiveMapBuilder(actorSystem)
      .withConcept("x", calc => calc.withConst(4))
      .withConcept("y", calc => calc.withFactor("x", 8))
      .withLink("x", "y", 0.5)
      .build()
    val data = cm.makeIterations(1)
    val actual = data.getOrElse("y", List()).head
    assume(actual === 16.0)
  }

  test("CognitiveMapBuilder should build 3 concept map with specified calc states") {
    val cm = CognitiveMapBuilder(actorSystem)
      .withConcept("x", calc => calc.withConst(4))
      .withConcept("z", calc => calc.withFactor("x", 6))
      .withConcept("y",
        calc => calc
          .withFactor("x", 8)
          .withFactor("z", 2)
      )
      .withLink("x", "y", 0.5)
      .withLink("x", "z", 0.5)
      .withLink("z", "y", 0.5)
      .build()
    val data = cm.makeIterations(1)
    val actual = data.getOrElse("y", List()).head
    assume(actual === 28.0)
  }
}
