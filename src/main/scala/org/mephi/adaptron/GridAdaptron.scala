package org.mephi.adaptron

import org.mephi.cg.CognitiveMap
import org.mephi.fit.CognitiveMapTrainer
import org.mephi.metric.CognitiveMapQuality

class GridAdaptron(cgTrainer: CognitiveMapTrainer, cognitiveMapQuality: CognitiveMapQuality) extends Adaptron {
  private val RollbackThr = 10

  override def adapt(cg: CognitiveMap): CognitiveMap = {
    // условие выхода из этой шняги
    var bestCg = cg.copy()
    var currCg = bestCg.copy()
    var maxQ = cognitiveMapQuality.calc(bestCg)
    var nRollback = 0
    while (nRollback > RollbackThr) {
      removeRandomLink(currCg)
      cgTrainer.train(currCg)
      val q = cognitiveMapQuality.calc(currCg)
      if (q >= maxQ) {
        maxQ = q
        bestCg = currCg.copy()
        nRollback = 0 // her znaet
      } else {
        currCg = bestCg
        nRollback += 1
      }
    }
    bestCg
  }

  // Удалить рандомное ребро из CM
  private def removeRandomLink(cognitiveMap: CognitiveMap): CognitiveMap = {
    cognitiveMap
  }
}
