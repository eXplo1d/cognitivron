package org.mephi.adaptron

import org.mephi.cm.CognitiveMap
import org.mephi.fit.CognitiveMapTrainer
import org.mephi.metric.MeanSquaredError
import org.mephi.metric.R2coefficient
import scala.util.control.Breaks._
import scala.util.Random

class RandomAdaptron(cmTrainer: CognitiveMapTrainer, meanSquaredError: MeanSquaredError, r2coefficient: R2coefficient) extends Adaptron {

  override def adapt(cm: CognitiveMap): CognitiveMap = {
    var linksLength = cognitiveMap.getLinks.length
    val threshold = (linksLength/3).toInt // max links number can be deleted
    breakable {
      for (i <- threshold) {
        bestCm = removeRandomLink(cm)
        if (bestCm.getLinks.length == linksLength) {
          break // stop if non link was deleted
        } else {
          linksLength = bestCm.getLinks.length
        }
      }
    }
    bestCm
  }

  private def removeRandomLink(cognitiveMap: CognitiveMap): CognitiveMap = {
    val links = cognitiveMap.getLinks
    var bestMeanSquaredQuality = 1/meanSquaredError.calc(cognitiveMap)
    var bestR2coefficientQuality = r2coefficient.calc(cognitiveMap)
    links.zipWithIndex.foreach{ case (link, index) => 
      val currCognitiveMap = cognitiveMap.removeLink(index)
      val currMeanSquaredQuality = 1/meanSquaredError.calc(cmTrainer.train(currCognitiveMap))
      val currR2coefficientQuality = r2coefficient.calc(cmTrainer.train(currCognitiveMap))
      if (currMeanSquaredQuality > bestMeanSquaredQuality || currR2coefficientQuality > bestR2coefficientQuality) {
        return cognitiveMap.removeLink(index)
      }
    }
    return cognitiveMap
  }
}
