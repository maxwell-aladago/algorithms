
def memoKnapsack(W, n, values, weights,F):
    """
    implements a memoized dp algorithm for the integer weight knapsack problem.
    :param W:
    :param n:
    :param values:
    :param weights:
    :param F: a (n + 1) x (W + 1) table for memoization
    :return:
    """
    if F[n][W] > float('-inf'):
        return F[n][W]
    elif W == 0 or n == 0:
        optimal_value = 0
    elif W >= weights[n - 1]:
        optimal_value = max(memoKnapsack(W, n - 1, values, weights, F),
                            values[n - 1] + memoKnapsack(W - weights[n - 1], n - 1, values, weights, F))

    else:
        optimal_value = memoKnapsack(W, n - 1, values, weights, F)
    F[n][W] = optimal_value
    return optimal_value


if __name__ == '__main__':

    w = [3, 2, 1, 4, 5]
    v = [25, 20, 15, 40, 50]
    n = 5
    W = 6

    F = [[float('-inf') for _ in range(0, W + 1)] for _ in range(0, n + 1)]

    print(memoKnapsack(6, 5, v, w, F))
    for row in F:
        print(row)




