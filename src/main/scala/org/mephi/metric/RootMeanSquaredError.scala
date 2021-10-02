package org.mephi.metric

import scala.math.{pow, sqrt}

object RootMeanSquaredError extends ErrorMetric {

  override def calc(predicted: Seq[Double], expected: Seq[Double]): Double = {
    sqrt((predicted zip expected).map{ case (pre, exp) => pow(pre - exp, 2)}.sum/predicted.length)
  }
}
