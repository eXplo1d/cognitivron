package org.mephi.cm

import akka.actor.{ActorRef, ActorSystem}
import org.mephi.calculation.Request
import org.mephi.concept.Listener
import org.mephi.events._

import scala.collection.immutable.Range
import scala.collection.mutable

class CommonCognitiveMap(private val concepts: Map[String, ActorRef],
                         private val concept2links: Map[String, Set[ActorRef]],
                         private val actorSystem: ActorSystem) extends CognitiveMap {
  build()
  private var currentIteration: Int = 0
  private val observer = new CmResultObserver(concepts.keySet)
  applyObserver(observer, concepts.values.toList)

  override def copy(): CognitiveMap = {
    this
  }

  override def removeLink(concept: String, link: ActorRef): Unit = {
    concept2links.get(concept).foreach {
      links =>
        if (links.contains(link)) {
          concepts.get(concept).foreach {
            conceptActor => conceptActor ! UnlinkEvent(link)
          }
        }
    }
  }

  override def getConcept2Link: Seq[(String, ActorRef)] = {
    concept2links.toSeq.flatMap {
      concept2links =>
        concept2links._2.map {
          link => (concept2links._1, link)
        }
    }
  }

  override def addLink(concept: String, link: ActorRef): Unit = {
    concepts.get(concept).foreach {
      conceptRef => conceptRef ! LinkEvent(link)
    }
  }

  override def updateValue(concept: String, value: Double): Unit = {
    concepts.get(concept).foreach {
      conceptActor =>
        conceptActor ! new UpdateValueEvent {
          override def getRequest: Request = Request(currentIteration)

          override def getValue: Double = value
        }
    }
  }

  override def makeIterations(n: Int): Map[String, List[Double]] = {
    val iterRange = Range(currentIteration, currentIteration + n)
    for (i <- iterRange) {
      currentIteration = i
      val req = Request(currentIteration)
      concepts.values.foreach {
        concept =>
          concept ! new CalculationEvent {
            override def getRequest: Request = req
          }
      }
    }
    observer
      .getIfReady(iterRange)
      .map(kv => (
        kv._1,
        kv._2
          .sortWith((left, right) => left.getRequest.iteration < right.getRequest.iteration)
          .map {
            calc => calc.getResult
          })
      )
  }

  private def build(): Unit = {
    concept2links.foreach {
      kv =>
        concepts.get(kv._1).foreach {
          concept =>
            kv._2.foreach {
              link => concept ! LinkEvent(link)
            }
        }
    }
  }

  private def applyObserver(observer: CmResultObserver, concepts: Seq[ActorRef]): Unit = {
    val listener = actorSystem.actorOf(Listener(calc => observer.collect(calc)))
    concepts.foreach {
      concept =>
        concept ! new LinkEvent {
          override def getLink: ActorRef = listener
        }
    }
  }
}

private[cm] class CmResultObserver(concepts: Set[String]) {
  private val dataSet: mutable.Map[String, mutable.HashMap[Int, CalculationResultEvent]] = buildMap(concepts)

  def collect(calculationResultEvent: CalculationResultEvent): Unit = {
    val concept = calculationResultEvent.getConceptName
    val conceptData = dataSet.getOrElse(concept, mutable.HashMap())
    conceptData.put(calculationResultEvent.getRequest.iteration, calculationResultEvent)
  }

  def getIfReady(iterRange: Range): Map[String, List[CalculationResultEvent]] = {
    var ready = false
    while (!ready) {
      ready = dataSet.values.forall {
        conceptCalculation => iterRange.forall(i => conceptCalculation.contains(i))
      }
    }
    copyValue(dataSet, iterRange)
  }

  private def copyValue(dataSet: mutable.Map[String, mutable.HashMap[Int, CalculationResultEvent]],
                        range: Range): Map[String, List[CalculationResultEvent]] = {
    dataSet.map {
      concept2data =>
        (concept2data._1, concept2data
          ._2
          .filter(calc => range.contains(calc._1)).values
          .toList)
    }.toMap
  }

  private def buildMap(concepts: Set[String]): mutable.HashMap[String, mutable.HashMap[Int, CalculationResultEvent]] = {
    val map = mutable.HashMap[String, mutable.HashMap[Int, CalculationResultEvent]]()
    for (concept <- concepts) {
      if (!map.contains(concept)) {
        map.put(concept, new mutable.HashMap[Int, CalculationResultEvent]())
      }
    }
    map
  }
}