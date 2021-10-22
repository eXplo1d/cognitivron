package org.mephi.concept

import akka.actor.{ActorRef, Props}
import org.mephi.calculation.{CalculationState, Request}
import org.mephi.events._

import scala.collection.immutable.HashMap
import scala.collection.mutable.ListBuffer

class CalculationConcept(private val conceptName: String,
                         private val calculationState: CalculationState) extends Concept {

  private var outgoingLinks = Seq[ActorRef]()
  private var incomingLinks = Set[String]()
  private var parametersMap = new HashMap[Int, Map[String, Double]]()
  private var results = new HashMap[Int, Double]()
  private var todoCalculations = new HashMap[Int, CalculationEvent]()

  override def receive: Receive = {
    case setUpValue: SetUpValueEvent => updateValue(setUpValue)
    case link: LinkEvent => addLink(link)
    case unlink: UnlinkEvent => unlinkEvent(unlink)
    case calculation: CalculationEvent => calculate(calculation)
    case delta: BackpropagationEvent => applyDelta(delta)
    case input: CalculationResultEvent => collectResult(input)
  }

  private def collectResult(calculated: CalculationResultEvent): Unit = {
    val calcState = parametersMap.getOrElse(calculated.getRequest.iteration, Map())
    val newMap = calcState + (calculated.getConceptName -> calculated.getResult)
    parametersMap = parametersMap + (calculated.getRequest.iteration -> newMap)
    val iterBuffer = new ListBuffer[Int]()
    for (calc <- todoCalculations) {
      if (checkAndNotify(calc._2.getRequest)) {
        iterBuffer.addOne(calc._1)
      }
    }
    todoCalculations = todoCalculations.removedAll(iterBuffer)
  }

  private def addLink(linkEvent: LinkEvent): Unit = {
    linkEvent.linkType match {
      case LinkTypes.IncomingLink => incomingLinks = incomingLinks ++ Seq(linkEvent.concept)
      case LinkTypes.OutgoingLink =>
        outgoingLinks = outgoingLinks ++ Seq(linkEvent.link)
        linkEvent.link ! linkEvent
    }
  }

  private def unlinkEvent(unlinkEvent: UnlinkEvent): Unit = {
    unlinkEvent.linkType match {
      case LinkTypes.IncomingLink => incomingLinks = incomingLinks -- Seq(unlinkEvent.concept)
      case LinkTypes.OutgoingLink =>
        unlinkEvent.link ! unlinkEvent
    }
  }

  private def calculate(calculationEvent: CalculationEvent): Unit = {
    val iter = calculationEvent.getRequest.iteration
    if (results.contains(iter)) {
      val res = CalculationResultEvent(
        calculationEvent.getRequest,
        this.conceptName,
        results.getOrElse(calculationEvent.getRequest.iteration, 0.0)
      )
      outgoingLinks.foreach { link => link ! res }
    } else {
      if (!checkAndNotify(calculationEvent.getRequest)) {
        todoCalculations += (calculationEvent.getRequest.iteration -> calculationEvent)
      }
    }
  }

  private def applyDelta(delta: BackpropagationEvent): Unit = {
    calculationState.applyDelta(delta.getError)
  }

  private def checkAndNotify(request: Request): Boolean = {
    val params = parametersMap.getOrElse(request.iteration - 1, Map())
    if (params.keySet == incomingLinks) {
      val previous = results.getOrElse(request.iteration - 1, 0.0)
      val result = calculationState.calculate(params, previous)
      val newRequest = request.copy(request.iteration)
      val calculation = CalculationResultEvent(newRequest, conceptName, result)
      results += (newRequest.iteration -> result)
      outgoingLinks.foreach {
        link => link ! calculation
      }
      true
    } else {
      false
    }
  }

  private def updateValue(updateValueEvent: SetUpValueEvent): Unit = {
    results += (updateValueEvent.request.iteration -> updateValueEvent.value)
    val calculation = CalculationResultEvent(updateValueEvent.request, conceptName, updateValueEvent.value)
    outgoingLinks.foreach {
      link => link ! calculation
    }
  }
}

object CalculationConcept {
  def apply(concept: String, calculationState: CalculationState): Props = Props(
    new CalculationConcept(concept, calculationState)
  )
}