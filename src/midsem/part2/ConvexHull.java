/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package midsem.part2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

/**
 *
 * @author aladago
 */
public class ConvexHull {

    private ArrayList<Point> convexPoints = new ArrayList<>();

    private static class Point implements Comparable {

        int lat, lon;

        Point(int la, int lo) {
            lat = la;
            lon = lo;
        }

        @Override
        public int compareTo(Object o) {
            Point p = (Point) o;
            if (this.lat > p.lat) {
                return 1;
            } else if (this.lat < p.lat) {
                return - 1;
            } else if (this.lon > p.lon) { //consider latitudes when the latitudes are the same
                return 1;
            } else {
                return -1;
            }
        }

    }

    private void mergeSortPoints(Point[] coordinates) {
        if (coordinates.length > 1) {
            int mid = coordinates.length / 2;
            int ln = coordinates.length;
            Point[] A = new Point[mid];
            Point[] B = new Point[ln - mid];
            for (int i = 0; i < mid; i++) {
                A[i] = coordinates[i];
            }

            for (int j = 0; j + mid < coordinates.length; j++) {
                B[j] = coordinates[mid + j];
            }

            mergeSortPoints(A);
            mergeSortPoints(B);
            merge(A, B, coordinates);
        }
    }

    /**
     *
     * @param A
     * @param B
     * @param C
     */
    private void merge(Point[] A, Point[] B, Point[] C) {
        int i = 0;
        int j = 0;
        int k = 0;
        int lnA = A.length;
        int lnB = B.length;
        while (i < lnA && j < lnB) {
            if (A[i].compareTo(B[j]) == -1) {
                C[k++] = A[i++];
            } else {
                C[k++] = B[j++];
            }
        }

        if (i == lnA) {
            while (j < lnB) {
                C[k++] = B[j++];
            }
        } else {
            while (i < lnA) {
                C[k++] = A[i++];

            }
        }
    }

    private void initialize(Point[] coordinates) {
        if (coordinates.length < 2) {
            return;
        }

        Point leftMostPoint = coordinates[0];
        Point rightMostPoint = coordinates[coordinates.length - 1];
        this.convexPoints.add(leftMostPoint);
        this.convexPoints.add(rightMostPoint);

        Point upperConvex = null; //coordinate of upper set
        Point lowerConvex = null; //coordinate of lower set
        float farthestUpperDist = 0;
        float farthestLowerDistance = 0;
        ArrayList<Point> upperPoints = new ArrayList<>();
        ArrayList<Point> lowerPoints = new ArrayList<>();

        for (int i = 1; i < coordinates.length - 1; i++) {
            Point curPoint = coordinates[i];
            float distance = this.getDistance(leftMostPoint, rightMostPoint, curPoint);
            if (distance > 0) {
                upperPoints.add(coordinates[i]);
                if (distance > farthestUpperDist) {
                    farthestUpperDist = distance;
                    upperConvex = curPoint;
                }

            } else if (distance < 0) {
                lowerPoints.add(curPoint);

                //the determinant of the points to the right (lower) of 
                //leftMostRightMost is always negative
                if (distance < farthestLowerDistance) {
                    farthestLowerDistance = distance;
                    lowerConvex = curPoint;
                }
            }
        }
        if (upperConvex != null) {
            this.convexPoints.add(upperConvex);
            this.markConvexPoints(upperPoints, leftMostPoint, upperConvex, rightMostPoint, true);
        }

        if (lowerConvex != null) {
            this.convexPoints.add(lowerConvex);
            //System.out.print(lowerConvex.lat + " " + lowerConvex.lon);
            this.markConvexPoints(lowerPoints, leftMostPoint, lowerConvex, rightMostPoint, false);

        }

    }

    private float getDistance(Point P1, Point P2, Point Pi) {
        float sum = P1.lat * P2.lon + Pi.lat * P1.lon + P2.lat * Pi.lon;
        float diff = Pi.lat * P2.lon + P1.lat * Pi.lon + P2.lat * P1.lon;
        return sum - diff;
    }

    /**
     *
     * @param points
     * @param point1
     * @param farthest
     * @param point2
     */
    private void markConvexPoints(ArrayList<Point> points, Point point1, Point farthest, Point point2, boolean upper) {

        if (points.size() > 1) {
            ArrayList<Point> leftHull = new ArrayList<>();
            ArrayList<Point> rightHull = new ArrayList<>();

            Point farthestPointLeft = null;
            Point farthestPointRight = null;

            float farthestLeftDist = 0;
            float farthestRightDist = 0;
            float distance;
            for (int i = 0; i < points.size(); i++) {
                Point curPoint = points.get(i);
                distance = this.getDistance(point1, farthest, curPoint);
                if (!upper) {
                    distance *= -1; // determinant of lower convex has different signs
                }
                if (distance > 0) {
                    leftHull.add(curPoint);
                    if (distance > farthestLeftDist) {
                        farthestLeftDist = distance;
                        farthestPointLeft = curPoint;
                    }
                } else {
                    distance = this.getDistance(point2, farthest, curPoint);
                    if (!upper) {
                        distance *= -1;
                    }
                    if (distance < 0) {

                        rightHull.add(curPoint);

                        if (distance < farthestRightDist) {
                            farthestRightDist = distance;
                            farthestPointRight = curPoint;
                        }
                    }
                }
            }

            if (farthestPointLeft != null) {
                this.convexPoints.add(farthestPointLeft);
                markConvexPoints(leftHull, point1, farthestPointLeft, farthest, upper);
            }

            if (farthestPointRight != null) {
                this.convexPoints.add(farthestPointRight);
                markConvexPoints(rightHull, farthest, farthestPointRight, point2, upper);
            }
        }
    }
}
