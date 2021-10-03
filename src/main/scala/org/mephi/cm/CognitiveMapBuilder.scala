package org.mephi.cm

import akka.actor.{ActorRef, ActorSystem}
import org.mephi.calculation.{CalculationState, CommonCalculationState, IdentityConstValueCalculationState}
import org.mephi.concept.{CalculationConcept, MultiplyLink}

import scala.util.Random

class CognitiveMapBuilder(actorSystem: ActorSystem) {

  private val random = new Random()

  private var concepts = Set[String]()
  private var outcominglinks = Map[String, Set[String]]()
  private var incomingLinks = Map[String, Set[String]]()
  private var concept2state = Map[String, CalculationStateBuilder => CalculationStateBuilder]()
  private var link2weight = Map[String, Double]()

  def withConcept(concept: String,
                  calculationStateBuilder: CalculationStateBuilder => CalculationStateBuilder = null): CognitiveMapBuilder = {
    concepts += concept
    if (calculationStateBuilder != null) {
      concept2state += (concept -> calculationStateBuilder)
    }
    this
  }

  def withLink(from: String, to: String): CognitiveMapBuilder = {
    var conceptOutLinks = outcominglinks.getOrElse(from, Set())
    conceptOutLinks += to
    outcominglinks += (from -> conceptOutLinks)

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
      concept =>
        (concept, createRandomInitConcept(concept, incomingLinks.getOrElse(concept, Set())))
    }.toMap

    val concept2Links: Map[String, Set[ActorRef]] = outcominglinks.map {
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

  private def createRandomInitConcept(concept: String, deps: Set[String]): ActorRef = {
    val calcStateBuilder: CalculationStateBuilder => CalculationStateBuilder = concept2state.getOrElse(
      concept,
      builder => createRandomCalculationState(builder, deps)
    )
    actorSystem.actorOf(CalculationConcept(concept, calcStateBuilder(new CalculationStateBuilder).build()))
  }

  private def createRandomInitLink(from: String, to: String, toActor: ActorRef): ActorRef = {
    val weight: Double = link2weight.getOrElse(s"$from$to", 0.5)
    actorSystem.actorOf(MultiplyLink(toActor, weight))
  }

  private def createRandomCalculationState(calculationStateBuilder: CalculationStateBuilder,
                                           params: Set[String]
                                          ): CalculationStateBuilder = {
    params.foreach {
      param => calculationStateBuilder.withFactor(param, random.nextDouble())
    }
    calculationStateBuilder.withConst(random.nextDouble())
    calculationStateBuilder
  }
}

object CognitiveMapBuilder {
  def apply(actorSystem: ActorSystem): CognitiveMapBuilder = {
    new CognitiveMapBuilder(actorSystem)
  }
}


class CalculationStateBuilder {

  private var formula: Map[String, Double] = Map()
  private var otherConcept: CalculationState = IdentityConstValueCalculationState
  private var const: Double = 0.0

  def withFactor(name: String, value: Double): CalculationStateBuilder = {
    this.formula += (name -> value)
    this
  }

  def wrapCalculation(other: CalculationState): CalculationStateBuilder = {
    this.otherConcept = other
    this
  }

  def withConst(const: Double): CalculationStateBuilder = {
    this.const = const
    this
  }

  def build(): CalculationState = {
    new CommonCalculationState(formula, const, otherConcept)
  }
}