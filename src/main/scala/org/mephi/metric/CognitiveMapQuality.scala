package org.mephi.metric

import org.mephi.cg.CognitiveMap

trait CognitiveMapQuality {
  def calc(cognitiveMap: CognitiveMap): Double
}
