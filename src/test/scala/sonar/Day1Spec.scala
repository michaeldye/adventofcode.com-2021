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

<<<<<<< HEAD
    describe("windowedInput") {
      it ("should group and sum") {
        Day1.windowedInput(Vector(100,101,5,6,1,201)) should contain allOf(206, 112, 12, 208)
=======
    describe("getMeasurementWindows") {
      it("should calculate a sliding window of 3 measurements") {
        val measurements = Vector(1, 2, 3, 4, 5, 6, 7, 8)
        val windows = Vector((1, 2, 3), (2, 3, 4), (3, 4, 5), (4, 5, 6), (5, 6, 7), (6, 7, 8))

        Day1.getMeasurementWindows(measurements) should equal(windows)
>>>>>>> f95e550 (Add measurement windows for day 1 part 2)
      }
    }
  }
}
