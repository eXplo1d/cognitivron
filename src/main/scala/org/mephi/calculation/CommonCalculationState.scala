package org.mephi.calculation

import org.mephi.events.CalculationResultEvent

class CommonCalculationState(formula: Map[String, Double],
                             const: Double = 0.0,
                             other: CalculationState = IdentityConstValueCalculationState) extends CalculationState {

  override def applyDelta(delta: Double): Unit = {

  }

  override def getAttributes: Set[String] = {
    formula.keySet
  }
  // sigmoid func
  override def calculate(attributes: Map[String, CalculationResultEvent], constValue: Double): Double = {
    val x = attributes.map {
      kv => formula.getOrElse(kv._1, 0.0) * kv._2.getResult
    }.sum + const + constValue
    other.calculate(attributes, x)
  }
}
