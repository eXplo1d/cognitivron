package org.mephi.metric

import org.mephi.cm.CognitiveMap

class CmErrorMetric(errorMetric: ErrorMetric, expected: Map[String, Seq[Double]], iterations: Int = 10) extends CmError {

  override def calc(cognitiveMap: CognitiveMap): Double = {
    val predicted = cognitiveMap.makeIterations(iterations)
    expected.map {
      case (concept, expectedValues) => {
        val predictedValues = predicted.getOrElse(concept, expectedValues)
        errorMetric.calc(expectedValues, predictedValues)
      }
    }.max
  }
}
