package org.mephi.events

import akka.actor.ActorRef

trait LinkEvent {
  def getLink: ActorRef
}

object LinkEvent {
  def apply(actorRef: ActorRef): LinkEvent = {
    new LinkEvent {
      override def getLink: ActorRef = actorRef
    }
  }
}