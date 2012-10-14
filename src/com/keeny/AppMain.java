package com.keeny;

import com.keeny.components.Person;
import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.Primitive;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.universe.SimpleUniverse;

import javax.media.j3d.*;
import javax.swing.*;
import javax.vecmath.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by IntelliJ IDEA.
 * User: keeny
 * Date: 05-Nov-2004
 * Time: 18:16:57
 * <p/>
 * This is the main runnable class.
 * Here we set up the background, lighting, etc. and then add the character model
 */
public class AppMain extends JFrame {

    BasicUniverse universe;
    BranchGroup root;
    BoundingSphere bounds = new BoundingSphere(new Point3d(0.0,0.0,0.0), 100); //area of effect


    private AppearanceFactory materials = new AppearanceFactory();
    private int normal = Primitive.GENERATE_NORMALS;
    private int textured = Primitive.GENERATE_NORMALS + Primitive.GENERATE_TEXTURE_COORDS;

    private TransformGroup viewPlatformTG;
    private Transform3D originalTransform;

    /**
     * TextureLoaders in the Appearances class need a component
     * to load files into - so here's one they can hold on to
     */
    public static AppMain theApp;

    /**
     * Constructor
     */
    public AppMain() {
        setSize(600, 600);
        initComponents();

        GraphicsConfiguration graphicsConfig = SimpleUniverse.getPreferredConfiguration();
        Canvas3D canvas = new Canvas3D(graphicsConfig);
        getContentPane().add("Center", canvas);

        this.universe = new BasicUniverse(canvas, 20.0f);
        this.root = new BranchGroup();
    }

    private void addProps() {
        // x, y, z

        //add the floor
        Box floor = new Box(50.0f, 0.1f, 50.0f, textured, materials.grass());
        this.addObject(floor, 0.0f, -2.1f, 0.0f);

//        Sphere sphere = new Sphere(3f, Sphere.GENERATE_NORMALS, Appearances.skinAppearance());
//        this.addObject(sphere);
    }

    private void addActors() {
        // Create an identity transform to permit bob to move
        Transform3D walkT3D = new Transform3D();
        TransformGroup walkTG = new TransformGroup();
        walkTG.setTransform(walkT3D);
        walkTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        walkTG.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        //Add bob
        Person bob = new Person();
        walkTG.addChild(bob);

        this.root.addChild(walkTG);

        //ANIMATION
        // Rotation is about the local X axis by default
        //Create a transform and rotate it by 90 degrees about the Y axis.
        //This means objects will now be translated along the z axis.
        Transform3D zAxis = new Transform3D();
        zAxis.rotY(Math.PI / 2);

        int loop = -1;
        long triggerTime = 1000;
        long delay = 2000;
        long duration = 15500;
        Alpha walkAlpha = new Alpha(loop, triggerTime, delay, duration, 0, 0);

        BoundingSphere system = new BoundingSphere();

        //Bob walks
        PositionInterpolator walkInterpolator = new PositionInterpolator(walkAlpha, walkTG, zAxis, 8.5f, -16f);
        walkInterpolator.setSchedulingBounds(system);
        walkTG.addChild(walkInterpolator);
    }

    private void addObject(Node object){
        this.addObject(object, 0, 0, 0);
    }

    // addNode, addPrimitive?
    private void addObject(Node object, float x, float y, float z){
        Transform3D t3d = new Transform3D();
        t3d.set(new Vector3f(x, y, z));
        TransformGroup tg = new TransformGroup(t3d);

        this.root.addChild(tg);
        tg.addChild(object);
    }

    private void initCamera() {
        //Whatever we do to this TG affects the view - used later to rotate the view
        viewPlatformTG = universe.getViewPlatformTransform();

        BoundingSphere system = new BoundingSphere();

        //PAN AND ROTATE CAMERA
        int loop = -1;
        long triggerTime = 1000;
        long delay = 2000;
        long duration = 9000;
        long easeInDuration = 300;
        long easeOutDuration = 6500;
        Alpha camAlpha = new Alpha(loop, triggerTime, delay, duration, easeInDuration, easeOutDuration);

        Transform3D axis = new Transform3D();
        float[] knots = {0.0f, 0.2f, 0.4f, 0.7f, 0.85f, 0.95f, 1.0f};

        Quat4f[] rots = new Quat4f[7];
        for (int i = 0; i < 7; i++) {
            rots[i] = new Quat4f();
        }
        rots[0].set(new AxisAngle4d(0, 1, 0, Math.toRadians(-180)));
        rots[1].set(new AxisAngle4d(0, 1, 0, Math.toRadians(-180)));
        rots[2].set(new AxisAngle4d(0, 1, 0, Math.toRadians(-180 - 45)));
        rots[3].set(new AxisAngle4d(0, 1, 0, Math.toRadians(-180 - 70)));
        rots[4].set(new AxisAngle4d(0, 1, 0, Math.toRadians(-180 - 110)));
        rots[5].set(new AxisAngle4d(0, 1, 0, Math.toRadians(-180 - 180)));
        rots[6].set(new AxisAngle4d(0, 1, 0, Math.toRadians(-180 - 220)));

        Point3f[] positions = {new Point3f(0f, 3f, -16f),
                new Point3f(0f, 2f, -12f),
                new Point3f(4f, 1f, -8f),
                new Point3f(6f, 0f, 0f),
                new Point3f(5f, -0.4f, 8f),
                new Point3f(0f, -0.2f, 12f),
                new Point3f(-4.5f, 0.6f, 17.5f)};
        RotPosPathInterpolator camInterpolator = new RotPosPathInterpolator(camAlpha, viewPlatformTG, axis, knots, rots, positions);
        camInterpolator.setSchedulingBounds(system);

        this.root.addChild(camInterpolator);

        // add the group of objects to the Universe
        universe.addBranchGraph(this.root);
    }

