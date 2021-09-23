package org.mephi

import akka.actor.ActorSystem
import org.mephi.calculation.CommonCalculationState
import org.mephi.concept.{CalculationConcept, Link, Listener}
import org.mephi.events.{CalculationEvent, LinkEvent}

object CognitiveMapsApplication extends App {

  val actorSystem = ActorSystem("cg")
  val actorX = actorSystem.actorOf(CalculationConcept("x", new CommonCalculationState(Map(), 4)))
  val actorY = actorSystem.actorOf(CalculationConcept("y", new CommonCalculationState(Map("x" -> 8))))
  actorX ! LinkEvent(actorSystem.actorOf(Link(actorY, 0.5)))
  val listener = actorSystem.actorOf(Listener())
  actorY ! LinkEvent(actorSystem.actorOf(Link(listener, 1.0)))
  actorX ! new CalculationEvent {
    override def getRequest: String = "testetst"
  }
}
