package org.mephi.concept

import akka.actor.{Actor, Props}
import org.mephi.events.CalculationResultEvent

class Listener extends Actor with Concept {

  override def receive: Receive = {
    case calculated: CalculationResultEvent => println(s"Calculated result: ${calculated.getConceptName} = ${calculated.getResult}")
  }
}

class FuncListener(func: CalculationResultEvent => Unit) extends Actor with Concept {

  override def receive: Receive = {
    case calculated: CalculationResultEvent => func(calculated)
  }
}

object Listener {
  def apply(): Props = {
    Props(
      new Listener()
    )
  }

  def apply(func: CalculationResultEvent => Unit): Props = {
    Props(
      new FuncListener(func)
    )
  }
}