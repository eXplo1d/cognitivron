package org.mephi.metric

import scala.math.pow

object R2CoefficientError extends ErrorMetric {

  override def calc(predicted: Seq[Double], expected: Seq[Double]): Double = {
    val expectedMean = expected.sum / expected.length
    val sumSquaresRegres = (predicted zip expected).map{ case (pre, exp) => pow(pre - exp, 2)}.sum
    val sumSquaresTotal = expected.map{ case exp => pow(exp - expectedMean, 2)}.sum
    sumSquaresRegres / sumSquaresTotal
  }
}
