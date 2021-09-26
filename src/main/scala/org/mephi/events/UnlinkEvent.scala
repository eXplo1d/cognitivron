package org.mephi.events

import akka.actor.ActorRef

trait UnlinkEvent {
  def getLink: ActorRef
}

object UnlinkEvent {
  def apply(link: ActorRef): UnlinkEvent = {
    new UnlinkEvent {
      override def getLink: ActorRef = link
    }
  }
}
