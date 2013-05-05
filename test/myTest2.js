// Test1: Display vertices of a cube 
// Vertigo project

// Set geometry and material
var vertices = [
  // Front face
  -0.40,0.0,  0.5,
   -0.15, 0.25,  0.5,
   0.15,  0.25,  0.5,
0.4,  0.0,  0.5,
0.15,  -0.25,  0.5,
  -0.15,  -0.25,  0.5,
   
  // Back face
   -0.40,0.0,  -0.5,
   -0.15, 0.25,  -0.5,
   0.15,  0.25,  -0.5,
0.4,  0.0, -0.5,
0.15,  -0.25,  -0.5,
  -0.15,  -0.25,  -0.5
];

var color = [
  // Front face
  0.0, 0.0,  0.7,
   0.0, 0.0,  0.7,
   0.0,  0.0,  0.7,
  0.0,  0.0,  0.7,
0.0,  0.0,  0.7,
0.0,  0.0,  0.7,
  // Back face
 0.0, 0.7, 0.0,
0.0, 0.7, 0.0,
0.0, 0.7, 0.0,
0.0, 0.7, 0.0,
0.0, 0.7, 0.0,
0.0, 0.7, 0.0
];

var indices=[0, 1, 2, 3,4,5,0,6,7,8,9,10,11,6,0,1,7,2,8,3,9,4,10,5,11];

// Creation and init viewer
var viewer = newInstance("Vertigo_Viewer");
viewer.setTitle("My Test");

// Get Scene 
var scene = viewer.getScene();
IJ.log(scene);

//Create and add a shape entitled "Cube" to the scene
// Use of default material
var cube = scene.addNewNode("Shape");
cube.setName("Cube");
cube.setGeometry("V3F",vertices);
cube.setGeometry("C3F",color);
cube.setIndices(indices);
cube.setDrawingStyle("TRIANGLE_FAN");
// Render
viewer.show();

// F U N C T I O N S
// Load and instanciation of a class
function newInstance(classname) {
  return IJ.getClassLoader().loadClass(classname).newInstance();
}
