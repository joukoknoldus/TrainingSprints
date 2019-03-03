object TypeClassExample {
  def main(args: Array[String]) = {

  }
    val jouko=new Human(170.0, 70.0)
    val bob=new Fish(1.0, 10.0)
    val poly=new Bird(10.0, 24.0)

    println("jouko BMI= "+jouko.CalculateBMI())
    println("bob BMI= "+bob.CalculateBMI())
    println("poly BMI= "+poly.CalculateBMI())

    implicit val jouko2=new Species[Human](170.0, 70.0)
    //{
    //  def CalculateBMITrait(human:Human): Double= { human.weight/human.height }}
  //}
}

class SpeciesCharacteristics() {
  def CalculateBMI(): Double = this match {
    case Human(w: Double, h: Double) => { w/h }
    case Fish(w: Double, v: Double) => { w/v }
    case Bird(w: Double, l: Double) => { w/l }
  }
}

case class Human(w: Double, h: Double) extends SpeciesCharacteristics {
  var weight=w
  var height=h
  override def CalculateBMI(): Double = { weight/height }
}

case class Fish(w: Double, v: Double) extends SpeciesCharacteristics {
  var weight=w
  var volume=v
  override def CalculateBMI(): Double = { weight/volume }
}

case class Bird(w: Double, l: Double) extends SpeciesCharacteristics {
  var weight=w
  var wingLength=l
  override def CalculateBMI(): Double = { weight/wingLength }
}

trait Species[T] {
  def CalculateBMITrait(x: T, y: T): T
}


