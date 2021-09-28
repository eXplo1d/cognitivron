package org.mephi.metric

import org.mephi.cm.CognitiveMap
import org.mephi.fit.CognitiveMapTrainer
import scala.math.abs

class MeanAbsoluteError(cognitiveMap: CognitiveMap, dataSeq: Seq[(String, Double)]) extends CognitiveMapError {

  override def calc(cognitiveMap: CognitiveMap): Double = {

  }

  private def train(cognitiveMapTrainer: CognitiveMapTrainer, cognitiveMap: CognitiveMap, dataSeq: Seq[(String, Double)]): Seq[String, Double] = {
    cognitiveMapTrainer.train(cognitiveMap)
  }
}
