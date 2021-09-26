package org.mephi.metric

import scala.collection.immutable.HashMap
import scala.math.abs

class MeanAbsoluteError(actualExpectedMap: HashMap) extends CognitiveMapError {
  override def calc(actualExpectedMap: HashMap): Double = {
    actualExpectedMap.map {
      case(actual, expected) => abs(expected - actual)
    }.mean()
  }
}
