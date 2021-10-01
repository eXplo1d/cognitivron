package org.mephi

import akka.actor.ActorSystem
import org.mephi.calculation.{CommonCalculationState, Request}
import org.mephi.concept.{CalculationConcept, Listener, MultiplyLink}
import org.mephi.events.{CalculationEvent, LinkEvent}
import org.scalatest.funsuite.AnyFunSuite

class ConceptsSuite extends AnyFunSuite {
  private val actorSystem: ActorSystem = ActorSystem("cm")

  test("Two concepts should calculate value") {
    val actorX = actorSystem.actorOf(CalculationConcept("x", new CommonCalculationState(Map(), 4)))
    val actorY = actorSystem.actorOf(CalculationConcept("y", new CommonCalculationState(Map("x" -> 8))))
    actorX ! LinkEvent(actorSystem.actorOf(MultiplyLink(actorY, 0.5)))
    val listener = actorSystem.actorOf(Listener(calc => {
      calc.getResult === 0.692302339452737
      actorSystem.terminate()
    }))
    actorY ! LinkEvent(actorSystem.actorOf(MultiplyLink(listener, 1.0)))
    actorX ! new CalculationEvent {
      override def getRequest: Request = Request(0)
    }
  }

  test("Three concepts should calculate value") {
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
      calc.getResult === 0.5757043393389744
      actorSystem.terminate()
    }))
    actorY ! LinkEvent(actorSystem.actorOf(MultiplyLink(listener, 1.0)))
    actorX ! new CalculationEvent {
      override def getRequest = Request(0)
    }
  }
}
