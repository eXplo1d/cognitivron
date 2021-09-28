package org.mephi

import akka.actor.ActorSystem
<<<<<<< HEAD
import org.mephi.calculation.CommonCalculationState
import org.mephi.concept.{CalculationConcept, MultiplyLink, Listener}
=======
import org.mephi.calculation.{CommonCalculationState, Request}
import org.mephi.concept.{CalculationConcept, Listener, MultiplyLink}
>>>>>>> origin/master
import org.mephi.events.{CalculationEvent, LinkEvent}

object ThreeConceptsTest extends App {
  val actorSystem = ActorSystem("cm")
  val actorX = actorSystem.actorOf(CalculationConcept("x", new CommonCalculationState(Map(), 4)))
  val actorY = actorSystem.actorOf(CalculationConcept("y", new CommonCalculationState(
    Map(
      "x" -> 8,
      "z" -> 2
    )
  )))
  val actorZ = actorSystem.actorOf(CalculationConcept("z", new CommonCalculationState(Map("x" -> 6))))

  actorX ! LinkEvent(actorSystem.actorOf(MultiplyLink(actorY, 0.5)))
  actorX ! LinkEvent(actorSystem.actorOf(MultiplyLink(actorZ, 0.5)))
  actorZ ! LinkEvent(actorSystem.actorOf(MultiplyLink(actorY, 0.5)))

  val listener = actorSystem.actorOf(Listener(calc => {
    assert(calc.getResult == 28.0)
    actorSystem.terminate()
  }))
  actorY ! LinkEvent(actorSystem.actorOf(MultiplyLink(listener, 1.0)))
  actorX ! new CalculationEvent {
    override def getRequest = Request(0)
  }
}
