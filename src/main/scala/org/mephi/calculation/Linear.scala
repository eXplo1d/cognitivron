package org.mephi.calculation

class Linear extends CalculationState {
  override def applyDelta(delta: Double): Unit = {

  }

  override def calculate(attributes: Map[String, Double], additionalValue: Double): Double = {
    attributes.values.sum + additionalValue
  }
}
