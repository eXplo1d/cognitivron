package org.mephi.adaptron

import org.mephi.cm.CognitiveMap

trait Adaptron {
  def adapt(cm: CognitiveMap): CognitiveMap
}
