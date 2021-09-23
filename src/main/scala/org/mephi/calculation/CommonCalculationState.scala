package org.mephi.calculation

import org.mephi.events.CalculationResultEvent

class CommonCalculationState(formula: Map[String, Double], const: Double = 0.0) extends CalculationState {
  override def applyDelta(delta: Double): Unit = {

  }

  override def getAttributes: Set[String] = {
    formula.keySet
  }

  override def calculate(attributes: Map[String, CalculationResultEvent]): Double = {
    attributes.map {
      kv => formula.getOrElse(kv._1, 0.0) * kv._2.getResult
    }.sum + const
  }
}
