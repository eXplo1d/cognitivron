package org.mephi

import akka.actor.ActorSystem
import org.mephi.cm.CognitiveMapBuilder

// TODO MOOVE TO SCALATEST
object CognitiveMapBuilderTest extends  App {

  val actorSystem = ActorSystem("cm")

  val cm = new CognitiveMapBuilder(actorSystem)
    .withConcept("x")
    .withConcept("y")
    .withConcept("z")
    .withConcept("k")
    .withLink("x", "y")
    .withLink("z", "y")
    .withLink("k", "y")
    .withLink("x", "z")
    .withLink("z", "k")
    .build()
  print(cm.makeIterations(10))
  print(cm)
}
