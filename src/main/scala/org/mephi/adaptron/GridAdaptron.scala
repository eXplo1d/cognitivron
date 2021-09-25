package org.mephi.adaptron

import org.mephi.cm.CognitiveMap
import org.mephi.fit.CognitiveMapTrainer
import org.mephi.metric.MeanSquaredError
import scala.util.Random
import scala.math.sqrt
import scala.util.control.Breaks._

class GridAdaptron(cmTrainer: CognitiveMapTrainer, cognitiveMapQuality: MeanSquaredError) extends Adaptron {

  override def adapt(cm: CognitiveMap): CognitiveMap = {
    var linksLength = cognitiveMap.getLinks.length
    val threshold = sqrt(currLinksLength).toInt // max links number can be deleted
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
    var bestQuality = cognitiveMapQuality.calc(cognitiveMap)
    links.zipWithIndex.foreach{ case (link, index) => 
      val currCognitiveMap = cognitiveMap.removeLink(index)
      val currQuality = 1/cognitiveMapQuality.calc(cmTrainer.train(currCognitiveMap))
      if (currQuality >= bestQuality) {
        bestQuality = currQuality
        worstIndex = index
      }
    }
    if (worstIndex != null) {
      cognitiveMap.removeLink(worstIndex)
    } else {
      cognitiveMap
    }
  }

  private def removeRandomLink(cognitiveMap: CognitiveMap): CognitiveMap = {
    val random = new Random
    cognitiveMap.removeLink(cognitiveMap.getLinks(random.nextInt(cognitiveMap.getLinks.length)))
  }
}
