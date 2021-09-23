package org.mephi.adaptron

import org.mephi.cg.CognitiveMap
import org.mephi.metric.CognitiveMapQuality

import scala.collection.immutable.Range

class RepeatableAdaption(adaptron: Adaptron,
                         cognitiveMapQuality: CognitiveMapQuality,
                         n: Int) extends Adaptron {
  override def adapt(cg: CognitiveMap): CognitiveMap = {
    var curr = cg.copy();
    var currQ = cognitiveMapQuality.calc(curr)
    for (i <- Range(0, n)) {
      val cm = adaptron.adapt(cg)
      val q = cognitiveMapQuality.calc(cm)
      if (q > currQ) {
        currQ = q
        curr = cm
      }
    }
    curr
  }
}
