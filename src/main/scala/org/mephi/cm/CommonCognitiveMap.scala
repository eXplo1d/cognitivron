package org.mephi.cm

import akka.actor.ActorRef
import org.mephi.events.{LinkEvent, UnlinkEvent}

class CommonCognitiveMap(private val concepts: Map[String, ActorRef],
                         private val concept2links: Map[String, Set[ActorRef]]) extends CognitiveMap {
  build()

  override def copy(): CognitiveMap = {
    this
  }

  private def build(): Unit = {
    concept2links.foreach {
      kv =>
        concepts.get(kv._1).foreach {
          concept =>
            kv._2.foreach {
              link => concept ! LinkEvent(link)
            }
        }
    }
  }

  override def removeLink(concept: String, link: ActorRef): Unit = {
    concept2links.get(concept).foreach {
      links =>
        if (links.contains(link)) {
          concepts.get(concept).foreach {
            conceptActor => conceptActor ! UnlinkEvent(link)
          }
        }
    }
  }

  override def getConcept2Link: Seq[(String, ActorRef)] = {
    concept2links.toSeq.flatMap {
      concept2links =>
        concept2links._2.map {
          link => (concept2links._1, link)
        }
    }
  }

  override def addLink(concept: String, link: ActorRef): Unit = {
    concepts.get(concept).foreach {
      conceptRef => conceptRef ! LinkEvent(link)
    }
  }
}
