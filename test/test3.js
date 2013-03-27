// test1: Display 3D vectorial data
// Jean-Christophe Taveau
// Nov 2012
// http://www.crazybiocomputing.blogspot.com


// Set geometry and material
var vertices = [
  // Front face
  -1.0, -1.0,  1.0,
   1.0, -1.0,  1.0,
   1.0,  1.0,  1.0,
  -1.0,  1.0,  1.0,
   
  // Back face
  -1.0, -1.0, -1.0,
  -1.0,  1.0, -1.0,
   1.0,  1.0, -1.0,
   1.0, -1.0, -1.0
];

// Creation and init viewer
var viewer = newInstance("Vertigo");
viewer.setTitle("Test #1");

// Get Scene 
var scene = viewer.getScene();

//Create and add a shape entitled "Cube" to the scene
// Use of default material
var cube = scene.addNewNode("Shape");
cube.setName("Cube");
cube.setGeometry(["V3F"],cube);
cube.setGraphicsType("POINTS");
var obj = scene.addNewNode("Light");
var group = scene.addNewNode("Group");
var s1 = group.addNewNode("Shape","pyramid");
var s2 = group.addNewNode("Shape","sphere");

// Render
viewer.show();

// F U N C T I O N S

// Load and instanciation of a class in plugins directory
// Thanks to Wayne Rasband
function newInstance(classname) {
  return IJ.getClassLoader().loadClass(classname).newInstance();
}
