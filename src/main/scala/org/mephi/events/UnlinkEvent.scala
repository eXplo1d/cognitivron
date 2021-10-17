package org.mephi.events

import akka.actor.ActorRef


case class UnlinkEvent(concept: String, link: ActorRef, linkType: LinkType)
