package com.gt.functional.todolist.pure

typealias State<S, A> = (S) -> Pair<A, S>

//fun <S, A, B> map(sa: (S) -> Pair<A, B>, f: (A) -> B): (S) -> Pair<B, S> = TODO()

val int: State<RNG, Int> = { rng -> rng.nextInt() }

fun ints(x: Int): State<RNG, List<Int>> {
    fun loop(xs: List<Int>, x: Int, rng: RNG): Pair<List<Int>, RNG> =
        when (x == 0) {
            true -> xs to rng
            false -> {
                val (nextInt, newRng) = rng.nextInt()
                loop(xs + nextInt, x - 1, newRng)
            }
        }

    return { rng -> loop(emptyList(), x, rng) }
}

fun ints2Iterative(x: Int): State<RNG, List<Int>> {
    return { rng ->
        (1..x)
            .fold(Pair(emptyList(), rng)) { (acc, r), _ ->
                val (nextInt, newRng) = r.nextInt()
                Pair(acc + nextInt, newRng)
            }
    }
}


fun <A, B> flatMap(s: State<RNG, A>, f: (A) -> State<RNG, B>): State<RNG, B> = TODO()

fun <A, B> map(s: State<RNG, A>, f: (A) -> B): State<RNG, B> = { rng ->
    val (a, n) = s(rng)
    f(a) to n
}