package org.mephi.events

import akka.actor.ActorRef

case class LinkEvent(concept: String, link: ActorRef, linkType: LinkType)

sealed trait LinkType

object LinkTypes {
  object OutgoingLink extends LinkType

  object IncomingLink extends LinkType
}