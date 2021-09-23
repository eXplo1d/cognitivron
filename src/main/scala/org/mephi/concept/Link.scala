package org.mephi.concept

import akka.actor.{Actor, ActorRef, Props}
import org.mephi.events.CalculationResultEvent

class Link(to: ActorRef,
           var multiplier: Double) extends Actor {
  override def receive: Receive = {
    case calculationResultEvent: CalculationResultEvent =>
      to ! CalculationResultEvent(
        calculationResultEvent.getRequest,
        calculationResultEvent.getConceptName,
        calculationResultEvent.getResult * multiplier
      )
    case _ => to ! _
  }
}

object Link {
  def apply(to: ActorRef, multiplier: Double): Props = Props(new Link(to, multiplier))
}
