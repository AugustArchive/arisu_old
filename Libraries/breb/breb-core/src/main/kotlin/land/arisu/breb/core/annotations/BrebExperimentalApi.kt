package land.arisu.breb.core.annotations

/**
 * Marks this object as an experimental API and API subject is to change.
 */
@MustBeDocumented
@RequiresOptIn(
    level = RequiresOptIn.Level.WARNING,
    message = "This declaration is experimental and not made in use for Production. Annotate this declaration with @OptIn(BrebExperimentApi::class) to remove this warning."
)
@Retention(AnnotationRetention.BINARY)
@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.PROPERTY,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.CONSTRUCTOR,
    AnnotationTarget.FIELD,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER,
    AnnotationTarget.TYPEALIAS
)
annotation class BrebExperimentalApi
