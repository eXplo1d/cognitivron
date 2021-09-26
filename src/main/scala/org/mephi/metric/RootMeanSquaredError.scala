package org.mephi.metric

import scala.collection.immutable.HashMap
import scala.math.{abs, sqrt}

class RootMeanSquaredError(actualExpectedMap: HashMap) extends Quality {
  override def calc(actualExpectedMap: HashMap): Double = {
    return sqrt(actualExpectedMap.map {
      case(actual, expected) => pow((expected - actual), 2)
    }.mean())
  }
}
