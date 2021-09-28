package org.mephi.concept

import akka.actor.{ActorRef, Props}
<<<<<<< HEAD
<<<<<<< HEAD
import org.mephi.calculation.CalculationState
=======
import org.mephi.calculation.{CalculationState, Request}
>>>>>>> origin/master
=======
import org.mephi.calculation.{CalculationState, Request}
>>>>>>> origin/master
import org.mephi.events._

import scala.collection.immutable.HashMap

class CalculationConcept(private val conceptName: String,
                         private val calculationState: CalculationState) extends Concept {

  private var links = Seq[ActorRef]()

  private var parametersMap = new HashMap[Int, Map[String, CalculationResultEvent]]()
  private val attributes = calculationState.getAttributes
  private var results = new HashMap[Int, Double]()

  override def receive: Receive = {
    case calculation: CalculationEvent => calculate(calculation)
    case delta: BackpropagationEvent => applyDelta(delta)
    case calculated: CalculationResultEvent => collectResult(calculated)
    case link: LinkEvent => addLink(link)
    case unlink: UnlinkEvent => unlinkEvent(unlink)
<<<<<<< HEAD
<<<<<<< HEAD
=======
    case updateValueEvent: UpdateValueEvent => updateValue(updateValueEvent)
>>>>>>> origin/master
=======
    case updateValueEvent: UpdateValueEvent => updateValue(updateValueEvent)
>>>>>>> origin/master
  }

  private def collectResult(calculated: CalculationResultEvent): Unit = {
    if (attributes.contains(calculated.getConceptName)) {
      val calcState = parametersMap.getOrElse(calculated.getRequest.iteration, Map())
      val newMap = calcState + (calculated.getConceptName -> calculated)
      parametersMap = parametersMap + (calculated.getRequest.iteration -> newMap)
    } else {
      parametersMap = parametersMap + (calculated.getRequest.iteration -> Map(calculated.getConceptName -> calculated))
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

  private def checkAndNotify(request: Request): Unit = {
    val params = parametersMap.getOrElse(request.iteration, Map())
    if (params.keySet == attributes) {
      val previous = results.getOrElse(request.iteration - 1, 0.0)
      val result = calculationState.calculate(params, previous)
      val calculation = CalculationResultEvent(request, conceptName, result)
      results += (request.iteration -> result)
      links.foreach {
        link => link ! calculation
      }
    }
  }

  private def updateValue(updateValueEvent: UpdateValueEvent): Unit = {
    results += (updateValueEvent.getRequest.iteration -> updateValueEvent.getValue)
    val calculation = CalculationResultEvent(updateValueEvent.getRequest, conceptName, updateValueEvent.getValue)
    links.foreach {
      link => link ! calculation
    }
  }
}

object CalculationConcept {
  def apply(concept: String, calculationState: CalculationState): Props = Props(
    new CalculationConcept(concept, calculationState)
  )
}