package org.mephi.adaptron

import org.mephi.cm.CognitiveMap
import org.mephi.fit.CognitiveMapTrainer
import org.mephi.metric.CognitiveMapError
import scala.util.control.Breaks._
import scala.util.Random

class RandomAdaptron(cmTrainer: CognitiveMapTrainer,
                     cognitiveMapError: CognitiveMapError) extends Adaptron {
  override def adapt(cognitiveMap: CognitiveMap): CognitiveMap = {
    var linksLength = cognitiveMap.getConcept2Link.length
    val threshold = linksLength / 3 // max links number can be deleted
    var bestCognitiveMap = cognitiveMap
    breakable {
      for (_ <- Range(0, threshold)) {
        bestCognitiveMap = removeRandomLink(cognitiveMap)
        if (bestCognitiveMap.getConcept2Link.length == linksLength) {
          break // stop if non link was deleted
        } else {
          linksLength = bestCognitiveMap.getConcept2Link.length
        }
      }
    }
    bestCognitiveMap
  }

  private def removeRandomLink(cognitiveMap: CognitiveMap): CognitiveMap = {
    val links = cognitiveMap.getConcept2Link
    val worstMeanSquaredError = cognitiveMapError.calc(cognitiveMap)
    val linksShuffled = Random.shuffle(links)
    breakable {
      linksShuffled.foreach {
        case (concept, link) =>
          cognitiveMap.removeLink(concept, link)
          cmTrainer.train(cognitiveMap)
          val currMeanSquaredError = cognitiveMapError.calc(cognitiveMap)
          if (currMeanSquaredError < worstMeanSquaredError) {
            break
          } else {
            cognitiveMap.addLink(concept, link)
          }
      }
    }
    cmTrainer.train(cognitiveMap)
    cognitiveMap
  }
}
