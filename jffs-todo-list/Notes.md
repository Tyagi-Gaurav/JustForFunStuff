# Referentially Transparent (RT)
* In any program, the expression can be replaced by its result without changing the meaning of the program.
*  And we say that a function is pure if calling it with RT arguments is also RT
* An expression `e` is referentially transparent if, for all programs `p`, all occurrences of `e` in `p` can be replaced 
  by the result of evaluating `e` without affecting the meaning of `p`.
* A function `f` is pure if the expression `f(x)` is referentially transparent for all referentially transparent `x`.

# Partial functions
* A function is typically partial because it makes some assumptions about its inputs that aren’t implied by the input types