package sparkle

import org.scalatest.{Matchers, FunSuite}

import scala.xml._

/**
 * Date: 9/23/15
 * Time: 12:40 PM
 */
final class SparkleTest extends FunSuite with Matchers {

    import sparkle._

    test("Strings are converted to text nodes") {
        val sampleText: String = "test"
        val textNode: Node = sampleText

        <test>{sampleText}</test> should equal (<test></test>.copy(child = Seq(textNode)))
    }

    test("Symbols are implicitly converted to nodes") {
        ('test: Elem) should equal (<test></test>)
    }

    test("The minimizing postfix operator should enable automatic minimizing") {
        ('test />) should equal (<test />)
    }

    test("Attribute building") {
        ('test % 'attr ~ "value") should equal (<test attr="value"></test>)
    }

    test("Null attributes") {
        // Two things worth noting: empty attributes can make IntelliJ upset, and scala does not support
        // empty attribute syntax
        ('test % 'attr) should equal (<test attr=""></test>)
    }

    test("Chaining attributes") {
        ('test % 'attr ~ "value" % 'another ~ "value") should equal (<test attr="value" another="value"></test>)
    }

    test("Postfix nesting syntax") {
        ('foo :: 'bar) should equal (<foo><bar></bar></foo>)
        ('foo :: 'bar :: "baz") should equal (<foo><bar>baz</bar></foo>)
    }

    test("Node sequence builder syntax") {
        val content = 'foo | 'bar | "baz"
        // XXX compiler refuses to apply matcher implicits here
        assert(content == Seq(<foo></foo>, <bar></bar>, Text("baz")))
    }

    test("Mixed content") {
        val sparkleDSL = 'foo :: ("bar" | 'baz :: "bah")
        val xml = <foo>bar<baz>bah</baz></foo>

        sparkleDSL should equal (xml)
    }
}
