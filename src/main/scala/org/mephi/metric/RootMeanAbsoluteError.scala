package org.mephi.metric

import scala.collection.immutable.HashMap
import org.apache.spark.mllib.evaluation.RegressionMetrics

class RootMeanAbsoluteError(actualExpectedMap: HashMap) extends Quality {
  override def calc(actualExpectedMap: HashMap): Double = {
    val metrics = new RegressionMetrics(actualExpectedMap) // HashMap -> RDD
    metrics.rootMeanSquaredError
  }
}
