package org.mephi.events

trait BackpropagationEvent {
  def getError: Double
}
