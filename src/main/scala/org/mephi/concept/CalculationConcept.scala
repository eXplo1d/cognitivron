package org.mephi.concept

import akka.actor.{ActorRef, Props}
import org.mephi.calculation.CalculationState
import org.mephi.events._

import scala.collection.immutable.HashMap

class CalculationConcept(private val conceptName: String,
                         private val calculationState: CalculationState) extends Concept {

  private var links = Seq[ActorRef]()

  private var parametersMap = new HashMap[String, Map[String, CalculationResultEvent]]()
  private val attributes = calculationState.getAttributes
  private var calculatedRequest: Set[String] = Set()

  override def receive: Receive = {
    case calculation: CalculationEvent => calculate(calculation)
    case delta: BackpropagationEvent => applyDelta(delta)
    case calculated: CalculationResultEvent => collectResult(calculated)
    case link: LinkEvent => addLink(link)
    case unlink: UnlinkEvent => unlinkEvent(unlink)
  }

  private def collectResult(calculated: CalculationResultEvent): Unit = {
    if (attributes.contains(calculated.getConceptName)) {
      val calcState = parametersMap.getOrElse(calculated.getRequest, Map())
      val newMap = calcState + (calculated.getConceptName -> calculated)
      parametersMap = parametersMap + (calculated.getRequest -> newMap)
    } else {
      parametersMap = parametersMap + (calculated.getRequest -> Map(calculated.getConceptName -> calculated))
    }
    checkAndNotify(calculated.getRequest)
  }

  private def addLink(linkEvent: LinkEvent): Unit = {
    links = links ++ Seq(linkEvent.getLink)
    linkEvent.getLink ! linkEvent
  }

  private def unlinkEvent(unlinkEvent: UnlinkEvent): Unit = {
    unlinkEvent.getLink ! unlinkEvent
  }

  private def calculate(calculationEvent: CalculationEvent): Unit = {
    checkAndNotify(calculationEvent.getRequest)
  }

  private def applyDelta(delta: BackpropagationEvent): Unit = {
    calculationState.applyDelta(delta.getError)
  }

  private def checkAndNotify(request: String): Unit = {
    val params = parametersMap.getOrElse(request, Map())
    if (params.keySet == attributes) {
      val calculation = CalculationResultEvent(request, conceptName, calculationState.calculate(params))
      links.foreach {
        link => link ! calculation
      }
      calculatedRequest += request
    }
  }
}

object CalculationConcept {
  def apply(concept: String, calculationState: CalculationState): Props = Props(
    new CalculationConcept(concept, calculationState)
  )
}