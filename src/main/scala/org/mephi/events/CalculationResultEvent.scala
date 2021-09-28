package org.mephi.events

import org.mephi.calculation.Request

trait CalculationResultEvent {
  def getConceptName: String

  def getRequest: Request

  def getResult: Double
}

object CalculationResultEvent {
  def apply(request: Request, name: String, result: Double): CalculationResultEvent = {
    new CalculationResultEvent {
      override def getConceptName: String = name

      override def getRequest: Request = request

      override def getResult: Double = result
    }
  }
}
