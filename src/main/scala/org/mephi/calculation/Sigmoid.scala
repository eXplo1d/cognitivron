package org.mephi.calculation

class Sigmoid(var m: Double = 0.5) extends CalculationState {

  override def applyDelta(delta: Double): Unit = {
    m += delta
  }

  override def calculate(attributes: Map[String, Double],
                         additionalValue: Double): Double = {
    1 / (1 + Math.exp((-1) * m * attributes.values.sum + additionalValue))
  }
}
