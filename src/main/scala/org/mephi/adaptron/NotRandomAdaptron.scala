package org.mephi.adaptron

import org.mephi.cm.CognitiveMap
import org.mephi.fit.CognitiveMapTrainer
import org.mephi.metric.MeanSquaredError
import org.mephi.metric.R2coefficient
import scala.util.control.Breaks._

class NotRandomAdaptron(cmTrainer: CognitiveMapTrainer, meanSquaredError: MeanSquaredError, r2coefficient: R2coefficient) extends Adaptron {

  override def adapt(cm: CognitiveMap): CognitiveMap = {
    var linksLength = cognitiveMap.getLinks.length
    val threshold = (linksLength/3).toInt // max links number can be deleted
    breakable {
      for (i <- threshold) {
        bestCm = removeWorstLink(cm)
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
    var worstIndex = null // do not delete any link if non of them improves quality
    var bestMeanSquaredQuality = 1/meanSquaredError.calc(cognitiveMap)
    var bestR2coefficientQuality = r2coefficient.calc(cognitiveMap)
    links.zipWithIndex.foreach{ case (link, index) => 
      val currCognitiveMap = cognitiveMap.removeLink(index)
      val currMeanSquaredQuality = 1/meanSquaredError.calc(cmTrainer.train(currCognitiveMap))
      val currR2coefficientQuality = r2coefficient.calc(cmTrainer.train(currCognitiveMap))
      if (currMeanSquaredQuality > bestMeanSquaredQuality || currR2coefficientQuality > bestR2coefficientQuality) {
        bestMeanSquaredQuality = currMeanSquaredQuality
        bestR2coefficientQuality = currR2coefficientQuality
        worstIndex = index
      }
    }
    if (worstIndex != null) {
      cognitiveMap.removeLink(worstIndex)
    } else {
      cognitiveMap
    }
  }
}
