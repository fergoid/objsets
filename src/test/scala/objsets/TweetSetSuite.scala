package objsets

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

import scala.annotation.tailrec

@RunWith(classOf[JUnitRunner])
class TweetSetSuite extends FunSuite {

  trait TestSets {
    val set1 = new Empty
    val set2 = set1.incl(new Tweet("a", "a body", 20))
    val set3 = set2.incl(new Tweet("b", "b body", 20))
    val c = new Tweet("c", "c body", 7)
    val d = new Tweet("d", "d body", 9)
    val set4c = set3.incl(c)
    val set4d = set3.incl(d)
    val set5 = set4c.incl(d)
    var set6 = set5.incl(new Tweet("m", " m body", 21))
    set6 = set6.incl(c)
    set6 = set6.incl(d)
    set6.incl(d)

  }

  def asSet(tweets: TweetSet): Set[Tweet] = {
    var res = Set[Tweet]()
    tweets.foreach(res += _)
    res
  }

  def size(set: TweetSet): Int = asSet(set).size

  test("filter: on empty set") {
    new TestSets {
      assert(size(set1.filter(tw => tw.user == "a")) === 0)
    }
  }

  test("filter: a on set5") {
    new TestSets {
      set5.toString
      assert(size(set5.filter(tw => tw.user == "a")) === 1)
    }
  }

  test("filter: 20 on set5") {
    new TestSets {
      assert(size(set5.filter(tw => tw.retweets == 20)) === 2)
    }
  }

  test("filter: < 10 on set5") {
    new TestSets {
      assert(size(set5.filter(tw => tw.retweets < 10)) === 2)
    }
  }

  test("filter: > a  on set5") {
    new TestSets {
      assert(size(set5.filter(tw => tw.user > "a")) === 3)
    }
  }

  test("filter: > a  on set1") {
    new TestSets {
      assert(size(set1.filter(tw => tw.user > "a")) === 0)
    }
  }

  test("union: set4c and set4d") {
    new TestSets {
      assert(size(set4c.union(set4d)) === 4)
    }
  }


  test("union: with empty set (1)") {
    new TestSets {
      assert(size(set5.union(set1)) === 4)
    }
  }

  test("union: with empty set (2)") {
    new TestSets {
      assert(size(set1.union(set5)) === 4)
    }
  }

  test("retweets: set5") {
    new TestSets {
      assert(set5.mostRetweeted.user == "a")
    }
  }

  test("retweets: set1") {
    new TestSets {
      intercept[NoSuchElementException] {
        set1.mostRetweeted.user
      }
      assert(true)
    }
  }

  test("retweets: exception set5") {
    new TestSets {
      try {
        set5.mostRetweeted.user
        assert(true)
      } catch {
        case _: NoSuchElementException => fail()
      }
    }
  }

  test("retweets: set6") {
    new TestSets {
      assert(set6.mostRetweeted.user == "m")
    }
  }

  test("descending: set5") {
    new TestSets {
      val trends = set5.descendingByRetweet
      assert(!trends.isEmpty)
      assert(trends.head.user == "a" || trends.head.user == "b")
    }
  }

  test("descending: set6") {
    new TestSets {
      val trends = set6.descendingByRetweet
      assert(!trends.isEmpty)
      assert(trends.head.user == "m")
    }
  }

  test("descending: set1") {
    new TestSets {
      val trends = set1.descendingByRetweet
      assert(trends.isEmpty)
    }
  }

  test("descending: Gizmodo") {
    val s = TweetReader.tweetSets
    println(s.head.mostRetweeted)
    println(size(s.head))
    println(s.head.descendingByRetweet)
    //s.head.descendingByRetweet foreach println
    assert(s.head.descendingByRetweet.head.retweets == s.head.mostRetweeted.retweets)
  }


}
