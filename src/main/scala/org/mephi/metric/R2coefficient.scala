package org.mephi.metric

import scala.collection.immutable.HashMap
import scala.math.pow

class R2coefficient(actualExpectedMap: HashMap) extends Quality {
  override def calc(actualExpectedMap: HashMap): Double = {
    val meanExpected = actualExpectedMap.foldReight(0)(_ + _) / actualExpectedMap.length
    return (1 - actualExpectedMap.map {
      case(actual, expected) => pow((expected - actual), 2)
    } / actualExpectedMap.map {
      case(actual, expected) => pow((expected - meanExpected), 2)
    }
  }
}
