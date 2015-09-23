import scala.xml._

/**
 * Date: 9/23/15
 * Time: 10:49 AM
 */
package object sparkle {

    type AttributeValue = Seq[Node]

    val NoPrefix = null // why did scala go with null here?

    /*
     * Section: scala.xml enhancements
     */

    implicit def StringNode(nodeContent: String): SparkleNode = AugmentScalaNode(Text(nodeContent))
    implicit def SymbolicNode(node: Symbol): SparkleNode = AugmentScalaNode(node)
    implicit def AugmentScalaNode(node: Node): SparkleNode = SparkleNode(node)

    implicit def AugmentScalaNodes(nodes: Seq[Node]): SparkleNodeSeq = SparkleNodeSeq(nodes)

    implicit def SymbolicElem(elem: Symbol): SparkleElem = SparkleElem(elem)
    implicit def AugmentScalaElem(elem: Elem): SparkleElem = SparkleElem(elem)

    implicit def AugmentScalaMetadata(metaData: MetaData): SparkleMetaData = SparkleMetaData(metaData)

    /*
     * Section: utilities
     */

    /**
     * Construct a text attribute, with or without a prefix
     *
     * @param key       Attribute name
     * @param value     Attribute value
     * @param prefix    Attribute prefix, if applicable
     * @return          Attribute
     */
    def TextAttribute(key: String, value: String, prefix: Option[String] = None): Attribute = prefix match {
        case Some(prefix) ⇒ new PrefixedAttribute(prefix, key, Text(value), Node.NoAttributes)
        case None ⇒ new UnprefixedAttribute(key, Text(value), Node.NoAttributes)
    }

    /**
     * Create a null attribute, e.g., an attribute that does not specify a name:
     *
     *      `<element nullAttribute />`
     *
     * @param key       Attribute name
     * @param prefix    Attribute prefix, if applicable
     * @return          Attribute
     */
    def NullAttribute(key: String, prefix: Option[String] = None): Attribute = prefix match {
        case Some(prefix) ⇒ new PrefixedAttribute(prefix, key, Nil, Node.NoAttributes)
        case None ⇒ new UnprefixedAttribute(key, Nil, Node.NoAttributes)
    }

    /*
     * Section: literal enhancements
     */

    /**
     * Convert text to a node where needed by creating a [[scala.xml.Text]] node with the
     * string.
     *
     * @param string    string
     * @return          Text(string)
     */
    implicit def StringToNode(string: String): Node = Text(string)

    implicit def AugmentLiteralString(string: String): SparkleString = SparkleString(string)

    /**
     * Create an [[sparkle.SparkleMetadataBuilder attribute builder monad]] from a symbol
     *
     * @param symbol    symbol representing the attribute name
     * @return          an attribute builder
     */
    implicit def BuildMetadataFromSymbol(symbol: Symbol): SparkleMetadataBuilder = SparkleMetadataBuilder(symbol.name)

    /**
     * Create a null attribute (an attribute without a value) from a symbol
     *
     * @param symbol    symbol representing the attribute name
     * @return          an attribute
     */
    implicit def NullAttributeFromSymbol(symbol: Symbol): MetaData = NullAttribute(symbol.name)

    /**
     * Create an attribute from a symbol
     *
     * @param tuple ('Key, Value) tuple
     * @return      attribute
     */
    implicit def AttributeFromTuple(tuple: (Symbol, AnyVal)): Attribute = TextAttribute(tuple._1.name, tuple._2.toString)

    /**
     * Create an element builder from a symbol
     *
     * @param symbol    symbol representing an element name
     * @return          element builder
     */
    implicit def ElementBuilderFromSymbol(symbol: Symbol): SparkleElementBuilder = SparkleElementBuilder(symbol.name)

    /**
     * Create an element from a symbol
     *
     * @param symbol    symbol representing an element name
     * @return          element
     */
    implicit def ElementFromSymbol(symbol: Symbol): Elem = new Elem(null, symbol.name, Node.NoAttributes, TopScope, false)
}
