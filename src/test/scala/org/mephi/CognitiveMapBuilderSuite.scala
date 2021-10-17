package org.mephi

import akka.actor.ActorSystem
import org.mephi.cm.CognitiveMapBuilder
import org.scalatest.funsuite.AnyFunSuite

class CognitiveMapBuilderSuite extends AnyFunSuite {
  private val actorSystem: ActorSystem = ActorSystem("cm")

  test("CognitiveMapBuilder should build 2 concept map with specified calc states") {
    val cm = CognitiveMapBuilder(actorSystem)
      .withConcept("x", b => b.linear())
      .withConcept("y", b => b.linear())
      .withLink("x", "y", 0.5)
      .build()
    cm.setValues(Map("x" -> 4, "y" -> 0))
    val data = cm.makeIterations(30)
    val actual = data.getOrElse("y", List()).last
    assume(actual === 60.0)
  }

  test("CognitiveMapBuilder should build 3 concept map with specified calc states") {
    val cm = CognitiveMapBuilder(actorSystem)
      .withConcept("x", b => b.linear())
      .withConcept("z", b => b.linear())
      .withConcept("y", b => b.linear())
      .withLink("x", "y", 0.5)
      .withLink("x", "z", 0.5)
      .withLink("z", "y", 0.5)
      .build()
    cm.setValues(Map("x" -> 4, "z" -> 1, "y" -> 0))
    val data = cm.makeIterations(1)
    val actual = data.getOrElse("y", List()).head
    assume(actual === 2.5)
  }
}
