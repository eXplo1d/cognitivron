package org.mephi.metric

import org.mephi.cm.CognitiveMap
import org.mephi.fit.CognitiveMapTrainer
import scala.math.abs

class MeanAbsoluteError(cognitiveMap: CognitiveMap, dataSeq: Seq[(String, Double)]) extends CognitiveMapError {

  override def calc(actualExpectedMap: CognitiveMap): Double = {
    actualExpectedMap.map {
      case(actual, expected) => abs(expected - actual)
    }.mean()
  }

  private def train(cognitiveMapTrainer: CognitiveMapTrainer, cognitiveMap: CognitiveMap, dataSeq: Seq[(String, Double)]): Seq[String, Double] {
    cognitiveMapTrainer.train(cognitiveMap)
  }
}
