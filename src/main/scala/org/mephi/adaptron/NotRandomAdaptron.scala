package org.mephi.adaptron

import akka.actor.ActorRef
import org.mephi.cm.CognitiveMap
import org.mephi.fit.CognitiveMapTrainer
import org.mephi.metric.CognitiveMapError

import scala.util.control.Breaks._

class NotRandomAdaptron(cmTrainer: CognitiveMapTrainer,
                        cognitiveMapError: CognitiveMapError) extends Adaptron {
  override def adapt(cognitiveMap: CognitiveMap): CognitiveMap = {
    var linksLength = cognitiveMap.getConcept2Link.length
    val threshold = (linksLength / 3) // max links number can be deleted
    var bestCm = cognitiveMap
    breakable {
      for (_ <- Range(0, threshold)) {
        bestCm = removeWorstLink(cognitiveMap)
        if (bestCm.getConcept2Link.length == linksLength) {
          break // stop if non link was deleted
        } else {
          linksLength = bestCm.getConcept2Link.length
        }
      }
    }
    bestCm
  }

  private def removeWorstLink(cognitiveMap: CognitiveMap): CognitiveMap = {
    val links = cognitiveMap.getConcept2Link
    var worstMeanSquaredError = cognitiveMapError.calc(cognitiveMap)
    var worstLink: Option[(String, ActorRef)] = None
    links.foreach {
      case (concept, link) =>
        cognitiveMap.removeLink(concept, link)
        cmTrainer.train(cognitiveMap)
        val currMeanSquaredError = cognitiveMapError.calc(cognitiveMap)
        if (currMeanSquaredError < worstMeanSquaredError) {
          worstMeanSquaredError = currMeanSquaredError
          worstLink = Some(concept -> link)
        } else {
          cognitiveMap.addLink(concept, link)
        }
    }
    worstLink.map {
      case (concept, link) =>
        cognitiveMap.removeLink(concept, link)
        cmTrainer.train(cognitiveMap)
        cognitiveMap
    }.getOrElse {
      cmTrainer.train(cognitiveMap)
      cognitiveMap
    }
  }
}
