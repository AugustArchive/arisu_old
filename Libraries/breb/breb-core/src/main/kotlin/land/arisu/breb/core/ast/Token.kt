package land.arisu.breb.core.ast

/**
 * Enumeration class of the available tokens when parsing a [Token].
 * @param value The value
 */
enum class TokenKind(val value: kotlin.String) {
    /**
     * Represents a marker that this the start of the file, let's
     * tokenize here!
     */
    StartOfFile("<SOF>"),

    /**
     * Well, we reached the end of the file. Now what?
     */
    EndOfFile("<EOF>"),

    /**
     * Represents a bang declaration, this is when a value
     * isn't null.
     */
    Bang("!"),

    /**
     * Represents a dollar symbol, this is a positional
     * argument when constructing queries or mutations.
     */
    Dollar("$"),
    Ampersand("&"),
    ParenthesisLeft("("),
    ParenthesisRight(")"),
    Spread("..."),
    Colon(":"),
    Equals("="),
    At("@"),
    BracketLeft("["),
    BracketRight("]"),
    BraceLeft("{"),
    BraceRight("}"),
    Pipe("|"),
    Name("Name"),
    Integer("Int"),
    Float("Float"),
    String("String"),
    BlockString("BlockString"),
    Comment("Comment");
}

/**
 * Represents a lexical token, this is the first step in the lexer.
 */
data class Token(
    /**
     * The kind this [Token] is.
     */
    val kind: TokenKind,

    /**
     * The offset of where this [Token] is at.
     */
    val offsetStart: Int = 0,

    /**
     * The offset of where this [Token] ends.
     */
    val offsetEnd: Int = 0,

    /**
     * The line number
     */
    val line: Int = 0,

    /**
     * The column number
     */
    val column: Int = 0,
    val value: String? = null,
    var prevToken: Token? = null,
    var nextToken: Token? = null
)

/*
    /**
     * The 1-indexed line number on which this Token appears.
     */
    val line: Int = 0,

    /**
     * The 1-indexed column number at which this Token begins.
     */
    val column: Int = 0,

    /**
     * For non-punctuation tokens, represents the interpreted value of the token.
     */
    val value: String? = null,

    /**
     * Tokens exist as nodes in a double-linked-list amongst all tokens
     * including ignored tokens. <SOF> is always the first node and <EOF>
     * the last.
     */
    var prev: Token? = null,
    var next: Token? = null
 */
