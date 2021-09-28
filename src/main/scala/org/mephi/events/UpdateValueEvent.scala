package org.mephi.events

import org.mephi.calculation.Request

trait UpdateValueEvent {
  def getRequest: Request
  def getValue: Double
}
