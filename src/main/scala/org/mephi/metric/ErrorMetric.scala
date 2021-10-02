package org.mephi.metric

trait ErrorMetric {
  def calc(left: Seq[Double], right: Seq[Double]) : Double
}
