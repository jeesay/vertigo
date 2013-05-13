// Test: Display a Toric Solenoid
// Vertigo project
// Jean-Christophe Taveau

// Creation and init viewer
var viewer = newInstance("Vertigo_Viewer");
viewer.setDimension(512,512);
viewer.setTitle("Toric Solenoid");

// Get Scene 
var scene = viewer.getScene();
var cam = viewer.getCamera();
cam.setPosition(0.0,0.0,30.0);


//Create and add a shape entitled "solenoid" to the scene
// Use of default material
var shape = scene.addNewNode("Shape");
shape.setName("solenoid");

var a = 4.0; // torus outer radius
var b = 2.0; // torus inner radius
var p = 7;
var q = 3;

var points=[];
var n=p/q;
var i=0;
for (t=0;t<q*2*Math.PI;t+=0.05) {
  points[i++]=(a+b*Math.cos(n*t))*Math.cos(t);
  points[i++]=(a+b*Math.cos(n*t))*Math.sin(t);
  points[i++]=b*Math.sin(n*t);
}
shape.setGeometry("V3F",points);
shape.setDrawingStyle("POINTS");

// Render
viewer.show("G2D");

// F U N C T I O N S
// Load and instanciation of a class
function newInstance(classname) {
  return IJ.getClassLoader().loadClass(classname).newInstance();
}
