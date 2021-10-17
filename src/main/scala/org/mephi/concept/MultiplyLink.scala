package org.mephi.concept

import akka.actor.{ActorRef, Props}
import org.mephi.events.{CalculationResultEvent, LinkEvent, LinkTypes, UnlinkEvent}

class MultiplyLink(val to: ActorRef,
                   var multiplier: Double) extends Link {

  private var enabled = true

  override def receive: Receive = {
    case calculationResultEvent: CalculationResultEvent =>
      to ! CalculationResultEvent(
        calculationResultEvent.getRequest,
        calculationResultEvent.getConceptName,
        calculationResultEvent.getResult * getMultiplier
      )
    case link: LinkEvent =>
      if (link.linkType == LinkTypes.OutgoingLink) {
        enabled = true
        to ! link.copy(linkType = LinkTypes.IncomingLink)
      }
    case unlink: UnlinkEvent =>
      if (unlink.linkType == LinkTypes.OutgoingLink) {
        enabled = false
        to ! unlink.copy(linkType = LinkTypes.IncomingLink)
      }
    case other => to ! other
  }

  override def getTo: ActorRef = to

  private def getMultiplier: Double = {
    if (enabled) {
      multiplier
    } else 0.0
  }

}

object MultiplyLink {
  def apply(to: ActorRef, multiplier: Double): Props = Props(new MultiplyLink(to, multiplier))
}
