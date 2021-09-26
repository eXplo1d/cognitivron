package org.mephi.metric

import scala.collection.immutable.HashMap
import scala.math.pow

class MeanSquaredError(actualExpectedMap: HashMap) extends CognitiveMapError {
  override def calc(actualExpectedMap: HashMap): Double = {
    actualExpectedMap.map {
      case(actual, expected) => pow((expected - actual), 2)
    }.mean()
  }
}
