package org.mephi.cm

import akka.actor.ActorRef

trait CognitiveMap {
  def copy(): CognitiveMap
  def addLink(concept: String, link: ActorRef): Unit
  def removeLink(concept: String, link: ActorRef): Unit
  def getConcept2Link: Seq[(String, ActorRef)]
<<<<<<< HEAD
=======
  def updateValue(concept: String, value: Double): Unit
  def makeIterations(n: Int): Map[String, List[Double]]
>>>>>>> origin/master
}
