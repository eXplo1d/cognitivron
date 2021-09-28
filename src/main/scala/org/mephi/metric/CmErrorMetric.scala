package org.mephi.metric

import org.mephi.cm.CognitiveMap

class CmErrorMetric(errorMetric: ErrorMetric, expected: Map[String, Seq[Double]], iterations: Int = 10) extends CmError {

  override def calc(cognitiveMap: CognitiveMap): Double = {
    val predicted = cognitiveMap.makeIterations(iterations)
    val res = errorMetric.calc(predicted.values.head, expected.values.head)
    res
  }
}
