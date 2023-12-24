import kotlin.math.abs

fun main() = test(
    ::solve1,
)

val STONE_REGEX = """(.+?), +(.+?), +(.+?) +@ +(.+?), +(.+?), +(.+?)""".toRegex()

private fun solve1(input: List<String>): Long {

    data class Stone(val pos: Pair<Long, Long>, val vel: Pair<Long, Long>)

    fun intersectXY(a: Stone, b: Stone): Triple<Pair<Double, Double>, Double, Double>? {
        val apx = a.pos.first
        val apy = a.pos.second
        val avx = a.vel.first
        val avy = a.vel.second
        val bpx = b.pos.first
        val bpy = b.pos.second
        val bvx = b.vel.first
        val bvy = b.vel.second

        val div = bvx * avy - bvy * avx
        if (div == 0L) return null

        val tb = 1.0 * ((bpy - apy) * avx - (bpx - apx) * avy) / div
        val ta = if (avx != 0L) (bpx + tb*bvx - apx) / avx else (bpy + tb*bvy - apy) / avy

        val rx = apx + ta * avx
        val ry = apy + ta * avy

        assert(abs((rx - (bpx + tb * bvx))/rx) < 0.0001)
        assert(abs((ry - (bpy + tb * bvy))/ry) < 0.0001)

        return Triple(Pair(rx, ry), ta, tb)
    }

    val min = 200_000_000_000_000
    val max = 400_000_000_000_000

    val stones = input.map {
        val (px, py, pz, vx, vy, vz) = STONE_REGEX.matchEntire(it)!!.groupValues.drop(1).map { it.toLong() }
        Stone(px to py, vx to vy)
    }

    return (0 until stones.size).sumOf { i ->
        val si = stones[i]
        (i + 1 until stones.size).sumOf { j ->
            val sj = stones[j]

            val (xy, ti, tj) = intersectXY(si, sj) ?: return@sumOf 0L // don't count parallel
            if (ti < 0 || tj < 0) return@sumOf 0L // don't count past

            val (x, y) = xy
            if (x < min || x > max || y < min || y > max) return@sumOf 0L // don't count out of range

            1L
        }
    }
}