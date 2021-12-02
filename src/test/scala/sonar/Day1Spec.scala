package sonar

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should._

class Day1Spec extends AnyFunSpec with Matchers {

  describe("Day1") {
    describe("countIncreasing") {
      it ("should work with trivial increasing") {

        // should be 2: 6 is greater than 5 (even though it's not greater than 101) and so it counts
        Day1.countIncreasing(Vector(100,101,5,6)) should equal(2)
      }

      it ("should determine non-increasing sequence") {

        Day1.countIncreasing((0 until 20).reverse.toVector) should equal(0)
      }
    }
  }
}
