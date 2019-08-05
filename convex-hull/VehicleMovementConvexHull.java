/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package midsem.part2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

/**
 *
 * @author aladago
 */
public class VehicleMovementConvexHull {

    private ArrayList<Point> convexPoints;
    private String[] hull; //the form of the hall returnable

    public VehicleMovementConvexHull(String filename) {
        convexPoints = new ArrayList<>();
        ArrayList<Point> points = this.readPoints(filename);
        this.constructHull(points);

        //sort the convex hull points.
        //sorting results in the outline of the hull.
        Collections.sort(convexPoints);
        this.restructure();

    }

    /**
     *
     */
    private static class Point implements Comparable {

        double lat, lon;

        Point(double la, double lo) {
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

    /**
     *
     * @return The convex hull for the vehicle moves
     */
    public String[] getHull() {
        return this.hull;
    }

    /**
     * Adds a new point to the list of points. The new point can be come part of
     * the convex hull or not. It can also result in one or more points taken
     * out of the convex hull Pre-Condition: a convex hull should have been
     * previously constructed Pos-condition: a call to this function may trigger
     * an update of the convex hull
     *
     * @param lat the latitude of the new point
     * @param lon the longitude of the new point
     * @return True if the new point has never been visited by the bus. Return
     * false otherwise
     */
    public boolean addPoint(double lat, double lon) {
        Point newPoint = new Point(lat, lon);
        return addnewPoint(newPoint);
    }

    private boolean addnewPoint(Point newPoint) {
        //get the two farthest left and farthest right points
        //of the current convex hull
        Point p1 = this.convexPoints.get(0);
        Point p2 = this.convexPoints.get(this.convexPoints.size() - 1);

        //a new point is automatically part of the convex hull 
        //if itis less than farthest left point or it is greater than the farthest 
        // right point.
        //consider points sandwich between points in the convex hull
        if (newPoint.compareTo(p1) > 0 && newPoint.compareTo(p2) < 0) {
            double d = this.getDistance(p1, p2, newPoint);

            //new point is not part of the convex hull if it lies on the 
            //line segement connecting the two extreme points. 
            if (d == 0) {
                return false;
            }

            //examine only the points which are on the same side as the 
            //new point of the line segement connecting p1p2 
            ArrayList<Point> desired = new ArrayList<>();
            desired.add(p1);
            this.convexPoints.stream().forEach((p) -> {
                double distance = this.getDistance(p1, p2, p);
                if (d < 0 && distance < 0) {
                    desired.add(p);
                } else if (d > 0 && distance > 0) {
                    desired.add(p);
                }
            });

            desired.add(p2);

            //search for the two points sandwiching the new point among the 
            //points on the same side as the new point of line segment p1p2
            Point prev = desired.get(0);
            Point next = desired.get(0);

            //keep the index of the points sandwitching the new point
            //to make easy insertion into the nearly sorted convex hull points
            //in case the new point is part of the convex hull
            int nextIn = 1;
            for (int i = 1; i < desired.size(); i++) {
                next = desired.get(i);
                if (prev.compareTo(newPoint) < 0 && next.compareTo(newPoint) > 0) {
                    break;
                }
                prev = next;
                nextIn++;
            }

            //to be part of the convex hull, the new point must
            //lie exclusively alone on one side of the line segement connecting 
            //the points sadwitching it. 
            double dist = this.getDistance(prev, next, newPoint);

            if (dist == 0) {
                return false;
            }

            //check that the new point lies alone on one side of line segment preNext
            for (Point p : desired) {
                double dis = this.getDistance(prev, next, p);
                if (dist < 0 && dis < 0) {
                    return false;
                } else if (dist > 0 && dis > 0) {
                    return false;
                }
            }

            //at this stage, the new point is part of the convex
            //hull. to stop sorting the already sorted arrayList,
            //insert the newpoint at the correct position in sorted 
            //convex hull
            this.convexPoints.add(nextIn, newPoint);
        }

        //update the convex hull
        //insert the new point at the correct position 
        if (newPoint.compareTo(p1) < 0) {
            this.convexPoints.add(0, newPoint);
        } else if (newPoint.compareTo(p2) > 0) {
            this.convexPoints.add(this.convexPoints.size(), newPoint);
        }

        this.updateHull();
        this.restructure();
        return true;
    }

    /**
     * This function reads the coordinates from a file
     *
     * @param filename the name of the file containing the gps coordinates
     * @return An ArrayList of all the GPS Coordinates sorted in non-decreasing
     * order of their latitudes. For the same latitudes, longitudes are used to
     * break ties.
     */
    private ArrayList<Point> readPoints(String filename) {
        ArrayList<Point> points = new ArrayList<>();
        try (BufferedReader reader
                = new BufferedReader(new FileReader(filename))) {

            int latIndex, lonIndex;
            latIndex = lonIndex = 0;
            String[] header = reader.readLine().split(",");
            for (int i = 0; i < header.length; i++) {
                if (header[i].equalsIgnoreCase("Latitude")) {
                    latIndex = i;
                }

                if (header[i].equalsIgnoreCase("Longitude")) {
                    lonIndex = i;
                }
            }

            String record;
            String[] row;
            double lat, lon;

            while ((record = reader.readLine()) != null) {
                row = record.split(",");
                lat = Double.parseDouble(row[latIndex]);
                lon = Double.parseDouble(row[lonIndex]);
                points.add(new Point(lat, lon));
            }
        } catch (IOException ex) {
            System.out.println("IOException " + ex.toString());
        }

        Collections.sort(points);
        return points;
    }

    /**
     * The untility class reorganizes the convex hall points and store them as
     * in array which can then be returned to external functions
     */
    private void restructure() {
        int size = this.convexPoints.size();
        this.hull = new String[size];
        int i = 0;
        for (Point p : this.convexPoints) {
            hull[i++] = p.lat + "," + p.lon;
        }

    }

    /**
     * This utility function updates a convex hall whenever there is a need to
     * do so. Pre-condition: a convex hull should have been previously
     * constructed. Post condition. The composition of the convex hall may chage
     */
    private void updateHull() {
        ArrayList<Point> points = this.convexPoints;
        this.convexPoints = new ArrayList<>();
        this.constructHull(points);
    }

    /**
     * This methods starts the process of constructing the entire convex hull
     *
     *
     * @param coordinates points sorted in non-decreasing order according to
     * their latitudes. For the same latitudes, longitudes are used.
     */
    private void constructHull(ArrayList<Point> coordinates) {
        int numCoordinates = coordinates.size();
        if (numCoordinates < 2) {
            return;
        }

        //extreme points on both left and right are part of the convex hull.
        //those points also help determine the lower hull and upper hull
        Point leftMostPoint = coordinates.get(0);
        Point rightMostPoint = coordinates.get(numCoordinates - 1);
        this.convexPoints.add(leftMostPoint);
        this.convexPoints.add(rightMostPoint);

        //extreme points on both sides of the line segment connection 
        //leftMostRightMost are part of the convex hull
        Point upperConvex = null;
        Point lowerConvex = null;
        double farthestUpperDist = 0;
        double farthestLowerDistance = 0;

        ArrayList<Point> upperHull = new ArrayList<>();
        ArrayList<Point> lowerHull = new ArrayList<>();

        for (int i = 1; i < numCoordinates - 1; i++) {
            Point curPoint = coordinates.get(i);
            double distance = this.getDistance(leftMostPoint, rightMostPoint, curPoint);
            if (distance > 0) {
                upperHull.add(coordinates.get(i));
                if (distance > farthestUpperDist) {
                    farthestUpperDist = distance;
                    upperConvex = curPoint;
                }

            } else if (distance < 0) {
                lowerHull.add(curPoint);

                //the determinant of the points to the right (lower) of 
                //leftMostRightMost is always negative
                if (distance < farthestLowerDistance) {
                    farthestLowerDistance = distance;
                    lowerConvex = curPoint;
                }
            }
        }

        //now construct both the upper hull and lower hull using hull stacks
        
        if (upperConvex != null) {
            this.convexPoints.add(upperConvex);
            Stack<ArrayList<Point>> upperH = new Stack<>();
            Stack<Point> vertices = new Stack<>();
            upperH.add(upperHull);
            vertices.add(leftMostPoint);
            vertices.add(upperConvex);
            vertices.add(rightMostPoint);
            this.markConvexPoints(upperH, vertices, true);
        }
        if (lowerConvex != null) {
            this.convexPoints.add(lowerConvex);
            Stack<ArrayList<Point>> lowerH = new Stack<>();
            Stack<Point> vertices = new Stack<>();
            lowerH.add(lowerHull); //to be popped third
            vertices.add(leftMostPoint); //to be popped second
            vertices.add(lowerConvex); //to be popped last
            vertices.add(rightMostPoint);
            this.markConvexPoints(lowerH, vertices, false);
        }

    }

    /**
     * This function returns the determinant of the points p1, p2, pi. The sign
     * and magnitude of the determinant shows which side pi lies on the line
     * segment connection P1P2 and how far away pi is from that line line
     * respectively
     *
     * @param P1 a 2d point
     * @param P2 a 2d points. Makes a line segment with P1
     * @param Pi a 2-d point. This function determines the position of this
     * point from p1p2
     * @return 0 if the three points make a straigt line. a positive distance if
     * Pi is to the left of P1P2. Retuns a negative distance if Pi is to the
     * right of P1P2
     */
    private double getDistance(Point P1, Point P2, Point Pi) {
        double sum = P1.lat * P2.lon + Pi.lat * P1.lon + P2.lat * Pi.lon;
        double diff = Pi.lat * P2.lon + P1.lat * Pi.lon + P2.lat * P1.lon;
        return sum - diff;
    }

    /**
     * A function which contructs contructs the both the upper and
     * lowr convex hulls Pos-condition: A call to this functions changes the
     * composition of the convex hull.
     *
     * @param points The set of non-dreasing sorted points to determine convex
     * hull
     * @param point1 the leftmost point of the sorted points points
     * @param farthest The farthest point from Point1Point2
     * @param point2 the rightmost point of the sorted points points
     * @param upperHull. True if contrusting the upper hull. False if
     * constructing the lower hull.
     */
    private void markConvexPoints(Stack<ArrayList<Point>> hulls,
            Stack<Point> vertices, boolean upperHull) {

        while (!hulls.empty()) {
            ArrayList<Point> hull = hulls.pop();
            Point farRight = vertices.pop();
            Point f = vertices.pop();
            Point farLeft = vertices.pop();
            if (hull.size() > 1) {
                ArrayList<Point> leftHull = new ArrayList<>();
                ArrayList<Point> rightHull = new ArrayList<>();
                Point farthestPointLeft = null;
                Point farthestPointRight = null;

                double farthestLeftDist = 0;
                double farthestRightDist = 0;
                double distance;

                for (int i = 0; i < hull.size(); i++) {
                    Point curPoint = hull.get(i);

                    distance = this.getDistance(farLeft, f, curPoint);
                    //distance has opposite sign for upper hull and lower hull.
                    //distance is negative for points on the 
                    //left of triangle Point1Point2LowerConvex
                    if (!upperHull) {
                        distance *= -1;
                    }

                    if (distance > 0) {
                        //System.out.println("l");

                        leftHull.add(curPoint);
                        if (distance > farthestLeftDist) {
                            farthestLeftDist = distance;
                            farthestPointLeft = curPoint;
                        }
                    } else {
                        distance = this.getDistance(farRight, f, curPoint);

                        if (!upperHull) {
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

                if (leftHull.size() > 0) {
                    this.convexPoints.add(farthestPointLeft);
                    vertices.add(farLeft);
                    vertices.add(farthestPointLeft);
                    vertices.add(f);
                    hulls.add(leftHull);
                }

                if (rightHull.size() > 0) {
                    this.convexPoints.add(farthestPointRight);
                    vertices.add(f);
                    vertices.add(farthestPointRight);
                    vertices.add(farRight);
                    hulls.add(rightHull);
                }
            }

        }
    }
}