    private Color3f rgb(int r, int g, int b){
        return new Color3f(new Color(r, g, b));
    }

    /**
     * Setup the lovely sky background
     *
     * @return
     */
    private void addBackground() {
//        BranchGroup backgroundRoot = new BranchGroup();

        // Create the background object, the sphere to be textured,
        // and connect the sphere to the background as its geometry
        Background background = new Background();
        background.setApplicationBounds(bounds);
        BranchGroup backGeoBranch = new BranchGroup();
        Sphere backSphere = new Sphere(1.0f,
                Sphere.GENERATE_NORMALS_INWARD |
                Sphere.GENERATE_TEXTURE_COORDS, 20);
        backGeoBranch.addChild(backSphere);
        background.setGeometry(backGeoBranch);

        // Create a texture from the image file
        TextureLoader tex = new TextureLoader("sky.jpg", this);
        // Scale the texture so it's about the same size as
        // in the original file (factor of 20 seems to do this)
        TextureAttributes texAttr = new TextureAttributes();
        Transform3D textureTrans = new Transform3D();
        textureTrans.setScale(1);
        texAttr.setTextureTransform(textureTrans);
        Appearance backgroundApp = backSphere.getAppearance();
        backgroundApp.setTextureAttributes(texAttr);
        // Attach the texture to the background's appearance
        if (tex != null)
            backgroundApp.setTexture(tex.getTexture());

        this.root.addChild(background);
    }

    private void addLighting(){
        Color3f white = new Color3f(1f, 1f, 1f);
        Color3f yellow = rgb(70, 70, 30);
        Color3f shadow = rgb(200, 200, 255);

        // Ambient light is a very dull grey (think shadow colour)
        // AmbientLight lightA = new AmbientLight(new Color3f(new Color(100, 100, 255)));
        AmbientLight ambient = new AmbientLight(shadow);
        ambient.setInfluencingBounds(bounds);
        this.root.addChild(ambient);

        Vector3f downDirection = new Vector3f(0f, -1.0f, 0f);
        DirectionalLight downLight = new DirectionalLight(yellow, downDirection);
        downLight.setInfluencingBounds(bounds);
        this.root.addChild(downLight);

//        this.addDirectionalLight(-1.0f, -1.0f, -2.0f); // right, down, into screen
        this.addDirectionalLight(-1.0f, -1.0f, 0f); // right, straight down
//        this.addDirectionalLight(-1.0f, -1.0f, 2.0f); // right, down, towards viewer
    }

    private void addDirectionalLight(float x, float y, float z){
        Color3f yellow = rgb(80, 80, 40);

        Vector3f direction = new Vector3f(x, y, z);
        DirectionalLight light = new DirectionalLight(yellow, direction);
        light.setInfluencingBounds(this.bounds);

        this.root.addChild(light);
    }

    /**
     * Main method starts the show
     *
     * @param args
     */
    public static void main(String[] args) {
        theApp = new AppMain();
        theApp.setVisible(true);

        theApp.addBackground();
        theApp.addLighting();
        theApp.addProps();
        theApp.addActors();

        theApp.initCamera();
    }

    /**
     * Add key listeners etc
     */
    private void initComponents() {

        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyChar() == 'f') {
                    viewPlatformTG.setTransform(originalTransform);
                    System.out.println("front");
                } else if (e.getKeyChar() == 't') {

                    //Make a rotate transform matrix
                    Transform3D transform = new Transform3D();
                    transform.rotX(-Math.PI / 2.0);

                    //Multiply the matrices + apply
                    transform.mul(originalTransform);
                    viewPlatformTG.setTransform(transform);
                    System.out.println("top");

                } else if (e.getKeyChar() == 's') {

                    //Make a rotate transform matrix
                    Transform3D transform = new Transform3D();
                    transform.rotY(-Math.PI / 2.0);

                    //Multiply the matrices + apply
                    transform.mul(originalTransform);
                    viewPlatformTG.setTransform(transform);
                    System.out.println("side");

                } else if (e.getKeyChar() == 'v') {

                    //Make a rotate transform matrix
                    Transform3D transform = new Transform3D();
                    transform.rotY(-Math.PI / 4.0);

                    //Multiply the matrices + apply
                    transform.mul(originalTransform);
                    viewPlatformTG.setTransform(transform);
                    System.out.println("view");
                } else if (e.getKeyChar() == 'x') {
                    System.out.println("exit");
                    System.exit(0);
                }
            }
        });

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                exitForm(evt);
            }
        });
    }

    /**
     * Exit Application **
     */
    private void exitForm(WindowEvent evt) {
        System.exit(0);
    }

}
