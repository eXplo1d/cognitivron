package org.mephi.metric

import org.mephi.cm.CognitiveMap

trait CmError {
  def calc(cognitiveMap: CognitiveMap): Double
}
