package org.mephi.events

import org.mephi.calculation.Request

case class SetUpValueEvent(request: Request, value: Double)
