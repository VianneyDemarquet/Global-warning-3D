package sample.view;

import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Sphere;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import com.interactivemesh.jfx.importer.ImportException;
import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import sample.view.CameraManager;

import java.net.URL;

public class Earth {

    private static final float TEXTURE_LAT_OFFSET = -0.2f;
    private static final float TEXTURE_LON_OFFSET = 2.8f;

    public Earth(Pane pane3D){
        //Create a Pane et graph scene root for the 3D content
        Group root3D = new Group();

        // Load geometry
        ObjModelImporter objImporter = new ObjModelImporter();
        try {
            URL modelUrl = this.getClass().getResource("Earth/earth.obj");
            objImporter.read(modelUrl);
        } catch (ImportException e) {
            System.out.println(e.getMessage());
        }
        MeshView[] meshViews = objImporter.getImport();
        Group earth = new Group(meshViews);
        //Group earth = new Group();
        root3D.getChildren().addAll(earth);


        SubScene subscene = new SubScene(root3D, 600, 500, true, SceneAntialiasing.BALANCED);

        // Add a camera group
        PerspectiveCamera camera = new PerspectiveCamera(true);
        new CameraManager(camera, pane3D, root3D);

        // Add point light
        PointLight light = new PointLight(Color.WHITE);
        light.setTranslateX(-180);
        light.setTranslateY(-90);
        light.setTranslateZ(-120);
        light.getScope().addAll(root3D);
        root3D.getChildren().add(light);

        // Add ambient light
        AmbientLight ambientLight = new AmbientLight(Color.WHITE);
        ambientLight.getScope().addAll(root3D);
        root3D.getChildren().add(ambientLight);

        // Create scene

        subscene.setCamera(camera);
        subscene.setFill(Color.GRAY);
        pane3D.getChildren().addAll(subscene);

    }


    // From Rahel Lüthy : https://netzwerg.ch/blog/2015/03/22/javafx-3d-line/
    public Cylinder createLine(Point3D origin, Point3D target) {
        Point3D yAxis = new Point3D(0, 1, 0);
        Point3D diff = target.subtract(origin);
        double height = diff.magnitude();

        Point3D mid = target.midpoint(origin);
        Translate moveToMidpoint = new Translate(mid.getX(), mid.getY(), mid.getZ());

        Point3D axisOfRotation = diff.crossProduct(yAxis);
        double angle = Math.acos(diff.normalize().dotProduct(yAxis));
        Rotate rotateAroundCenter = new Rotate(-Math.toDegrees(angle), axisOfRotation);

        Cylinder line = new Cylinder(0.01f, height);

        line.getTransforms().addAll(moveToMidpoint, rotateAroundCenter);

        return line;
    }

    public static Point3D geoCoordTo3dCoord(float lat, float lon) {
        float lat_cor = lat + TEXTURE_LAT_OFFSET;
        float lon_cor = lon + TEXTURE_LON_OFFSET;
        return new Point3D(
                -java.lang.Math.sin(java.lang.Math.toRadians(lon_cor))
                        * java.lang.Math.cos(java.lang.Math.toRadians(lat_cor)),
                -java.lang.Math.sin(java.lang.Math.toRadians(lat_cor)),
                java.lang.Math.cos(java.lang.Math.toRadians(lon_cor))
                        * java.lang.Math.cos(java.lang.Math.toRadians(lat_cor)));
    }

    public static Point3D geoCoordTo3dCoord(float lat, float lon, float radius) {
        float lat_cor = lat + TEXTURE_LAT_OFFSET;
        float lon_cor = lon + TEXTURE_LON_OFFSET;
        return new Point3D(
                -java.lang.Math.sin(java.lang.Math.toRadians(lon_cor))
                        * java.lang.Math.cos(java.lang.Math.toRadians(lat_cor))*radius,
                -java.lang.Math.sin(java.lang.Math.toRadians(lat_cor))*radius,
                java.lang.Math.cos(java.lang.Math.toRadians(lon_cor))
                        * java.lang.Math.cos(java.lang.Math.toRadians(lat_cor))*radius);
    }

    public void displayTown(Group parent, String name, float latitude, float longitude){
        Sphere sphere = new Sphere(0.01);
        Group ville = new Group();
        ville.getChildren().add(sphere);
        ville.setId(name);
        Point3D pos = geoCoordTo3dCoord(latitude, longitude);
        sphere.setTranslateX(pos.getX());
        sphere.setTranslateY(pos.getY());
        sphere.setTranslateZ(pos.getZ());
        parent.getChildren().addAll(sphere);
        setQuadrilater(parent, latitude, longitude);
    }

    private void setQuadrilater(Group parent, float latitude, float longitude){
        Point3D topLeft = geoCoordTo3dCoord(latitude-10, longitude-10,1.05f);
        Point3D topRight = geoCoordTo3dCoord(latitude+10, longitude-10, 1.05f);
        Point3D botomLeft = geoCoordTo3dCoord(latitude-10, longitude+10, 1.05f);
        Point3D botomRight = geoCoordTo3dCoord(latitude+10, longitude+10, 1.05f);

        final PhongMaterial material = new PhongMaterial();
        Color red = new Color(0.3, 0, 0, 0.3);
        material.setDiffuseColor(red);
        material.setSpecularColor(red);

        addQuadrilateral(parent, topRight, botomRight, topLeft, botomLeft, material);
    }

    private void addQuadrilateral(Group parent, Point3D botomRight, Point3D topRight, Point3D bottomLeft, Point3D topLeft, PhongMaterial material){
        final TriangleMesh triangleMesh = new TriangleMesh();

        final float[] points = {
                (float)botomRight.getX(), (float)botomRight.getY(), (float)botomRight.getZ(),
                (float)bottomLeft.getX(), (float)bottomLeft.getY(), (float)bottomLeft.getZ(),
                (float)topLeft.getX(), (float)topLeft.getY(), (float)topLeft.getZ(),
                (float)topRight.getX(), (float)topRight.getY(), (float)topRight.getZ(),
        };


        final float[]textCoords = {
                1, 1,
                1, 0,
                0, 1,
                0, 0
        };

        final int[] faces = {
                0, 1, 1, 0, 2, 2,
                0, 1, 2, 2, 3, 3
        };

        triangleMesh.getPoints().setAll(points);
        triangleMesh.getTexCoords().setAll(textCoords);
        triangleMesh.getFaces().setAll(faces);

        final MeshView meshView = new MeshView(triangleMesh);
        meshView.setMaterial(material);
        parent.getChildren().addAll(meshView);
    }
}
