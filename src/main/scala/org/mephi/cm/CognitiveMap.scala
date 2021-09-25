package org.mephi.cm

import scala.collection.immutable.HashMap

trait CognitiveMap {
  def copy(): CognitiveMap
  def addLink(link: ActorRef): CognitiveMap
  def removeLink(link: ActorRef): CognitiveMap
  def getLinks(): Seq[ActorRef]()

}
