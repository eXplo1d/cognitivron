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
    cm.setValues(Map("x" -> 4, "z" -> 1, "y" -> 1))
    val data = cm.makeIterations(30)
    val actual = data.getOrElse("y", List()).head
    assume(actual === 3.5)
  }

  test("CognitiveMapBuilder should build 3 concept full linked map with specified calc states") {
    val cm = CognitiveMapBuilder(actorSystem)
      .withConcept("x", b => b.linear())
      .withConcept("z", b => b.linear())
      .withConcept("y", b => b.linear())
      .withLink("x", "y", 0.5)
      .withLink("x", "z", 0.5)
      .withLink("z", "y", 0.5)
      .withLink("z", "x", 0.5)
      .withLink("y", "z", 0.5)
      .withLink("y", "x", 0.5)
      .build()
    cm.setValues(Map("x" -> 4, "z" -> 3, "y" -> 1))
    val data = cm.makeIterations(2)
    val actualY = data.getOrElse("y", List()).last
    val actualX = data.getOrElse("x", List()).last
    val actualZ = data.getOrElse("z", List()).last
    assume(actualY === 10.25)
    assume(actualX === 11.00)
    assume(actualZ === 10.75)
  }
}
