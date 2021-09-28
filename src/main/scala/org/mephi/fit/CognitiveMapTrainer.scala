package org.mephi.fit

import org.mephi.cm.CognitiveMap

trait CognitiveMapTrainer {
  def train(cm: CognitiveMap): CognitiveMap
}
