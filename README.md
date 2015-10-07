# Sparkle

Sparkle is (another) XML DSL for Scala inspired by
[glitter](https://github.com/julienrf/glitter); however, unlike glitter,
sparkle uses Scala's XML support as the underlying XML representation.

The intention behind sparkle is the create an XML AST syntax more
syntactically native to Scala than embedded XML, while remaining fully
compatible with embedded XML. Sparkle is not guaranteed to be fully
compatible with glitter, but takes design inspiration from glitter.

# Why use Sparkle over Glitter?

There are several reasons for this. Firstly, scala.xml literals and
Sparkle can be mixed interchangeably, and all Sparkle operators can be
applied to scala.xml objects, as can all scala.xml operators be applied to
Sparkle objects. Secondly, sparkle is far less complicated than Glitter,
which uses its own XML generator. Another bonus to using scala.xml over
emitting stringified XML is that Sparkle supports its output being
traversed and inspected during runtime, also via scala.xml. This is used
to some extent in Sparkles unit tests. According to
[cloc](https://github.com/AlDanial/cloc), Sparkle is only 69 LOC, whereas
Glitter is 101 LOC. Not that this matters.

Sparkle:

```
-------------------------------------------------------------------------------
Language                     files          blank        comment           code
-------------------------------------------------------------------------------
Scala                            2             49            168             69
-------------------------------------------------------------------------------
SUM:                             2             49            168             69
-------------------------------------------------------------------------------
```

Glitter:

```
-------------------------------------------------------------------------------
Language                     files          blank        comment           code
-------------------------------------------------------------------------------
Scala                            4             44             66            101
-------------------------------------------------------------------------------
SUM:                             4             44             66            101
-------------------------------------------------------------------------------
```

I also feel that Sparkle is better documented (notice the ratio of
comments-to-code in Sparkle, versus in Glitter -- they are almost
opposite!).

Additionally, Sparkle should (theoretically, though I've not tested) use
fewer allocations as it leverages Value types as wrappers around
scala.xml, so all Sparkle classes should become static methods at compile
time.

# Why use Glitter over Sparkle?

scala.xml does not have a means to insert a `<!DOCTYPE .../>` element.
This means that when using scala.xml, and thus Sparkle, you must insert
this after stringifying your XML and before you send it to the client.

Glitter, due to its using text globs instead of XML elements has a more
concise manner of inserting this.

Personally, I think that Sparkle/scala.xml is still a better option.

# Examples

A simple element with some attributes:

```scala
'div % 'class ~ "menu-item" % 'id ~ "menu-item-open" :: "Open a new document"
```

this will become:

```xml
<div class="menu-item" id="menu-item-open">
    Open a new document
</div>
```

A self-closing tag element:

```scala
'hr />
```

this becomes:

```xml
<hr />
```

Making a re-usable template (in MVC terms, a Layout) by embedding Sparkle in a function:

```scala
def StylesheetLink(url: String): Elem = 'link % 'rel ~ "stylesheet" % 'type ~ "text/css" % 'href ~ url 
def ScriptLink(url: String): Elem = 'script % 'src ~ url

def StandardLayout(content: Elem): Elem = {
    'html :: (
        'head :: (
            'title :: "My Sparkle Document"
            | StylesheetLink("/styles/document.css")
            | StylesheetLink("/styles/foundation.css")
            | ScriptLink("/scripts/extremely_massive_minified_javascript_blob.js")
        )
        | 'body :: (
            'nav % 'class ~ "top-bar" % NullAttribute("data-topbar") % 'role ~ "navigation" :: (
                  'li % 'class ~ "name" :: 'hi :: 'a % 'href ~ "#" :: "My Sparkle Document"
                | 'li % 'class ~ "toggle-topbar menu-icon" :: 'a % 'href ~ "#" :: 'span :: "Menu"
            )
            | 'div % 'class ~ "row" % 'id ~ "viewport-row"
                :: 'section % 'class ~ "small-12 small-centered columns" % 'id ~ "viewport"
                    :: content
        )
    )
}
```

# Plans for Sparkle

Personally, I do not feel that sparkle needs much done to it at the
moment, although I may add an `:=` operator to the attribute monad that
behaves identically to the preexisting `~` operator of the same class, as
it's more convenient for me to type than reaching for the grave accent
key/shift in the middle of writing something.
