package org.mephi.calculation

import org.mephi.events.CalculationResultEvent

class CommonCalculationState(formula: Map[String, Double],
<<<<<<< HEAD
                             const: Double = 0.0) extends CalculationState {
=======
                             const: Double = 0.0,
                             m: Double = -0.243) extends CalculationState {
>>>>>>> origin/master

  override def applyDelta(delta: Double): Unit = {

  }

  override def getAttributes: Set[String] = {
    formula.keySet
  }
<<<<<<< HEAD

  override def calculate(attributes: Map[String, CalculationResultEvent], weightCost: Double): Double = {
    attributes.map {
      kv => formula.getOrElse(kv._1, 0.0) * kv._2.getResult
    }.sum + const + weightCost
=======
  // sigmoid func
  override def calculate(attributes: Map[String, CalculationResultEvent], weightCost: Double): Double = {
    val x = attributes.map {
      kv => formula.getOrElse(kv._1, 0.0) * kv._2.getResult
    }.sum + const + weightCost
    1.0 / Math.pow(Math.E, (-1) * m * x)
>>>>>>> origin/master
  }
}
