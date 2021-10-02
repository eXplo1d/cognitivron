package org.mephi.metric

import scala.math.abs

object MeanAbsoluteError extends ErrorMetric {

  override def calc(predicted: Seq[Double], expected: Seq[Double]): Double = {
    (predicted zip expected).map{ case (pre, exp) => abs(pre - exp)}.sum/predicted.length
  }
}
