package org.mephi.metric

import org.mephi.cm.CognitiveMap

trait CognitiveMapQuality {
  def calc(cognitiveMap: CognitiveMap): Double
}
