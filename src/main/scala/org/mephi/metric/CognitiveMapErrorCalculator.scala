package org.mephi.metric

import org.mephi.cm.CognitiveMap

trait CognitiveMapErrorCalculator {
  def calc(cognitiveMap: CognitiveMap): Double
}
