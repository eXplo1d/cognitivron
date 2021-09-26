package org.mephi.metric

import scala.collection.immutable.HashMap
import scala.math.{pow, sqrt}

class RootMeanSquaredError(actualExpectedMap: HashMap) extends CognitiveMapError {
  override def calc(actualExpectedMap: HashMap): Double = {
    sqrt(actualExpectedMap.map {
      case(actual, expected) => pow((expected - actual), 2)
    }.mean())
  }
}
