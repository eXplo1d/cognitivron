package org.mephi.adaptron

import org.mephi.cm.CognitiveMap
import org.mephi.fit.CognitiveMapTrainer
import org.mephi.metric.CognitiveMapError
import scala.util.control.Breaks._

class NotRandomAdaptron(cmTrainer: CognitiveMapTrainer,
                        cognitiveMapError: CognitiveMapError) extends Adaptron {
  override def adapt(cognitiveMap: CognitiveMap): CognitiveMap = {
    var linksLength = cognitiveMap.getLinks.length
    val threshold = (linksLength/3).toInt // max links number can be deleted
    var bestCm = cognitiveMap
    breakable {
      for (_ <- Range(0, threshold)) {
        bestCm = removeWorstLink(cognitiveMap)
        if (bestCm.getLinks.length == linksLength) {
          break // stop if non link was deleted
        } else {
          linksLength = bestCm.getLinks.length
        }
      }
    }
    bestCm
  }

  private def removeWorstLink(cognitiveMap: CognitiveMap): CognitiveMap = {
    val links = cognitiveMap.getLinks
    var worstIndex = -1 // do not delete any link if non of them improves quality
    var bestMeanSquaredError = cognitiveMapError.calc(cognitiveMap)
    links.zipWithIndex.foreach{ case (link, index) => 
      val currCognitiveMap = cognitiveMap.removeLink(index)
      val currMeanSquaredError = cognitiveMapError.calc(cmTrainer.train(currCognitiveMap))
      if (currMeanSquaredError < bestMeanSquaredError) {
        bestMeanSquaredError = currMeanSquaredError
        worstIndex = index
      }
    }
    if (worstIndex != -1) {
      cognitiveMap.removeLink(worstIndex)
    } else {
      cognitiveMap
    }
  }
}
