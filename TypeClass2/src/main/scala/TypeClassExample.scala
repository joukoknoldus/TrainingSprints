object TypeClassExample {
  def main(args: Array[String]): Unit =
  {
    val radius=1.0
    val distance=1.3
    val epsilon=1.2
    val a=0-1844.0
    val b=0.34
    val c=0-192.58
    val vdw: TwoAtoms = VanDerWaals(radius, epsilon, distance)
    val calcObject=new Calc(vdw)
    println("Distance before optimization: "+vdw.distance)
    val optimizedVdwDistance=calcObject.optimizeDistance
    println("Distance After optimization: "+optimizedVdwDistance)


    val buck: TwoAtoms = Buckingham(a, b, c, distance)
    val calcObject2=new Calc(buck)
    println("Distance before optimization: "+buck.distance)
    val optimizedBuckDistance=calcObject2.optimizeDistance
    println("Distance After optimization: "+optimizedBuckDistance)

    val poly: Animal = Bird(5.0, 4.0, 3.0)
    val bob: Animal = Fish(7.0, 8.0, 9.0)
    val zoo: Animals[Animal] = new Animals(Seq[Animal](poly, bob))
    zoo.printAnimals
  }
}

abstract class Animal(w: Double, h: Double) {
  val weight=w
  val height=h
  def printAnimal(): Unit
}

case class Bird(l: Double, w: Double, h: Double) extends Animal(w: Double, h: Double) {
  val wingSpan=l
  def printAnimal(): Unit = {
    println("Weight: "+ this.weight+" Height: "+this.height+" Wing Span: "+this.wingSpan)
  }
}

case class Fish(b: Double, w: Double, h: Double) extends Animal(w: Double, h: Double) {
  val buoyancy=b
  def printAnimal(): Unit = {
    println("Weight: " + this.weight + " Height: " + this.height + " Buoyancy: " + this.buoyancy)
  }
}

class Animals[A <: Animal](animals: Seq[A]) {
  def printAnimals: Unit= animals.foreach(_.printAnimal)
}

abstract class TwoAtoms(d: Double) {
  val distance=d
  def calcU(): Double
  def copyAtomPairWithNewDistance(d: Double): TwoAtoms
}

case class VanDerWaals(r: Double, e: Double, d: Double) extends TwoAtoms(d: Double) {
  val radius=r
  val epsilon=e

  def calcU(): Double = {
    val r1=this.radius/this.distance
    val r3=r1*r1*r1
    val r6=r3*r3
    val r12=r6*r6
    this.epsilon*(r12-r6)
  }

  def copyAtomPairWithNewDistance(d: Double): VanDerWaals = {
    VanDerWaals(this.r, this.e, d)
  }
}

case class Buckingham(aij: Double, bij: Double, cij: Double, d: Double) extends TwoAtoms(d: Double) {
  val a=aij
  val b=bij
  val c=cij

  def calcU(): Double = {
    val r1=1.0/this.distance
    val r3=r1*r1*r1
    val r6=r3*r3
    this.a*Math.exp(-this.b*this.distance)-this.c*r6
  }

  def copyAtomPairWithNewDistance(d: Double): Buckingham = {
    Buckingham(this.a, this.b, this.c, d)
  }

}


class Calc[A <: TwoAtoms](atomPair: A) {

  def optimizeDistanceRecursive[A <: TwoAtoms](newAtomPair: A, oldU: Double, stepSize: Double, maxIterations: Int): Double = {
    if (maxIterations==0) {return newAtomPair.distance}
    //println("distance= "+newAtomPair.distance)
    val u=newAtomPair.calcU()
    if (u<oldU) {
      val newAtomPair2 = atomPair.copyAtomPairWithNewDistance(newAtomPair.distance + stepSize)
      optimizeDistanceRecursive(newAtomPair2, u, stepSize*1.1, maxIterations-1)
    }
    else {
      val newAtomPair2 = atomPair.copyAtomPairWithNewDistance(newAtomPair.distance - stepSize*1.5)
      optimizeDistanceRecursive(newAtomPair2, oldU, -stepSize*0.5, maxIterations-1)
    }
  }

  def optimizeDistance: Double = {
    val stepSize: Double = 0.1
    val oldU = atomPair.calcU()
    val maxIterations = 20
    val radius=1.0
    val distance=1.3
    val epsilon=1.2
    val newAtomPair = atomPair.copyAtomPairWithNewDistance(atomPair.distance + stepSize)
    optimizeDistanceRecursive(newAtomPair, oldU, stepSize, maxIterations)
  }
}

/*
class Calc[A <: TwoAtoms](atomPair: A) {
  def calcU2: Double = {
    return atomPair.calcU()
  }

  def optimizeDistance: Double = {
    var stepSize: Double=0.1
    var oldU=atomPair.calcU()
    var i=0
    for (i <- 1 to 10) {
      atomPair.distance+=stepSize
      var u=atomPair.calcU()
      if (u>oldU) {
        atomPair.distance-=stepSize
        stepSize=0-stepSize*0.5
      }
      else {
        stepSize=stepSize*1.1
        oldU=u
      }
    }
    oldU
  }

}
*/
