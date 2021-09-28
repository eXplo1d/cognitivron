package org.mephi.metric

import org.mephi.cm.CognitiveMap

trait CognitiveMapError {
  def calc(cognitiveMap: CognitiveMap): Double
}
