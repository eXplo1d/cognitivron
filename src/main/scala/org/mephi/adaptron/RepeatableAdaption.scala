package org.mephi.adaptron

import org.mephi.cm.CognitiveMap
import org.mephi.metric.CognitiveMapQuality

import scala.collection.immutable.Range

class RepeatableAdaption(adaptron: Adaptron,
                         cognitiveMapQuality: CognitiveMapQuality,
                         n: Int) extends Adaptron {
  override def adapt(cm: CognitiveMap): CognitiveMap = {
    var curr = cm.copy();
    var currQ = cognitiveMapQuality.calc(curr)
    for (i <- Range(0, n)) {
      val cmTmp = adaptron.adapt(cm)
      val q = cognitiveMapQuality.calc(cm)
      if (q > currQ) {
        currQ = q
        curr = cm
      }
    }
    curr
  }
}
