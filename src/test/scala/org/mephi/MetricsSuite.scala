package org.mephi

import akka.actor.ActorSystem
import org.mephi.calculation.{CommonCalculationState, Request}
import org.mephi.concept.{CalculationConcept, Listener, MultiplyLink}
import org.mephi.events.{CalculationEvent, LinkEvent}
import org.scalatest.funsuite.AnyFunSuite
import org.mephi.metric.{MeanAbsoluteError,MeanSquaredError,RootMeanSquaredError,R2CoefficientError}
import scala.math.sqrt

class MetricsSuite extends AnyFunSuite {

  private val predictedTestData = Seq(1.0, 2.0, 5.0, 6.0)
  private val expectedTestData = Seq(1.0, 2.0, 3.0, 4.0)

  test("Test MeanAbsoluteError") {
    val error = MeanAbsoluteError.calc(predictedTestData, expectedTestData)
    assume(error === 1.0)
  }
  test("Test MeanSquaredError") {
    val error = MeanSquaredError.calc(predictedTestData, expectedTestData)
    assume(error === 2.0)
  }
  test("Test RootMeanSquaredError") {
    val error = RootMeanSquaredError.calc(predictedTestData, expectedTestData)
    assume(error === sqrt(2.0))
  }
  test("Test R2CoefficientError") {
    val error = R2CoefficientError.calc(predictedTestData, expectedTestData)
    assume(error === 1.6)
  }
}
