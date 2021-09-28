package org.mephi.cm

import akka.actor.{ActorRef, ActorSystem}
import org.mephi.calculation.{CalculationState, CommonCalculationState}
import org.mephi.concept.{CalculationConcept, MultiplyLink}

import scala.util.Random

class CognitiveMapBuilder(actorSystem: ActorSystem) {

  private val random = new Random()

  private var concepts = Set[String]()
  private var outcominglinks = Map[String, Set[String]]()
  private var incomingLinks = Map[String, Set[String]]()

  def withConcept(concept: String): CognitiveMapBuilder = {
    concepts += concept
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

  def build(): CognitiveMap = {
    val conceptMap: Map[String, ActorRef] = concepts.map {
      concept => (concept, createRandomInitConcept(concept, incomingLinks.getOrElse(concept, Set())))
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
                  conceptTo => createRandomInitLink(conceptTo)
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
    actorSystem.actorOf(CalculationConcept(concept, createRandomCalculationState(deps)))
  }

  private def createRandomInitLink(to: ActorRef): ActorRef = {
    actorSystem.actorOf(MultiplyLink(to, 0.5))
  }

  private def createRandomCalculationState(params: Set[String]): CalculationState = {
    new CommonCalculationState(
      params.map {
        param => (param, random.nextDouble())
      }.toMap,
      random.nextDouble()
    )
  }
}
