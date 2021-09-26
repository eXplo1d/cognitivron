package org.mephi.adaptron

import org.mephi.cm.CognitiveMap
import org.mephi.fit.CognitiveMapTrainer
import org.mephi.metric.CognitiveMapError
import scala.util.control.Breaks._

class RandomAdaptron(cmTrainer: CognitiveMapTrainer,
                     cognitiveMapError: CognitiveMapError) extends Adaptron {
  override def adapt(cognitiveMap: CognitiveMap): CognitiveMap = {
    var linksLength = cognitiveMap.getLinks.length
    val threshold = (linksLength/3) // max links number can be deleted
    var bestCognitiveMap = cognitiveMap
    breakable {
      for (_ <- Range(0, threshold)) {
        bestCognitiveMap = removeRandomLink(cognitiveMap)
        if (bestCognitiveMap.getLinks.length == linksLength) {
          break // stop if non link was deleted
        } else {
          linksLength = bestCognitiveMap.getLinks.length
        }
      }
    }
    bestCognitiveMap
  }

  private def removeRandomLink(cognitiveMap: CognitiveMap): CognitiveMap = {
    val links = cognitiveMap.getLinks
    val bestMeanSquaredError = cognitiveMapError.calc(cognitiveMap)
    links.zipWithIndex.foreach{ case (link, index) =>
      val currCognitiveMap = cognitiveMap.removeLink(index)
      val currMeanSquaredError = cognitiveMapError.calc(cmTrainer.train(currCognitiveMap))
      if (currMeanSquaredError < bestMeanSquaredError) {
        return cognitiveMap.removeLink(index)
      }
    }
    cognitiveMap
  }
}
