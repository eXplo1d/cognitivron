package org.mephi.events

import org.mephi.calculation.Request

trait CalculationEvent {
  def getRequest: Request
}
