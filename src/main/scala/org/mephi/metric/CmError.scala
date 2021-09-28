package org.mephi.metric

import org.mephi.cm.CognitiveMap
import scala.math.pow

class CmError(errorMetric: ErrorMetric, expected: Map[String, Seq[Double]]) extends CognitiveMapError {

  override def calc(cognitiveMap: CognitiveMap): Double = {
    val predicted = cognitiveMap.makeIterations(10)
    val res = errorMetric.calc(predicted, expected)
    val x = predicted.map{
      case (str, value) =>

    }
  }
}



override def calc(predicted: Seq[Double], expected: Seq[Double]): Double = {
  (predicted zip expected).map{ case (pre, exp) => pow(pre - exp, 2)}.sum/predicted.length
}