package org.mephi.metric

import scala.collection.immutable.HashMap

trait CognitiveMapQuality {
  def calc(actualExpectedMap: HashMap): Double
}
