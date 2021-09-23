package org.mephi.events

trait CalculationResultEvent {
  def getConceptName: String

  def getRequest: String

  def getResult: Double
}

object CalculationResultEvent {
  def apply(request: String, name: String, result: Double): CalculationResultEvent = {
    new CalculationResultEvent {
      override def getConceptName: String = name

      override def getRequest: String = request

      override def getResult: Double = result
    }
  }
}
