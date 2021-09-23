package org.mephi.fit

import org.mephi.cg.CognitiveMap

trait CognitiveMapTrainer {
  def train(cg: CognitiveMap): Unit
}
