package org.mephi.cm

import akka.actor.{ActorRef, ActorSystem}
import org.mephi.calculation.{CalculationState, CommonCalculationState, Linear, Sigmoid}
import org.mephi.concept.{CalculationConcept, MultiplyLink}

class CognitiveMapBuilder(actorSystem: ActorSystem) {

  private var concepts = Set[String]()
  private var outgoinglinks = Map[String, Set[String]]()
  private var incomingLinks = Map[String, Set[String]]()
  private var concept2state = Map[String, CalculationStateBuilder => CalculationStateBuilder]()
  private var link2weight = Map[String, Double]()

  def withConcept(concept: String,
                  calculationStateBuilder: CalculationStateBuilder => CalculationStateBuilder = builder => builder.sigmoid()): CognitiveMapBuilder = {
    concepts += concept
    if (calculationStateBuilder != null) {
      concept2state += (concept -> calculationStateBuilder)
    }
    this
  }

  def withLink(from: String, to: String): CognitiveMapBuilder = {
    var conceptOutLinks = outgoinglinks.getOrElse(from, Set())
    conceptOutLinks += to
    outgoinglinks += (from -> conceptOutLinks)

    var conceptIncLinks = incomingLinks.getOrElse(to, Set())
    conceptIncLinks += from
    incomingLinks += (to -> conceptIncLinks)
    this
  }

  def withLink(from: String, to: String, weight: Double): CognitiveMapBuilder = {
    withLink(from, to)
    link2weight += (s"$from$to" -> weight)
    this
  }

  def build(): CognitiveMap = {
    val conceptMap: Map[String, ActorRef] = concepts.map {
      concept => (concept, createRandomInitConcept(concept))
    }.toMap

    val concept2Links: Map[String, Set[ActorRef]] = outgoinglinks.map {
      conceptFrom =>
        val linkActors = conceptFrom
          ._2
          .map {
            conceptToName =>
              conceptMap
                .get(conceptToName)
                .map {
                  conceptTo => createRandomInitLink(conceptFrom._1, conceptToName, conceptTo)
                }
          }.filter(opt => opt.isDefined)
          .map {
            opt => opt.get
          }
        (conceptFrom._1, linkActors)
    }
    new CommonCognitiveMap(conceptMap, concept2Links, actorSystem)
  }

  private def createRandomInitConcept(concept: String): ActorRef = {
    val calcStateBuilder: CalculationStateBuilder => CalculationStateBuilder = concept2state.getOrElse(
      concept,
      builder => builder.sigmoid()
    )
    actorSystem.actorOf(CalculationConcept(concept, calcStateBuilder(new CalculationStateBuilder).build()))
  }

  private def createRandomInitLink(from: String, to: String, toActor: ActorRef): ActorRef = {
    val weight: Double = link2weight.getOrElse(s"$from$to", 0.5)
    actorSystem.actorOf(MultiplyLink(toActor, weight))
  }
}

object CognitiveMapBuilder {
  def apply(actorSystem: ActorSystem): CognitiveMapBuilder = {
    new CognitiveMapBuilder(actorSystem)
  }
}

class CalculationStateBuilder {

  private var applicative: CalculationState = new Sigmoid()

  def sigmoid(m: Double = 0.5): CalculationStateBuilder = {
    applicative = new Sigmoid(m)
    this
  }

  def linear(): CalculationStateBuilder = {
    applicative = new Linear()
    this
  }

  def build(): CalculationState = {
    if (applicative != null) {
      applicative
    } else {
      new CommonCalculationState()
    }
  }
}