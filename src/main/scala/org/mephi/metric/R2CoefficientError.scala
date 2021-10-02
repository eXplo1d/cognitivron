package org.mephi.metric

import scala.math.pow

object R2CoefficientError extends ErrorMetric {

  override def calc(predicted: Seq[Double], expected: Seq[Double]): Double = {
    val expectedMean = expected.sum / expected.length
    (predicted zip expected).map{ case (pre, exp) => pow(pre - exp, 2)}.sum /
      (predicted zip expected).map{ case (pre, exp) => pow(exp - expectedMean, 2)}.sum
  }
}
