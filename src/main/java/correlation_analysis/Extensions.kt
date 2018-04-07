package correlation_analysis

/**
 * Created by Cyxou on 4/7/18.
 */

fun <T> Collection<T>.combineUnique(): List<Pair<T, T>> {
    return combineUnique { thisItem: T, otherItem: T -> Pair(thisItem, otherItem) }
}

fun <T, R> Collection<T>.combineUnique(transformer: (thisItem: T, otherItem: T) -> R): List<R> {
    return { var index = 0; this.flatMap { thisItem -> this.drop(1 + index++).map { otherItem -> transformer(thisItem, otherItem) } } }()
}
