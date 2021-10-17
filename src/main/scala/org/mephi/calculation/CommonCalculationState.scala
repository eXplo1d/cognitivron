package org.mephi.calculation

class CommonCalculationState(other: CalculationState = IdentityConstValueCalculationState) extends CalculationState {

  override def applyDelta(delta: Double): Unit = {
    other.applyDelta(delta)
  }

  override def calculate(attributes: Map[String, Double], constValue: Double): Double = {
    val sum = attributes.values.sum
    other.calculate(Map(), sum)
  }
}
