package sparkle

import scala.xml._

/*
 * Section: literal support
 */

case class SparkleString(self: String) extends AnyVal {

    /**
     * Create a [[scala.xml.Text]] object from the string
     *
     * @return Text(self)
     */
    def raw: Text = Text(self)

}

/*
 * Section: scala.xml augmentations
 */

/**
 * Wrapper for [[scala.xml.Node]] that adds the sibling operator
 *
 * @param self node to augment
 */
case class SparkleNode(self: Node) extends AnyVal {

    /**
     * Emit a newly created sequence of nodes from a single element containing the
     * Element with its adjacent sibling
     *
     * @param newSibling new sibling
     * @return set of nodes
     */
    def | (newSibling: Node): Seq[Node] = {
        Seq(self, newSibling)
    }

    /**
     * Boo! Undocumented right-associative operator has spooked you.
     * If you don't go complain about it on the mailing list in the next
     * ten seconds, a terrible fate will befall you tonight.
     *
     * In all reality, this nests this element below the left-hand element
     *
     * @param parent element to nest under
     * @return updated parent
     */
    def :: (parent: Elem): Elem = {
        parent.copy(child = parent.child :+ self)
    }

}

/**
 * Wrapper for a [[scala.collection.Seq]] of [[scala.xml.Node]] that adds the sibling operator
 *
 * @param self Seq[Node] to augment
 */
case class SparkleNodeSeq(self: Seq[Node]) extends AnyVal {

    /**
     * Append the specified node to the node sequence
     *
     * @param newSibling node
     * @return updated node sequence
     */
    def | (newSibling: Seq[Node]): Seq[Node] = {
        self ++ newSibling
    }

    /**
     * Append the contents of the node sequence to the specified parent
     *
     * @param parent    parent element
     * @return          updated parent
     */
    def :: (parent: Elem): Elem = {
        parent.copy(child = parent.child ++ self)
    }

}

/**
 * Wrapper for [[scala.xml.Elem]] that adds the nest operators
 *
 * @param self elem to augment
 */
case class SparkleElem(self: Elem) extends AnyVal {

    /**
     * Return the element with minimizeEmpty set to true.
     * Use this on a tag without children to create a self-closing tag
     *
     * @return self-closable tag
     */
    def /> : Elem = {
        self.copy(minimizeEmpty = true)
    }

    /*
     * Looking for `%`?
     *
     * That's implemented by scala.xml.Element already and does exactly what we need it to
     */

}


/**
 * Wrapper for [[scala.xml.MetaData]] that supports appending
 * @param self metadata to augment
 */
case class SparkleMetaData(self: MetaData) extends AnyVal {

    /**
     * Append the specified metadata
     *
     * @param nextData  metadata to append
     * @return updated metadata
     */
    def % (nextData: MetaData): MetaData = {
        self.copy(nextData)
    }

}

/*
 * Section: Sparkle DSL support
 */

/**
 * Monadic builder for metadata to support `'key ~ "value"` syntax
 *
 * @param name metadata name
 */
case class SparkleMetadataBuilder(name: String) extends AnyVal {

    def ~ (value: Any): MetaData = {
        new UnprefixedAttribute(name, Text(value.toString), Node.NoAttributes)
    }

}

/**
 * Monadic builder for [[scala.xml.Elem]] that is functionally similar to glitter's EmptyXml
 *
 * @param name
 */
case class SparkleElementBuilder(name: String) extends AnyVal {

    /**
     * Create an element with the specified children
     *
     * @param children  children to create the element with
     * @return          element
     */
    def apply(children: Iterable[Node]): Elem = {
        new Elem(NoPrefix, name, Node.NoAttributes, TopScope, false)
    }

    /**
     * Var-args form of [[apply(Iterable[Node])]]
     *
     * @param children  children to create the element with
     * @return          element
     */
    def apply(children: Node*): Elem = apply(children)

}