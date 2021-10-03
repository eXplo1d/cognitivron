package org.mephi.calculation

import org.mephi.events.CalculationResultEvent

trait CalculationState {
  def applyDelta(delta: Double): Unit

  def getAttributes: Set[String]

  def calculate(attributes: Map[String, CalculationResultEvent], constValue: Double = 0.0): Double
}

object IdentityConstValueCalculationState extends CalculationState {
  override def applyDelta(delta: Double): Unit = {}

  override def getAttributes: Set[String] = Set()

  override def calculate(attributes: Map[String, CalculationResultEvent], constValue: Double = 0.0): Double = {
    constValue
  }
}