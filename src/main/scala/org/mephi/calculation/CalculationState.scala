package org.mephi.calculation

import org.mephi.events.CalculationResultEvent

trait CalculationState {
  def applyDelta(delta: Double): Unit
  def getAttributes: Set[String]
  def calculate(attributes: Map[String, CalculationResultEvent], weightCost: Double = 0.0): Double
}
