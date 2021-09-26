package org.mephi.concept

import akka.actor.{Actor, ActorRef}

trait Link extends Actor {
  def getTo: ActorRef
}
