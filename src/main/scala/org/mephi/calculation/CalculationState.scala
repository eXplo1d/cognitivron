package org.mephi.calculation

import org.mephi.events.CalculationResultEvent

trait CalculationState {
  def applyDelta(delta: Double): Unit
  def calculate(attributes: Map[String, CalculationResultEvent]): Double
  def getAttributes: Set[String]
}
