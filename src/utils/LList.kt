package utils

class LList<T>(val head: T, val tail: LList<T>?) {
    // For debugging purposes.
    override fun toString(): String =
        (this as LList<T>?).toString()
}

fun <T> LList<T>?.toList(): List<T> =
    when (this) {
        null ->
            emptyList<T>()
        else ->
            buildList<T> {
                var cur: LList<T>? = this@toList
                while (cur != null) {
                    add(cur.head)
                    cur = cur.tail
                }
            }
    }

fun <T> LList<T>?.toString(): String =
    this.toList().toString()

fun <T> Iterable<T>.toLList(): LList<T>? =
    this.iterator().toLList()

fun <T> Iterator<T>.toLList(): LList<T>? =
    if (this.hasNext()) LList(this.next(), this.toLList()) else null

