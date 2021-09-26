package org.mephi.adaptron

import org.mephi.cm.CognitiveMap
import org.mephi.metric.MeanSquaredError
import org.mephi.metric.R2coefficient

import scala.collection.immutable.Range

class BestAdaptron(randomAdaptron: RandomAdaptron,
                    notRandomAdaptron: NotRandomAdaptron,
                    meanSquaredError: MeanSquaredError,
                    randomAdaptronTries: Int = 10) extends Adaptron {
  override def adapt(cm: CognitiveMap): CognitiveMap = {
    val initQuality = 1/meanSquaredError.calc(cm)
    val notRandomCm = notRandomAdaptron(cm)
    val notRandomQuality = 1/meanSquaredError.calc(notRandomCm)
    var randomCm = notRandomCm
    var randomQuality = notRandomQuality
    for (i <- Range(0, randomAdaptronTries)) {
      val currRandomCm = randomAdaptron.adapt(cm)
      val currRandomQuality = 1/meanSquaredError.calc(currRandomCm)
      if (currRandomQuality > randomQuality) {
        randomQuality = currRandomQuality
        randomCm = currRandomCm
      }
    }
    if (initQuality >= notRandomQuality && initQuality >= randomQuality &&) {
      return cm
    } else if (notRandomQuality >= randomQuality) {
      return notRandomCm
    } else {
      return randomCm
    }
  }
}
