// Drawing built-in shapes with Vertigo
// Jean-Christophe Taveau
// May 2013

// Init
var viewer = newInstance("Vertigo_Viewer");
viewer.setTitle("Sphere");
viewer.setBackgroundColor(0,0,0);


// Get Camera and translate it 
var cam = viewer.getCamera();
cam.setPosition(0.0,0.0,20.0);

// Get Scene 
var scene = viewer.getScene();

var cube=scene.addNewNode("WireCube");
cube.setColor(255,128,50);
cube.translate(-2.5,-2.5,0);
var torus=scene.addNewNode("Torus");
torus.translate(0,-2.5,0);
torus.rotate(90,1,0,0);
var sph=scene.addNewNode("Sphere");
sph.setColor(0,255,0);
sph.rotate(90,1,0,0);
sph.translate(2.5,-2.5,0);


var torus=scene.addNewNode("Torus");
torus.translate(-2.5,0,0);
torus.setColor(10,100,50);
var cube=scene.addNewNode("WireCube");
cube.setColor(100,100,50);
var sph=scene.addNewNode("Sphere");
sph.translate(2.5,0,0);

var torus=scene.addNewNode("Torus");
torus.translate(-2.5,2.5,0);
torus.setColor(100,100,50);
var pyramid=scene.addNewNode("WirePyramid");
pyramid.setColor(100,100,50);
pyramid.translate(0.0,2.5,0);
var sph=scene.addNewNode("Sphere");
sph.setColor(255,0,0);
sph.translate(2.5,2.5,0);


viewer.show("G2D");

// F U N C T I O N S
// Load and instanciation of a class
function newInstance(classname) {
  return IJ.getClassLoader().loadClass(classname).newInstance();
}
