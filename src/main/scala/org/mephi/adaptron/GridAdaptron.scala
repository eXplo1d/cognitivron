package org.mephi.adaptron

import org.mephi.cm.CognitiveMap
import org.mephi.fit.CognitiveMapTrainer
import org.mephi.metric.CognitiveMapQuality

class GridAdaptron(cmTrainer: CognitiveMapTrainer, cognitiveMapQuality: CognitiveMapQuality) extends Adaptron {
  private val RollbackThr = 10

  override def adapt(cm: CognitiveMap): CognitiveMap = {
    // условие выхода из этой шняги
    var bestCm = cm.copy()
    var currCm = bestCm.copy()
    var maxQ = cognitiveMapQuality.calc(bestCm)
    var nRollback = 0
    while (nRollback > RollbackThr) {
      removeRandomLink(currCm)
      cmTrainer.train(currCm)
      val q = cognitiveMapQuality.calc(currCm)
      if (q >= maxQ) {
        maxQ = q
        bestCm = currCm.copy()
        nRollback = 0 // her znaet
      } else {
        currCm = bestCm
        nRollback += 1
      }
    }
    bestCm
  }

  // Удалить рандомное ребро из CM
  private def removeRandomLink(cognitiveMap: CognitiveMap): CognitiveMap = {
    cognitiveMap
  }
}
