package org.mephi

import akka.actor.ActorSystem
import org.mephi.adaptron.Adaptron
import org.mephi.calculation.{CommonCalculationState, Request}
import org.mephi.cm.CognitiveMap
import org.mephi.concept.{CalculationConcept, Listener, MultiplyLink}
import org.mephi.events.{CalculationEvent, LinkEvent}
import org.mephi.metric.{CmErrorMetric, MeanAbsoluteError}

object CognitiveMapsApplication extends App {

  val actorSystem = ActorSystem("cm")
  val actorX = actorSystem.actorOf(CalculationConcept("x", new CommonCalculationState(Map(), 4)))
  val actorY = actorSystem.actorOf(CalculationConcept("y", new CommonCalculationState(Map("x" -> 8))))
  actorX ! LinkEvent(actorSystem.actorOf(MultiplyLink(actorY, 0.5)))
  val listener = actorSystem.actorOf(Listener())
  actorY ! LinkEvent(actorSystem.actorOf(MultiplyLink(listener, 1.0)))
  actorX ! new CalculationEvent {
    override def getRequest: Request = Request(0)
  }
}
