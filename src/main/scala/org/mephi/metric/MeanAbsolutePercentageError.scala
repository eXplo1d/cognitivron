package org.mephi.metric

import scala.math.{pow, abs}

object MeanAbsolutePercentageError extends ErrorMetric {

  override def calc(predicted: Seq[Double], expected: Seq[Double]): Double = {
    (predicted zip expected).map{ case (pre, exp) => abs((pre - exp)/exp)}.sum*100/predicted.length
  }
}
