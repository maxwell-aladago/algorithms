def determinant(a, b, c):
    """
    computes the distance and direction of a point c relative to the line segment ab
    :param a: a tuple, the first point in the line segment
    :param b: a tuple, the second point in the line segment
    :param c: a tuple, the point to determine distance and direction
    :return:
    """
    # This formula is taken from Levitin, the course book. Half of this value is the
    # area under the triangle abc.

    return a[0] * b[1] + c[0] * a[1] + b[0] * c[1] - c[0] * b[1] - b[0] * a[1] - a[0] * c[1]


def convex_hull(points):
    """
    Computes the convex hull of a set of 2-d points
    :param points: A list of tuples. The points to compute the convex-hull off
    :return: The A list of points forming the convex hull. The points are listed clockwisely
    in the order in which they occur in the hull
    """
    points.sort()
    a = points[0]
    b = points[-1]

    upperhull = []
    lowerhull = []

    for i in range(1, len(points) - 1):
        d = determinant(a, b, points[i])
        if d > 0:
            upperhull.append(points[i])
        if d < 0:
            lowerhull.append(points[i])

    convex_set = [a]
    construct_hull(upperhull, a, b, convex_set)
    convex_set.append(b)
    construct_hull(lowerhull, b, a, convex_set)

    return convex_set


def construct_hull(points, a, b, convex_set):
    """
    Constructs convex hull of a set of points recursively given the extreme points
    :param points: The set of points to compute the convex-hull of
    :param a: The starting point of the line segment
    :param b: The end point of the line segment
    :param convex_set: the convex set so far
    :return:
    """
    if points:
        # Remove these comments if using the convex hull for the decision problem.
        # if len(convex_set) > 3:
        #    return False
        #
        left_most_point = None
        left_most_point_d = float('-inf')
        left_most_points = []

        for point in points:
            d = determinant(a, b, point)
            if d > 0:
                left_most_points.append(point)
                if d > left_most_point_d:
                    left_most_point_d = d
                    left_most_point = point

        if left_most_point:
            construct_hull(left_most_points, a, left_most_point, convex_set)
            convex_set.append(left_most_point)
            construct_hull(left_most_points, left_most_point, b, convex_set)


def main():
    # this is just a rough test of the algorithm. it contains points from both the lower hull and the upper hull.
    # The expected convex hull printed clockwisely is {(0, 0), (0, 3), (3, 3), (3, 0), (2, -4), (1, -3)}

    # Test the algorithm on other inputs and report any bugs to me (fix them if you can)
    points = [(0, 3), (2, 2), (1, 1), (2, 1), (3, 0), (0, 0), (3, 3), (2, -1), (2, -4), (1, -3)]
    hull = convex_hull(points)
    print(hull)


main()

