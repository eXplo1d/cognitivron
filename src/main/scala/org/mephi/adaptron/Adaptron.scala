package org.mephi.adaptron

import org.mephi.cg.CognitiveMap

trait Adaptron {
  def adapt(cg: CognitiveMap): CognitiveMap
}
