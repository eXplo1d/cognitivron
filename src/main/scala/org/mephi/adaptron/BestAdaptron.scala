package org.mephi.adaptron

import org.mephi.cm.CognitiveMap
import org.mephi.fit.CognitiveMapTrainer
import org.mephi.metric.CognitiveMapErrorCalculator
import scala.collection.immutable.Range

class BestAdaptron(cmTrainer: CognitiveMapTrainer,
                   cognitiveMapError: CognitiveMapErrorCalculator,
                   randomAdaptronTries: Int = 10) extends Adaptron {
  override def adapt(initCm: CognitiveMap): CognitiveMap = {
    val randomAdaptron = new RandomAdaptron(cmTrainer, cognitiveMapError)
    val notRandomAdaptron = new NotRandomAdaptron(cmTrainer, cognitiveMapError)
    val initError = cognitiveMapError.calc(initCm)
    val notRandomCm = notRandomAdaptron.adapt(initCm)
    val notRandomError = cognitiveMapError.calc(notRandomCm)
    var randomCm = randomAdaptron.adapt(initCm)
    var randomError = cognitiveMapError.calc(randomCm)
    for (_ <- Range(0, randomAdaptronTries)) {
      val currRandomCm = randomAdaptron.adapt(initCm)
      val currRandomError = cognitiveMapError.calc(currRandomCm)
      if (currRandomError < randomError) {
        randomError = currRandomError
        randomCm = currRandomCm
      }
    }

    if (initError <= notRandomError && initError <= randomError) {
      initCm
    } else if (notRandomError <= randomError) {
      notRandomCm
    } else {
      randomCm
    }
  }
}
