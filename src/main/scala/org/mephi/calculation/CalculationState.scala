package org.mephi.calculation

trait CalculationState {
  def applyDelta(delta: Double): Unit

  def calculate(attributes: Map[String, Double], additionalValue: Double = 0.0): Double
}

object IdentityConstValueCalculationState extends CalculationState {
  override def applyDelta(delta: Double): Unit = {}

  override def calculate(attributes: Map[String, Double], additionalValue: Double = 0.0): Double = {
    additionalValue
  }
}